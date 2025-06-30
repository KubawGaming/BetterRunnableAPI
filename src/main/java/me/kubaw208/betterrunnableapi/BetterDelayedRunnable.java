package me.kubaw208.betterrunnableapi;

import lombok.AccessLevel;
import lombok.Getter;
import me.kubaw208.betterrunnableapi.structs.PauseType;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.function.Consumer;

/**
 * Classic synchronous delayed task class that is executed once when started after given delay.
 * Can be paused and unpaused.
 */
@Getter
public class BetterDelayedRunnable extends BetterTask {

    protected final JavaPlugin plugin;
    protected final HashSet<BetterRunnableGroup> groups = new HashSet<>();
    protected final PauseType pauseType;
    protected final Consumer<BetterTask> task;
    protected Object runnableID = null;
    protected long delay;
    private final long interval = -1;
    private final long executions = -1;
    @Getter(AccessLevel.PRIVATE) protected long taskStartedTime;
    protected boolean isStopped = false;
    protected long passedTime;

    /**
     * Creates a new synchronous delayed task executed only once after given delay.
     * @param plugin plugin main class that runs task.
     * @param group tasks group that automatically adds a task to that group if a group is not null.
     * @param task code in task to execute.
     * @param delay time in ticks to wait before the first run (default: 0).
     */
    public BetterDelayedRunnable(JavaPlugin plugin, BetterRunnableGroup group, Consumer<BetterTask> task, long delay) {
        this.plugin = plugin;
        this.pauseType = PauseType.AUTOMATIC;
        this.task = task;
        this.delay = delay;

        start();

        if(group != null)
            group.addTask(this);
    }

    /** @see BetterDelayedRunnable#BetterDelayedRunnable(JavaPlugin, BetterRunnableGroup, Consumer, long) */
    public BetterDelayedRunnable(JavaPlugin plugin, Consumer<BetterTask> task, long delay) {
        this(plugin, null, task, delay);
    }

    @Override
    public boolean isAsync() {
        return false;
    }

    @Override
    public boolean isDelayed() {
        return true;
    }

    @Override
    public void start() {
        runnableID = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, this::execute, delay - passedTime);
        taskStartedTime = Bukkit.getCurrentTick();
        isStopped = false;
    }

    @Override
    public boolean stop() {
        return stop(true);
    }

    @Override
    public boolean stop(boolean removeFromGroups) {
        if(removeFromGroups)
            while(!groups.isEmpty())
                groups.iterator().next().removeTask(this);

        isStopped = true;

        if(runnableID == null) return false;

        Bukkit.getScheduler().cancelTask((int) runnableID);
        runnableID = null;
        return true;
    }

    @Override
    public void execute() {
        task.accept(this);
        passedTime = 0;
        taskStartedTime = 0;
        stop(true);
    }

    @Override
    void pauseInternal(boolean wasHardPause, boolean wasSoftPause, boolean willHardPause, boolean willSoftPause) {
        boolean wasTaskPreviousPaused = wasHardPause || wasSoftPause;
        boolean willTaskBePaused = willHardPause || willSoftPause;

        isHardPause = willHardPause;
        isSoftPause = willSoftPause;

        if(wasTaskPreviousPaused || !willTaskBePaused) return;

        if(runnableID != null) {
            Bukkit.getScheduler().cancelTask((int) runnableID);
            runnableID = null;
        }

        passedTime += (Bukkit.getCurrentTick() - taskStartedTime);
    }

    @Override
    void unpauseInternal(boolean wasHardPause, boolean wasSoftPause, boolean willHardPause, boolean willSoftPause) {
        boolean wasTaskPreviousPaused = wasHardPause || wasSoftPause;
        boolean willTaskBePaused = willHardPause || willSoftPause;

        isHardPause = willHardPause;
        isSoftPause = willSoftPause;

        if(!wasTaskPreviousPaused || willTaskBePaused) return;

        start();
    }

}