package me.kubaw208.betterrunableapi;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Consumer;

/**
 * Classic synchronous task class that is repeating in given interval with possibly delay on start.
 * Can be paused and unpaused.
 */
public class BetterRunnable {

    @Getter private final JavaPlugin plugin;
    @Getter private Consumer<BetterRunnable> task;
    @Getter private boolean isPaused = false;
    @Getter private long delay = 0;
    @Getter private long interval;
    public long executions = 0;
    @Getter protected Object runnableID = null;

    /**
     * Creates new synchronous task
     * @param plugin plugin main class that runs task
     * @param task code in task to run
     * @param interval time in ticks between runs
     */
    public BetterRunnable(JavaPlugin plugin, Consumer<BetterRunnable> task, long interval) {
        this.plugin = plugin;
        this.task = task;
        this.interval = interval > 0 ? interval : 1;

        startTask();
    }

    /**
     * Creates new synchronous task
     * @param plugin plugin main class that runs task
     * @param task code in task to run
     * @param delay time in ticks to wait before first run
     * @param interval time in ticks between runs
     */
    public BetterRunnable(JavaPlugin plugin, Consumer<BetterRunnable> task, long delay, long interval) {
        this.plugin = plugin;
        this.task = task;
        this.delay = delay;
        this.interval = interval > 0 ? interval : 1;

        startTask();
    }

    /**
     * Creates new synchronous task
     * @param plugin plugin main class that runs task
     * @param group tasks group that automatically adds task to that group
     * @param task code in task to run
     * @param interval time in ticks between runs
     */
    public BetterRunnable(JavaPlugin plugin, BetterRunnableGroup group, Consumer<BetterRunnable> task, long interval) {
        this(plugin, task, interval);

        if(group != null)
            group.addTask(this);
    }

    /**
     * Creates new synchronous task
     * @param plugin plugin main class that runs task
     * @param group tasks group that automatically adds task to that group
     * @param task code in task to run
     * @param delay time in ticks to wait before first run
     * @param interval time in ticks between runs
     */
    public BetterRunnable(JavaPlugin plugin, BetterRunnableGroup group, Consumer<BetterRunnable> task, long delay, long interval) {
        this(plugin, task, delay, interval);

        if(group != null)
            group.addTask(this);
    }

    /**
     * Pause task
     */
    public void pause() {
        this.isPaused = true;
    }

    /**
     * Unpause task
     */
    public void unpause() {
        this.isPaused = false;
    }

    /**
     * Cancel task if it is running and starts it. Can be used to start task again
     */
    public void startTask() {
        cancel();
        runnableID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this::run, delay, interval);
    }

    /**
     * Code in method that executes when task runs
     */
    public void run() {
        if(!isPaused) {
            task.accept(this);
            if(Long.MAX_VALUE > executions + 1) executions++;
        }
    }

    /**
     * Stops task
     */
    public void cancel() {
        if(runnableID != null) {
            Bukkit.getScheduler().cancelTask((int) runnableID);
            runnableID = null;
        }
    }

}