package tqdream.other;

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
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    public static String simpleMapToJsonStr(Map<String, Object> values) {
        if (values == null || values.isEmpty()) {
            return "null";
        }
        String jsonStr = "{";
        Set<?> keySet = values.keySet();
        for (Object key : keySet) {
            jsonStr += "\"" + key + "\":\"" + values.get(key) + "\",";
        }
        jsonStr = jsonStr.substring(0, jsonStr.length() - 1);
        jsonStr += "}";
        return jsonStr;
    }


    /**
     * ListView中提前测量View尺寸，如headerView
     * <p>用的时候去掉注释拷贝到ListView中即可</p>
     * <p>参照以下注释代码</p>
     *
     * @param view 视图
     */
    public static void measureViewInLV(View view) {
        Log.d("tips", "U should copy the following code.");
        /*
        ViewGroup.LayoutParams p = view.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int width = ViewGroup.getChildMeasureSpec(0, 0, p.width);
        int height;
        int tempHeight = p.height;
        if (tempHeight > 0) {
            height = MeasureSpec.makeMeasureSpec(tempHeight,
                    MeasureSpec.EXACTLY);
        } else {
            height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        view.measure(width, height);
        */
    }


}
