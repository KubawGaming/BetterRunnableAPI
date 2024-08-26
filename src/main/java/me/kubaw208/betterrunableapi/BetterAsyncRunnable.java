package me.kubaw208.betterrunableapi;

import me.kubaw208.betterrunableapi.enums.PauseType;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Classic asynchronous task class that is repeating in given interval with possibly delay on start.
 * Can be paused and unpaused.
 */
public class BetterAsyncRunnable extends BetterRunnable {

    /**
     * Creates new asynchronous task
     * @param plugin plugin main class that runs task
     * @param task code in task to run
     * @param interval time in milliseconds between runs
     */
    public BetterAsyncRunnable(JavaPlugin plugin, Consumer<BetterRunnable> task, long interval) {
        super(plugin, task, interval);
    }

    /**
     * Creates new asynchronous task
     * @param plugin plugin main class that runs task
     * @param pauseType pause type
     * @param task code in task to run
     * @param interval time in milliseconds between runs
     */
    public BetterAsyncRunnable(JavaPlugin plugin, PauseType pauseType, Consumer<BetterRunnable> task, long interval) {
        super(plugin, pauseType, task, interval);
    }

    /**
     * Creates new asynchronous task
     * @param plugin plugin main class that runs task
     * @param task code in task to run
     * @param delay time in milliseconds to wait before first run
     * @param interval time in milliseconds between runs
     */
    public BetterAsyncRunnable(JavaPlugin plugin, Consumer<BetterRunnable> task, long delay, long interval) {
        super(plugin, task, delay, interval);
    }

    /**
     * Creates new asynchronous task
     * @param plugin plugin main class that runs task
     * @param pauseType pause type
     * @param task code in task to run
     * @param delay time in milliseconds to wait before first run
     * @param interval time in milliseconds between runs
     */
    public BetterAsyncRunnable(JavaPlugin plugin, PauseType pauseType, Consumer<BetterRunnable> task, long delay, long interval) {
        super(plugin, pauseType, task, delay, interval);
    }

    /**
     * Creates new asynchronous task
     * @param plugin plugin main class that runs task
     * @param group tasks group that automatically adds task to that group
     * @param task code in task to run
     * @param interval time in milliseconds between runs
     */
    public BetterAsyncRunnable(JavaPlugin plugin, BetterRunnableGroup group, Consumer<BetterRunnable> task, long interval) {
        super(plugin, group, task, interval);
    }

    /**
     * Creates new asynchronous task
     * @param plugin plugin main class that runs task
     * @param pauseType pause type
     * @param group tasks group that automatically adds task to that group
     * @param task code in task to run
     * @param interval time in milliseconds between runs
     */
    public BetterAsyncRunnable(JavaPlugin plugin, PauseType pauseType, BetterRunnableGroup group, Consumer<BetterRunnable> task, long interval) {
        super(plugin, pauseType, group, task, interval);
    }

    /**
     * Creates new asynchronous task
     * @param plugin plugin main class that runs task
     * @param group tasks group that automatically adds task to that group
     * @param task code in task to run
     * @param delay time in milliseconds to wait before first run
     * @param interval time in milliseconds between runs
     */
    public BetterAsyncRunnable(JavaPlugin plugin, BetterRunnableGroup group, Consumer<BetterRunnable> task, long delay, long interval) {
        super(plugin, group, task, delay, interval);
    }

    /**
     * Creates new asynchronous task
     * @param plugin plugin main class that runs task
     * @param pauseType pause type
     * @param group tasks group that automatically adds task to that group
     * @param task code in task to run
     * @param delay time in milliseconds to wait before first run
     * @param interval time in milliseconds between runs
     */
    public BetterAsyncRunnable(JavaPlugin plugin, PauseType pauseType, BetterRunnableGroup group, Consumer<BetterRunnable> task, long delay, long interval) {
        super(plugin, pauseType, group, task, delay, interval);
    }

    @Override
    public void startTask() {
        cancel();
        runnableID = Bukkit.getAsyncScheduler().runAtFixedRate(getPlugin(), scheduledTask -> run(), getDelay(), getInterval(), TimeUnit.MILLISECONDS);
    }

}