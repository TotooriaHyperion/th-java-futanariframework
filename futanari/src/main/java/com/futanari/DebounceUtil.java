package com.futanari;

import org.apache.log4j.Logger;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by user on 2017/4/26.
 */
public class DebounceUtil {

	public static DebounceUtil getInstance(Runnable callback,long delay,long limit) {
		return new DebounceUtil(callback,delay,limit);
	}

	DebounceUtil(Runnable callback,long delay, long limit) {
		this.callback = callback;
		this.delay = delay;
		this.limit = limit;
	}

	private Date debounceTime;
	private long delay;
	private long limit;
	private Runnable callback;
	private TimerTask aTask;

	private TimerTask getTask() {
		return new TimerTask() {
			@Override
			public void run() {
				callback.run();
				debounceTime = null;
				this.cancel();
			}
		};
	}


	public Timer debounce(Timer timer) {
		Date currentTime = new Date();

		System.out.println(currentTime);
		System.out.println(debounceTime);
		if (debounceTime == null) {
			System.out.println(1);
			debounceTime = currentTime;
			timer.purge();
			aTask = getTask();
			timer.schedule(aTask, delay);
		} else if (currentTime.getTime() - debounceTime.getTime() < limit) {
			System.out.println(2);
			if (aTask != null) {
				aTask.cancel();
			}
			timer.purge();
			aTask = getTask();
			timer.schedule(aTask, delay);
		} else {
			System.out.println(3);
			if (aTask != null) {
				aTask.cancel();
			}
			timer.purge();
			aTask = getTask();
			timer.schedule(aTask, 0);
			debounceTime = null;
		}
		return timer;
	}

}
