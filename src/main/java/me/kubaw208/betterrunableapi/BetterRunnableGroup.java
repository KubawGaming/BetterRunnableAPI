package me.kubaw208.betterrunableapi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class representing a group of tasks allowing the operation of multiple tasks in the same moment
 */
public final class BetterRunnableGroup {

    public ArrayList<BetterRunnable> tasks = new ArrayList<>();

    /**
     * Creates new tasks group
     * @param tasks list of tasks to add to the group
     */
    public BetterRunnableGroup(List<BetterRunnable> tasks) {
        this.tasks = (ArrayList<BetterRunnable>) tasks;
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
     * Stops all tasks in the group
     */
    public void cancelAll() {
        for(BetterRunnable task : tasks)
            task.cancel();
    }

    /**
     * Stops all tasks and removes it from group
     */
    public void cancelAllAndRemove() {
        for(BetterRunnable task : tasks) {
            task.cancel();
        }
        this.tasks.clear();
    }

}