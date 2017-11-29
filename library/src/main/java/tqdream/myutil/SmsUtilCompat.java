/*
 * 创建日期：2012-10-18
 */
package tqdream.myutil;

import android.content.Intent;
import android.telephony.SmsMessage;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * 版权所有 (c) 2012 北京新媒传信科技有限公司。 保留所有权利。<br>
 * 项目名： 飞信 - Android客户端<br>
 * 描述： 软硬件平台适配工具类<br>暂时保留，后面根据实际情况可删除
 * 
 * @version 1.0
 * @since JDK1.5
 */
public class SmsUtilCompat {
	private static final String TAG = "SmsUtilCompat";

	/**
	 * 获取短信信息，注意：为解决双卡双待手机解析短信异常问题，使用Java反射机制，优先解析GSM类型的短信，假如解析失败才按CDMA类型的短信进行解析）
	 * 
	 * @param intent
	 * @return
	 */
	public static SmsMessage[] getSmsMessage(Intent intent) {
		SmsMessage[] msgs = null;
		Object messages[] = (Object[]) intent.getSerializableExtra("pdus");
		int len = 0;
		if (null != messages && (len = messages.length) > 0) {
			msgs = new SmsMessage[len];
			try {
				for (int i = 0; i < len; i++) {
					SmsMessage message = null;
					if ("GSM".equals(intent.getStringExtra("from"))) { // 适配MOTO XT800双卡双待手机
						message = createFromPduGsm((byte[]) messages[i]);
					} else if ("CDMA".equals(intent.getStringExtra("from"))) { // 适配MOTO XT800双卡双待手机
						message = createFromPduCdma((byte[]) messages[i]);
					} else {
						message = SmsMessage.createFromPdu((byte[]) messages[i]); // 系统默认的解析短信方式
					}
					if (null == message) { // 解决双卡双待类型手机解析短信异常问题
						message = createFromPduGsm((byte[]) messages[i]);
						if (null == message) {
							message = createFromPduCdma((byte[]) messages[i]);
						}
					}
					if (null != message) {
						msgs[i] = message;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				msgs = getSmsMessageByReflect(intent); // 解决双卡双待手机解析短信异常问题
			} catch (Error er) {
				er.printStackTrace();
				msgs = getSmsMessageByReflect(intent); // 解决双卡双待手机解析短信异常问题
			}
		}
		return msgs;
	}

	/**
	 * 使用Java反射机制获取短信信息（解决双卡双待手机解析短信异常问题，优先解析GSM类型的短信，假如解析失败才按CDMA类型的短信进行解析）
	 * 
	 * @param intent
	 * @return
	 */
	private static SmsMessage[] getSmsMessageByReflect(Intent intent) {
		SmsMessage[] msgs = null;
		Object messages[] = (Object[]) intent.getSerializableExtra("pdus");
		int len = 0;
		if (null != messages && (len = messages.length) > 0) {
			msgs = new SmsMessage[len];
			try {
				for (int i = 0; i < len; i++) {
					SmsMessage message = createFromPduGsm((byte[]) messages[i]);
					if (null == message) {
						message = createFromPduCdma((byte[]) messages[i]);
					}
					if (null != message) {
						msgs[i] = message;
					}
				}
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			}
		}
		return msgs;
	}

	/**
	 * 通过Java反射机制解析GSM类型的短信
	 * @param pdu
	 * @return
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws ClassNotFoundException
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 */
	private static SmsMessage createFromPduGsm(byte[] pdu) throws SecurityException, IllegalArgumentException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException,
	InvocationTargetException, InstantiationException {
		return createFromPdu(pdu, "com.android.internal.telephony.gsm.SmsMessage");
	}

	/**
	 * 通过Java反射机制解析CDMA类型的短信
	 * @param pdu
	 * @return
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws ClassNotFoundException
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 */
	private static SmsMessage createFromPduCdma(byte[] pdu) throws SecurityException, IllegalArgumentException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException,
	InvocationTargetException, InstantiationException {
		return createFromPdu(pdu, "com.android.internal.telephony.cdma.SmsMessage");
	}

	/**
	 * 通过Java反射机制解析GSM或者CDMA类型的短信
	 * 
	 * @param pdu
	 * @param className GSM: com.android.internal.telephony.gsm.SmsMessage, CDMA: com.android.internal.telephony.cdma.SmsMessage
	 * @return
	 * @throws ClassNotFoundException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws InstantiationException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws Exception
	 */
	private static SmsMessage createFromPdu(byte[] pdu, String className) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException,
	InvocationTargetException, InstantiationException {
		Class<?> clazz = Class.forName(className);
		Object object = clazz.getMethod("createFromPdu", byte[].class).invoke(clazz.newInstance(), pdu);
		if (null != object) {
			Constructor<?> constructor = SmsMessage.class.getDeclaredConstructor(Class.forName("com.android.internal.telephony.SmsMessageBase"));
			constructor.setAccessible(true);
			return (SmsMessage) constructor.newInstance(object);
		} else {
			return null;
		}
	}


}