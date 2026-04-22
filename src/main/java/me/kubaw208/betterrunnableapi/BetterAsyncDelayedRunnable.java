package me.kubaw208.betterrunnableapi;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.function.Consumer;

/**
 * Classic asynchronous delayed task class that is executed after given delay.
 * Can be paused and unpaused.
 */
@Getter
public class BetterAsyncDelayedRunnable extends BetterDelayedRunnable {

    private BukkitTask runnableID;

    /**
     * Creates a new asynchronous delayed task executed only once after given delay.
     * @param plugin plugin main class that runs task.
     * @param group tasks group that automatically adds a task to that group if a group is not null.
     * @param task code in task to execute.
     * @param delay time in ticks to wait before the first run (default: 0).
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
        runnableID = Bukkit.getScheduler().runTaskLaterAsynchronously(
                getPlugin(),
                this::execute,
                (getDelay() - passedTime)
        );
        taskStartedTime = System.currentTimeMillis();
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

        runnableID.cancel();
        runnableID = null;
        return true;
    }

    @Override
    void pauseInternal(boolean wasHardPause, boolean wasSoftPause, boolean willHardPause, boolean willSoftPause) {
        boolean wasTaskPreviousPaused = wasHardPause || wasSoftPause;
        boolean willTaskBePaused = willHardPause || willSoftPause;

        isHardPause = willHardPause;
        isSoftPause = willSoftPause;

        if(wasTaskPreviousPaused || !willTaskBePaused) return;

        if(runnableID != null) {
            runnableID.cancel();
            runnableID = null;
        }

        passedTime += (System.currentTimeMillis() - taskStartedTime);
    }

}