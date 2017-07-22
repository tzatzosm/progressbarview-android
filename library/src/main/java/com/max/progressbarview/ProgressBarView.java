package com.max.progressbarview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.max.progressbarview.R;

/**
 * Created by marseltzatzo on 01/07/2017.
 */

public class ProgressBarView extends View {

    //region Listener

    public interface OnProgressChangedListener {

        /**
         * Fires when progress changes either because user moved the thumb circle or because
         * it was programmatically set to a new value.
         * @param progressBarView Instance
         * @param progress Primary progress value
         * @param fromUser True is the event was triggered from user action.
         */
        void onProgressChanged(ProgressBarView progressBarView, float progress, boolean fromUser);

        /**
         * Fires when user touches down the view.
         * @param progressBarView Instance
         */
        void onTouchStart(ProgressBarView progressBarView);

        /**
         * Fires when user releases the view.
         * @param progressBarView Instance
         */
        void onTouchEnd(ProgressBarView progressBarView);

    }

    private OnProgressChangedListener onProgressChangedListener;

    // endregion

    //region Properties

    private static final String TAG = ProgressBarView.class.getSimpleName();

    private Context context;

    private boolean isDragging = false;

    private float barHeightCoefficient = 0.2f;

    private TouchEventHandler touchEventHandler;

    // region Value

    private float minProgressValue = 0;
    private float maxProgressValue = 1;

    private float primaryProgressValue = 0;
    private float secondaryProgressValue = 0;

    private float thumbSnapValue = 0;

    /**
     * Radius of the thumb.
     */
    private int thumbSize = 0;

    /**
     * Border of the thumb.
     */
    private int thumbBorderSize = 0;

    /**
     * Keeps track of touchEvent's X axis value upon user interaction (onTouchEvent).
     */
    private float lastX;
    /**
     * Keeps track of touchEvent's Y axis value upon user interaction (onTouchEvent).
     */
    private float lastY;

    //endregion

    //region Color

    private int barColor = 0;
    private int primaryColor = 0;
    private int secondaryColor = 0;
    private int thumbFillColor = 0;
    private int thumbStrokeColor = 0;

    //endregion

    //region Paint

    private Paint barPaint;
    private Paint primaryProgressPaint;
    private Paint secondaryProgressPaint;
    private Paint thumbFillPaint;
    private Paint thumbStrokePaint;

    //endregion

    //region Rectangles

    private RectF barRect;
    private RectF primaryProgressRect;
    private RectF secondaryProgressRect;
    private RectF thumbRect;

    //endregion

    //endregion

    //region Getters

    public OnProgressChangedListener getOnProgressChangedListener() {
        return onProgressChangedListener;
    }

    public float getMinProgressValue() {
        return minProgressValue;
    }

    public float getMaxProgressValue() {
        return maxProgressValue;
    }

    public float getPrimaryProgressValue() {
        return primaryProgressValue;
    }

    public float getSecondaryProgressValue() {
        return secondaryProgressValue;
    }

    public float getThumbSnapValue() {
        return thumbSnapValue;
    }

    public int getThumbSize() {
        return thumbSize;
    }

    public float getThumbBorderSize() {
        return this.thumbStrokePaint.getStrokeWidth();
    }

    //endregion

    //region Setters

    public void setOnProgressChangedListener(OnProgressChangedListener onProgressChangedListener) {
        this.onProgressChangedListener = onProgressChangedListener;
    }

    public void setMinProgressValue(float minProgressValue) {
        this.minProgressValue = minProgressValue;
        invalidate();
    }

    public void setMaxProgressValue(float maxProgressValue) {
        this.maxProgressValue = maxProgressValue;
        invalidate();
    }

    public void setPrimaryProgressValue(float primaryProgressValue) {
        this.primaryProgressValue = primaryProgressValue;
        if (this.onProgressChangedListener != null) {
            this.onProgressChangedListener.onProgressChanged(this, this.primaryProgressValue, isDragging);
        }
        invalidate();
    }

    public void setSecondaryProgressValue(float secondaryProgressValue) {
        this.secondaryProgressValue = secondaryProgressValue;
        invalidate();
    }

    /**
     * @param thumbSize in pixels.
     */
    public void setThumbSize(int thumbSize) {
        this.thumbSize = thumbSize;
        adjustThumbSizeIfNeeded();
        invalidate();
    }

    /**
     * @param thumbStrokeWidth
     */
    public void setThumbBorderSize(float thumbStrokeWidth) {
        this.thumbStrokePaint.setStrokeWidth(thumbStrokeWidth);
        invalidate();
    }

    public void setThumbSnapValue(float thumbSnapValue) {
        this.thumbSnapValue = thumbSnapValue;
    }

    //region Colors

    public void setBarColor(@ColorInt int barColor) {
        this.barColor = barColor;
        this.barPaint.setColor(this.barColor);
        invalidate();
    }

    public void setPrimaryColor(@ColorInt int primaryColor) {
        this.primaryColor = primaryColor;
        this.primaryProgressPaint.setColor(this.primaryColor);
        invalidate();
    }

    public void setSecondaryColor(@ColorInt int secondaryColor) {
        this.secondaryColor = secondaryColor;
        this.secondaryProgressPaint.setColor(this.secondaryColor);
        invalidate();
    }

    public void setThumbFillColor(@ColorInt int thumbFillColor) {
        this.thumbFillColor = thumbFillColor;
        this.thumbFillPaint.setColor(this.thumbFillColor);
        invalidate();
    }

    public void setThumbStrokeColor(@ColorInt int thumbStrokeColor) {
        this.thumbStrokeColor = thumbStrokeColor;
        this.thumbStrokePaint.setColor(this.thumbStrokeColor);
        invalidate();
    }

    //endregion

    //region Helpers

    private void setLastX(float lastX) {
        this.lastX = (lastX > getRightEdge()) ?
                getRightEdge() : (lastX < getLeftEdge() ? getLeftEdge() : lastX);
    }

    private void setLastY(float lastY) {
        this.lastY = lastY;
    }

    //endregion

    //endregion

    //region Constructor

    public ProgressBarView(Context context) {
        super(context);
        init();
        initDefaults();
    }

    public ProgressBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
        initDefaults();
        initAttrs(attrs);
    }

    public ProgressBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        initDefaults();
        initAttrs(attrs);
    }

    @TargetApi(21)
    public ProgressBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
        initDefaults();
        initAttrs(attrs);
    }

    //endregion

    //region Overrides

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(barRect, barPaint);
        canvas.drawRect(secondaryProgressRect, secondaryProgressPaint);
        canvas.drawRect(primaryProgressRect, primaryProgressPaint);
        canvas.drawOval(thumbRect, thumbStrokePaint);
        canvas.drawOval(thumbRect, thumbFillPaint);
    }

    @Override
    public void invalidate() {
        updateDrawingRectangles();
        super.invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
//        Log.d(TAG, "onSizeChanged");
        adjustThumbSizeIfNeeded();
        updateDrawingRectangles();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        setLastX(event.getX());
        setLastY(event.getY());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                onStartTrackingTouch();
                invalidate();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                invalidate();
                break;
            }
            case MotionEvent.ACTION_UP: {
                float primaryProgressValue =
                        (lastX - getLeftEdge()) / getContentWidth() * maxProgressValue;
                setPrimaryProgressValue(primaryProgressValue);
                onEndTrackingTouch();
                break;
            }
            case MotionEvent.ACTION_CANCEL: {
                onEndTrackingTouch();
                break;
            }
        }
        return true;
    }

    //endregion

    //region Public

    //endregion

    //region Private

    //region Initialization

    /**
     * Initialize variables that will be used extensively here, such as context or paints.
     */
    private void init() {
        this.context = getContext();
        this.barPaint = createPaint(Paint.Style.FILL);
        this.primaryProgressPaint = createPaint(Paint.Style.FILL);
        this.secondaryProgressPaint = createPaint(Paint.Style.FILL);
        this.thumbFillPaint = createPaint(Paint.Style.FILL);
        this.thumbStrokePaint = createPaint(Paint.Style.STROKE);
        this.touchEventHandler = new TouchEventHandler(this);
    }

    /**
     * Convenience function that instantiates Paint objects.
     * @param style     Style to be used for Paint
     * @return          Paint object
     */
    private Paint createPaint(Paint.Style style) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(style);
        return paint;
    }

    /**
     * Initialize defaults values here.
     */
    private void initDefaults() {

        setBarColor(ContextCompat.getColor(
                context,
                R.color.max_progress_bar_bar_default_color
        ));
        setPrimaryColor(ContextCompat.getColor(
                context,
                R.color.max_progress_bar_primary_progress_default_color
        ));
        setSecondaryColor(ContextCompat.getColor(
                context,
                R.color.max_progress_bar_secondary_progress_default_color
        ));
        setThumbSize(
                getResources().getDimensionPixelSize(R.dimen.max_progress_bar_thumb_size)
        );
        setThumbBorderSize(
                getResources().getDimensionPixelSize(R.dimen.max_progress_bar_thumb_border_size)
        );
        setThumbFillColor(ContextCompat.getColor(
                context,
                R.color.max_progress_bar_thumb_fill_default_color
        ));
        setThumbStrokeColor(ContextCompat.getColor(
                context,
                R.color.max_progress_bar_thumb_stroke_default_color
        ));
    }

    /**
     * Initialize any attributes defined in xml.
     */
    private void initAttrs(@Nullable AttributeSet attributeSet) {
        if (attributeSet == null) {
            return;
        }
        TypedArray typedArray = context.obtainStyledAttributes(
                attributeSet, R.styleable.qq_max_progressbar
        );

        setBarColor(typedArray.getColor(
                R.styleable.qq_max_progressbar_barColor,
                ContextCompat.getColor(
                        context,
                        R.color.max_progress_bar_bar_default_color
                )
            )
        );
        setPrimaryColor(typedArray.getColor(
                R.styleable.qq_max_progressbar_primaryProgressColor,
                ContextCompat.getColor(
                        context,
                        R.color.max_progress_bar_primary_progress_default_color
                )));
        setSecondaryColor(typedArray.getColor(
                R.styleable.qq_max_progressbar_secondaryProgressColor,
                ContextCompat.getColor(
                        context,
                        R.color.max_progress_bar_secondary_progress_default_color
                )));
        setThumbSize(typedArray.getDimensionPixelSize(
                R.styleable.qq_max_progressbar_thumbSize,
                getResources().getDimensionPixelSize(
                        R.dimen.max_progress_bar_thumb_size
                )));
        setThumbBorderSize(typedArray.getDimensionPixelSize(
                R.styleable.qq_max_progressbar_thumbBorderSize,
                getResources().getDimensionPixelSize(
                        R.dimen.max_progress_bar_thumb_border_size
                )));
        setThumbFillColor(typedArray.getColor(
                R.styleable.qq_max_progressbar_thumbFillColor,
                ContextCompat.getColor(
                        context,
                        R.color.max_progress_bar_thumb_fill_default_color
                )));
        setThumbStrokeColor(typedArray.getColor(
                R.styleable.qq_max_progressbar_thumbStrokeColor,
                ContextCompat.getColor(
                        context,
                        R.color.max_progress_bar_thumb_stroke_default_color
                )));

        setMinProgressValue(typedArray.getFloat(
                R.styleable.qq_max_progressbar_minProgressValue, 0f));

        setMaxProgressValue(typedArray.getFloat(
                R.styleable.qq_max_progressbar_maxProgressValue, 1f));

        setThumbSnapValue(typedArray.getFloat(
                R.styleable.qq_max_progressbar_thumbSnapValue, 0f));

        setPrimaryProgressValue(typedArray.getFloat(
                R.styleable.qq_max_progressbar_primaryProgressValue, 0f));

        setSecondaryProgressValue(typedArray.getFloat(
                R.styleable.qq_max_progressbar_secondaryProgressValue, 0f));

        typedArray.recycle();
    }

    /**
     * Updates rectangles needed for drawing.
     */
    private void updateDrawingRectangles() {
        barRect = new RectF(getLeftEdge(), getBarTopEdge(), getRightEdge(), getBarBottomEdge());
        primaryProgressRect = new RectF(getLeftEdge(), getBarTopEdge(), getPrimaryProgressRightEdge(), getBarBottomEdge());
        secondaryProgressRect = new RectF(getLeftEdge(), getBarTopEdge(), getSecondaryProgressRightEdge(), getBarBottomEdge());
        thumbRect = new RectF(getThumbLeftEdge(), getThumbTopEdge(), getThumbRightEdge(), getThumbBottomEdge());
    }

    //endregion

    //region Measurements

    /**
     * Adjusts thumb size to max valid size which is view.height - paddingTop - paddingBottom
     */
    private void adjustThumbSizeIfNeeded() {
        // View hasn't measured yet
        if (this.getHeight() == 0) {
            return;
        }
        int maxThumbSize = this.getHeight() - getPaddingTop() - getPaddingBottom();
        if (this.thumbSize > maxThumbSize) {
            this.thumbSize = this.getHeight() - getPaddingTop() - getPaddingBottom();
            String warningMessage =
                    String.format(
                            getResources().getString(
                                    R.string.qq_max_progressbar_warning_thumb_size_exceeds_max_size),
                            maxThumbSize);
            Log.w(TAG, warningMessage);
        }
    }

    /**
     * @return View's center x.
     */
    private float getCenterX() {
        return getWidth() / 2;
    }

    /**
     * @return View's center y.
     */
    private float getCenterY() {
        return getHeight() / 2;
    }

    /**
     * @return Height of the bar.
     */
    private float getBarHeight() {
        return (int) (barHeightCoefficient * getHeight());
    }

    /**
     * @return The top edge of the bar's drawing rectangle.
     */
    private float getBarTopEdge() {
        return getCenterY() - getBarHeight() / 2;
    }

    /**
     * @return The top edge of the bar's drawing rectangle.
     */
    private float getBarBottomEdge() {
        return getCenterY() + getBarHeight() / 2;
    }

    /**
     * @return The left/starting edge of the drawing rectangle.
     */
    private float getLeftEdge() {
        return getPaddingLeft();
    }

    /**
     * @return The right/ending edge of the drawing rectangle.
     */
    private float getRightEdge() {
        return getWidth() - getPaddingRight();
    }

    private float getContentWidth() {
        return getRightEdge() - getLeftEdge();
    }

    /**
     * @return The right/ending edge of the primary progress bar's rectangle.
     */
    private float getPrimaryProgressRightEdge() {
        return (primaryProgressValue / maxProgressValue) * getContentWidth() + getLeftEdge();
    }

    /**
     * @return The right/ending edge of the secondary progress bar's rectangle.
     */
    private float getSecondaryProgressRightEdge() {
        return ((secondaryProgressValue / maxProgressValue) * getRightEdge() + getLeftEdge());
    }

    /**
     *
     * @return Thumbs center for X axis.
     */
    private float getThumbCenterX() {
        if (isDragging) {
            // Calculate delta in value between current thumb center and primary progress value
            float thumbPrimaryDeltaValue =
                    Math.abs(lastX - getPrimaryProgressRightEdge()) / getContentWidth() * maxProgressValue;
            // If value is greater than thumbSnapValue the thumb should start moving
            if (thumbPrimaryDeltaValue > thumbSnapValue) {
                return lastX;
            }
        }
        return getPrimaryProgressRightEdge();
    }

    private float getThumbLeftEdge() {
        return getThumbCenterX() - thumbSize / 2;
    }

    private float getThumbRightEdge() {
        return getThumbCenterX() + thumbSize / 2;
    }

    private float getThumbTopEdge() {
        return getCenterY() - thumbSize / 2;
    }

    private float getThumbBottomEdge() {
        return getCenterY() + thumbSize / 2;
    }

    //endregion

    //region Helper

    private void onStartTrackingTouch() {
        isDragging = true;
        if (onProgressChangedListener != null) {
            onProgressChangedListener.onTouchStart(this);
        }
    }

    private void onEndTrackingTouch() {
        isDragging = false;
        if (onProgressChangedListener != null) {
            onProgressChangedListener.onTouchEnd(this);
        }
    }

    //endregion

    //endregion

}
