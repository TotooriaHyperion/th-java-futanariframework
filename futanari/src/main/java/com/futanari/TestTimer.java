package com.futanari;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by user on 2017/4/26.
 */
public class TestTimer {

	public static void main(String[] args) {
//		timer.schedule(timerTask,5000,1000);

		Runnable run = new Runnable() {
			@Override
			public void run() {
				System.out.println("延迟任务");

			}
		};
		DebounceUtil aDebounce = DebounceUtil.getInstance(run,2000,5000);

		aDebounce.debounce(new Timer());

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Timer timer = new Timer();


		new Thread() {
			@Override
			public void run() {
				int i=0;
				while (i<10) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					aDebounce.debounce(timer);
					i++;
				}
			}
		}.start();


		try {
			Thread.sleep(15000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		new Thread() {
			@Override
			public void run() {
				int i=0;
				while (i<10) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					aDebounce.debounce(timer);
					i++;
				}
			}
		}.start();

	}

}
