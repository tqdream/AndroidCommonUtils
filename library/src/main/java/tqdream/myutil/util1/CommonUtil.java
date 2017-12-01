package tqdream.myutil.util1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.List;

/**
 * @author taoqiang @create on 2017/2/10 13:59
 * @desc
 */

public class CommonUtil {
    private static long lastClickTime;
    /**
     * 判断是否为连击
     *
     * @return  boolean
     */
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 释放图片资源
     * @param vi
     */
    public void freesImageViewResource(View vi) {
        if (vi instanceof ImageView) {
            Drawable d = ((ImageView) vi).getDrawable();
            if (d instanceof BitmapDrawable) {
                Bitmap bmp = ((BitmapDrawable) d).getBitmap();
                bmp.recycle();
            }
            ((ImageView) vi).setImageBitmap(null);
            if (d != null) {
                d.setCallback(null);
            }
        } else if (vi instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) vi).getChildCount(); i++) {
                freesImageViewResource(((ViewGroup) vi).getChildAt(i));
            }
        }
    }


    /**
     * 照相功能 调用系统相机
     * imgPath指定照片存储路径
     */
    public static void cameraMethod(Activity activity, int RESULT_CAPTURE_IMAGE,
                                    String imgPath) {
        try {
            Intent imageCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File out = new File(imgPath);
            Uri uri = Uri.fromFile(out);
            try {// 尽可能调用系统相机
                String cameraPackageName = getCameraPhoneAppInfos(activity);
                if (cameraPackageName == null) {
                    cameraPackageName = "com.android.camera";
                }
                final Intent intent_camera = activity.getPackageManager()
                        .getLaunchIntentForPackage(cameraPackageName);
                if (intent_camera != null) {
                    imageCaptureIntent.setPackage(cameraPackageName);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            imageCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            imageCaptureIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            activity.startActivityForResult(imageCaptureIntent,
                    RESULT_CAPTURE_IMAGE);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // 对使用系统拍照的处理

    private static String getCameraPhoneAppInfos(Activity context) {
        try {
            String strCamera = "";
            List<PackageInfo> packages = context.getPackageManager()
                    .getInstalledPackages(0);
            for (int i = 0; i < packages.size(); i++) {
                try {
                    PackageInfo packageInfo = packages.get(i);
                    String strLabel = packageInfo.applicationInfo.loadLabel(
                            context.getPackageManager()).toString();
                    // 一般手机系统中拍照软件的名字
                    if ("相机,照相机,照相,拍照,摄像,Camera,camera".contains(strLabel)) {
                        strCamera = packageInfo.packageName;
                        if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (strCamera != null) {
                return strCamera;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断app手否处于debug模式
     * @param context
     * @return
     */

    public static boolean isApkDebugable(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {

        }
        return false;
    }

    public static boolean isApkDebugable(Context context, String packageName) {
        try {
            PackageInfo pkginfo = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            if (pkginfo != null) {
                ApplicationInfo info = pkginfo.applicationInfo;
                return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
            }

        } catch (Exception e) {

        }
        return false;
    }


    /**
     * 指定小数输出
     *
     * @param s      输入
     * @param format 小数格式，比如保留两位0.00
     * @return 输出结果
     */
    public static String decimalFormat(double s, String format) {
        DecimalFormat decimalFormat = new DecimalFormat(format);
        return decimalFormat.format(s);
    }


    /**
     * 把Bitmap转Byte
     *
     * @param bitmap bitmap对象
     * @return Bytes
     */
    public static byte[] bitmap2Bytes(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }


    /**
     * MD5加密
     *
     * @param plainText 需要加密的字符串
     * @return 加密后字符串
     */
    public static String md5(String plainText) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString().toLowerCase();// 32位的加密（转成小写）

            buf.toString().substring(8, 24);// 16位的加密

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 安装apk
     *
     * @param context 上下文
     * @param path    文件路劲
     */
    public static void installAPK(Context context, String path) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 直接拨号，需要增加CALL_PHONE权限
     *
     * @param context 上下文
     * @param phone   手机号码
     */
    public static void actionCall(Context context, String phone) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
        intent.setAction(Intent.ACTION_CALL);// 直接拨号
        context.startActivity(intent);
    }

    /**
     * 跳到拨号盘-拨打电话
     *
     * @param context 上下文
     * @param phone   手机号码
     */
    public static void actionDial(Context context, String phone) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
        intent.setAction(Intent.ACTION_DIAL);// 拨号盘
        context.startActivity(intent);
    }

}
