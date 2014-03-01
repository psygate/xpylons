package com.psygate.xpylons;

public class WatchDogConfiguration {
	private boolean isActive;
	private float lowerLimit;
	private float adjust;
	private int startAtTicks;

	public WatchDogConfiguration(XPylons pylons) {
		isActive = true;
		lowerLimit = 0.02f;
		adjust = 0.02f;
		startAtTicks = 10;
	}

	public boolean isActive() {
		return isActive;
	}

	public float getLowerLimit() {
		return lowerLimit;
	}

	public float getAdjust() {
		return adjust;
	}

	public int getStartAtTicks() {
		return startAtTicks;
	}

}
