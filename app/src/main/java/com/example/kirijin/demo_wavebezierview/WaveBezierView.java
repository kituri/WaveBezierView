package com.example.kirijin.demo_wavebezierview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * 波浪图形
 *
 */
public class WaveBezierView extends View implements View.OnClickListener {

    private Paint mPaint;
    private Path mPath;
    private int mWaveLength = 1000;
    private int mOffset;
    private int mScreenHeight;
    private int mScreenWidth;
    private int mWaveCount;
    private int mCenterY;
    private LinearGradient mShader;

    private final int colorSeastart = 0xff2896C8;
    private final int colorSeaEnd = 0xff003778;

    public WaveBezierView(Context context) {
        super(context);
    }

    public WaveBezierView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public WaveBezierView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPath = new Path();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(colorSeastart);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        setOnClickListener(this);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mScreenHeight = h;
        mScreenWidth = w;
        mWaveCount = (int) Math.round(mScreenWidth / mWaveLength + 1.5);
        mCenterY = mScreenHeight / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.reset();
        mPath.moveTo(-mWaveLength + mOffset, mCenterY);
        for (int i = 0; i < mWaveCount; i++) {
            mPath.quadTo((-mWaveLength * 3 / 4) + (i * mWaveLength) + mOffset, mCenterY + 60, (-mWaveLength / 2) + (i * mWaveLength) + mOffset, mCenterY);
            mPath.quadTo((-mWaveLength / 4) + (i * mWaveLength) + mOffset, mCenterY - 60, i * mWaveLength + mOffset, mCenterY);
        }
        mPath.lineTo(mScreenWidth, mScreenHeight);
        mPath.lineTo(0, mScreenHeight);
        mPath.close();
        if(mShader == null){
            mShader = new LinearGradient(0, mScreenHeight / 2, 0, mScreenHeight,
                    new int[] { colorSeastart,  colorSeaEnd},
                    null, Shader.TileMode.MIRROR);
            mPaint.setShader(mShader);
        }
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    public void onClick(View view) {
        ValueAnimator animator = ValueAnimator.ofInt(0, mWaveLength);
        animator.setDuration(1000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mOffset = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        animator.start();
    }
}