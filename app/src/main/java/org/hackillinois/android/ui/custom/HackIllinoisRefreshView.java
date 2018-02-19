package org.hackillinois.android.ui.custom;

import android.content.Context;
import android.util.AttributeSet;

import com.airbnb.lottie.LottieAnimationView;
import com.dinuscxj.refresh.IRefreshStatus;

public class HackIllinoisRefreshView extends LottieAnimationView implements IRefreshStatus {
	public HackIllinoisRefreshView(Context context) {
		super(context);
		initView();
	}

	public HackIllinoisRefreshView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public HackIllinoisRefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView();
	}

	private void initView() {
		setFrame(0);
		setSpeed(0.5f);
	}

	@Override
	public void reset() {
		pauseAnimation();
		setFrame(0);
	}

	@Override
	public void refreshing() {
		playAnimation();
	}

	@Override
	public void refreshComplete() {

	}

	@Override
	public void pullToRefresh() {

	}

	@Override
	public void releaseToRefresh() {

	}

	@Override
	public void pullProgress(float pullDistance, float pullProgress) {

	}
}
