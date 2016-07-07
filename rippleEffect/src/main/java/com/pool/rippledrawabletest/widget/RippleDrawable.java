package com.pool.rippledrawabletest.widget;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * Created by pool on 2016/7/5.
 */
public class RippleDrawable extends Drawable {
    protected final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    protected float mRadius;
    protected int mColor;
    protected PointF mPointF;
    protected boolean mDrawFullSize;
    protected long mDuration = 200L;

    private Animator mAnimator;

    public RippleDrawable(int color) {
        setColor(color, true);
    }

    @Override
    public void draw(Canvas canvas) {
        final Rect bounds = getBounds();
        if (mRadius >= bounds.right || mDrawFullSize) {
            canvas.drawRect(bounds, mPaint);
        } else {
            float centerX, centerY;
            if (mPointF != null) {
                centerX = mPointF.x;
                centerY = mPointF.y;
            } else {
                centerX = bounds.centerX();
                centerY = bounds.centerY();
            }
            canvas.drawCircle(centerX, centerY, mRadius, mPaint);
        }
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    public void setColor(int color, boolean drawImmediately) {
        if (color == mColor)
            return;
        mColor = color;
        mPaint.setColor(mColor);
        if (drawImmediately) {
            mDrawFullSize = true;
            invalidateSelf();
        }
    }

    @Override
    public int getOpacity() {
        return 0;
    }

    public void setHotspot(float x, float y) {
        if (mPointF == null)
            mPointF = new PointF();
        mPointF.set(x, y);
    }

    public float getRadius() {
        return mRadius;
    }


    public void setRadius(float radius) {
        if (radius != mRadius) {
            mRadius = radius;
            invalidateSelf();
        }
    }


    public void startRippleAnimator(final Animator.AnimatorListener animatorListener) {
        synchronized (RippleDrawable.class) {
            if (mAnimator != null && mAnimator.isRunning()) {
                mDrawFullSize = true;
                mAnimator.cancel();
            }
            mDrawFullSize = false;
        }
        float maxRadius;
        if (mPointF != null) {
            maxRadius = Math.max(getBounds().right - mPointF.x, mPointF.x);
        } else {
            maxRadius = getBounds().right / 2;
        }
        Log.d(getClass().getSimpleName(), "max radius:" + maxRadius);
        mAnimator = ObjectAnimator.ofFloat(this, "radius", 0, maxRadius);
        mAnimator.setDuration(mDuration);
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (animatorListener != null)
                    animatorListener.onAnimationStart(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //draw full size
                synchronized (RippleDrawable.class) {
                    if (!mDrawFullSize) {
                        mDrawFullSize = true;
                        invalidateSelf();
                        mAnimator = null;
                    }
                }
                if (animatorListener != null)
                    animatorListener.onAnimationEnd(animation);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                synchronized (RippleDrawable.class) {
                    if (!mDrawFullSize) {
                        mDrawFullSize = true;
                        invalidateSelf();
                        mAnimator = null;
                    }
                }
                if (animatorListener != null)
                    animatorListener.onAnimationCancel(animation);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                if (animatorListener != null)
                    animatorListener.onAnimationRepeat(animation);
            }
        });
        mAnimator.start();
    }

}
