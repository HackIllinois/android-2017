package org.hackillinois.android.ui.modules.home;

import android.animation.Animator;
import android.content.Context;
import android.os.Handler;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieDrawable;

import org.joda.time.DateTime;
import org.joda.time.Period;

import java.util.concurrent.TimeUnit;

public class HomeClock {
	private static final int START_FRAME = numberToFrame(60);
	private static final int END_FRAME = numberToFrame(0);
	private static final float SECONDS_SPEED_RATIO = 1f / (2.0f);
	private static final String ANIMATION_FILE = "countdown.json";

	private final LottieAnimationView secondAnimation;
	private int seconds;
	private final LottieAnimationView minuteAnimation;
	private int minutes;
	private final LottieAnimationView hourAnimation;
	private int hours;

	public HomeClock(
			LottieAnimationView secondAnimation,
			LottieAnimationView minuteAnimation,
			LottieAnimationView hourAnimation
	) {
		this.secondAnimation = secondAnimation;
		this.minuteAnimation = minuteAnimation;
		this.hourAnimation = hourAnimation;

		initializeBounds();
		initializeSpeeds();
		initializeIncrementListeners();
	}

	public void setCountDownTo(Context context, DateTime time) {
		Period diff = new Period(DateTime.now(), time);
		seconds = diff.getSeconds();
		minutes = diff.getMinutes();
		hours = diff.getHours();
		LottieComposition.Factory.fromAssetFileName(context, ANIMATION_FILE, composition -> {
			secondAnimation.setFrame(numberToFrame(seconds));
			minuteAnimation.setFrame(numberToFrame(minutes));
			hourAnimation.setFrame(numberToFrame(hours));

			secondAnimation.resumeAnimation();
			secondAnimation.setRepeatMode(LottieDrawable.INFINITE);
			secondAnimation.setRepeatCount(LottieDrawable.INFINITE);
		});
	}

	private void initializeBounds() {
		secondAnimation.setMinAndMaxFrame(START_FRAME, END_FRAME);
		minuteAnimation.setMinAndMaxFrame(START_FRAME, END_FRAME);
		hourAnimation.setMinAndMaxFrame(START_FRAME, END_FRAME);
	}

	private void initializeSpeeds() {
		secondAnimation.setSpeed(SECONDS_SPEED_RATIO);
		minuteAnimation.setSpeed(SECONDS_SPEED_RATIO);
		hourAnimation.setSpeed(SECONDS_SPEED_RATIO);
	}

	private void initializeIncrementListeners() {
		secondAnimation.addAnimatorListener(getDecrementAnimator());
	}

	public static int numberToFrame(int displayNumber) {
		return 1830 - 30 * displayNumber;
	}

	private Animator.AnimatorListener getDecrementAnimator() {
		return new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animator) {
			}

			@Override
			public void onAnimationEnd(Animator animator) {
			}

			@Override
			public void onAnimationCancel(Animator animator) {
			}

			@Override
			public void onAnimationRepeat(Animator animator) {
				int almostLastFrame = (numberToFrame(0) + numberToFrame(1)) / 2;
				if (minuteAnimation.getFrame() > almostLastFrame && hours >= 1) {
					--hours;
					minutes += 59;
					hourAnimation.resumeAnimation();
					new Handler().postDelayed(hourAnimation::pauseAnimation, TimeUnit.SECONDS.toMillis(1));
				}

				if (minutes >= 1) {
					--minutes;
					minuteAnimation.resumeAnimation();
					new Handler().postDelayed(minuteAnimation::pauseAnimation, TimeUnit.SECONDS.toMillis(1));
				}

				if (hours == 0 && minutes == 0) {
					// remember that we already subtracted for hours/minutes, so only seconds remain.

					new Handler().postDelayed(() -> {
						secondAnimation.setRepeatCount(0);
						secondAnimation.setMaxFrame(numberToFrame(0));
					}, TimeUnit.SECONDS.toMillis(5)); // 5 seconds to be safe
				}
			}
		};
	}
}
