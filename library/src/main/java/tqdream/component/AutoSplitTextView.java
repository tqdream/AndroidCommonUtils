package tqdream.component;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

public class AutoSplitTextView extends android.support.v7.widget.AppCompatTextView {
    private boolean mEnabled = true;

    public AutoSplitTextView(Context context) {
        super(context);
    }

    public AutoSplitTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoSplitTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    
    public void setAutoSplitEnabled(boolean enabled) {
        mEnabled = enabled;
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY 
            && MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY
            && getWidth() > 0 
            && getHeight() > 0
            && mEnabled) {
            String newText = autoSplitText(this);
            if (!TextUtils.isEmpty(newText)) {
                setText(newText);
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    
    private String autoSplitText(final TextView tv) {
        final String rawText = tv.getText().toString(); //鍘熷鏂囨湰
        final Paint tvPaint = tv.getPaint(); //paint锛屽寘鍚瓧浣撶瓑淇℃伅
        final float tvWidth = tv.getWidth() - tv.getPaddingLeft() - tv.getPaddingRight(); //鎺т欢鍙敤瀹藉害
        
        //灏嗗師濮嬫枃鏈寜琛屾媶鍒?
        String [] rawTextLines = rawText.replaceAll("\r", "").split("\n");
        StringBuilder sbNewText = new StringBuilder();
        for (String rawTextLine : rawTextLines) {
            if (tvPaint.measureText(rawTextLine) <= tvWidth) {
                //濡傛灉鏁磋瀹藉害鍦ㄦ帶浠跺彲鐢ㄥ搴︿箣鍐咃紝灏变笉澶勭悊浜?
                sbNewText.append(rawTextLine);
            } else {
                //濡傛灉鏁磋瀹藉害瓒呰繃鎺т欢鍙敤瀹藉害锛屽垯鎸夊瓧绗︽祴閲忥紝鍦ㄨ秴杩囧彲鐢ㄥ搴︾殑鍓嶄竴涓瓧绗﹀鎵嬪姩鎹㈣
                float lineWidth = 0;
                for (int cnt = 0; cnt != rawTextLine.length(); ++cnt) {
                    char ch = rawTextLine.charAt(cnt);
                    lineWidth += tvPaint.measureText(String.valueOf(ch));
                    if (lineWidth <= tvWidth) {
                        sbNewText.append(ch);
                    } else {
                        sbNewText.append("\n");
                        lineWidth = 0;
                        --cnt;
                    }
                }
            }
            sbNewText.append("\n");
        }
        
        //鎶婄粨灏惧浣欑殑\n鍘绘帀
        if (!rawText.endsWith("\n")) {
            sbNewText.deleteCharAt(sbNewText.length() - 1);
        }
        
        return sbNewText.toString();
    }
}