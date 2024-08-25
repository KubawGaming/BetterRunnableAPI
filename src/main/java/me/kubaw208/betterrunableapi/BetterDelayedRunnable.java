package me.kubaw208.betterrunableapi;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Consumer;

/**
 * Classic synchronous delayed task class that is executes after given delay.
 * Can be paused and unpaused.
 */
public class BetterDelayedRunnable extends BetterRunnable {

    private int passedTimeInTicks;
    private long taskStartedTime;

    /**
     * Creates new synchronous delayed task that is executed only once after given delay
     * @param plugin plugin main class that runs task
     * @param task code in task to run
     * @param delay time in ticks to wait before task executes code
     */
    public BetterDelayedRunnable(JavaPlugin plugin, Consumer<BetterRunnable> task, long delay) {
        super(plugin, task, delay, 0);
    }

    /**
     * Creates new synchronous delayed task that is executed only once after given delay
     * @param plugin plugin main class that runs task
     * @param group tasks group that automatically adds task to that group
     * @param task code in task to run
     * @param delay time in ticks to wait before task executes code
     */
    public BetterDelayedRunnable(JavaPlugin plugin, BetterRunnableGroup group, Consumer<BetterRunnable> task, long delay) {
        super(plugin, group, task, delay, 0);
    }

    /**
     * Starts delayed task. Can be used again after {@link #run()} method to repeat code in task with the same delay
     */
    @Override
    public void startTask() {
        cancel();
        runnableID = Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), this::run, getDelay() - passedTimeInTicks);
        taskStartedTime = System.currentTimeMillis();
    }

    /**
     * Code in method, that executes when delayed task is done
     */
    @Override
    public void run() {
        super.run();
        passedTimeInTicks = 0;
        taskStartedTime = 0;
    }

    /**
     * Pauses delayed task and saves the time already waited
     */
    @Override
    public void pause() {
        super.pause();
        cancel();
        passedTimeInTicks += (int) (System.currentTimeMillis() - taskStartedTime) / 50;
    }

    /**
     * Unpauses delayed task and starts it with missing time to complete task in correct time
     */
    @Override
    public void unpause() {
        super.unpause();
        startTask();
    }

}