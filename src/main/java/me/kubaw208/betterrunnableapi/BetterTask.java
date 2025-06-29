package me.kubaw208.betterrunnableapi;

import lombok.Getter;
import me.kubaw208.betterrunnableapi.structs.PauseType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.function.Consumer;

@Getter
public abstract class BetterTask {

    boolean isHardPause = false;
    boolean isSoftPause = false;

    /**
     * List of groups in which the task is added.
     */
    public abstract HashSet<BetterRunnableGroup> getGroups();

    /**
     * Task to be executed.
     */
    public abstract Consumer<BetterTask> getTask();

    /**
     * Plugin that created the task.
     */
    public abstract JavaPlugin getPlugin();

    /**
     * Pause type of the task. Delayed tasks are always 'AUTOMATIC'.
     */
    public abstract PauseType getPauseType();

    /**
     * For synchronous tasks, returns the ID of the task.
     * For asynchronous tasks, returns 'ScheduledTask' object.
     * Can be null if a task is stopped.
     */
    public abstract Object getRunnableID();

    /**
     * Returns true if the task is soft paused or hard paused. Else returns false.
     */
    public boolean isAnyPaused() {
        return isHardPause || isSoftPause;
    }

    /**
     * Delay of the task.
     */
    public abstract long getDelay();

    /**
     * Interval of the task. Delayed tasks are always '-1'.
     */
    public abstract long getInterval();

    /**
     * Number of times the task has been executed. Stopping the task will reset this value.
     */
    public abstract long getExecutions();

    /**
     * Returns true if the task is stopped. Else returns false.
     */
    public abstract boolean isStopped();

    /**
     * Returns true if the task is asynchronous. Else returns false.
     */
    public abstract boolean isAsync();

    /**
     * Returns true if the task is delayed. Else returns false.
     */
    public abstract boolean isDelayed();

    /**
     * Starts the task.
     */
    public abstract void start();

    /**
     * Stops the task.
     */
    public abstract boolean stop();

    /**
     * Stops the task and removes it from a group.
     * @param removeFromGroups if true, the task will be removed from all groups.
     */
    public abstract boolean stop(boolean removeFromGroups);

    /**
     * Executes code in a task.
     */
    public abstract void execute();

    /**
     * Pauses the task.
     * @see PauseType
     */
    public void pause() {
        pauseInternal(isHardPause, isSoftPause, true, isSoftPause);
    }

    /**
     * Unpauses the task.
     * @see PauseType
     */
    public void unpause() {
        unpauseInternal(isHardPause, isSoftPause, false, isSoftPause);
    }

    abstract void pauseInternal(boolean wasHardPause, boolean wasSoftPause, boolean willHardPause, boolean willSoftPause);

    abstract void unpauseInternal(boolean wasHardPause, boolean wasSoftPause, boolean willHardPause, boolean willSoftPause);

    /**
     * Updates isHardPause and isSoftPause.
     */
    void updatePauseState() {
        for(var group : getGroups()) {
            if(group.isHardPause() || group.isSoftPause()) {
                pauseInternal(isHardPause, isSoftPause, isHardPause, true);
                return;
            }
        }

        unpauseInternal(isHardPause, isSoftPause, isHardPause, false);
    }

}