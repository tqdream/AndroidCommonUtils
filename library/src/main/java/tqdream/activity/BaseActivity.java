package tqdream.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidcommonutil.tqdreamlibrary.R;
import tqdream.ok.widget.TitleBar;

public class BaseActivity extends AppCompatActivity {

    private static final String TAG = BaseActivity.class.getSimpleName();
    protected Context mContext;
    /**
     * 整个标题栏View
     */
    protected TitleBar mTitleBarView;

    private boolean mIsNoTitle;
    //    protected ProgressDialogF mProgressDialog;
    public RelativeLayout mLayoutBaseTitlebarMiddle;
    /**
     * 是否注册EventBus
     * ACt处于OnPause状态时是否需要销毁EventBus监听，默认是不销毁的，但是会在所有的Act的OnDestory方法中销毁EventBus监听
     */
    private boolean eventEnable = false;
    private boolean eventUnregit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
    }

    @Override
    public void setContentView(int layoutResId) {
        super.setContentView(R.layout.activity_base_layout);
        getLayoutInflater().inflate(layoutResId, (ViewGroup) findViewById(R.id.layout_base_content)); // 将子布局填充到父布局中
        mTitleBarView = (TitleBar) findViewById(R.id.layout_base_title_bar);
        mTitleBarView.setVisibility(mIsNoTitle ? View.GONE : View.VISIBLE);

        if (!mIsNoTitle) {
            mTitleBarView.setOnTitleBarListener(new TitleBar.OnTitleBarListener() {
                @Override
                public void onTitleBarClick(View view) {
                    if (view == mTitleBarView.getLeftView()) {
                        Log.e("base", "左侧");
                    } else if (view == mTitleBarView.getRightView()) {
                        Log.e("base", "右侧");
                    }
                }
            });
        }
    }

    /**
     * 设置窗口无标题栏（必须在super.onCreate(savedInstanceState);之前调用） 默认是显示title <br>
     * true 不显示<br>
     * false 显示<br>
     */
    protected void requestWindowNoTitle(boolean isNoTitle) {
        mIsNoTitle = isNoTitle;
    }

    protected void showOrHideTitleBar(int visibility) {
        mTitleBarView.setVisibility(visibility);
    }

    protected void onTitleBackPressed() {
        dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
        dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK));
    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }


//    /**
//     * 显示处理提示框
//     *
//     * @param messageId
//     */
//    public void showBaseLoadingDialog(int messageId) {
//        if (mProgressDialog == null) {
//            mProgressDialog = new ProgressDialogF(this);
//            mProgressDialog.setIndeterminate(true);
//            mProgressDialog.setCanceledOnTouchOutside(false);
//            mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                @Override
//                public void onCancel(DialogInterface dialog) {
//
//                }
//            });
//        }
//        if (!mProgressDialog.isShowing()) {
//            mProgressDialog.show();
//        }
//        mProgressDialog.getWindow().setContentView(R.layout.rf_login_loading_alertdialog_layout);
//        TextView tvLoginLoadingAlertdialogText = (TextView) mProgressDialog.getWindow().findViewById(R.id.tv_login_loading_alertdialog_text);
//        tvLoginLoadingAlertdialogText.setText(getString(messageId));
//
//    }
//
//    /**
//     * 显示处理提示框(按后退键不可取消)
//     *
//     * @param messageId
//     */
//    protected void showNoCancelableLoadingDialog(int messageId) {
//        if (mProgressDialog == null) {
//            mProgressDialog = new ProgressDialogF(this);
//            mProgressDialog.setIndeterminate(true);
//            mProgressDialog.setCanceledOnTouchOutside(false);
//            mProgressDialog.setCancelable(false);
//            mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                @Override
//                public void onCancel(DialogInterface dialog) {
//
//                }
//            });
//        }
//        if (!mProgressDialog.isShowing()) {
//            mProgressDialog.show();
//        }
//        mProgressDialog.getWindow().setContentView(R.layout.rf_login_loading_alertdialog_layout);
//        TextView tvLoginLoadingAlertdialogText = (TextView) mProgressDialog.getWindow().findViewById(R.id.tv_login_loading_alertdialog_text);
//        tvLoginLoadingAlertdialogText.setText(getString(messageId));
//
//    }
//
//    /**
//     * 隐藏处理提示框
//     */
//    protected void dismissBaseLoadingDialog() {
//        if (mProgressDialog != null && mProgressDialog.isShowing()) {
//            mProgressDialog.dismiss();
//        }
//    }
//
//
//
//    public void popError(String errorTitle, String errorMsg) {
//        PromptDialog.Builder builder = new PromptDialog.Builder(this);
//        builder.setMessage(errorMsg);
//        builder.setTitle(errorTitle);
//        builder.setPositiveButton(R.string.rf_str_common_btn_ok, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                }
//        );
//        PromptDialog dialog = builder.createDialog();
//        dialog.show();
//    }
//
//    /**
//     * 从底部弹出的confirm
//     * @param context
//     * @param style
//     * @return
//     */
//    public Dialog createDialog(Context context, int style) {
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        LinearLayout dialogView = (LinearLayout) inflater.inflate(R.layout.rf_setting_backup_push_dialog, null);
//        final Dialog customDialog = new Dialog(context, style);
//
//        WindowManager.LayoutParams localLayoutParams = customDialog.getWindow().getAttributes();
//        localLayoutParams.gravity = Gravity.BOTTOM | Gravity.LEFT;
//        localLayoutParams.x = 0;
//        localLayoutParams.y = 0;
//
//        int screenWidth = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();
//        dialogView.setMinimumWidth(screenWidth);
//        // dialogView.setMinimumHeight(10);
//
//        customDialog.onWindowAttributesChanged(localLayoutParams);
//        customDialog.getWindow().setSoftInputMode(
//                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
//                        | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
//        customDialog.setCanceledOnTouchOutside(false);
//        customDialog.setCancelable(true);
//        customDialog.setCanceledOnTouchOutside(true);
//        customDialog.setContentView(dialogView);
//
//        return customDialog;
//    }
}
