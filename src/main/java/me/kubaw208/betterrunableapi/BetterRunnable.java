package me.kubaw208.betterrunableapi;

import lombok.Getter;
import me.kubaw208.betterrunableapi.enums.PauseType;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Consumer;

/**
 * Classic synchronous task class that is repeating in given interval with possibly delay on start.
 * Can be paused and unpaused.
 */
public class BetterRunnable {

    @Getter private final JavaPlugin plugin;
    @Getter private final Consumer<BetterRunnable> task;
    @Getter private PauseType pauseType = PauseType.AUTOMATIC;
    @Getter protected boolean isPaused = false;
    @Getter private long delay = 0;
    @Getter private final long interval;
    public long executions = 0;
    protected long lastTaskExecutionTime;
    protected long newDelayAfterPauseTask;
    protected boolean isRunning;
    protected int taskStartedTime;
    private boolean hasRunnedOnce = false;
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
     * @param pauseType pause type
     * @param task code in task to run
     * @param interval time in ticks between runs
     */
    public BetterRunnable(JavaPlugin plugin, PauseType pauseType, Consumer<BetterRunnable> task, long interval) {
        this.plugin = plugin;
        this.pauseType = pauseType;
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
     * @param pauseType pause type
     * @param task code in task to run
     * @param delay time in ticks to wait before first run
     * @param interval time in ticks between runs
     */
    public BetterRunnable(JavaPlugin plugin, PauseType pauseType, Consumer<BetterRunnable> task, long delay, long interval) {
        this.plugin = plugin;
        this.pauseType = pauseType;
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
     * @param pauseType pause type
     * @param group tasks group that automatically adds task to that group
     * @param task code in task to run
     * @param interval time in ticks between runs
     */
    public BetterRunnable(JavaPlugin plugin, PauseType pauseType, BetterRunnableGroup group, Consumer<BetterRunnable> task, long interval) {
        this(plugin, pauseType, task, interval);

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
     * Creates new synchronous task
     * @param plugin plugin main class that runs task
     * @param pauseType pause type
     * @param group tasks group that automatically adds task to that group
     * @param task code in task to run
     * @param delay time in ticks to wait before first run
     * @param interval time in ticks between runs
     */
    public BetterRunnable(JavaPlugin plugin, PauseType pauseType, BetterRunnableGroup group, Consumer<BetterRunnable> task, long delay, long interval) {
        this(plugin, pauseType, task, delay, interval);

        if(group != null)
            group.addTask(this);
    }

    /**
     * Pause task
     */
    public void pause() {
        newDelayAfterPauseTask = lastTaskExecutionTime - Bukkit.getCurrentTick() + (hasRunnedOnce ? interval : delay);

        if(!isPaused && pauseType == PauseType.AUTOMATIC && runnableID != null) {
            Bukkit.getScheduler().cancelTask((int) runnableID);
            runnableID = null;
        }

        Bukkit.broadcastMessage("HEJA PAUZA");

        this.isPaused = true;
    }

    /**
     * Unpause task
     */
    public void unpause() {
        if(isPaused && pauseType == PauseType.AUTOMATIC)
            startTask();

        Bukkit.broadcastMessage("HEJA UNPAUZA");

        this.isPaused = false;
    }

    /**
     * Cancel task if it is running and starts it. Can be used to start task again
     */
    public void startTask() {
        if(!isRunning) {
            taskStartedTime = Bukkit.getCurrentTick();
            lastTaskExecutionTime = Bukkit.getCurrentTick();
        }

        if(runnableID != null) {
            Bukkit.getScheduler().cancelTask((int) runnableID);
            runnableID = null;
        }

        runnableID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this::run, (isRunning ? newDelayAfterPauseTask : delay), interval);

        isRunning = true;
    }

    /**
     * Code in method that executes when task runs
     */
    public void run() {
        task.accept(this);
        if(Long.MAX_VALUE != executions + 1) executions++;
        lastTaskExecutionTime = Bukkit.getCurrentTick();
        hasRunnedOnce = true;
    }

    /**
     * Stops task
     */
    public boolean cancel() {
        isRunning = false;

        if(runnableID != null) {
            Bukkit.getScheduler().cancelTask((int) runnableID);
            runnableID = null;
            return true;
        }
        return false;
    }

}