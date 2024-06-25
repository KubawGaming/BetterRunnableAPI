package me.kubaw208.betterrunableapi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class BetterRunnableGroup {

    ArrayList<BetterRunable> tasks = new ArrayList<>();

    public BetterRunnableGroup(List<BetterRunable> tasks) {
        this.tasks = (ArrayList<BetterRunable>) tasks;
    }

    public BetterRunnableGroup(BetterRunable... tasks) {
        this.tasks.addAll(Arrays.asList(tasks));
    }

    public void addTask(BetterRunable task) {
        tasks.add(task);
    }

    public void pauseAll() {
        for(BetterRunable task : tasks)
            task.pause();
    }

    public void unpauseAll() {
        for(BetterRunable task : tasks)
            task.unpause();
    }

    public void cancelAll() {
        for(BetterRunable task : tasks)
            task.cancel();
    }

}