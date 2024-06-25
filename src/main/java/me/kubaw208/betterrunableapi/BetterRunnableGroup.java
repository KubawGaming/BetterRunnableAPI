package me.kubaw208.betterrunableapi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class BetterRunnableGroup {

    ArrayList<BetterRunnable> tasks = new ArrayList<>();

    public BetterRunnableGroup(List<BetterRunnable> tasks) {
        this.tasks = (ArrayList<BetterRunnable>) tasks;
    }

    public BetterRunnableGroup(BetterRunnable... tasks) {
        this.tasks.addAll(Arrays.asList(tasks));
    }

    public void addTask(BetterRunnable task) {
        tasks.add(task);
    }

    public void pauseAll() {
        for(BetterRunnable task : tasks)
            task.pause();
    }

    public void unpauseAll() {
        for(BetterRunnable task : tasks)
            task.unpause();
    }

    public void cancelAll() {
        for(BetterRunnable task : tasks)
            task.cancel();
    }

}