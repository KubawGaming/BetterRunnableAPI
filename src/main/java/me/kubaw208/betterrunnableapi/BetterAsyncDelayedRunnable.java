package me.kubaw208.betterrunnableapi;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Classic asynchronous delayed task class that is executed after given delay.
 * Can be paused and unpaused.
 */
public class BetterAsyncDelayedRunnable extends BetterDelayedRunnable {

    /**
     * Creates a new asynchronous delayed task executed only once after given delay.
     * @param plugin plugin main class that runs task.
     * @param group tasks group that automatically adds a task to that group if a group is not null.
     * @param task code in task to execute.
     * @param delay time in milliseconds to wait before the first run (default: 0).
     */
    public BetterAsyncDelayedRunnable(JavaPlugin plugin, BetterRunnableGroup group, Consumer<BetterDelayedRunnable> task, long delay) {
        super(plugin, group, task, delay);
    }

    public BetterAsyncDelayedRunnable(JavaPlugin plugin, Consumer<BetterDelayedRunnable> task, long delay) {
        super(plugin, task, delay);
    }

    @Override
    public void startTask() {
        runnableID = Bukkit.getAsyncScheduler().runDelayed(getPlugin(), scheduledTask -> execute(), getDelay() - passedTime, TimeUnit.MILLISECONDS);
        taskStartedTime = System.currentTimeMillis();
        isStopped = false;
    }

    @Override
    public void pause() {
        if(isPaused) return;

        if(runnableID != null) {
            Bukkit.getScheduler().cancelTask((int) runnableID);
            runnableID = null;
        }

        isPaused = true;
        passedTime += (System.currentTimeMillis() - taskStartedTime);
    }

}