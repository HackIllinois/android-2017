package org.hackillinois.app2017.UI;

import android.content.Context;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class CustomBottomSheetBehavior extends BottomSheetBehavior {

    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, View child, MotionEvent event) {
        return false;
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
