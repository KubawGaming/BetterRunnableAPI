package me.kubaw208.betterrunnableapi;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import me.kubaw208.betterrunnableapi.structs.BetterTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Class representing a group of tasks allowing the operation of multiple tasks in the same moment.
 */
@Getter
public final class BetterRunnableGroup {

    private final ArrayList<BetterTask> tasks = new ArrayList<>();
    private final HashSet<BetterRunnableGroup> parents = new HashSet<>();
    private final HashSet<BetterRunnableGroup> children = new HashSet<>();
    @Getter(AccessLevel.PRIVATE) private boolean isGroupPaused = false;
    @Setter private boolean savePauseState = true;

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
    public BetterRunnableGroup addTask(BetterTask task) {
        if(!task.getGroups().contains(this))
            task.getGroups().add(this);

        if(!tasks.contains(task))
            tasks.add(task);

        if(savePauseState && isGroupPaused)
            task.pause();
        else if(savePauseState)
            task.unpause();
        return this;
    }

    /**
     * Adds a list of tasks to group.
     */
    public BetterRunnableGroup addTasks(BetterTask... tasks) {
        for(BetterTask task : tasks)
            if(!this.tasks.contains(task))
                addTask(task);
        return this;
    }

    /**
     * Adds a list of tasks to group.
     */
    public BetterRunnableGroup addTasks(List<BetterTask> tasks) {
        for(BetterTask task : tasks)
            if(!this.tasks.contains(task))
                addTask(task);
        return this;
    }

    /**
     * Removes a task from a group.
     */
    public BetterRunnableGroup removeTask(BetterTask task) {
        task.getGroups().remove(this);
        tasks.remove(task);
        return this;
    }

    /**
     * Removes a list of tasks to group.
     */
    public BetterRunnableGroup removeTasks(BetterTask... tasks) {
        for(BetterTask task : tasks)
            removeTask(task);
        return this;
    }

    /**
     * Removes a list of tasks to group.
     */
    public BetterRunnableGroup removeTasks(List<BetterTask> tasks) {
        for(BetterTask task : tasks)
            removeTask(task);
        return this;
    }

    /**
     * Adds a child group to this group.
     */
    public BetterRunnableGroup addChildGroup(BetterRunnableGroup group) {
        group.parents.add(this);
        children.add(group);
        return this;
    }

    /**
     * Adds a children groups to this group.
     */
    public BetterRunnableGroup addChildGroup(BetterRunnableGroup... groups) {
        for(BetterRunnableGroup group : groups)
            addChildGroup(group);
        return this;
    }

    /**
     * Adds a children groups to this group.
     */
    public BetterRunnableGroup addChildGroup(List<BetterRunnableGroup> groups) {
        for(BetterRunnableGroup group : groups)
            addChildGroup(group);
        return this;
    }

    /**
     * Removes a child group from this group.
     */
    public BetterRunnableGroup removeChildGroup(BetterRunnableGroup group) {
        group.parents.remove(this);
        children.remove(group);
        return this;
    }

    /**
     * Removes a children groups from this group.
     */
    public BetterRunnableGroup removeChildGroup(BetterRunnableGroup... groups) {
        for(BetterRunnableGroup group : groups)
            removeChildGroup(group);
        return this;
    }

    /**
     * Removes a children groups from this group.
     */
    public BetterRunnableGroup removeChildGroup(List<BetterRunnableGroup> groups) {
        for(BetterRunnableGroup group : groups)
            removeChildGroup(group);
        return this;
    }

    /**
     * If a task is not null and is in group, stops task and removes it from a group.
     * @returns true if a task was removed from a group. Else returns false.
     */
    public BetterRunnableGroup stopAndRemove(BetterTask task) {
        if(task == null || !tasks.contains(task)) return this;

        task.stop();
        return this;
    }

    /**
     * Pauses all tasks in the group.
     */
    public BetterRunnableGroup pauseAll() {
        for(BetterTask task : tasks)
            task.pause();

        for(BetterRunnableGroup group : children)
            group.pauseAll();

        isGroupPaused = true;
        return this;
    }

    /**
     * Unpauses all tasks in the group.
     */
    public BetterRunnableGroup unpauseAll() {
        for(BetterTask task : tasks)
            task.unpause();

        for(BetterRunnableGroup group : children)
            group.unpauseAll();

        isGroupPaused = false;
        return this;
    }

    /**
     * Starts all tasks in the group.
     */
    public BetterRunnableGroup startAll() {
        for(BetterTask task : tasks)
            task.start();

        for(BetterRunnableGroup group : children)
            group.startAll();
        return this;
    }

    /**
     * Stops all tasks in the group and removes it from all groups.
     */
    public BetterRunnableGroup stopAll() {
        while(!tasks.isEmpty())
            tasks.get(0).stop();

        for(BetterRunnableGroup group : children)
            group.stopAll();
        return this;
    }

    /**
     * Stops all tasks in the group.
     * @param removeFromGroups if true, the tasks will be removed from all groups.
     */
    public BetterRunnableGroup stopAll(boolean removeFromGroups) {
        while(!tasks.isEmpty())
            tasks.get(0).stop(removeFromGroups);

        for(BetterRunnableGroup group : children)
            group.stopAll(removeFromGroups);
        return this;
    }

}