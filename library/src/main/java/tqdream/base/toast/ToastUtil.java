package tqdream.base.toast;


import android.content.Context;
import android.widget.Toast;


/**
 * @author taoqiang @create on 2016/10/26 8:45
 * @email tqdream@163.com
 */
public class ToastUtil {
    private Toast mToast;
    private Context mContext;
    private static ToastUtil sInstance;

    private ToastUtil(Context context) {
        mContext = context;
    }

    public static ToastUtil getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new ToastUtil(context.getApplicationContext());
        }
        return sInstance;
    }

    public void showLongMsg(String msg) {
        makeText(mContext, msg, Toast.LENGTH_LONG).show();
    }

    /**
     * @param resId 资源ID
     */
    public void showLongMsg(int resId) {
        makeText(mContext, mContext.getResources().getString(resId), Toast.LENGTH_LONG).show();

    }

    public void showShortMsg(String msg) {
        makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * @param resId 资源ID
     */
    public void showShortMsg(int resId) {
        makeText(mContext, mContext.getResources().getString(resId), Toast.LENGTH_SHORT).show();
    }

    private Toast makeText(Context context, String message, int showTime) {
        if (mToast == null) {
            mToast = Toast.makeText(context, message, showTime);
        } else {
            mToast.setDuration(showTime);
            mToast.setText(message);
        }
        return mToast;
    }
}
