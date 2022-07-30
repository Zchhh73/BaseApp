package com.zch.base.widget;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.zch.base.R;


public class CommonToolBar extends AppBarLayout {
    private Context mContext;
    private Toolbar toolbar;
    private ImageView mLeftIv;
    private ImageView mRightIv;
    private ImageView mSecondRightIv;
    private TextView mLeftTv;
    private TextView mRightTv;
    private TextView mSubTitle;
    private TextView mLeftSubTitle;
    private TextView mTvFixTitle;
    private TextView mSecondRightTv;
    private String mTitle = "";
    private RelativeLayout mRlWrapper;
    private boolean mIsFixedTitle = false;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private float mToolBarHeight = -1F;
    private int mLeftButtonPadding = 0;
    private int mRightButtonPadding = 0;
    private int mToolbarPadding = 0;
    private int mTittleMaxLength = 20;
    OnClickListener mOnLeftClickListener;
    OnClickListener mOnRightClickListener;
    OnClickListener mOnSecondRightClickListener;

    public CommonToolBar(Context context) {
        this(context, null);
    }

    public CommonToolBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = getActivity(this);
        initView(attrs);
    }

    private void initView(AttributeSet attrs) {
        // findViewById拿到各种view
        findViews();

        // 设置自定义属性，很多属性用了局部变量，防止对象占用内存过大
        TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.YDToolBar);
        mTitle = ta.getString(R.styleable.YDToolBar_xTitle);
        mToolbarPadding = (int) ta.getDimension(R.styleable.YDToolBar_xToolbarPadding, 0);
        mTittleMaxLength = ta.getInteger(R.styleable.YDToolBar_xTittleMaxLength, 20);
        mIsFixedTitle = ta.getBoolean(R.styleable.YDToolBar_xFixedTitle, true);
        mToolBarHeight = ta.getDimension(R.styleable.YDToolBar_xCollapsedToolBarHeight, -1);
        mLeftButtonPadding = (int) ta.getDimension(R.styleable.YDToolBar_xLeftButtonPadding, -1F);
        mRightButtonPadding = (int) ta.getDimension(R.styleable.YDToolBar_xRightButtonPadding, -1F);
        String leftButtonText = ta.getString(R.styleable.YDToolBar_xLeftButtonText);
        float leftButtonTextSize = ta.getDimension(R.styleable.YDToolBar_xLeftButtonTextSize, 16);
        int leftButtonTextColor = ta.getColor(R.styleable.YDToolBar_xLeftButtonTextColor, getResources().getColor(R.color.text_color_black));
        int leftButtonWidth = ta.getDimensionPixelSize(R.styleable.YDToolBar_xLeftButtonWidth, 0);
        int leftButtonHeight = ta.getDimensionPixelSize(R.styleable.YDToolBar_xLeftButtonHeight, 0);
        int leftButtonMarginLeft = (int) ta.getDimension(R.styleable.YDToolBar_xLeftButtonMarginLeft, 0F);
        String rightButtonText = ta.getString(R.styleable.YDToolBar_xRightButtonText);
        float rightButtonTextSize = ta.getDimension(R.styleable.YDToolBar_xRightButtonTextSize, 16);
        int rightButtonTextColor = ta.getColor(R.styleable.YDToolBar_xRightButtonTextColor, getResources().getColor(R.color.text_color_black));
        int rightButtonWidth = ta.getDimensionPixelSize(R.styleable.YDToolBar_xRightButtonWidth, 0);
        int rightButtonHeight = ta.getDimensionPixelSize(R.styleable.YDToolBar_xRightButtonHeight, 0);
        int rightButtonMarginRight = (int) ta.getDimension(R.styleable.YDToolBar_xRightButtonMarginRight, 0F);
        int rightButtonBackGround = ta.getResourceId(R.styleable.YDToolBar_xRightButtonImageBackground, 0);
        int leftButtonBackGround = ta.getResourceId(R.styleable.YDToolBar_xLeftButtonImageBackground, 0);
        int collapsedTitleTextAppearance = ta.getResourceId(R.styleable.YDToolBar_xCollapsedTitleTextAppearance, 0);
        int collapsedTitleTextColor = ta.getColor(R.styleable.YDToolBar_xCollapsedTitleTextColor, 0);
        int expandedTitleTextAppearance = ta.getResourceId(R.styleable.YDToolBar_xExpandedTitleTextAppearance, 0);
        int expandedTitleTextColor = ta.getColor(R.styleable.YDToolBar_xExpandedTitleTextColor, 0);
        int collapsedTitleTextStyle = ta.getInt(R.styleable.YDToolBar_xCollapsedTitleTextStyle, 0);
        int expandedTitleTextStyle = ta.getInt(R.styleable.YDToolBar_xExpandedTitleTextStyle, 0);
        int expandedTitleMarginLeft = (int) ta.getDimension(R.styleable.YDToolBar_xExpandedTitleMarginLeft, -1);
        int expandedTitleMarginBottom = (int) ta.getDimension(R.styleable.YDToolBar_xExpandedTitleMarginBottom, -1);
        int collapsedTitleGravity = ta.getInt(R.styleable.YDToolBar_xCollapsedTitleGravity, Gravity.NO_GRAVITY);
        int expandedTitleGravity = ta.getInt(R.styleable.YDToolBar_xExpandedTitleGravity, Gravity.BOTTOM);
        boolean expandedSubTitleVisible = ta.getBoolean(R.styleable.YDToolBar_xExpandedSubTitleVisible, true);
        float expandedSubTitleTextSize = ta.getDimension(R.styleable.YDToolBar_xExpandedSubTitleTextSize, 0);
        int expandedSubTitleTextColor = ta.getColor(R.styleable.YDToolBar_xExpandedSubTitleTextColor, 0);
        int expandedSubTitleGravity = ta.getInt(R.styleable.YDToolBar_xExpandedSubTitleGravity, Gravity.BOTTOM);
        float subTitleMarginLeft = ta.getDimension(R.styleable.YDToolBar_xExpandedSubTitleMarginLeft, -1);
        float subTitleMarginBottom = ta.getDimension(R.styleable.YDToolBar_xExpandedSubTitleMarginBottom, -1);
        boolean supportedActionBar = ta.getBoolean(R.styleable.YDToolBar_xSupportedActionBar, true);
        ta.recycle();

        // 初始化一下居中标题的配置
        initTitle();

        // 左边按钮的配置
        if (mLeftButtonPadding > -1) {
            mLeftTv.setPadding(mLeftButtonPadding, mLeftButtonPadding, mLeftButtonPadding, mLeftButtonPadding);
            mLeftIv.setPadding(mLeftButtonPadding, mLeftButtonPadding, mLeftButtonPadding, mLeftButtonPadding);
        }
        MarginLayoutParams leftImageViewLp = (MarginLayoutParams) mLeftIv.getLayoutParams();
        if (leftButtonWidth > 0) {
            mLeftTv.setWidth(leftButtonWidth);
            leftImageViewLp.width = leftButtonWidth;
        }
        if (leftButtonHeight > 0) {
            mLeftTv.setHeight(leftButtonHeight);
            leftImageViewLp.height = leftButtonHeight;
        }
        leftImageViewLp.leftMargin = leftButtonMarginLeft;
        mLeftIv.setLayoutParams(leftImageViewLp);
        MarginLayoutParams leftTextViewLp = (MarginLayoutParams) mLeftTv.getLayoutParams();
        leftTextViewLp.leftMargin = leftButtonMarginLeft;
        mLeftTv.setLayoutParams(leftTextViewLp);
        if (0 != leftButtonBackGround) {
            mLeftIv.setVisibility(VISIBLE);
            mLeftTv.setVisibility(GONE);
            mLeftIv.setImageResource(leftButtonBackGround);
        } else {
            if (!TextUtils.isEmpty(leftButtonText)) {
                mLeftIv.setVisibility(GONE);
                mLeftTv.setVisibility(VISIBLE);
                mLeftTv.setTextSize(leftButtonTextSize);
                mLeftTv.setTextColor(leftButtonTextColor);
                mLeftTv.setText(leftButtonText);
            }
        }

        // 右边按钮的配置
        if (mRightButtonPadding > -1) {
            mRightTv.setPadding(mRightButtonPadding, mRightButtonPadding, mRightButtonPadding, mRightButtonPadding);
            mRightIv.setPadding(mRightButtonPadding, mRightButtonPadding, mRightButtonPadding, mRightButtonPadding);
        }
        MarginLayoutParams rightImageViewLp = (MarginLayoutParams) mRightIv.getLayoutParams();
        if (rightButtonWidth > 0) {
            mRightTv.setWidth(rightButtonWidth);
            rightImageViewLp.width = rightButtonWidth;
        }
        if (rightButtonHeight > 0) {
            mRightTv.setHeight(rightButtonHeight);
            rightImageViewLp.height = rightButtonHeight;
        }
        rightImageViewLp.rightMargin = rightButtonMarginRight;
        mRightIv.setLayoutParams(rightImageViewLp);
        MarginLayoutParams rightTextViewLp = (MarginLayoutParams) mRightTv.getLayoutParams();
        rightTextViewLp.rightMargin = rightButtonMarginRight;
        mRightTv.setLayoutParams(rightTextViewLp);
        if (0 != rightButtonBackGround) {
            mRightIv.setVisibility(VISIBLE);
            mRightTv.setVisibility(GONE);
            mRightIv.setImageResource(rightButtonBackGround);
        } else {
            if (!TextUtils.isEmpty(rightButtonText)) {
                mRightIv.setVisibility(GONE);
                mRightTv.setVisibility(VISIBLE);
                mRightTv.setTextSize(rightButtonTextSize);
                mRightTv.setTextColor(rightButtonTextColor);
                mRightTv.setText(rightButtonText);
            }
        }

        // 展开与收缩时的Title的一些属性
        if (collapsedTitleTextAppearance != 0) {
            mCollapsingToolbarLayout.setCollapsedTitleTextAppearance(collapsedTitleTextAppearance);
        }
        if (collapsedTitleTextColor != 0) {
            mCollapsingToolbarLayout.setCollapsedTitleTextColor(collapsedTitleTextColor);
        }
        if (expandedTitleTextAppearance != 0) {
            mCollapsingToolbarLayout.setExpandedTitleTextAppearance(expandedTitleTextAppearance);
        }
        if (collapsedTitleTextColor != 0) {
            mCollapsingToolbarLayout.setCollapsedTitleTextColor(expandedTitleTextColor);
        }
        // CollapsedTitle和ExpandedTitle只能单独设typeface, 在style.xml设没用
        // 设typeface一定要在Appearance之后才会生效
//        mCollapsingToolbarLayout.setCollapsedTitleTypeface(FontUtils.getFont(mContext, collapsedTitleTextStyle));
//        mCollapsingToolbarLayout.setExpandedTitleTypeface(FontUtils.getFont(mContext, collapsedTitleTextStyle));
        if (expandedTitleMarginLeft >= 0) {
            mCollapsingToolbarLayout.setExpandedTitleMarginStart(expandedTitleMarginLeft);
        }
        if (expandedTitleMarginBottom >= 0) {
            mCollapsingToolbarLayout.setExpandedTitleMarginBottom(expandedTitleMarginBottom);
        }
        mCollapsingToolbarLayout.setCollapsedTitleGravity(collapsedTitleGravity);
        mCollapsingToolbarLayout.setExpandedTitleGravity(expandedTitleGravity);

        // 展开时副标题的各种属性
        if (!expandedSubTitleVisible) {
            mSubTitle.setVisibility(GONE);
        }
        if (expandedSubTitleTextSize > 0) {
            mSubTitle.setTextSize(expandedSubTitleTextSize);
        }
        if (expandedSubTitleTextColor != 0) {
            mSubTitle.setTextColor(expandedSubTitleTextColor);
        }
        MarginLayoutParams subTitleLp = (MarginLayoutParams) mSubTitle.getLayoutParams();
        if (subTitleMarginLeft >= 0) {
            subTitleLp.leftMargin = (int) subTitleMarginLeft;
        }
        if (subTitleMarginBottom >= 0) {
            subTitleLp.bottomMargin = (int) subTitleMarginBottom;
        }
        mSubTitle.setLayoutParams(subTitleLp);
        FrameLayout.LayoutParams subTitleLp1 = (FrameLayout.LayoutParams) mSubTitle.getLayoutParams();
        subTitleLp1.gravity = expandedSubTitleGravity;
        mSubTitle.setLayoutParams(subTitleLp);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (mContext instanceof AppCompatActivity && supportedActionBar) {
                ((AppCompatActivity) mContext).setSupportActionBar(toolbar);
            }
        }
        // 不显示返回键
        if (mContext instanceof AppCompatActivity
                && ((AppCompatActivity) mContext).getSupportActionBar() != null && supportedActionBar) {
            ((AppCompatActivity) mContext).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        // 设一下各种监听事件
        initListeners();
    }

    private void initTitle() {
        if (mToolbarPadding > 0) {
            toolbar.setContentInsetsAbsolute(mToolbarPadding, mToolbarPadding);
        }
        if (!TextUtils.isEmpty(mTitle)) {
            mCollapsingToolbarLayout.setTitle(mTitle);
            mTvFixTitle.setText(mTitle);
        }
        mTvFixTitle.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mTittleMaxLength)});
        FrameLayout.LayoutParams toolbarLayoutParams = (FrameLayout.LayoutParams) toolbar.getLayoutParams();
        FrameLayout.LayoutParams wrapperLayoutParams = (FrameLayout.LayoutParams) mRlWrapper.getLayoutParams();
        if (mIsFixedTitle) {
            toolbarLayoutParams.height = LayoutParams.MATCH_PARENT;
            toolbar.setLayoutParams(toolbarLayoutParams);
            mSubTitle.setVisibility(GONE);
            wrapperLayoutParams.height = LayoutParams.MATCH_PARENT;
            mRlWrapper.setLayoutParams(wrapperLayoutParams);
        } else {
            ViewGroup.LayoutParams layoutParams = mCollapsingToolbarLayout.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            mCollapsingToolbarLayout.setLayoutParams(layoutParams);
            if (mToolBarHeight > -1) {
                toolbarLayoutParams.height = (int) mToolBarHeight;
                toolbar.setLayoutParams(toolbarLayoutParams);
                wrapperLayoutParams.height = (int) mToolBarHeight;
                mRlWrapper.setLayoutParams(wrapperLayoutParams);
            }
            mTvFixTitle.setVisibility(GONE);
        }
    }

    private void findViews() {
        View view = inflate(mContext, R.layout.common_toolbar, this);
        toolbar = view.findViewById(R.id.yd_toolbar);
        mCollapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar);
        mSubTitle = view.findViewById(R.id.tv_subtitle);
        mLeftIv = view.findViewById(R.id.iv_left);
        mLeftSubTitle = view.findViewById(R.id.tv_left_title);
        mLeftTv = view.findViewById(R.id.tv_left);
        mRightIv = view.findViewById(R.id.iv_right);
        mRightTv = view.findViewById(R.id.tv_right);
        mTvFixTitle = view.findViewById(R.id.tv_fixtitle);
        mRlWrapper = view.findViewById(R.id.rl_wrapper);
        mSecondRightIv = view.findViewById(R.id.iv_right2);
        mSecondRightTv = view.findViewById(R.id.tv_right2);
    }

    public void setLeftImageSize(int height,int width) {
        ViewGroup.LayoutParams params = mLeftIv.getLayoutParams();
        params.height = height;
        params.width = width;
        mLeftIv.setLayoutParams(params);
    }

    private void initListeners() {
        mLeftIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnLeftClickListener != null) {
                    mOnLeftClickListener.onClick(v);
                } else {
                    if (mContext instanceof Activity) {
                        ((Activity) mContext).finish();
                    }
                }
            }
        });

        mLeftTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnLeftClickListener != null) {
                    mOnLeftClickListener.onClick(v);
                }
            }
        });

        mRightIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnRightClickListener != null) {
                    mOnRightClickListener.onClick(v);
                }
            }
        });

        mRightTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnRightClickListener != null) {
                    mOnRightClickListener.onClick(v);
                }
            }
        });

        mSecondRightTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnSecondRightClickListener != null) {
                    mOnSecondRightClickListener.onClick(v);
                }
            }
        });

        mSecondRightIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnSecondRightClickListener != null) {
                    mOnSecondRightClickListener.onClick(v);
                }
            }
        });

        this.addOnOffsetChangedListener(new OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int newalpha = 255 + verticalOffset;
                newalpha = newalpha < 210 ? 0 : newalpha;
                Log.d("newalpha", newalpha + "");
                mSubTitle.setAlpha(newalpha);
            }
        });
    }

    public void setOnLeftClickListener(OnClickListener listener) {
        mOnLeftClickListener = listener;
    }

    public void setOnRightClickListener(OnClickListener listener) {
        mOnRightClickListener = listener;
    }

    public void setOnSecondRightClickListener(OnClickListener listener) {
        mOnSecondRightClickListener = listener;
    }

    public void setTitle(String title) {
        mTitle = title;
        mCollapsingToolbarLayout.setTitle(mTitle);
        mTvFixTitle.setText(mTitle);
    }

    public void setTitleColor(int colorId) {
        mTvFixTitle.setTextColor(colorId);
    }

    public void setSubTitle(String text) {
        mSubTitle.setText(text);
    }

    public void setLeftSubTitle(String text,int colorId) {
        mLeftSubTitle.setText(text);
        mLeftSubTitle.setTextColor(colorId);
    }

    public void setLeftButtonImage(@DrawableRes int resId) {
        mLeftIv.setVisibility(VISIBLE);
        mLeftTv.setVisibility(GONE);
        mLeftIv.setImageResource(resId);
    }

    public void setLeftButtonImage(Drawable drawable) {
        mLeftIv.setVisibility(VISIBLE);
        mLeftTv.setVisibility(GONE);
        mLeftIv.setImageDrawable(drawable);
    }

    public void setLeftButtonImage(Bitmap bitmap) {
        mLeftIv.setVisibility(VISIBLE);
        mLeftTv.setVisibility(GONE);
        mLeftIv.setImageBitmap(bitmap);
    }

    public void setRightButtonImage(Drawable drawable) {
        mRightIv.setVisibility(VISIBLE);
        mRightTv.setVisibility(GONE);
        mRightIv.setImageDrawable(drawable);
    }

    public void setRightButtonImage(Bitmap bitmap) {
        mRightIv.setVisibility(VISIBLE);
        mRightTv.setVisibility(GONE);
        mRightIv.setImageBitmap(bitmap);
    }

    public void setRightButtonImage(@DrawableRes int resId) {
        mRightIv.setVisibility(VISIBLE);
        mRightTv.setVisibility(GONE);
        mRightIv.setImageResource(resId);
    }

    public void setSecondRightButtonImage(Drawable drawable) {
        mSecondRightTv.setVisibility(GONE);
        mSecondRightIv.setVisibility(VISIBLE);
        mSecondRightIv.setImageDrawable(drawable);
    }

    public void setSecondRightButtonImage(@DrawableRes int resId) {
        mSecondRightTv.setVisibility(GONE);
        mSecondRightIv.setVisibility(VISIBLE);
        mSecondRightIv.setImageResource(resId);
    }

    public void setSecondRightButtonImage(Bitmap bitmap) {
        mSecondRightTv.setVisibility(GONE);
        mSecondRightIv.setVisibility(VISIBLE);
        mSecondRightIv.setImageBitmap(bitmap);
    }

    public void setSecondRightButtonText(String text) {
        mSecondRightIv.setVisibility(GONE);
        mSecondRightTv.setVisibility(VISIBLE);
        mSecondRightTv.setText(text);
    }

    public void setLeftButtonText(String text) {
        mLeftIv.setVisibility(GONE);
        mLeftTv.setVisibility(VISIBLE);
        mLeftTv.setText(text);
    }

    public void setRightButtonText(String text) {
        mRightIv.setVisibility(GONE);
        mRightTv.setVisibility(VISIBLE);
        mRightTv.setText(text);
    }

    public TextView getmTvFixTitle() {
        return mTvFixTitle;
    }

    public CollapsingToolbarLayout getCollapsingToolbarLayout() {
        return mCollapsingToolbarLayout;
    }

    public void setLeftButtonTextColor(int color) {
        mLeftTv.setTextColor(color);
    }

    public void setRightButtonTextColor(int color) {
        mRightTv.setTextColor(color);
    }

    public TextView getLeftButtonTextView() {
        return mLeftTv;
    }

    public TextView getRightButtonTextView() {
        return mRightTv;
    }

    public TextView getSecondRightTextView() {
        return mSecondRightTv;
    }

    public ImageView getLeftButtonImage() {
        return mLeftIv;
    }

    public ImageView getRightButtonImage() {
        return mRightIv;
    }

    public ImageView getSecondRightImage() {
        return mSecondRightIv;
    }

    public int getTittleMaxLength() {
        return mTittleMaxLength;
    }

    public void setTittleMaxLength(int mTittleMaxLength) {
        this.mTittleMaxLength = mTittleMaxLength;
        mTvFixTitle.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mTittleMaxLength)});
    }

    public Activity getActivity(View view) {
        Context context = view.getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

}
