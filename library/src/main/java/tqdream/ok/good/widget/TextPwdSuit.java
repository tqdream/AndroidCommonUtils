/*Copyright ©2015 TommyLemon(https://github.com/TommyLemon)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

package tqdream.ok.good.widget;

import android.support.annotation.IntDef;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 带icon按钮EditText或TextView套件
 *
 * @author taoqiang
 * @use new TextPwdSuit().addClearListener(...);
 */
public class TextPwdSuit {
    private static final String TAG = "TextPwdSuit";

    private TextView tv;
    private View eyeView;

    private String inputedString;

    public TextView getTextView() {
        return tv;
    }

    public View getEyeView() {
        return eyeView;
    }

    public String getInputedString() {
        return inputedString;
    }


    public static final int BLANK_TYPE_DEFAULT = 0;
    public static final int BLANK_TYPE_TRIM = 1;
    public static final int BLANK_TYPE_NO_BLANK = 2;

    @IntDef({BLANK_TYPE_DEFAULT, BLANK_TYPE_TRIM, BLANK_TYPE_NO_BLANK})
    @Retention(RetentionPolicy.SOURCE)
    public @interface BlankType {
    }

    public static final int ALWAYS = 0;
    public static final int INVISIBLE = 1;
    public static final int GONE = 2;

    @IntDef({ALWAYS, INVISIBLE, GONE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface EyeView {
    }

    /**
     * 默认trim，隐藏方式为gone
     *
     * @param tv
     * @param eyeView
     */
    public void addClearListener(final TextView tv, final View eyeView) {
        addClearListener(tv, BLANK_TYPE_DEFAULT, eyeView, INVISIBLE);
    }

    /**
     * 默认隐藏方式为gone
     *
     * @param tv
     * @param blankType
     * @param eyeView
     */
    public void addClearListener(final TextView tv, @BlankType final int blankType, final View eyeView) {
        addClearListener(tv, blankType, eyeView, INVISIBLE);
    }

    /**
     * @param tv                 输入框
     * @param blankType          et内容前后是否不能含有空格
     * @param eyeView            清除输入框内容按钮
     * @param eyeViewVisibleType 如果et输入为空，隐藏clearView的方式为gone(false)还是invisible(true)
     */
    public void addClearListener(final TextView tv, @BlankType final int blankType, final View eyeView, @EyeView final int eyeViewVisibleType) {
        if (tv == null || eyeView == null) {
            Log.e(TAG, "addClearListener  (tv == null || eyeView == null)  >> return;");
            return;
        }

        this.tv = tv;
        this.eyeView = eyeView;
        if (tv.getText() != null) {
            inputedString = tv.getText().toString();
        }

        if (eyeViewVisibleType == ALWAYS) {
            eyeView.setVisibility(View.VISIBLE);
        } else {
            eyeView.setVisibility(isNotEmpty(tv, false) ? View.VISIBLE : View.GONE);
        }

        //默认隐藏密码
        tv.setTransformationMethod(PasswordTransformationMethod.getInstance());

        eyeView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.setTransformationMethod(
                        tv.getTransformationMethod() == PasswordTransformationMethod.getInstance() ?
                                HideReturnsTransformationMethod.getInstance() : PasswordTransformationMethod.getInstance());
                tv.requestFocus();
                if (tv instanceof EditText)
                    ((EditText) tv).setSelection(tv.length());
            }
        });

        if (blankType == BLANK_TYPE_NO_BLANK) {
            InputFilter filter = new InputFilter() {
                @Override
                public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                    if (source.equals(" ")) return "";
                    else return null;
                }
            };
            tv.setFilters(new InputFilter[]{filter});
        }

        tv.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s == null || isEmpty(s.toString(), false)) {
                    inputedString = "";
                    if (eyeViewVisibleType == INVISIBLE) {
                        eyeView.setVisibility(View.INVISIBLE);
                    } else {
                        eyeView.setVisibility(View.GONE);
                    }
                } else {
                    inputedString = "" + s.toString();
                    if (inputedString.contains(" ")) {
                        if (blankType == BLANK_TYPE_TRIM
                                && (String.valueOf(inputedString.charAt(0)).equals(" ") || (String.valueOf(inputedString.charAt(inputedString.length() - 1)).equals(" ")))) {
                            inputedString = inputedString.trim();

                            tv.setText(inputedString);
                            if (tv instanceof EditText)
                                ((EditText) tv).setSelection(inputedString.length());
                        }
                    }

                    if (inputedString.length() > 0 && eyeView.getVisibility() != View.VISIBLE)
                        eyeView.setVisibility(View.VISIBLE);
                }

                if (listener != null)
                    listener.onTextChanged(s, start, before, count);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public interface onTextChangedListener {
        void onTextChanged(CharSequence s, int start, int before, int count);
    }

    public onTextChangedListener listener;

    public void addTextChangedListener(onTextChangedListener onTextChangedListener) {
        listener = onTextChangedListener;
    }


    /**
     * 判断字符是否非空
     *
     * @param tv
     * @param trim
     * @return
     */
    public static boolean isNotEmpty(TextView tv, boolean trim) {
        return isNotEmpty(get(tv), trim);
    }

    /**
     * 判断字符是否非空
     *
     * @param s
     * @param trim
     * @return
     */
    public static boolean isNotEmpty(String s, boolean trim) {
        return !isEmpty(s, trim);
    }

    /**
     * 判断字符是否为空
     *
     * @param s
     * @param trim
     * @return
     */
    public static boolean isEmpty(String s, boolean trim) {
        //		Log.i(TAG, "isEmpty   s = " + s);
        if (s == null) {
            return true;
        }
        if (trim) {
            s = s.trim();
        }
        if (s.length() <= 0) {
            return true;
        }
        return false;
    }

    /**
     * 获取string,为null则返回""
     *
     * @param tv
     * @return
     */
    public static String get(TextView tv) {
        if (tv == null || tv.getText() == null) {
            return "";
        }
        return tv.getText().toString();
    }
}
