package com.bb1.tub.api.interfaces;

import java.util.function.Supplier;

/**
 * An interface for delayed and repeating tasks
 */
public interface TaskManager {
	/**
	 * Runs the task provided
	 * 
	 * @param runnable The runnable that is executed
	 * @param asynchronous If the task should be handled asynchronously
	 */
	public void runTask(Runnable runnable, boolean asynchronous);
	/**
	 * Runs the task provided after a delay period
	 * 
	 * @param runnable The runnable that is executed
	 * @param delay The delay before the task is ran (in ms)
	 * @param asynchronous If the task should be handled asynchronously
	 */
	public default void runDelayedTask(Runnable runnable, long delay, boolean asynchronous) {
		runTask(new Runnable() {

			@Override
			public void run() {
				long time = System.currentTimeMillis()+delay;
				// Loop while we wait the set time
				while (time>System.currentTimeMillis()) {
					continue;
				}
				runTask(runnable, asynchronous);
			}
			
		}, true);
	}
	/**
	 * Runs the task provided a set amount of times with a set delay between each run
	 * 
	 * @param runnable The runnable that is executed
	 * @param amountOfRepeats The amount of times to repeat the task
	 * @param delayBetweenRepeats The delay between tasks (in ms)
	 * @param asynchronous If the task should be handled asynchronously
	 */
	public default void runRepeatingTask(Runnable runnable, int amountOfRepeats, long delayBetweenRepeats, boolean asynchronous) {
		runTask(new Runnable() {

			@Override
			public void run() {
				int repeats = amountOfRepeats;
				while (repeats>0) {
					long time = System.currentTimeMillis()+delayBetweenRepeats;
					// Loop while we wait the set time
					while (time>System.currentTimeMillis()) {
						continue;
					}
					runTask(runnable, asynchronous);
					repeats--;
				}
			}
			
		}, true);
	}
	/**
	 * The same as {@link #runRepeatingTask(Runnable, int, long, boolean)} but with a supplier for more versatility
	 * 
	 * @param runnable The runnable that is executed
	 * @param whileTrue If true, the task will run; If false, the task will not run; If null, the task gets discarded
	 * @param delayBetweenRepeats The delay between tasks (in ms)
	 * @param asynchronous If the task should be handled asynchronously
	 */
	public default void runRepeatingTask(Runnable runnable, Supplier<Boolean> whileTrue, long delayBetweenRepeats, boolean asynchronous) {
		runTask(new Runnable() {

			@Override
			public void run() {
				while (true) {
					long time = System.currentTimeMillis()+delayBetweenRepeats;
					// Loop while we wait the set time
					while (time>System.currentTimeMillis()) {
						continue;
					}
					Boolean suplierResult = whileTrue.get();
					if (suplierResult==null) { // If supplier returns null
						break; // Stop the task
					} else if (suplierResult) { // If supplier returns true
						runTask(runnable, asynchronous); // Run the task
					} else { // If supplier returns false
						 continue; // Skip this run of the task
					}
					
				}
			}
			
		}, true);
	}
}
