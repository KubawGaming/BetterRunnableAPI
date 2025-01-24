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
 * Classic synchronous delayed task class that is executed once when started after given delay.
 * Can be paused and unpaused.
 */
@Getter
public class BetterDelayedRunnable implements BetterTask {

    protected final JavaPlugin plugin;
    protected final ArrayList<BetterRunnableGroup> groups = new ArrayList<>();
    protected final PauseType pauseType;
    protected final Consumer<BetterTask> task;
    protected Object runnableID = null;
    protected boolean isPaused = false;
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

        if(group != null)
            group.addTask(this);

        start();
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
        while(!groups.isEmpty()) {
            groups.get(0).removeTask(this);
        }

        isStopped = true;

        if(runnableID == null) return false;

        Bukkit.getScheduler().cancelTask((int) runnableID);
        runnableID = null;
        return true;
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
    public void pause() {
        if(isPaused) return;

        if(runnableID != null) {
            Bukkit.getScheduler().cancelTask((int) runnableID);
            runnableID = null;
        }

        isPaused = true;
        passedTime += (Bukkit.getCurrentTick() - taskStartedTime);
    }

    @Override
    public void unpause() {
        if(!isPaused) return;

        isPaused = false;
        start();
    }

}