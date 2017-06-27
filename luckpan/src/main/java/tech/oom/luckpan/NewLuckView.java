package tech.oom.luckpan;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;


public class NewLuckView extends View {


    /**
     * 默认显示奖品字体大小
     */
    private final float default_text_size;
    /**
     * 默认显示奖品的字体颜色
     */
    private final int default_text_color = Color.rgb(255, 255, 255);
    /**
     * 默认显示奖品字体距离圆心的偏移量占半径长度的比
     */
    private final float defaultTextOffsetRatio = 0.85f;
    /**
     * 奇数扇形的填充色
     */
    private final int default_odd_sector_color = Color.rgb(255, 133, 132);
    /**
     * 偶数扇形的填充色
     */
    private final int default_even_sector_color = Color.rgb(254, 104, 105);
    /**
     * 默认奖品图片的宽度占转盘宽度的比
     */
    private final float defaultWidthRatio = 1f / 8;
    /**
     * 默认奖品图片距离圆心的偏移长度占圆盘半径长度的比
     */
    private final float defaultItemOffsetRatio = 3f / 5;
    /**
     * 默认indicator的宽度和直径的比
     */
    private final float defaultIndicatorWidthRatio = 1f / 5;
    /**
     * 默认indicator的高度和直径的比
     */
    private final float defaultIndicatorHeightRatio = 1f / 5;
    /**
     * 默认转一圈所用的时间
     */
    private final int defaultCycleInMilliseconds = 800;

    private RectF luckViewRect = new RectF();
    private RectF indicatorRect = new RectF();

    private LuckBean mLuckBean;
    private boolean isRolling;
    private int radius;
    private int firstAngle = 0;

    /**
     * 转盘所有图片的bitmap集合
     */
    private List<Bitmap> bitmaps = new ArrayList<>();

    /**
     * 默认奖品图片的高度占转盘高度度的比
     */
    private float defaultHeightRatio = 1f / 8;

    /**
     * 控件的最小长宽
     */
    private int min_size;

    /**
     * 奖项字体大小
     */
    private float textSize;

    /**
     * 奖项字体颜色
     */
    private int textColor;

    /**
     * 奖品字体距离圆心的偏移量占半径长度的比
     */
    private float textOffsetRatio;


    /**
     * 奇数扇形的填充色
     */
    private int oddSectorColor;

    /**
     * 偶数扇形的填充色
     */
    private int evenSectorColor;

    private Paint oddSectorPaint;
    private Paint evenSectorPaint;
    private TextPaint textPaint;

    /**
     * 奖品图片的宽度占转盘宽度的比
     */
    private float itemWidthRatio;

    /**
     * 奖品图片的高度占转盘宽度的比
     */
    private float itemHeightRatio;

    /**
     * 奖品图片距离圆心的偏移长度占圆盘半径长度的比
     */
    private float itemOffsetRatio;

    /**
     * indicator 的资源ID
     */
    private int indicatorResourceId;

    /**
     *  默认indicator的宽度和直径的比
     */
    private float indicatorWidthRatio;

    /**
     * 默认indicator的高度和直径的比
     */
    private float indicatorHeightRatio;

    private boolean enable = true;

    private Bitmap indicatorBitmap;
    private int cycleInMilliseconds;
    private ValueAnimator animator;
    private LuckViewListener luckViewListener;

    public NewLuckView(Context context) {
        this(context, null);
    }

    public NewLuckView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NewLuckView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        default_text_size = dip2px(context, 14);
        min_size = dip2px(context, 200);
        final TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.NewLuckView, defStyleAttr, 0);
        initByAttributes(attributes);
        attributes.recycle();
        initPainters();
    }


    private void initByAttributes(TypedArray attributes) {
        textSize = attributes.getDimension(R.styleable.NewLuckView_luck_text_size, default_text_size);
        textColor = attributes.getColor(R.styleable.NewLuckView_luck_text_color, default_text_color);
        textOffsetRatio = attributes.getFloat(R.styleable.NewLuckView_luck_text_offset_ratio, defaultTextOffsetRatio);
        oddSectorColor = attributes.getColor(R.styleable.NewLuckView_luck_odd_sector_color, default_odd_sector_color);
        evenSectorColor = attributes.getColor(R.styleable.NewLuckView_luck_even_sector_color, default_even_sector_color);
        itemWidthRatio = attributes.getFloat(R.styleable.NewLuckView_luck_item_width_ratio, defaultWidthRatio);
        itemHeightRatio = attributes.getFloat(R.styleable.NewLuckView_luck_item_height_ratio, defaultHeightRatio);
        itemOffsetRatio = attributes.getFloat(R.styleable.NewLuckView_luck_item_offset_ratio, defaultItemOffsetRatio);
        indicatorResourceId = attributes.getResourceId(R.styleable.NewLuckView_luck_indicator_drawable, 0);
        indicatorWidthRatio = attributes.getFloat(R.styleable.NewLuckView_luck_indicator_width_ratio, defaultIndicatorWidthRatio);
        indicatorHeightRatio = attributes.getFloat(R.styleable.NewLuckView_luck_indicator_height_ratio, defaultIndicatorHeightRatio);
        cycleInMilliseconds = attributes.getInteger(R.styleable.NewLuckView_luck_cycle_millis, defaultCycleInMilliseconds);
    }

    private void initPainters() {
        oddSectorPaint = new Paint();
        oddSectorPaint.setColor(oddSectorColor);
        oddSectorPaint.setStyle(Paint.Style.FILL);
        oddSectorPaint.setAntiAlias(true);

        evenSectorPaint = new Paint();
        evenSectorPaint.setColor(evenSectorColor);
        evenSectorPaint.setStyle(Paint.Style.FILL);
        evenSectorPaint.setAntiAlias(true);

        textPaint = new TextPaint();
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setAntiAlias(true);
    }


    /**
     * 设置相应的实体类
     *
     * @param bean
     */
    public void loadData(LuckBean bean, List<Bitmap> bitmaps) {
        mLuckBean = bean;
        this.bitmaps.addAll(bitmaps);
        invalidate();
    }

    /**
     * 设置抽奖指针的bitmap
     *
     * @param indicatorBitmap
     */
    public void setIndicator(Bitmap indicatorBitmap) {
        this.indicatorBitmap = indicatorBitmap;
        invalidate();
    }

    /**
     * 设置抽奖指针的bitmap resources id
     * @param indicatorResourceId
     */
    public void setIndicatorResourceId(int indicatorResourceId) {
        this.indicatorResourceId = indicatorResourceId;
        invalidate();
    }

    /**
     * 设置奇数扇形区域的颜色
     *
     * @param color
     */
    public void setOddSectorColor(@ColorInt int color) {
        oddSectorColor = color;
        invalidateNow();
    }

    /**
     * 设置偶数扇形区域的颜色
     *
     * @param color
     */
    public void setEvenSectorColor(@ColorInt int color) {
        evenSectorColor = color;
        invalidateNow();
    }

    /**
     * 设置奖项文字颜色
     *
     * @param color
     */
    public void setItemTextColor(@ColorInt int color) {
        textColor = color;
        invalidateNow();
    }

    /**
     * 设置奖项文字大小
     *
     * @param size
     */
    public void setItemTextSize(float size) {
        textSize = size;
        invalidateNow();
    }

    public float getTextOffsetRatio() {
        return textOffsetRatio;
    }

    /**
     * 奖品字体距离圆心的偏移量占半径长度的比
     */
    public void setTextOffsetRatio(float textOffsetRatio) {
        this.textOffsetRatio = textOffsetRatio;
    }

    public float getItemWidthRatio() {
        return itemWidthRatio;
    }

    /**
     * 奖品图片的宽度占转盘宽度的比
     */
    public void setItemWidthRatio(float itemWidthRatio) {
        this.itemWidthRatio = itemWidthRatio;
    }

    public float getItemHeightRatio() {
        return itemHeightRatio;
    }

    /**
     * 奖品图片的高度占转盘宽度的比
     */
    public void setItemHeightRatio(float itemHeightRatio) {
        this.itemHeightRatio = itemHeightRatio;
    }

    public float getItemOffsetRatio() {
        return itemOffsetRatio;
    }

    /**
     * 奖品图片距离圆心的偏移长度占圆盘半径长度的比
     */
    public void setItemOffsetRatio(float itemOffsetRatio) {
        this.itemOffsetRatio = itemOffsetRatio;
    }

    public float getIndicatorWidthRatio() {
        return indicatorWidthRatio;
    }

    /**
     * 默认indicator的宽度和直径的比
     * @param indicatorWidthRatio
     */
    public void setIndicatorWidthRatio(float indicatorWidthRatio) {
        this.indicatorWidthRatio = indicatorWidthRatio;
    }

    public float getIndicatorHeightRatio() {
        return indicatorHeightRatio;
    }

    /**
     * 默认indicator的高度和直径的比
     * @param indicatorHeightRatio
     */
    public void setIndicatorHeightRatio(float indicatorHeightRatio) {
        this.indicatorHeightRatio = indicatorHeightRatio;
    }

    public int getCycleInMilliseconds() {
        return cycleInMilliseconds;
    }

    /**
     * 转一圈所用的时间
     * @param cycleInMilliseconds
     */
    public void setCycleInMilliseconds(int cycleInMilliseconds) {
        this.cycleInMilliseconds = cycleInMilliseconds;
    }

    /**
     * 是否可用 默认为true
     * @return
     */
    public boolean isEnable() {
        return enable;
    }

    /**
     * 设置是否可用 当为false时，表示不对点击事件进行监听
     * @param enable
     */
    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    /**
     * 如果改变相应配置  需要刷新相应属性
     */
    public void invalidateNow() {
        initPainters();
        invalidate();
    }

    public Bitmap getIndicatorBitmap() {
        if (indicatorBitmap == null) {
            if (indicatorResourceId != 0) {
                return BitmapFactory.decodeResource(getResources(), indicatorResourceId);
            }
        }
        return indicatorBitmap;

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mLuckBean == null || mLuckBean.details == null) {
            return;
        }

        int MinValue = Math.min(getWidth(), getHeight());
        radius = MinValue / 2;
//        RectF rectF = new RectF(0, 0, MinValue, MinValue);
        luckViewRect.set(0, 0, MinValue, MinValue);
        int size = mLuckBean.details.size();
        int sectorAnger = (int) (360 / size + 0.5f);
        int startAngle = (int) (270 - sectorAnger / 2 + 0.5f) + firstAngle;
        for (int i = 0; i < size; i++) {
            if (i % 2 == 0) {
                canvas.drawArc(luckViewRect, startAngle, sectorAnger, true, oddSectorPaint);
            } else {
                canvas.drawArc(luckViewRect, startAngle, sectorAnger, true, evenSectorPaint);
            }
            startAngle += sectorAnger;
        }
        canvas.translate(radius, radius);
//        RectF rect = new RectF(-indicatorWidthRatio * radius, (-indicatorHeightRatio) * radius, indicatorWidthRatio * radius, (indicatorHeightRatio) * radius);
        indicatorRect.set(-getIndicatorWidthRatio() * radius, (-getIndicatorHeightRatio()) * radius, getIndicatorWidthRatio() * radius, (getIndicatorHeightRatio()) * radius);
//        if (indicatorBitmap != null) {
//            canvas.drawBitmap(indicatorBitmap, null, indicatorRect, null);
//        } else {
//            if (indicatorResourceId != 0) {
//                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), indicatorResourceId);
//                canvas.drawBitmap(bitmap, null, indicatorRect, null);
//            }
//        }

        canvas.drawBitmap(getIndicatorBitmap(), null, indicatorRect, null);
        int rotatedDegree = firstAngle;
        canvas.rotate(firstAngle);
        for (int i = 0; i < size; i++) {
            LuckItemInfo luckItemInfo = mLuckBean.details.get(i);
            drawText(canvas, luckItemInfo.prize_name);
            drawIcon(canvas, bitmaps.get(i));
            rotatedDegree += sectorAnger;
            canvas.rotate(sectorAnger);
        }

    }

    private void drawText(Canvas mCanvas, String drawText) {
        mCanvas.drawText(drawText, 0, -radius * getTextOffsetRatio(), textPaint);
    }

    private void drawIcon(Canvas mCanvas, Bitmap bitmap) {
        RectF rect = new RectF(-getItemWidthRatio() * radius, (-getItemHeightRatio() + (-getItemOffsetRatio())) * radius, getItemWidthRatio() * radius, (getItemHeightRatio() + (-getItemOffsetRatio())) * radius);
        mCanvas.drawBitmap(bitmap, null, rect, null);
    }

    /**
     * 选择变换
     *
     * @param origin 原图
     * @param alpha  旋转角度，可正可负
     * @return 旋转后的图片
     */
    private Bitmap rotateBitmap(Bitmap origin, float alpha) {
        if (origin == null) {
            return null;
        }
        int width = origin.getWidth();
        int height = origin.getHeight();
        Matrix matrix = new Matrix();
        matrix.setRotate(alpha);
        // 围绕原地进行旋转
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (newBM.equals(origin)) {
            return newBM;
        }

        return newBM;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(!enable){
            return false;
        }
        int centerX = getMeasuredWidth() / 2;
        int centerY = getMeasuredHeight() / 2;

        //还没在Viewtree视图上面绘制出来的时候，不管点击事件
        if (centerX <= 0 || centerY <= 0) {
            return true;
        }

        //宽度位置超过箭头图标区域
        if (Math.abs(event.getX() - centerX) > indicatorWidthRatio * radius) {
            return true;
        }
        //高度位置超过箭头图标区域
        if (Math.abs(event.getY() - centerY) > indicatorHeightRatio * radius) {
            return true;
        }

        //点击在箭头上面
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            startRolling();
        }

        return true;
    }

    private void startRolling() {
        if (isRolling) {
            return;
        }
        animator = ValueAnimator.ofInt(0, 360);
        animator.setDuration(getCycleInMilliseconds());
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                firstAngle = (Integer) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.start();
        if (luckViewListener != null) {
            luckViewListener.onStart();
        }
        isRolling = true;
    }

    public void setStop(int index) {
        int size = mLuckBean.details.size();
        if (index < 0 || index >= size) {
            animator.cancel();
            return;
        }
        int sectorAnger = (int) (360 / size + 0.5f);
        animator.setIntValues(firstAngle, 360 + (size - index) * sectorAnger);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setRepeatCount(1);
        if (luckViewListener != null) {
            luckViewListener.onStop(index);
        }
        isRolling = false;

    }


    /**
     * 设置抽奖的监听回调
     * @param luckViewListener
     */
    public void setLuckViewListener(LuckViewListener luckViewListener) {
        this.luckViewListener = luckViewListener;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        setMeasuredDimension(measure(widthMeasureSpec), measure(heightMeasureSpec));
    }

    private int measure(int measureSpec) {
        int result;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            return size;
        } else {

            result = min_size;
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(size, result);
            }
        }
        return result;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != h) {
            throw new RuntimeException("the width of luck view must be the same as it's height");
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    public interface LuckViewListener {

        void onStart();

        void onStop(int index);
    }


}
