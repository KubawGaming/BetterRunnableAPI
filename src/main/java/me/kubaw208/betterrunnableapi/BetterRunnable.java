package me.kubaw208.betterrunnableapi;

import lombok.AccessLevel;
import lombok.Getter;
import me.kubaw208.betterrunnableapi.structs.BetterTask;
import me.kubaw208.betterrunnableapi.structs.PauseType;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * Classic synchronous task class that is repeating in given an interval with possible delay on start.
 * Can be paused and unpaused.
 */
@Getter
public class BetterRunnable implements BetterTask {

    protected final JavaPlugin plugin;
    protected final ArrayList<BetterRunnableGroup> groups = new ArrayList<>();
    protected PauseType pauseType;
    private Consumer<BetterTask> task;
    protected Object runnableID = null;
    protected boolean isPaused = false;
    protected long delay;
    protected long interval;
    protected long executions = 0;
    @Getter(AccessLevel.PRIVATE) protected long pauseTime = 0;
    @Getter(AccessLevel.PRIVATE) protected long pausedTime = 0;
    @Getter(AccessLevel.PRIVATE) protected long taskStartedTime;
    @Getter(AccessLevel.PRIVATE) protected long lastTaskExecutionTime;
    @Getter(AccessLevel.PRIVATE) protected long newDelayAfterPauseTask;
    protected boolean isStopped;

    /**
     * Creates a new synchronous task.
     * @param plugin plugin main class that runs task.
     * @param pauseType pause type (default: AUTOMATIC).
     * @param group tasks group that automatically adds a task to that group if a group is not null.
     * @param task code in task to execute.
     * @param delay time in ticks to wait before the first run (default: 0).
     * @param interval time in ticks between runs.
     */
    public BetterRunnable(JavaPlugin plugin, PauseType pauseType, BetterRunnableGroup group, Consumer<BetterTask> task, long delay, long interval) {
        this.plugin = plugin;
        this.pauseType = pauseType;
        this.task = task;
        this.delay = delay > 0 ? delay : 0;
        this.interval = interval > 0 ? interval : 1;
        this.isStopped = true;

        if(group != null)
            group.addTask(this);

        startTask();
    }

    /** @see #BetterRunnable(JavaPlugin, PauseType, BetterRunnableGroup, Consumer, long, long) */
    public BetterRunnable(JavaPlugin plugin, BetterRunnableGroup group, Consumer<BetterTask> task, long delay, long interval) {
        this(plugin, PauseType.AUTOMATIC, group, task, delay, interval);
    }

    /** @see #BetterRunnable(JavaPlugin, PauseType, BetterRunnableGroup, Consumer, long, long) */
    public BetterRunnable(JavaPlugin plugin, PauseType pauseType, BetterRunnableGroup group, Consumer<BetterTask> task, long interval) {
        this(plugin, pauseType, group, task, 0, interval);
    }

    /** @see #BetterRunnable(JavaPlugin, PauseType, BetterRunnableGroup, Consumer, long, long) */
    public BetterRunnable(JavaPlugin plugin, BetterRunnableGroup group, Consumer<BetterTask> task, long interval) {
        this(plugin, PauseType.AUTOMATIC, group, task, 0, interval);
    }

    /** @see #BetterRunnable(JavaPlugin, PauseType, BetterRunnableGroup, Consumer, long, long) */
    public BetterRunnable(JavaPlugin plugin, PauseType pauseType, Consumer<BetterTask> task, long delay, long interval) {
        this(plugin, pauseType, null, task, delay, interval);
    }

    /** @see #BetterRunnable(JavaPlugin, PauseType, BetterRunnableGroup, Consumer, long, long) */
    public BetterRunnable(JavaPlugin plugin, Consumer<BetterTask> task, long delay, long interval) {
        this(plugin, PauseType.AUTOMATIC, null, task, delay, interval);
    }

    /** @see #BetterRunnable(JavaPlugin, PauseType, BetterRunnableGroup, Consumer, long, long) */
    public BetterRunnable(JavaPlugin plugin, PauseType pauseType, Consumer<BetterTask> task, long interval) {
        this(plugin, pauseType, null, task, 0, interval);
    }

    /** @see #BetterRunnable(JavaPlugin, PauseType, BetterRunnableGroup, Consumer, long, long) */
    public BetterRunnable(JavaPlugin plugin, Consumer<BetterTask> task, long interval) {
        this(plugin, PauseType.AUTOMATIC, null, task, 0, interval);
    }

    @Override
    public boolean isAsync() {
        return false;
    }

    @Override
    public boolean isDelayed() {
        return false;
    }

    @Override
    public void startTask() {
        if(isStopped) {
            taskStartedTime = Bukkit.getCurrentTick();
            lastTaskExecutionTime = Bukkit.getCurrentTick();
        }

        if(runnableID != null) {
            Bukkit.getScheduler().cancelTask((int) runnableID);
            runnableID = null;
        }

        runnableID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this::execute, (isStopped ?  delay : newDelayAfterPauseTask), interval);

        isStopped = false;
    }

    /**
     * Stops the tasks and also resets 'executions' to 0.
     * @return true if a task has been stopped. Else returns false.
     */
    @Override
    public boolean stop() {
        isStopped = true;

        if(runnableID == null) return false;

        Bukkit.getScheduler().cancelTask((int) runnableID);
        runnableID = null;
        executions = 0;
        pauseTime = 0;
        pausedTime = 0;
        return true;
    }

    /**
     * @param removeFromGroups if true, the task will be removed from all groups
     * @see #stop()
     */
    @SuppressWarnings("unchecked")
    @Override
    public boolean stop(boolean removeFromGroups) {
        if(removeFromGroups)
            for(var group : (ArrayList<BetterRunnableGroup>) groups.clone())
                group.removeTask(this);

        return this.stop();
    }

    @Override
    public void execute() {
        task.accept(this);
        lastTaskExecutionTime = Bukkit.getCurrentTick();
        pausedTime = 0;

        if(Long.MAX_VALUE != executions + 1)
            executions++;
    }

    @Override
    public void pause() {
        if(isPaused) return;

        newDelayAfterPauseTask = lastTaskExecutionTime - (Bukkit.getCurrentTick() - pausedTime) + (isStopped ? delay : interval);
        pauseTime = Bukkit.getCurrentTick();

        if(pauseType == PauseType.AUTOMATIC && runnableID != null) {
            Bukkit.getScheduler().cancelTask((int) runnableID);
            runnableID = null;
        }

        this.isPaused = true;
    }

    @Override
    public void unpause() {
        if(!isPaused) return;

        pausedTime += Bukkit.getCurrentTick() - pauseTime;

        if(pauseType == PauseType.AUTOMATIC)
            startTask();

        this.isPaused = false;
    }

}