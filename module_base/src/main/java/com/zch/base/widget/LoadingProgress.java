package com.zch.base.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import com.zch.base.R;

/**
 *
 * BaseDemo
 * Description:
 * Created by chenhanzhang on 2022/7/30 19:47
 **/
public class LoadingProgress extends View {
    private static final String TAG = LoadingProgress.class.getSimpleName();
    private final int barLength = 16;
    private final int barMaxLength = 270;
    private final long pauseGrowingTime = 200L;
    private int circleRadius = 28;
    private int barWidth = 4;
    private int rimWidth = 4;
    private boolean fillRadius = false;
    private double timeStartGrowing = 0.0D;
    private double barSpinCycleTime = 460.0D;
    private float barExtraLength = 0.0F;
    private boolean barGrowingFromFront = true;
    private long pausedTimeWithoutGrowing = 0L;
    private int barColor = -1442840576;
    private int rimColor = 16777215;
    private Paint barPaint = new Paint();
    private Paint rimPaint = new Paint();
    private RectF circleBounds = new RectF();
    private float spinSpeed = 230.0F;
    private long lastTimeAnimated = 0L;
    private boolean linearProgress;
    private float mProgress = 0.0F;
    private float mTargetProgress = 0.0F;
    private boolean isSpinning = false;
    private LoadingProgress.ProgressCallback callback;
    private boolean shouldAnimate;

    public LoadingProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.parseAttributes(context.obtainStyledAttributes(attrs, R.styleable.YDProgressView));
        this.setAnimationEnabled();
    }

    public LoadingProgress(Context context) {
        super(context);
        this.setAnimationEnabled();
    }

    @TargetApi(17)
    private void setAnimationEnabled() {
        int currentApiVersion = Build.VERSION.SDK_INT;
        float animationValue;
        if (currentApiVersion >= 17) {
            animationValue = Settings.Global.getFloat(this.getContext().getContentResolver(), "animator_duration_scale", 1.0F);
        } else {
            animationValue = Settings.System.getFloat(this.getContext().getContentResolver(), "animator_duration_scale", 1.0F);
        }

        this.shouldAnimate = animationValue != 0.0F;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int viewWidth = this.circleRadius + this.getPaddingLeft() + this.getPaddingRight();
        int viewHeight = this.circleRadius + this.getPaddingTop() + this.getPaddingBottom();
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        if (widthMode == 1073741824) {
            width = widthSize;
        } else if (widthMode == -2147483648) {
            width = Math.min(viewWidth, widthSize);
        } else {
            width = viewWidth;
        }

        int height;
        if (heightMode != 1073741824 && widthMode != 1073741824) {
            if (heightMode == -2147483648) {
                height = Math.min(viewHeight, heightSize);
            } else {
                height = viewHeight;
            }
        } else {
            height = heightSize;
        }

        this.setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.setupBounds(w, h);
        this.setupPaints();
        this.invalidate();
    }

    private void setupPaints() {
        this.barPaint.setColor(this.barColor);
        this.barPaint.setAntiAlias(true);
        this.barPaint.setStyle(Paint.Style.STROKE);
        this.barPaint.setStrokeWidth((float)this.barWidth);
        this.rimPaint.setColor(this.rimColor);
        this.rimPaint.setAntiAlias(true);
        this.rimPaint.setStyle(Paint.Style.STROKE);
        this.rimPaint.setStrokeWidth((float)this.rimWidth);
    }

    private void setupBounds(int layout_width, int layout_height) {
        int paddingTop = this.getPaddingTop();
        int paddingBottom = this.getPaddingBottom();
        int paddingLeft = this.getPaddingLeft();
        int paddingRight = this.getPaddingRight();
        if (!this.fillRadius) {
            int minValue = Math.min(layout_width - paddingLeft - paddingRight, layout_height - paddingBottom - paddingTop);
            int circleDiameter = Math.min(minValue, this.circleRadius * 2 - this.barWidth * 2);
            int xOffset = (layout_width - paddingLeft - paddingRight - circleDiameter) / 2 + paddingLeft;
            int yOffset = (layout_height - paddingTop - paddingBottom - circleDiameter) / 2 + paddingTop;
            this.circleBounds = new RectF((float)(xOffset + this.barWidth), (float)(yOffset + this.barWidth), (float)(xOffset + circleDiameter - this.barWidth), (float)(yOffset + circleDiameter - this.barWidth));
        } else {
            this.circleBounds = new RectF((float)(paddingLeft + this.barWidth), (float)(paddingTop + this.barWidth), (float)(layout_width - paddingRight - this.barWidth), (float)(layout_height - paddingBottom - this.barWidth));
        }

    }

    private void parseAttributes(TypedArray a) {
        DisplayMetrics metrics = this.getContext().getResources().getDisplayMetrics();
        this.barWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float)this.barWidth, metrics);
        this.rimWidth = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float)this.rimWidth, metrics);
        this.circleRadius = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float)this.circleRadius, metrics);
        this.circleRadius = (int)a.getDimension(R.styleable.YDProgressView_matProg_circleRadius, (float)this.circleRadius);
        this.fillRadius = a.getBoolean(R.styleable.YDProgressView_matProg_fillRadius, false);
        this.barWidth = (int)a.getDimension(R.styleable.YDProgressView_matProg_barWidth, (float)this.barWidth);
        this.rimWidth = (int)a.getDimension(R.styleable.YDProgressView_matProg_rimWidth, (float)this.rimWidth);
        float baseSpinSpeed = a.getFloat(R.styleable.YDProgressView_matProg_spinSpeed, this.spinSpeed / 360.0F);
        this.spinSpeed = baseSpinSpeed * 360.0F;
        this.barSpinCycleTime = (double)a.getInt(R.styleable.YDProgressView_matProg_barSpinCycleTime, (int)this.barSpinCycleTime);
        this.barColor = a.getColor(R.styleable.YDProgressView_matProg_barColor, this.barColor);
        this.rimColor = a.getColor(R.styleable.YDProgressView_matProg_rimColor, this.rimColor);
        this.linearProgress = a.getBoolean(R.styleable.YDProgressView_matProg_linearProgress, false);
        if (a.getBoolean(R.styleable.YDProgressView_matProg_progressIndeterminate, false)) {
            this.spin();
        }

        a.recycle();
    }

    public void setCallback(LoadingProgress.ProgressCallback progressCallback) {
        this.callback = progressCallback;
        if (!this.isSpinning) {
            this.runCallback();
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawArc(this.circleBounds, 360.0F, 360.0F, false, this.rimPaint);
        boolean mustInvalidate = false;
        if (this.shouldAnimate) {
            float progress;
            float factor;
            if (this.isSpinning) {
                mustInvalidate = true;
                long deltaTime = SystemClock.uptimeMillis() - this.lastTimeAnimated;
                progress = (float)deltaTime * this.spinSpeed / 1000.0F;
                this.updateBarLength(deltaTime);
                this.mProgress += progress;
                if (this.mProgress > 360.0F) {
                    this.mProgress -= 360.0F;
                    this.runCallback(-1.0F);
                }

                this.lastTimeAnimated = SystemClock.uptimeMillis();
                factor = this.mProgress - 90.0F;
                float length = 16.0F + this.barExtraLength;
                if (this.isInEditMode()) {
                    factor = 0.0F;
                    length = 135.0F;
                }

                canvas.drawArc(this.circleBounds, factor, length, false, this.barPaint);
            } else {
                float oldProgress = this.mProgress;
                float offset;
                if (this.mProgress != this.mTargetProgress) {
                    mustInvalidate = true;
                    offset = (float)(SystemClock.uptimeMillis() - this.lastTimeAnimated) / 1000.0F;
                    progress = offset * this.spinSpeed;
                    this.mProgress = Math.min(this.mProgress + progress, this.mTargetProgress);
                    this.lastTimeAnimated = SystemClock.uptimeMillis();
                }

                if (oldProgress != this.mProgress) {
                    this.runCallback();
                }

                offset = 0.0F;
                progress = this.mProgress;
                if (!this.linearProgress) {
                    factor = 2.0F;
                    offset = (float)(1.0D - Math.pow((double)(1.0F - this.mProgress / 360.0F), (double)(2.0F * factor))) * 360.0F;
                    progress = (float)(1.0D - Math.pow((double)(1.0F - this.mProgress / 360.0F), (double)factor)) * 360.0F;
                }

                if (this.isInEditMode()) {
                    progress = 360.0F;
                }

                canvas.drawArc(this.circleBounds, offset - 90.0F, progress, false, this.barPaint);
            }

            if (mustInvalidate) {
                this.invalidate();
            }

        }
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == 0) {
            this.lastTimeAnimated = SystemClock.uptimeMillis();
        }

    }

    private void updateBarLength(long deltaTimeInMilliSeconds) {
        if (this.pausedTimeWithoutGrowing >= 200L) {
            this.timeStartGrowing += (double)deltaTimeInMilliSeconds;
            if (this.timeStartGrowing > this.barSpinCycleTime) {
                this.timeStartGrowing -= this.barSpinCycleTime;
                this.pausedTimeWithoutGrowing = 0L;
                this.barGrowingFromFront = !this.barGrowingFromFront;
            }

            float distance = (float)Math.cos((this.timeStartGrowing / this.barSpinCycleTime + 1.0D) * 3.141592653589793D) / 2.0F + 0.5F;
            float destLength = 254.0F;
            if (this.barGrowingFromFront) {
                this.barExtraLength = distance * destLength;
            } else {
                float newLength = destLength * (1.0F - distance);
                this.mProgress += this.barExtraLength - newLength;
                this.barExtraLength = newLength;
            }
        } else {
            this.pausedTimeWithoutGrowing += deltaTimeInMilliSeconds;
        }

    }

    public boolean isSpinning() {
        return this.isSpinning;
    }

    public void resetCount() {
        this.mProgress = 0.0F;
        this.mTargetProgress = 0.0F;
        this.invalidate();
    }

    public void stopSpinning() {
        this.isSpinning = false;
        this.mProgress = 0.0F;
        this.mTargetProgress = 0.0F;
        this.invalidate();
    }

    public void spin() {
        this.lastTimeAnimated = SystemClock.uptimeMillis();
        this.isSpinning = true;
        this.invalidate();
    }

    private void runCallback(float value) {
        if (this.callback != null) {
            this.callback.onProgressUpdate(value);
        }

    }

    private void runCallback() {
        if (this.callback != null) {
            float normalizedProgress = (float)Math.round(this.mProgress * 100.0F / 360.0F) / 100.0F;
            this.callback.onProgressUpdate(normalizedProgress);
        }

    }

    public void setInstantProgress(float progress) {
        if (this.isSpinning) {
            this.mProgress = 0.0F;
            this.isSpinning = false;
        }

        if (progress > 1.0F) {
            --progress;
        } else if (progress < 0.0F) {
            progress = 0.0F;
        }

        if (progress != this.mTargetProgress) {
            this.mTargetProgress = Math.min(progress * 360.0F, 360.0F);
            this.mProgress = this.mTargetProgress;
            this.lastTimeAnimated = SystemClock.uptimeMillis();
            this.invalidate();
        }
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        LoadingProgress.WheelSavedState ss = new LoadingProgress.WheelSavedState(superState);
        ss.mProgress = this.mProgress;
        ss.mTargetProgress = this.mTargetProgress;
        ss.isSpinning = this.isSpinning;
        ss.spinSpeed = this.spinSpeed;
        ss.barWidth = this.barWidth;
        ss.barColor = this.barColor;
        ss.rimWidth = this.rimWidth;
        ss.rimColor = this.rimColor;
        ss.circleRadius = this.circleRadius;
        ss.linearProgress = this.linearProgress;
        ss.fillRadius = this.fillRadius;
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof LoadingProgress.WheelSavedState)) {
            super.onRestoreInstanceState(state);
        } else {
            LoadingProgress.WheelSavedState ss = (LoadingProgress.WheelSavedState)state;
            super.onRestoreInstanceState(ss.getSuperState());
            this.mProgress = ss.mProgress;
            this.mTargetProgress = ss.mTargetProgress;
            this.isSpinning = ss.isSpinning;
            this.spinSpeed = ss.spinSpeed;
            this.barWidth = ss.barWidth;
            this.barColor = ss.barColor;
            this.rimWidth = ss.rimWidth;
            this.rimColor = ss.rimColor;
            this.circleRadius = ss.circleRadius;
            this.linearProgress = ss.linearProgress;
            this.fillRadius = ss.fillRadius;
            this.lastTimeAnimated = SystemClock.uptimeMillis();
        }
    }

    public float getProgress() {
        return this.isSpinning ? -1.0F : this.mProgress / 360.0F;
    }

    public void setProgress(float progress) {
        if (this.isSpinning) {
            this.mProgress = 0.0F;
            this.isSpinning = false;
            this.runCallback();
        }

        if (progress > 1.0F) {
            --progress;
        } else if (progress < 0.0F) {
            progress = 0.0F;
        }

        if (progress != this.mTargetProgress) {
            if (this.mProgress == this.mTargetProgress) {
                this.lastTimeAnimated = SystemClock.uptimeMillis();
            }

            this.mTargetProgress = Math.min(progress * 360.0F, 360.0F);
            this.invalidate();
        }
    }

    public void setLinearProgress(boolean isLinear) {
        this.linearProgress = isLinear;
        if (!this.isSpinning) {
            this.invalidate();
        }

    }

    public int getCircleRadius() {
        return this.circleRadius;
    }

    public void setCircleRadius(int circleRadius) {
        this.circleRadius = circleRadius;
        if (!this.isSpinning) {
            this.invalidate();
        }

    }

    public int getBarWidth() {
        return this.barWidth;
    }

    public void setBarWidth(int barWidth) {
        this.barWidth = barWidth;
        if (!this.isSpinning) {
            this.invalidate();
        }

    }

    public int getBarColor() {
        return this.barColor;
    }

    public void setBarColor(int barColor) {
        this.barColor = barColor;
        this.setupPaints();
        if (!this.isSpinning) {
            this.invalidate();
        }

    }

    public int getRimColor() {
        return this.rimColor;
    }

    public void setRimColor(int rimColor) {
        this.rimColor = rimColor;
        this.setupPaints();
        if (!this.isSpinning) {
            this.invalidate();
        }

    }

    public float getSpinSpeed() {
        return this.spinSpeed / 360.0F;
    }

    public void setSpinSpeed(float spinSpeed) {
        this.spinSpeed = spinSpeed * 360.0F;
    }

    public int getRimWidth() {
        return this.rimWidth;
    }

    public void setRimWidth(int rimWidth) {
        this.rimWidth = rimWidth;
        if (!this.isSpinning) {
            this.invalidate();
        }

    }

    static class WheelSavedState extends BaseSavedState {
        public static final Creator<LoadingProgress.WheelSavedState> CREATOR = new Creator<LoadingProgress.WheelSavedState>() {
            @Override
            public LoadingProgress.WheelSavedState createFromParcel(Parcel in) {
                return new LoadingProgress.WheelSavedState(in);
            }

            @Override
            public LoadingProgress.WheelSavedState[] newArray(int size) {
                return new LoadingProgress.WheelSavedState[size];
            }
        };
        float mProgress;
        float mTargetProgress;
        boolean isSpinning;
        float spinSpeed;
        int barWidth;
        int barColor;
        int rimWidth;
        int rimColor;
        int circleRadius;
        boolean linearProgress;
        boolean fillRadius;

        WheelSavedState(Parcelable superState) {
            super(superState);
        }

        private WheelSavedState(Parcel in) {
            super(in);
            this.mProgress = in.readFloat();
            this.mTargetProgress = in.readFloat();
            this.isSpinning = in.readByte() != 0;
            this.spinSpeed = in.readFloat();
            this.barWidth = in.readInt();
            this.barColor = in.readInt();
            this.rimWidth = in.readInt();
            this.rimColor = in.readInt();
            this.circleRadius = in.readInt();
            this.linearProgress = in.readByte() != 0;
            this.fillRadius = in.readByte() != 0;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeFloat(this.mProgress);
            out.writeFloat(this.mTargetProgress);
            out.writeByte((byte)(this.isSpinning ? 1 : 0));
            out.writeFloat(this.spinSpeed);
            out.writeInt(this.barWidth);
            out.writeInt(this.barColor);
            out.writeInt(this.rimWidth);
            out.writeInt(this.rimColor);
            out.writeInt(this.circleRadius);
            out.writeByte((byte)(this.linearProgress ? 1 : 0));
            out.writeByte((byte)(this.fillRadius ? 1 : 0));
        }
    }

    public interface ProgressCallback {
        void onProgressUpdate(float var1);
    }
}


