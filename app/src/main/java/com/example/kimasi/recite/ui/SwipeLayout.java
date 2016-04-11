package com.example.kimasi.recite.ui;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * 抽屉小组件
 * Created by Bruce on 11/24/14.
 */
public class SwipeLayout extends LinearLayout {

    private ViewDragHelper viewDragHelper;
    private View contentView;
    private View actionView;
    private int dragDistance;
    private final double AUTO_OPEN_SPEED_LIMIT =800.0;
    private int draggedX;

    public SwipeLayout(Context context) {
        this(context, null);
    }

    public SwipeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public SwipeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        viewDragHelper = ViewDragHelper.create(this, new DragHelperCallback());
    }

    @Override
    protected void onFinishInflate() {//映射到xml时触发
        contentView = getChildAt(0);//组件1,看得到的
        actionView = getChildAt(1);//组件2,后面那个
        actionView.setVisibility(GONE);//后面那个隐藏
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {//应该是设置大小时 触发
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        dragDistance = actionView.getMeasuredWidth();//组件的宽(后面那个)
    }

    private class DragHelperCallback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View view, int i) {
            return view == contentView || view == actionView;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {//应该是捕获视图变化
            draggedX = left;
            if (changedView == contentView) {
                actionView.offsetLeftAndRight(dx);//水平移动
            } else {
                contentView.offsetLeftAndRight(dx);
            }
            if (actionView.getVisibility() == View.GONE) {
                actionView.setVisibility(View.VISIBLE);
            }
            invalidate();
        }

        @Override //手指触摸移动时实时回调, left表示要到的x位置
        public int clampViewPositionHorizontal(View child, int left, int dx) {//应该是限制拖动的到哪个位置;
            if (child == contentView) {
                final int leftBound = getPaddingLeft();//组件与桌面的间隔距离
                final int minLeftBound = -leftBound - dragDistance;//300
                final int newLeft = Math.min(Math.max(minLeftBound, left), 0);//求最小值(一个可拖动的最小范围
                return newLeft;
            } else {
                final int minLeftBound = getPaddingLeft() + contentView.getMeasuredWidth() - dragDistance;
                final int maxLeftBound = getPaddingLeft() + contentView.getMeasuredWidth() + getPaddingRight();
                final int newLeft = Math.min(Math.max(left, minLeftBound), maxLeftBound);
                return newLeft;
            }
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return dragDistance;
        }//水平运动的范围

        @Override//手指释放时回调
        public void onViewReleased(View releasedChild, float xvel, float yvel) {//没有拖动一定距离时复位
            super.onViewReleased(releasedChild, xvel, yvel);
            boolean settleToOpen = false;
            if (xvel > AUTO_OPEN_SPEED_LIMIT) {  //x没有移动到这个值时复位
                settleToOpen = false;
            } else if (xvel < -AUTO_OPEN_SPEED_LIMIT) {
                settleToOpen = true;
            } else if (draggedX <= -dragDistance / 10*9) {//10*9控制后面的隐藏块,没拖出来十分之九就会复原
                settleToOpen = true;
            } else if (draggedX > -dragDistance / 10*9) {
                settleToOpen = false;
            }

            final int settleDestX = settleToOpen ? -dragDistance : 0;
            viewDragHelper.smoothSlideViewTo(contentView, settleDestX, 0);//移动视图动画
            ViewCompat.postInvalidateOnAnimation(SwipeLayout.this);//
        }
    }

    @Override //重写onInterceptTouchEvent回调ViewDragHelper中对应的方法
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(viewDragHelper.shouldInterceptTouchEvent(ev)) {
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(viewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }
}
