package me.kubaw208.betterrunableapi;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Consumer;

public class BetterRunable {

    @Getter private final JavaPlugin plugin;
    @Getter private Consumer<BetterRunable> task;
    @Getter private boolean isPaused = false;
    @Getter private long delay = 0;
    @Getter private long interval = 20;
    public long executions = 0;
    @Getter protected Object runnableID = null;

    public BetterRunable(JavaPlugin plugin, Consumer<BetterRunable> task, long interval) {
        this.plugin = plugin;
        this.task = task;
        this.interval = interval > 0 ? interval : 1;

        startTask();
    }

    public BetterRunable(JavaPlugin plugin, Consumer<BetterRunable> task, long delay, long interval) {
        this.plugin = plugin;
        this.task = task;
        this.delay = delay;
        this.interval = interval > 0 ? interval : 1;

        startTask();
    }

    public BetterRunable(JavaPlugin plugin, BetterRunnableGroup group, Consumer<BetterRunable> task, long interval) {
        this(plugin, task, interval);
        group.addTask(this);
    }

    public BetterRunable(JavaPlugin plugin, BetterRunnableGroup group, Consumer<BetterRunable> task, long delay, long interval) {
        this(plugin, task, delay, interval);
        group.addTask(this);
    }

    public void pause() {
        this.isPaused = true;
    }

    public void unpause() {
        this.isPaused = false;
    }

    protected void startTask() {
        runnableID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            if(!isPaused) {
                task.accept(this);
                if(Long.MAX_VALUE > executions + 1) executions++;
            }
        }, delay, interval);
    }

    public void cancel() {
        if(runnableID != null) Bukkit.getScheduler().cancelTask((int) runnableID);
    }

}