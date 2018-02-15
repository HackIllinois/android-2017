package org.hackillinois.android.ui.modules.home;

import android.animation.Animator;
import android.content.Context;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieDrawable;
import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import org.joda.time.DateTime;
import org.joda.time.Period;

import java.util.List;
import java.util.concurrent.TimeUnit;

import timber.log.Timber;

public class HomeClock {
	public interface OnFinishListener {

		void onFinish(int timerIndex);
	}

	private static final float SECONDS_SPEED_RATIO = 1f / (2.0f);

	private static final String COUNTDOWN_60_JSON = "countdown-60.json";
	private static final String COUNTDOWN_24_JSON = "countdown-24.json";
	private final LottieAnimationView secondAnimation;
	private OnFinishListener onFinishListener;

	private List<DateTime> activeTimers;
	private int finishedTimers;

	private int seconds;
	private final LottieAnimationView minuteAnimation;
	private int minutes;
	private final LottieAnimationView hourAnimation;
	private int hours;
	private final LottieAnimationView daysAnimation;
	private int days;

	public HomeClock(
			LottieAnimationView secondAnimation,
			LottieAnimationView minuteAnimation,
			LottieAnimationView hourAnimation,
			LottieAnimationView daysAnimation
	) {
		this.secondAnimation = secondAnimation;
		this.minuteAnimation = minuteAnimation;
		this.hourAnimation = hourAnimation;
		this.daysAnimation = daysAnimation;

		initializeSpeeds();
		initializeIncrementListeners();
	}

	/**
	 * The input list of dates is assumed to be sorted
	 *
	 * @param onFinishListener
	 * @param dateTimes
	 */
	public void setCountDownTo(OnFinishListener onFinishListener, List<DateTime> dateTimes) {
		this.onFinishListener = onFinishListener;
		this.activeTimers = Stream.of(dateTimes).filter(DateTime::isAfterNow).collect(Collectors.toList());

		finishedTimers = dateTimes.size() - activeTimers.size();
		if (onFinishListener != null) {
			onFinishListener.onFinish(finishedTimers);
		}

		playNext();
	}

	private void setCountdownView(DateTime time) {
		if (time.isBeforeNow()) {
			return;
		}

		Context context = secondAnimation.getContext();

		LottieComposition.Factory.fromAssetFileName(context, COUNTDOWN_24_JSON, composition -> {
			Period diff = new Period(DateTime.now(), time);
			hours = diff.getHours();
			hourAnimation.setFrame(numberToFrame(hours));
		});

		LottieComposition.Factory.fromAssetFileName(context, COUNTDOWN_60_JSON, composition -> {
			Period diff = new Period(DateTime.now(), time);
			seconds = diff.getSeconds();
			minutes = diff.getMinutes();
			days = diff.getDays() + 7 * diff.getWeeks();

			secondAnimation.setFrame(numberToFrame(seconds));
			minuteAnimation.setFrame(numberToFrame(minutes));
			daysAnimation.setFrame(numberToFrame(days));

			if (isLastLoop()) {
				secondAnimation.setRepeatCount(0);
			} else {
				secondAnimation.setRepeatMode(LottieDrawable.INFINITE);
				secondAnimation.setRepeatCount(LottieDrawable.INFINITE);
			}
			secondAnimation.resumeAnimation();
		});
	}

	private void playNext() {
		if (activeTimers.size() > 0) {
			DateTime nextTimer = activeTimers.get(0);
			Timber.d("Going to next active timer %s", nextTimer);
			setCountdownView(nextTimer);
		}
	}

	private void initializeSpeeds() {
		secondAnimation.setSpeed(-SECONDS_SPEED_RATIO);
		minuteAnimation.setSpeed(-SECONDS_SPEED_RATIO);
		hourAnimation.setSpeed(-SECONDS_SPEED_RATIO);
		daysAnimation.setSpeed(-SECONDS_SPEED_RATIO);
	}

	private void initializeIncrementListeners() {
		secondAnimation.addAnimatorListener(getDecrementAnimator());
	}

	public static int numberToFrame(int displayNumber) {
		return 30 * displayNumber;
	}

	private Animator.AnimatorListener getDecrementAnimator() {
		return new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animator) {
			}

			@Override
			public void onAnimationEnd(Animator animator) {
				activeTimers.remove(0);
				++finishedTimers;
				if (onFinishListener != null) {
					onFinishListener.onFinish(finishedTimers);
				}
				playNext();
			}

			@Override
			public void onAnimationCancel(Animator animator) {
			}

			@Override
			public void onAnimationRepeat(Animator animator) {
				int almostLastFrame = (numberToFrame(0) + numberToFrame(1)) / 2;
				if (hourAnimation.getFrame() < almostLastFrame && minuteAnimation.getFrame() < almostLastFrame && days >= 1) {
					--days;
					hours += 24;
					daysAnimation.setMinFrame(numberToFrame(days));
					daysAnimation.resumeAnimation();
				}

				if (minuteAnimation.getFrame() < almostLastFrame && hours >= 1) {
					--hours;
					minutes += 60;
					hourAnimation.setMinFrame(numberToFrame(hours));
					hourAnimation.resumeAnimation();
				}

				if (minutes >= 1) {
					--minutes;
					minuteAnimation.setMinFrame(numberToFrame(minutes));
					minuteAnimation.resumeAnimation();
				}

				if (isLastLoop()) {
					// remember that we already subtracted for hours/minutes, so only seconds remain.
					secondAnimation.setRepeatCount(0);
					secondAnimation.playAnimation();
				}
			}
		};
	}

	private boolean isLastLoop() {
		return days == 0 && hours == 0 && minutes == 0;
	}
}
