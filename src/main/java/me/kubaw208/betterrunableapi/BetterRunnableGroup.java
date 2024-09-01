package me.kubaw208.betterrunableapi;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class representing a group of tasks allowing the operation of multiple tasks in the same moment
 */
@Getter
public final class BetterRunnableGroup {

    private final ArrayList<BetterRunnable> tasks = new ArrayList<>();

    /**
     * Creates new tasks group
     * @param tasks list of tasks to add to the group
     */
    public BetterRunnableGroup(List<BetterRunnable> tasks) {
        this.tasks.addAll(tasks);
    }

    /**
     * Creates new tasks group
     * @param tasks array of tasks to add to the group
     */
    public BetterRunnableGroup(BetterRunnable... tasks) {
        this.tasks.addAll(Arrays.asList(tasks));
    }

    /**
     * Adds new task to group
     */
    public void addTask(BetterRunnable task) {
        tasks.add(task);
    }

    /**
     * If task is not null and is in group, stops task and removes it from group
     * @returns true if task was removed. Else returns false
     */
    public boolean stopAndRemove(BetterRunnable task) {
        if(task == null || !tasks.contains(task)) return false;

        tasks.remove(task);
        return task.cancel();
    }

    /**
     * Removes task from group
     */
    public void removeTask(BetterRunnable task) {
        tasks.remove(task);
    }

    /**
     * Pauses all tasks in the group
     */
    public void pauseAll() {
        for(BetterRunnable task : tasks)
            task.pause();
    }

    /**
     * Unpauses all tasks in the group
     */
    public void unpauseAll() {
        for(BetterRunnable task : tasks)
            task.unpause();
    }

    /**
     * Starts all tasks in the group
     */
    public void startAll() {
        for(BetterRunnable task : tasks)
            task.startTask();
    }

    /**
     * Stops all tasks in the group
     */
    public void cancelAll() {
        for(BetterRunnable task : tasks)
            task.cancel();
    }

    /**
     * Stops all tasks and removes it from group
     */
    public void cancelAndRemoveAll() {
        for(BetterRunnable task : tasks) {
            task.cancel();
        }
        this.tasks.clear();
    }

}