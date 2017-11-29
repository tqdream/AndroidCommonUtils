package tqdream.myutil;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.Rect;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.os.StatFs;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.feinno.androidbase.utils.log.LogFeinno;
import com.feinno.androidbase.utils.log.RFAssert;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.List;
import java.util.UUID;


/**
 * 设备信息类
 * edited by wangxiaohong
 */
public class SystemUtils extends SmsUtilCompat {
    private static final String TAG = "SystemUtils";
    public static final int APN_WIFI = 1;
    public static final int APN_CMNET = 2;
    public static final int APN_CMWAP = 3;


    private static final String FILE_CPU = "/proc/cpuinfo";

    public static int screenHeight = 0;
    public static int screenWidth = 0;
    public static float screenDensity = 0;
    public static float statusBarHeight = 0;

    private static SystemUtils mInstance;
    private PackageManager packageManager;
    private Context context;

    private SystemUtils(Context context) {
        this.context = context;
    }

    public static SystemUtils getInstance() {
//        RFAssert.rfAssert(mInstance != null);
        return mInstance;
    }

    /**
     * 在APP的主Act中初始化调用，只获取一次屏幕宽高度和密度，防止重复获取
     *
     * @param context
     */
    public static void createInstance(Context context) {
//        RFAssert.rfAssert(mInstance == null);
        mInstance = new SystemUtils(context);
        mInstance.getPackageManager();
    }

    /**
     * 获取设备的宽高还有密度
     *
     * @param context
     */
    public static void initScreenInfo(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay()
                .getMetrics(dm);
        SystemUtils.screenDensity = dm.density;
        SystemUtils.screenHeight = dm.heightPixels;
        SystemUtils.screenWidth = dm.widthPixels;
        Rect frame = new Rect();
        ((Activity) context).getWindow().getDecorView()
                .getWindowVisibleDisplayFrame(frame);
        SystemUtils.statusBarHeight = frame.top;
    }

    /**
     * 判断手机屏幕是否是唤醒状态
     *
     * @param context
     * @return
     */
    public static boolean isScreenOn(Context context) {
        return ((PowerManager) context.getSystemService(Context.POWER_SERVICE)).isScreenOn();
    }

    /**
     * @return
     */
    public static String getImsi(Context context) {
        return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getSubscriberId();
    }

    /**
     * 获取设备ID号(国际移动设备身份码，储存在移动设备中，全球唯一的一组号码)
     *
     * @return
     */
    public static String getDeviceId(Context context) {
        String DeviceID = "";
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager.getDeviceId() == null) {
            DeviceID = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        } else {
            DeviceID = telephonyManager.getDeviceId(); // 用于获取无IMEI设备的序列号
        }
        return DeviceID;
    }


//    /**
//     * 获取本地Ip地址
//     *
//     * @param context
//     * @return
//     */
//    public static String getLocalIpAddress(Context context) {
//        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//        if (wifiInfo != null && wifiInfo.getIpAddress() > 0) {
//            return android.text.format.Formatter.formatIpAddress(wifiInfo.getIpAddress());
//        } else {
//            try {
//                Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
//                while (en.hasMoreElements()) {
//                    NetworkInterface intf = en.nextElement();
//                    Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
//                    while (enumIpAddr.hasMoreElements()) {
//                        InetAddress inetAddress = enumIpAddr.nextElement();
//                        if (!inetAddress.isLoopbackAddress() && inetAddress.getHostAddress().indexOf(":") == -1) {
//                            String ipAddress = inetAddress.getHostAddress();
//                            if (!TextUtils.isEmpty(ipAddress) && !ipAddress.contains(":")) {
//                                return ipAddress;
//                            }
//                        }
//                    }
//                }
//
//            } catch (SocketException e) {
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }

    /**
     * 获取版本名称
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        String versionName = "-1";
        try {
            versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 通过包名获取版本名称
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context, String packageName) {
        String versionName = "-1";
        try {
            versionName = context.getPackageManager().getPackageInfo(packageName, 0).versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 获取APP的版本号
     */
    public static int getVersionCode(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(),
                    0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int version = packInfo.versionCode;
        return version;
    }

//    /**
//     * 获取手机mac地址 错误返回12个0
//     */
//    public static String getMacAddress(Context context) {
//        // 获取mac地址：
//        String macAddress = "000000000000";
//        try {
//            WifiManager wifiMgr = (WifiManager) context
//                    .getSystemService(Context.WIFI_SERVICE);
//            WifiInfo info = (null == wifiMgr ? null : wifiMgr
//                    .getConnectionInfo());
//            if (null != info) {
//                if (!TextUtils.isEmpty(info.getMacAddress()))
//                    macAddress = info.getMacAddress().replace(":", "");
//                else
//                    return macAddress;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return macAddress;
//        }
//        return macAddress;
//    }

    /**
     * get phone sys version
     *
     * @return 手机的系统版本信息. 9
     */
    public static String getSysVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * Returns the ISO country code equivalent of the current registered
     * operator's MCC (Mobile Country Code).
     *
     * @param context
     * @return 手机网络国家编码 cn
     */
    public static String getNetWorkCountryIso(Context context) {
        TelephonyManager manager = (TelephonyManager) context
                .getSystemService(Activity.TELEPHONY_SERVICE);
        return manager.getNetworkCountryIso();
    }

    /**
     * Returns the numeric name (MCC+MNC) of current registered operator.may not
     * work on CDMA phone
     *
     * @param context
     * @return 手机网络运营商ID。46001
     */
    public static String getNetWorkOperator(Context context) {
        TelephonyManager manager = (TelephonyManager) context
                .getSystemService(Activity.TELEPHONY_SERVICE);
        return manager.getNetworkOperator();
    }

    /**
     * Returns the alphabetic name of current registered operator.may not work
     * on CDMA phone
     *
     * @param context
     * @return 手机网络运营商名称 china unicom
     */
    public static String getNetWorkOperatorName(Context context) {
        TelephonyManager manager = (TelephonyManager) context
                .getSystemService(Activity.TELEPHONY_SERVICE);
        return manager.getNetworkOperatorName();
    }

    /**
     * get type of current network
     *
     * @param context
     * @return 手机的数据链接类型 3
     */
    public static int getNetworkType(Context context) {
        TelephonyManager manager = (TelephonyManager) context
                .getSystemService(Activity.TELEPHONY_SERVICE);

        return manager.getNetworkType();
    }

    /**
     * get the cpu info
     *
     * @return 手机CPU型号 ARMv7 Processor
     */
    public static String getCpuInfo() {
        try {
            FileReader fr = new FileReader(FILE_CPU);
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            String[] array = text.split(":\\s+", 2);
            for (int i = 0; i < array.length; i++) {
            }
            return array[1];
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * get product name of phone
     *
     * @return 手机名称 libra_mione_plus
     */
    public static String getProductName() {
        return Build.PRODUCT;
    }

    /**
     * get model of phone
     *
     * @return 手机型号 MI-ONE Plus
     */
    public static String getModelName() {

        return Build.MODEL;
    }

    /**
     * get Manufacturer Name of phone
     *
     * @return 手机设备制造商名称 xiaomi
     */
    public static String getManufacturerName() {
        return Build.MANUFACTURER;
    }

    /**
     * @param @param  context
     * @param @return 参数
     * @return String    返回类型
     * @throws
     * @Title: getAppName
     * @Description: TODO(获取应用名称)
     */
    public static String getAppName(Context context) {
        return context.getApplicationInfo()
                .loadLabel(context.getPackageManager()).toString();
    }

    /**
     * 设置组件（activity, receiver, service, provider）的启用状态
     *
     * @param context
     * @param cls
     * @param enabled
     */
    public static void setComponentEnabledSetting(Context context, Class<?> cls, boolean enabled) {
        context.getPackageManager().setComponentEnabledSetting(new ComponentName(context, cls),
                enabled ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED : PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    /**
     * 获取当前屏幕亮度，范围0-255
     *
     * @param context
     * @return 屏幕当前亮度值
     */
    public static int getScreenBrightness(Context context) {
        int rightnessValue = 0;
        try {
            rightnessValue = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (SettingNotFoundException e) {
            e.printStackTrace();
        }
        return rightnessValue;
    }

    /**
     * 设置屏幕亮度（0-255）
     *
     * @param activity
     * @param screenBrightness
     */
    public static void setScreenBrightness(Activity activity, float screenBrightness) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.screenBrightness = screenBrightness / 255f;
        activity.getWindow().setAttributes(lp);
    }

    /**
     * 判断是否开启了自动亮度调节
     *
     * @param context
     * @return
     */
    public static boolean isAutomicBrightness(Context context) {
        boolean automicBrightness = false;
        try {
            automicBrightness = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
        } catch (SettingNotFoundException e) {
            e.printStackTrace();
        }
        return automicBrightness;
    }

    /**
     * 开启亮度自动调节
     *
     * @param context
     */
    public static void startAutoBrightness(Context context) {
        Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
    }

    /**
     * 停止自动亮度调节
     *
     * @param context
     */
    public static void stopAutoBrightness(Context context) {
        Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
    }


    /**
     * 调整程序声音类型为媒体播放声音，并且与媒体播放声音大小一致
     *
     * @param context
     */
    public static void adjustVoiceToSystemSame(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_SAME, 0);
    }

    /**
     * sdcard是否可读写
     *
     * @return
     */
    public static boolean isSdcardReady() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * SD卡是否可用
     *
     * @return
     */
    public static boolean isSdcardAvailable() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File sdcardDir = Environment.getExternalStorageDirectory();
            StatFs sf = new StatFs(sdcardDir.getPath());
            long availCount = sf.getAvailableBlocks();
            long blockSize = sf.getBlockSize();
            long availSize = availCount * blockSize / 1024;

            if (availSize >= 3072) {
                return true;
            } else {
                return false;
            }
        }

        return false;
    }

    /**
     * 获取文件系统的剩余空间，单位：KB
     *
     * @return
     */
    public static long getFileSystemAvailableSize(File dirName) {
        long availableSize = -1;
        if (dirName != null && dirName.exists()) {
            StatFs sf = new StatFs(dirName.getPath());
            long blockSize = sf.getBlockSize();
            long blockCount = sf.getBlockCount();
            long availableBlocks = sf.getAvailableBlocks();
            availableSize = availableBlocks * blockSize / 1024;
//            if (LogFeinno.DEBUG) {
//                LogFeinno.d(TAG, "blockSize = " + blockSize + ", blockCount = " + blockCount + ", totalSize = " + blockSize * blockCount / 1024 + " KB" + "\navailableBlocks = " + availableBlocks
//                        + ", availableSize = " + availableSize + " KB");
//            }
        }
        return availableSize;
    }

    /**
     * 通过dip获取像素
     *
     * @param
     * @param dip
     * @return 转换后的像素值
     */
    public static int dip2px(Context context, int dip) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        manager.getDefaultDisplay().getMetrics(displayMetrics);
        return (int) (dip * displayMetrics.density + 0.5f); // 错误的方式：context.getResources().getDisplayMetrics()
    }

    /**
     * 通过像素获取dip
     *
     * @param activity
     * @param pix      像素值
     * @return 转换后的dip
     */
    public static int px2dip(Activity activity, int pix) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return (int) (pix / displayMetrics.density + 0.5f); // 错误的方式：context.getResources().getDisplayMetrics()
    }

    /**
     * 获取Apn类型，该数值为服务器端支持的数值；
     *
     * @param context
     * @return
     */
    public static int getApnType(Context context) {
        return getApnType(((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo());
    }

    /**
     * 获取Apn类型，该数值为服务器端支持的数值；
     *
     * @param networkInfo
     * @return
     */
    private static int getApnType(NetworkInfo networkInfo) {
        int apnType = 0;
        if (networkInfo != null) {
            switch (networkInfo.getType()) {
                case ConnectivityManager.TYPE_WIFI:
                    apnType = APN_WIFI;
                    break;
                case ConnectivityManager.TYPE_MOBILE:
                    String extraInfo = networkInfo.getExtraInfo();
                    if (!TextUtils.isEmpty(extraInfo)) {
                        String extraInfoLowerCase = extraInfo.toLowerCase();
                        if (extraInfoLowerCase.contains("cmnet")) {
                            apnType = APN_CMNET;
                        } else if (extraInfoLowerCase.contains("cmwap")) {
                            apnType = APN_CMWAP;
                        }
                    }
                    break;
            }
        }
        return apnType;
    }

    public static boolean isNetworkConnected(Context context){
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return null == networkInfo ?false:true;
    }
    public static boolean isNetworkWifi(Context context){
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if(networkInfo != null){
            if(networkInfo.getType() == ConnectivityManager.TYPE_WIFI)
                return true;
        }
        return false;
    }
    public static boolean isChinaMobileNetwork(Context context){
        int type = getApnType(context);
        if(type == APN_CMNET || type == APN_CMWAP)
            return true;
        return false;
    }

    /**
     * 获取统计信息<br>
     * 格式：imsi:xxxxx;apn:xxxxx;imei:xxxxx;sdk:xxxxx;Phone Model:xxxxx
     *
     * @return
     */
    public static String getStatistics(Context context) {
        StringBuffer sb = new StringBuffer();
        sb.append("imsi:").append(getImsi(context)).append(';').append("apn:").append(getApnType(context)).append(';').append("imei:")
                .append(getDeviceId(context)).append(';').append("sdk:").append(Build.VERSION.SDK_INT).append(';').append("Phone Model:").append(Build.MODEL);
        return sb.toString();
    }

    /**
     * 程序是否在前台运行
     *
     * @return
     */
    public static boolean isAppOnForeground(Context context) {
        // Returns a list of application processes that are running on the
        // device

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = context.getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }

    /**
     * 获取包管理器
     *
     * @return
     */
    public PackageManager getPackageManager() {
        if (packageManager == null) {
            packageManager = context.getPackageManager();
        }
        return packageManager;
    }

    /**
     * 获取包信息
     *
     * @return
     */
    public PackageInfo getPackageInfo() {
        try {
            PackageInfo packInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packInfo;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取包名
     *
     * @return
     */
    public String getPackageName() {
        return getPackageInfo().packageName;
    }

    /**
     * 获取应用渠道号
     *
     * @return
     */
    public final String getMetaData(String meta_data_key) {
        try {
            PackageManager packageManager = getPackageManager();
            ApplicationInfo applicationInfo = packageManager
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            if (applicationInfo != null && applicationInfo.metaData != null) {
                return String.valueOf(applicationInfo.metaData
                        .get(meta_data_key));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取平台来源
     *
     * @return
     */
    public final String getSourceId() {
        return "8888";
    }

    /**
     * 检查应用是否拥有某权限
     */
    public boolean hasPermission(String permission) {
        PackageManager pm = getPackageManager();
        boolean isFlag = (PackageManager.PERMISSION_GRANTED == pm
                .checkPermission(permission, getPackageName()));
        if (isFlag) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取签名信息
     * @return
     */
    public String getSingInfo() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            Signature[] signs = packageInfo.signatures;
            Signature sign = signs[0];
            return getMessageDigest(sign.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析应用签名md5值
     *
     * @param paramArrayOfByte
     * @return
     */
    public String getMessageDigest(byte[] paramArrayOfByte) {
        char[] arrayOfChar1 = { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98,
                99, 100, 101, 102 };
        try {
            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
            localMessageDigest.update(paramArrayOfByte);
            byte[] arrayOfByte = localMessageDigest.digest();
            int i = arrayOfByte.length;
            char[] arrayOfChar2 = new char[i * 2];
            int j = 0;
            int k = 0;
            while (true) {
                if (j >= i)
                    return new String(arrayOfChar2);
                int m = arrayOfByte[j];
                int n = k + 1;
                arrayOfChar2[k] = arrayOfChar1[(0xF & m >>> 4)];
                k = n + 1;
                arrayOfChar2[n] = arrayOfChar1[(m & 0xF)];
                j++;
            }
        } catch (Exception localException) {
        }
        return null;
    }

    /**
     * 随机生成32位字符
     *
     */
    public static String generateNonce32() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("-", "");
    }

    /**
     * 网络状态 0-wifi 1-4g 2-other
     * @param context
     * @return
     */
    public int  getNetworkIntType(Context context) {
        int strNetworkType = 0;
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                strNetworkType = 0;
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                String _strSubTypeName = networkInfo.getSubtypeName();
                // TD-SCDMA   networkType is 17
                int networkType = networkInfo.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                        strNetworkType = 2;
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                    case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                    case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                        strNetworkType = 2;
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                        strNetworkType = 1;
                        break;
                    default:
                        // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                        if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA") || _strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                            strNetworkType = 2;
                        } else {
                            strNetworkType = 2;
                        }
                        break;
                }
            }
        }
        return strNetworkType;
    }
}