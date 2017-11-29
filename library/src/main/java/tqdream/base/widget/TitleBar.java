package tqdream.base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidcommonutil.tqdreamlibrary.R;

/**
 * @author taoqiang @create on 2016/10/21 15:42
 * @desc 自定义titlebar
 */
public class TitleBar extends FrameLayout {

    //左侧布局
    TextView tvLeft;

    //中间布局
    LinearLayout llTitleLayout;
    TextView tvTitle;
    ImageView ivTitle;

    //右侧布局
    LinearLayout llRightLayout;
    ImageView ivRight;
    TextView tvRight;

    //根布局
    RelativeLayout rlTitleBar;

    private OnTitleBarListener onTitleBarListener;

    //中间文字默认字体大小
    private int defaultTextSize = 18;
    //左右文字默认字体大小
    private int defaultTextSize_small = 14;

    private int defaultBarHeight = 50;

    //默认颜色
    private int defaultTextColor = Color.parseColor("#ffffff");

    private String titleText;
    private String leftText;
    private String rightText;

    private float titleTextSize;
    private float leftTextSize;
    private float rightTextSize;

    private int titleTextColor;
    private int leftTextColor;
    private int rightTextColor;

    private Drawable rightImage;
    private Drawable leftImage;
    private Drawable centerImage;

    private static final int[] ATTRS = new int[]{
            android.R.attr.text,
            android.R.attr.textSize,
            android.R.attr.textColor,
    };

    public TitleBar(Context context) {
        super(context, null);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
        initListenter();
    }

    private void init(Context context, AttributeSet attrs) {
        View view = View.inflate(context, R.layout.layout_titlebar, this);

        //根布局
        rlTitleBar = (RelativeLayout) view.findViewById(R.id.rl_base_title_bar);
        rlTitleBar.setLayoutParams(initLayoutParams(context, defaultBarHeight));

        //左侧布局
        tvLeft = (TextView) view.findViewById(R.id.tv_left);

        //中间布局
        llTitleLayout = (LinearLayout) view.findViewById(R.id.ll_center_layout);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        ivTitle = (ImageView) view.findViewById(R.id.iv_title);

        //右侧布局
        llRightLayout = (LinearLayout) view.findViewById(R.id.ll_right_layout);
        tvRight = (TextView) view.findViewById(R.id.tv_right);
        ivRight = (ImageView) view.findViewById(R.id.iv_right);


        DisplayMetrics dm = getResources().getDisplayMetrics();
//        TypedArray a = context.obtainStyledAttributes(attrs, ATTRS);
//        titleText = a.getString(R.styleable.TitleBar_android_text);
//        a.recycle();

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TitleBar);

        titleText = a.getString(R.styleable.TitleBar_tBar_titleText);
        leftText = a.getString(R.styleable.TitleBar_tBar_leftText);
        rightText = a.getString(R.styleable.TitleBar_tBar_rightText);

        defaultTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, defaultTextSize, dm);
        defaultTextSize_small = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, defaultTextSize_small, dm);

        titleTextSize = a.getDimensionPixelSize(R.styleable.TitleBar_tBar_titleTextSize, defaultTextSize);
//        titleTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, titleTextSize, dm);

        leftTextSize = a.getDimensionPixelSize(R.styleable.TitleBar_tBar_leftTextSize, defaultTextSize_small);
//        leftTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, leftTextSize, dm);

        rightTextSize = a.getDimensionPixelSize(R.styleable.TitleBar_tBar_rightTextSize, defaultTextSize_small);
//        rightTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, rightTextSize, dm);

        titleTextColor = a.getColor(R.styleable.TitleBar_tBar_titleTextColor, defaultTextColor);
        leftTextColor = a.getColor(R.styleable.TitleBar_tBar_leftTextColor, defaultTextColor);
        rightTextColor = a.getColor(R.styleable.TitleBar_tBar_rightTextColor, defaultTextColor);

        rightImage = a.getDrawable(R.styleable.TitleBar_tBar_rightImage);
        leftImage = a.getDrawable(R.styleable.TitleBar_tBar_leftImage);
        centerImage = a.getDrawable(R.styleable.TitleBar_tBar_centerImage);
        a.recycle();

//        ivLeft.setImageResource(R.drawable.back);

        //控制左侧布局的显示和隐藏
        if (TextUtils.isEmpty(leftText) && leftImage == null) {
            tvLeft.setVisibility(GONE);
        } else {
            tvLeft.setVisibility(VISIBLE);
            if (!TextUtils.isEmpty(leftText) && leftImage != null) {
                tvLeft.setText(leftText);
                //设置左侧字体大小
                tvLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX, leftTextSize);
                //设置左侧文字颜色
                tvLeft.setTextColor(leftTextColor);
                leftImage.setBounds(0, 0, leftImage.getMinimumWidth(), leftImage.getMinimumHeight());
                tvLeft.setCompoundDrawables(leftImage, null, null, null);
            } else if (!TextUtils.isEmpty(leftText) && leftImage == null) {
                tvLeft.setText(leftText);
                //设置左侧字体大小
                tvLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX, leftTextSize);
                //设置左侧文字颜色
                tvLeft.setTextColor(leftTextColor);
                tvLeft.setCompoundDrawables(null, null, null, null);
            } else if (TextUtils.isEmpty(leftText) && leftImage != null) {
                tvLeft.setText("");
                leftImage.setBounds(0, 0, leftImage.getMinimumWidth(), leftImage.getMinimumHeight());
                tvLeft.setCompoundDrawables(leftImage, null, null, null);
            }
        }

        //控制右侧布局的显示和隐藏
        if (TextUtils.isEmpty(rightText) && rightImage == null) {
            llRightLayout.setVisibility(GONE);
        } else {
            llRightLayout.setVisibility(VISIBLE);
            if (!TextUtils.isEmpty(rightText) && rightImage != null) {
                tvRight.setVisibility(VISIBLE);
                ivRight.setVisibility(VISIBLE);
                //设置右侧文字
                tvRight.setText(rightText);
                //设置右侧字体大小
                tvRight.setTextSize(TypedValue.COMPLEX_UNIT_PX, rightTextSize);
                //设置右侧文字颜色
                tvRight.setTextColor(rightTextColor);
            } else if (!TextUtils.isEmpty(rightText) && rightImage == null) {
                tvRight.setVisibility(VISIBLE);
                ivRight.setVisibility(GONE);
                //设置右侧文字
                tvRight.setText(rightText);
                //设置右侧字体大小
                tvRight.setTextSize(TypedValue.COMPLEX_UNIT_PX, rightTextSize);
                //设置右侧文字颜色
                tvRight.setTextColor(rightTextColor);
            } else if (TextUtils.isEmpty(rightText) && rightImage != null) {
                tvRight.setVisibility(View.GONE);
                ivRight.setVisibility(View.VISIBLE);
                ivRight.setImageDrawable(rightImage);
            }
        }

        //控制中间布局的显示和隐藏
        //设置标题文字
        if (!TextUtils.isEmpty(titleText)) {
            tvTitle.setVisibility(VISIBLE);
            tvTitle.setText(titleText);
            //设置标题文字大小
            tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize);
            //设置标题文字颜色
            tvTitle.setTextColor(titleTextColor);
        } else {
            tvTitle.setVisibility(GONE);
        }

        //设置标题右侧的图片
        if (centerImage != null) {
            ivTitle.setVisibility(VISIBLE);
            ivTitle.setImageDrawable(centerImage);
        } else {
            ivTitle.setVisibility(GONE);
        }
    }

    /**
     * 设置左侧文本
     *
     * @param text
     */
    public void setLeftText(String text) {
        tvLeft.setVisibility(VISIBLE);
        tvLeft.setText(text);
    }

    /**
     * 设置并显示左侧文本
     *
     * @param res
     */
    public void setLeftText(int res) {
        tvLeft.setVisibility(VISIBLE);
        tvLeft.setText(res);
    }

    /**
     * 设置并显示左侧文本字体颜色
     *
     * @param res
     */
    public void setLeftTextColor(int res) {
        tvLeft.setVisibility(VISIBLE);
        tvLeft.setTextColor(res);
    }

    /**
     * 设置并显示左侧文本字体大小
     *
     * @param textSize
     */
    public void setLeftTextSize(float textSize) {
        tvLeft.setVisibility(VISIBLE);
        tvLeft.setTextSize(textSize);
    }


    /**
     * 设置并显示左侧图片
     *
     * @param res
     */
    public void setLeftImage(int res) {
        tvLeft.setVisibility(View.VISIBLE);
        leftImage = getResources().getDrawable(res);
        if (leftImage != null) {
            leftImage.setBounds(0, 0, leftImage.getMinimumWidth(), leftImage.getMinimumHeight());
        }
        tvLeft.setCompoundDrawables(leftImage, null, null, null);
    }

    /**
     * 设置并显示右侧文本
     *
     * @param text
     */
    public void setRightText(String text) {
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText(text);
    }

    /**
     * 设置并显示右侧文本
     *
     * @param resText
     */
    public void setRightText(int resText) {
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText(resText);
    }

    /**
     * 设置右侧文本字体颜色
     *
     * @param resColor
     */
    public void setRightTextColor(int resColor) {
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setTextColor(resColor);
    }

    /**
     * 设置右侧文本字体大小
     *
     * @param res
     */
    public void setRightTextSize(float res) {
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setTextSize(res);
    }

    /**
     * 设置并显示右侧图片
     *
     * @param res
     */
    public void setRightImage(int res) {
        ivRight.setVisibility(View.VISIBLE);
        ivRight.setImageResource(res);
    }

    /**
     * 设置右侧图片的状态
     *
     * @param isEnable
     */
    public void setRightImage(boolean isEnable) {
        ivRight.setVisibility(View.VISIBLE);
        ivRight.setEnabled(isEnable);
    }

    /**
     * 设置标题文字
     *
     * @param text
     */
    public void setTitle(String text) {
        tvTitle.setText(text);
    }

    public void setTitle(int resource) {
        tvTitle.setText(resource);
    }

    public void setTitleBarBackground(Drawable drawable) {
        rlTitleBar.setBackground(drawable);
    }

    public void setTitleBarBackgroundColor(int color) {
        rlTitleBar.setBackgroundColor(color);
    }

    public void setTitleBarBackgroundResource(int resoure) {
        rlTitleBar.setBackgroundResource(resoure);
    }

    public void seTitleBarHeight(Context context, int dpHeight) {
        rlTitleBar.setLayoutParams(initLayoutParams(context, dpHeight));
    }

    public View getLeftView() {
        return tvLeft;
    }

    public View getRightView() {
        return llRightLayout;
    }

    public View getRootView() {
        return rlTitleBar;
    }


    public interface OnTitleBarListener {
        void onTitleBarClick(View view);
    }

    public void setOnTitleBarListener(OnTitleBarListener onTitleBarListener) {
        this.onTitleBarListener = onTitleBarListener;
    }

    void initListenter() {
        tvLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onTitleBarListener != null)
                    onTitleBarListener.onTitleBarClick(view);
            }
        });

        llRightLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onTitleBarListener != null)
                    onTitleBarListener.onTitleBarClick(view);
            }
        });
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public LayoutParams initLayoutParams(Context context, int height) {
        return new LayoutParams(LayoutParams.MATCH_PARENT, dip2px(context, height));
    }


}
