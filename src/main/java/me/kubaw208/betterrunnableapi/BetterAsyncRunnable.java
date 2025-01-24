package me.kubaw208.betterrunnableapi;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import lombok.Getter;
import me.kubaw208.betterrunnableapi.structs.BetterTask;
import me.kubaw208.betterrunnableapi.structs.PauseType;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Classic asynchronous task class that is repeating in given an interval with possible delay on start.
 * Can be paused and unpaused.
 */
@Getter
public class BetterAsyncRunnable extends BetterRunnable {

    private ScheduledTask runnableID;

    /**
     * Creates a new asynchronous task.
     * @param plugin plugin main class that runs task.
     * @param pauseType pause type (default: AUTOMATIC).
     * @param group tasks group that automatically adds a task to that group if a group is not null.
     * @param task code in task to execute.
     * @param delay time in milliseconds to wait before the first run (default: 0).
     * @param interval time in milliseconds between runs.
     */
    public BetterAsyncRunnable(JavaPlugin plugin, PauseType pauseType, BetterRunnableGroup group, Consumer<BetterTask> task, long delay, long interval) {
        super(plugin, pauseType, group, task, delay, interval);
    }

    /** @see #BetterAsyncRunnable(JavaPlugin, PauseType, BetterRunnableGroup, Consumer, long, long) */
    public BetterAsyncRunnable(JavaPlugin plugin, BetterRunnableGroup group, Consumer<BetterTask> task, long delay, long interval) {
        super(plugin, group, task, delay, interval);
    }

    /** @see #BetterAsyncRunnable(JavaPlugin, PauseType, BetterRunnableGroup, Consumer, long, long) */
    public BetterAsyncRunnable(JavaPlugin plugin, PauseType pauseType, BetterRunnableGroup group, Consumer<BetterTask> task, long interval) {
        super(plugin, pauseType, group, task, interval);
    }

    /** @see #BetterAsyncRunnable(JavaPlugin, PauseType, BetterRunnableGroup, Consumer, long, long) */
    public BetterAsyncRunnable(JavaPlugin plugin, BetterRunnableGroup group, Consumer<BetterTask> task, long interval) {
        super(plugin, group, task, interval);
    }

    /** @see #BetterAsyncRunnable(JavaPlugin, PauseType, BetterRunnableGroup, Consumer, long, long) */
    public BetterAsyncRunnable(JavaPlugin plugin, PauseType pauseType, Consumer<BetterTask> task, long delay, long interval) {
        super(plugin, pauseType, task, delay, interval);
    }

    /** @see #BetterAsyncRunnable(JavaPlugin, PauseType, BetterRunnableGroup, Consumer, long, long) */
    public BetterAsyncRunnable(JavaPlugin plugin, Consumer<BetterTask> task, long delay, long interval) {
        super(plugin, task, delay, interval);
    }

    /** @see #BetterAsyncRunnable(JavaPlugin, PauseType, BetterRunnableGroup, Consumer, long, long) */
    public BetterAsyncRunnable(JavaPlugin plugin, PauseType pauseType, Consumer<BetterTask> task, long interval) {
        super(plugin, pauseType, task, interval);
    }

    /** @see #BetterAsyncRunnable(JavaPlugin, PauseType, BetterRunnableGroup, Consumer, long, long) */
    public BetterAsyncRunnable(JavaPlugin plugin, Consumer<BetterTask> task, long interval) {
        super(plugin, task, interval);
    }

    @Override
    public void start() {
        if(isStopped) {
            taskStartedTime = System.currentTimeMillis();
            lastTaskExecutionTime = System.currentTimeMillis();
        }

        if(runnableID != null) {
            runnableID.cancel();
            runnableID = null;
        }

        runnableID = Bukkit.getAsyncScheduler().runAtFixedRate(getPlugin(), scheduledTask -> execute(), (isStopped ? delay : newDelayAfterPauseTask), getInterval(), TimeUnit.MILLISECONDS);

        isStopped = false;
    }

    @Override
    public boolean stop() {
        while(!groups.isEmpty()) {
            groups.get(0).removeTask(this);
        }

        isStopped = true;

        executions = 0;
        pauseTime = 0;
        pausedTime = 0;

        if(runnableID == null) return false;

        runnableID.cancel();
        runnableID = null;
        return true;
    }

    /**
     * @param removeFromGroups if true, the task will be removed from all groups
     * @see #stop()
     */
    @Override
    public boolean stop(boolean removeFromGroups) {
        if(removeFromGroups) {
            while(!groups.isEmpty()) {
                groups.get(0).removeTask(this);
            }
        }

        isStopped = true;

        executions = 0;
        pauseTime = 0;
        pausedTime = 0;

        if(runnableID == null) return false;

        runnableID.cancel();
        runnableID = null;
        return true;
    }

    @Override
    public void execute() {
        super.execute();

        lastTaskExecutionTime = System.currentTimeMillis();
    }

    @Override
    public void pause() {
        if(isPaused) return;

        newDelayAfterPauseTask = lastTaskExecutionTime - System.currentTimeMillis() + (isStopped ? delay : interval);
        pauseTime = System.currentTimeMillis();

        if(pauseType == PauseType.AUTOMATIC && runnableID != null) {
            runnableID.cancel();
            runnableID = null;
        }

        this.isPaused = true;
    }

    @Override
    public void unpause() {
        if(!isPaused) return;

        pausedTime += System.currentTimeMillis() - pauseTime;

        if(pauseType == PauseType.AUTOMATIC)
            start();

        this.isPaused = false;
    }

}