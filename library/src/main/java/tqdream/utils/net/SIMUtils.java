package tqdream.base.base.utils.net;

import android.content.Context;
import android.telephony.TelephonyManager;

public class SIMUtils {

	private Context context;
	private TelephonyManager telephonyManager;

	private static SIMUtils instance;

	private SIMUtils(Context context) {
		this.context = context;
		this.telephonyManager = getTelephonyManager();
	}


	public static final SIMUtils getInstance(Context context) {
		if (instance == null) {
			instance = new SIMUtils(context);
		}
		return instance;
	}

	private TelephonyManager getTelephonyManager() {
		return (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
	}

	/**
	 * check the SIM Card is avaliable
	 * 
	 * @return
	 */
	public boolean SIMAvaliable() {
		return telephonyManager.getSimState() == TelephonyManager.SIM_STATE_READY;
	}

	/**
	 * 获取IMSI
	 * 
	 * @return
	 */
	public String getIMSI() {
		String imsi = telephonyManager.getSubscriberId();
		if (imsi == null)
			imsi = "";
		if (!imsi.startsWith("4600")) {
			imsi = "";
		}
		return imsi;
	}

	/**
	 * 获取网络提供商
	 * 
	 * @return
	 */
	public String getNetworkOperator() {
		return telephonyManager.getNetworkOperator();
	}

	/**
	 * 获取网络提供商名称
	 * 
	 * @return
	 */
	public String getNetworkOperatorName() {
		return telephonyManager.getNetworkOperatorName();
	}

	/**
	 * Returns the ISO country code equivalent of the current registered
	 * operator's MCC
	 * 
	 * @return
	 */
	public String getNetworkCountryIso() {
		return telephonyManager.getNetworkCountryIso();
	}

	/**
	 * 获取SIM卡提供商
	 * 
	 * @return
	 */
	public String getSimOperator() {
		return telephonyManager.getSimOperator();
	}

	/**
	 * 获取SIM卡提供商名称
	 * 
	 * @return
	 */
	public String getSimOperatorName() {
		return telephonyManager.getSimOperatorName();
	}

	/**
	 * <p>
	 * 获取设备号
	 * <p>
	 * Returns the unique device ID, for example, the IMEI for GSM and the MEID
	 * or ESN for CDMA phones. Return null if device ID is not available
	 * 
	 * @return
	 */
	public String getDeviceId() {
		String deviceId = telephonyManager.getDeviceId();
		if (deviceId == null)
			deviceId = "";
		return deviceId;
	}

}
