package me.kubaw208.betterrunnableapi;

import lombok.AccessLevel;
import lombok.Getter;
import me.kubaw208.betterrunnableapi.structs.PauseType;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.function.Consumer;

/**
 * Classic synchronous task class that is repeating in given an interval with possible delay on start.
 * Can be paused and unpaused.
 */
@Getter
public class BetterRunnable extends BetterTask {

    protected final JavaPlugin plugin;
    protected final HashSet<BetterRunnableGroup> groups = new HashSet<>();
    protected PauseType pauseType;
    private Consumer<BetterTask> task;
    protected Object runnableID = null;
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

        start();

        if(group != null)
            group.addTask(this);
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
    public void start() {
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
        return stop(true);
    }

    /**
     * @param removeFromGroups if true, the task will be removed from all groups
     * @see #stop()
     */
    @Override
    public boolean stop(boolean removeFromGroups) {
        if(removeFromGroups)
            while(!groups.isEmpty())
                groups.iterator().next().removeTask(this);

        isStopped = true;

        executions = 0;
        pauseTime = 0;
        pausedTime = 0;

        if(runnableID == null) return false;

        Bukkit.getScheduler().cancelTask((int) runnableID);
        runnableID = null;
        return true;
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
    void pauseInternal(boolean wasHardPause, boolean wasSoftPause, boolean willHardPause, boolean willSoftPause) {
        boolean wasTaskPreviousPaused = wasHardPause || wasSoftPause;
        boolean willTaskBePaused = willHardPause || willSoftPause;

        isHardPause = willHardPause;
        isSoftPause = willSoftPause;

        if(wasTaskPreviousPaused || !willTaskBePaused) return;

        newDelayAfterPauseTask = lastTaskExecutionTime - (Bukkit.getCurrentTick() - pausedTime) + (isStopped ? delay : interval);
        pauseTime = Bukkit.getCurrentTick();

        if(pauseType == PauseType.AUTOMATIC && runnableID != null) {
            Bukkit.getScheduler().cancelTask((int) runnableID);
            runnableID = null;
        }
    }

    @Override
    void unpauseInternal(boolean wasHardPause, boolean wasSoftPause, boolean willHardPause, boolean willSoftPause) {
        boolean wasTaskPreviousPaused = wasHardPause || wasSoftPause;
        boolean willTaskBePaused = willHardPause || willSoftPause;

        isHardPause = willHardPause;
        isSoftPause = willSoftPause;

        if(!wasTaskPreviousPaused || willTaskBePaused) return;

        pausedTime += Bukkit.getCurrentTick() - pauseTime;

        if(pauseType == PauseType.AUTOMATIC)
            start();
    }

}