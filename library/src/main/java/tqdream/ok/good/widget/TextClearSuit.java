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

import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 带清除按钮EditText或TextView套件，如果输入为空则隐藏清除按钮
 *
 * @author taoqiang
 * @use new TextClearSuit().addClearListener(...);
 */
public class TextClearSuit {
    private static final String TAG = "TextClearSuit";

    private TextView tv;
    private View clearView;

    private String inputedString;

    public TextView getTextView() {
        return tv;
    }

    public View getClearView() {
        return clearView;
    }

    public String getInputedString() {
        return inputedString;
    }


    public static final int BLANK_TYPE_DEFAULT = 0;
    public static final int BLANK_TYPE_TRIM = 1;
    public static final int BLANK_TYPE_NO_BLANK = 2;

    /**
     * 默认trim，隐藏方式为gone
     *
     * @param tv
     * @param clearView
     */
    public void addClearListener(final TextView tv, final View clearView) {
        addClearListener(tv, BLANK_TYPE_DEFAULT, clearView, false);
    }

    /**
     * 默认隐藏方式为gone
     *
     * @param tv
     * @param blankType
     * @param clearView
     */
    public void addClearListener(final TextView tv, final int blankType, final View clearView) {
        addClearListener(tv, blankType, clearView, false);
    }

    /**
     * @param tv                   输入框
     * @param blankType            et内容前后是否不能含有空格
     * @param clearView            清除输入框内容按钮
     * @param isClearViewInvisible 如果et输入为空，隐藏clearView的方式为gone(false)还是invisible(true)
     */
    public void addClearListener(final TextView tv, final int blankType, final View clearView, final boolean isClearViewInvisible) {
        if (tv == null || clearView == null) {
            Log.e(TAG, "addClearListener  (tv == null || clearView == null)  >> return;");
            return;
        }

        this.tv = tv;
        this.clearView = clearView;
        if (tv.getText() != null) {
            inputedString = tv.getText().toString();
        }

        clearView.setVisibility(isNotEmpty(tv, false) ? View.VISIBLE : View.GONE);
        clearView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.setText("");
                tv.requestFocus();
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
                    if (isClearViewInvisible) {
                        clearView.setVisibility(View.INVISIBLE);
                    } else {
                        clearView.setVisibility(View.GONE);
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

                    if (inputedString.length() > 0 && clearView.getVisibility() != View.VISIBLE)
                        clearView.setVisibility(View.VISIBLE);
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
