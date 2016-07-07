package com.pool.rippledrawabletest.widget;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.pool.rippledrawabletest.R;

/**
 * Created by pool on 2016/7/4.
 */
public class RippleStyleTab extends FrameLayout {

    protected final SparseIntArray mItemRippleColor = new SparseIntArray();

    protected View mRippleView;

    protected ViewGroup mTabContainer;

    protected ViewGroup mRoot;

    protected PointF mHotPoint = new PointF();

    private Animator mCurAnimator;

    private RippleDrawable mRippleDrawable;

    private OnClickListener mOnTabClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            int index = mTabContainer.indexOfChild(v);
            playRippleEffect(index);
            onTabSelect(index);
        }
    };


    public RippleStyleTab(Context context) {
        super(context);
        inflateView();
    }

    public RippleStyleTab(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflateView();
    }

    public RippleStyleTab(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflateView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RippleStyleTab(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        inflateView();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN)
            mHotPoint.set(ev.getX(), ev.getY());
        return super.onInterceptTouchEvent(ev);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    protected void inflateView() {
        LayoutInflater.from(getContext()).inflate(R.layout.main_tab_layout, this);
        mRoot = (ViewGroup) getChildAt(0);
        mRoot.setBackgroundColor(mItemRippleColor.get(0));
        mTabContainer = (ViewGroup) mRoot.getChildAt(1);
        mRippleView = mRoot.getChildAt(0);
        mRippleDrawable = new RippleDrawable(Color.TRANSPARENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            mRippleView.setBackground(mRippleDrawable);
        else
            mRippleView.setBackgroundDrawable(mRippleDrawable);

    }

    protected void onTabSelect(int index) {

    }

    public void addTab(int resId, String text, int color) {
        View tab = LayoutInflater.from(getContext()).inflate(R.layout.item_tab_view, mTabContainer, false);
        ((ImageView) tab.findViewById(R.id.icon)).setImageResource(resId);
        TextView title = (TextView) tab.findViewById(R.id.text);
        if (!TextUtils.isEmpty(text))
            title.setText(text);
        else
            title.setVisibility(View.GONE);
        addTab(tab, color);
    }

    protected void addTab(View tab, int color) {
        if (mTabContainer.getChildCount() == 0) {
            mRoot.setBackgroundColor(color);

        }
        mTabContainer.addView(tab);
        tab.setOnClickListener(mOnTabClickListener);
        mItemRippleColor.put(mTabContainer.getChildCount() - 1, color);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void playRippleEffect(final int index) {
        mRippleDrawable.setColor(mItemRippleColor.get(index), true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (mCurAnimator != null && mCurAnimator.isRunning()) {
                mCurAnimator.cancel();
                mCurAnimator = null;
            }
            float maxRadius = Math.max(mRippleView.getRight() - mHotPoint.x, mHotPoint.x);
            mCurAnimator = ViewAnimationUtils.createCircularReveal(mRippleView, (int) mHotPoint.x, (int) mHotPoint.y, 0, maxRadius);
            mCurAnimator.addListener(new Animator.AnimatorListener() {

                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mRoot.setBackgroundColor(mItemRippleColor.get(index));
                    mCurAnimator = null;
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    mRoot.setBackgroundColor(mItemRippleColor.get(index));
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
            mCurAnimator.start();
        } else {
            mRippleDrawable.setHotspot(mHotPoint.x, mHotPoint.y);
            mRippleDrawable.startRippleAnimator(new com.nineoldandroids.animation.Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(com.nineoldandroids.animation.Animator animation) {

                }

                @Override
                public void onAnimationEnd(com.nineoldandroids.animation.Animator animation) {
                    mRoot.setBackgroundColor(mItemRippleColor.get(index));
                }

                @Override
                public void onAnimationCancel(com.nineoldandroids.animation.Animator animation) {
                    mRoot.setBackgroundColor(mItemRippleColor.get(index));
                }

                @Override
                public void onAnimationRepeat(com.nineoldandroids.animation.Animator animation) {

                }
            });
        }
    }


}
