package me.kubaw208.betterrunnableapi.structs;

import me.kubaw208.betterrunnableapi.BetterRunnableGroup;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.function.Consumer;

public interface BetterTask {

    /**
     * List of groups in which the task is added.
     */
    ArrayList<BetterRunnableGroup> getGroups();

    /**
     * Task to be executed.
     */
    Consumer<BetterTask> getTask();

    /**
     * Plugin that created the task.
     */
    JavaPlugin getPlugin();

    /**
     * Pause type of the task. Delayed tasks are always 'AUTOMATIC'.
     */
    PauseType getPauseType();

    /**
     * For synchronous tasks, returns the ID of the task.
     * For asynchronous tasks, returns 'ScheduledTask' object.
     * Can be null if a task is stopped.
     */
    Object getRunnableID();

    /**
     * Returns true if the task is paused. Else returns false.
     */
    boolean isPaused();

    /**
     * Delay of the task.
     */
    long getDelay();

    /**
     * Interval of the task. Delayed tasks are always '-1'.
     */
    long getInterval();

    /**
     * Number of times the task has been executed. Stopping the task will reset this value.
     */
    long getExecutions();

    /**
     * Returns true if the task is stopped. Else returns false.
     */
    boolean isStopped();

    /**
     * Returns true if the task is asynchronous. Else returns false.
     */
    boolean isAsync();

    /**
     * Returns true if the task is delayed. Else returns false.
     */
    boolean isDelayed();

    /**
     * Starts the task.
     */
    void startTask();

    /**
     * Stops the task.
     */
    boolean stop();

    /**
     * Stops the task and removes it from a group.
     * @param removeFromGroups if true, the task will be removed from all groups.
     */
    boolean stop(boolean removeFromGroups);

    /**
     * Executes code in a task.
     */
    void execute();

    /**
     * Pauses the task.
     * @see PauseType
     */
    void pause();

    /**
     * Unpauses the task.
     * @see PauseType
     */
    void unpause();

}