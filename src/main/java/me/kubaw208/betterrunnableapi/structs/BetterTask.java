package me.kubaw208.betterrunnableapi.structs;

import me.kubaw208.betterrunnableapi.BetterRunnableGroup;

import java.util.List;

public interface BetterTask {

    List<BetterRunnableGroup> getGroups();

    void startTask();

    boolean stop();

    boolean stop(boolean removeFromGroups);

    /**
     * Executes code in task.
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