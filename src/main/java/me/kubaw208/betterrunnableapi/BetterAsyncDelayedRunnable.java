package me.kubaw208.betterrunnableapi;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import lombok.Getter;
import me.kubaw208.betterrunnableapi.structs.BetterTask;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Classic asynchronous delayed task class that is executed after given delay.
 * Can be paused and unpaused.
 */
@Getter
public class BetterAsyncDelayedRunnable extends BetterDelayedRunnable {

    private ScheduledTask runnableID;

    /**
     * Creates a new asynchronous delayed task executed only once after given delay.
     * @param plugin plugin main class that runs task.
     * @param group tasks group that automatically adds a task to that group if a group is not null.
     * @param task code in task to execute.
     * @param delay time in milliseconds to wait before the first run (default: 0).
     */
    public BetterAsyncDelayedRunnable(JavaPlugin plugin, BetterRunnableGroup group, Consumer<BetterTask> task, long delay) {
        super(plugin, group, task, delay);
    }

    public BetterAsyncDelayedRunnable(JavaPlugin plugin, Consumer<BetterTask> task, long delay) {
        super(plugin, task, delay);
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public void start() {
        runnableID = Bukkit.getAsyncScheduler().runDelayed(getPlugin(), scheduledTask -> execute(), getDelay() - passedTime, TimeUnit.MILLISECONDS);
        taskStartedTime = System.currentTimeMillis();
        isStopped = false;
    }

    @Override
    public boolean stop() {
        return stop(true);
    }

    @Override
    public boolean stop(boolean removeFromGroups) {
        if(removeFromGroups) {
            while(!groups.isEmpty()) {
                groups.get(0).removeTask(this);
            }
        }

        isStopped = true;

        if(runnableID == null) return false;

        runnableID.cancel();
        runnableID = null;
        return true;
    }

    @Override
    public void pause() {
        if(isPaused) return;

        if(runnableID != null) {
            runnableID.cancel();
            runnableID = null;
        }

        isPaused = true;
        passedTime += (System.currentTimeMillis() - taskStartedTime);
    }

}