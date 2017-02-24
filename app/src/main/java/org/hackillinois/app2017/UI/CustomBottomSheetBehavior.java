package org.hackillinois.app2017.UI;

import android.content.Context;
import android.os.Parcelable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class CustomBottomSheetBehavior extends BottomSheetBehavior {
    private boolean allowUserDragging = false;

    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, View child, MotionEvent event) {
        if (allowUserDragging) {
            return super.onInterceptTouchEvent(parent, child, event);
        }

        return allowUserDragging;
    }

    public CustomBottomSheetBehavior() {
        super();
    }

    public CustomBottomSheetBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean getSkipCollapsed() {
        return super.getSkipCollapsed();
    }

    @Override
    public boolean isHideable() {
        return super.isHideable();
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        return super.onLayoutChild(parent, child, layoutDirection);
    }

    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, View child, View target, float velocityX, float velocityY) {
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
    }

    @Override
    public void onRestoreInstanceState(CoordinatorLayout parent, View child, Parcelable state) {
        super.onRestoreInstanceState(parent, child, state);
    }

    @Override
    public Parcelable onSaveInstanceState(CoordinatorLayout parent, View child) {
        return super.onSaveInstanceState(parent, child);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        return super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target) {
        super.onStopNestedScroll(coordinatorLayout, child, target);
    }

    @Override
    public boolean onTouchEvent(CoordinatorLayout parent, View child, MotionEvent event) {
        return super.onTouchEvent(parent, child, event);
    }

    @Override
    public void setBottomSheetCallback(BottomSheetCallback callback) {
        super.setBottomSheetCallback(callback);
    }

    @Override
    public void setHideable(boolean hideable) {
        super.setHideable(hideable);
    }

    @Override
    public void setSkipCollapsed(boolean skipCollapsed) {
        super.setSkipCollapsed(skipCollapsed);
    }
}
