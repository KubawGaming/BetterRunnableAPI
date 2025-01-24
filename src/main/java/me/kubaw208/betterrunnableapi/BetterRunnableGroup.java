package me.kubaw208.betterrunnableapi;

import lombok.Getter;
import me.kubaw208.betterrunnableapi.structs.BetterTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a group of tasks allowing the operation of multiple tasks in the same moment.
 */
@Getter
public final class BetterRunnableGroup {

    private final ArrayList<BetterTask> tasks = new ArrayList<>();

    /**
     * Creates a new tasks group.
     * @param tasks list of tasks to add to the group.
     */
    public BetterRunnableGroup(List<BetterTask> tasks) {
        addTasks(tasks);
    }

    /**
     * Creates a new tasks group.
     * @param tasks array of tasks to add to the group.
     */
    public BetterRunnableGroup(BetterTask... tasks) {
        addTasks(tasks);
    }

    /**
     * Adds a new task to group.
     */
    public void addTask(BetterTask task) {
        if(!task.getGroups().contains(this))
            task.getGroups().add(this);

        tasks.add(task);
    }

    /**
     * Adds a list of tasks to group.
     */
    public void addTasks(BetterTask... tasks) {
        for(BetterTask task : tasks)
            addTask(task);
    }

    /**
     * Adds a list of tasks to group.
     */
    public void addTasks(List<BetterTask> tasks) {
        for(BetterTask task : tasks)
            addTask(task);
    }

    /**
     * Removes a task from a group.
     */
    public void removeTask(BetterTask task) {
        task.getGroups().remove(this);
        tasks.remove(task);
    }

    /**
     * Removes a list of tasks to group.
     */
    public void removeTasks(BetterTask... tasks) {
        for(BetterTask task : tasks)
            removeTask(task);
    }

    /**
     * Removes a list of tasks to group.
     */
    public void removeTasks(List<BetterTask> tasks) {
        for(BetterTask task : tasks)
            removeTask(task);
    }

    /**
     * If a task is not null and is in group, stops task and removes it from a group.
     * @returns true if a task was removed from a group. Else returns false.
     */
    public boolean stopAndRemove(BetterTask task) {
        if(task == null || !tasks.contains(task)) return false;

        tasks.remove(task);
        return task.stop();
    }

    /**
     * Pauses all tasks in the group.
     */
    public void pauseAll() {
        for(BetterTask task : tasks)
            task.pause();
    }

    /**
     * Unpauses all tasks in the group.
     */
    public void unpauseAll() {
        for(BetterTask task : tasks)
            task.unpause();
    }

    /**
     * Starts all tasks in the group.
     */
    public void startAll() {
        for(BetterTask task : tasks)
            task.startTask();
    }

    /**
     * Stops all tasks in the group.
     */
    public void stopAll() {
        for(BetterTask task : tasks)
            task.stop();
    }

    /**
     * Stops all tasks in the group.
     * @param removeFromGroups if true, the tasks will be removed from all groups.
     */
    public void stopAll(boolean removeFromGroups) {
        for(BetterTask task : tasks)
            task.stop(removeFromGroups);
    }

    /**
     * Stops all tasks and removes it from a group.
     */
    public void stopAndRemoveAll() {
        for(BetterTask task : tasks) {
            task.getGroups().remove(this);
            task.stop();
        }

        this.tasks.clear();
    }

}