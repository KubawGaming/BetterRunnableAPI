package me.kubaw208.betterrunableapi;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Consumer;

public class BetterRunnable {

    @Getter private final JavaPlugin plugin;
    @Getter private Consumer<BetterRunnable> task;
    @Getter private boolean isPaused = false;
    @Getter private long delay = 0;
    @Getter private long interval = 20;
    public long executions = 0;
    @Getter protected Object runnableID = null;

    public BetterRunnable(JavaPlugin plugin, Consumer<BetterRunnable> task, long interval) {
        this.plugin = plugin;
        this.task = task;
        this.interval = interval > 0 ? interval : 1;

        startTask();
    }

    public BetterRunnable(JavaPlugin plugin, Consumer<BetterRunnable> task, long delay, long interval) {
        this.plugin = plugin;
        this.task = task;
        this.delay = delay;
        this.interval = interval > 0 ? interval : 1;

        startTask();
    }

    public BetterRunnable(JavaPlugin plugin, BetterRunnableGroup group, Consumer<BetterRunnable> task, long interval) {
        this(plugin, task, interval);

        if(group != null)
            group.addTask(this);
    }

    public BetterRunnable(JavaPlugin plugin, BetterRunnableGroup group, Consumer<BetterRunnable> task, long delay, long interval) {
        this(plugin, task, delay, interval);

        if(group != null)
            group.addTask(this);
    }

    public void pause() {
        this.isPaused = true;
    }

    public void unpause() {
        this.isPaused = false;
    }

    public void startTask() {
        cancel();
        runnableID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this::run, delay, interval);
    }

    public void run() {
        if(!isPaused) {
            task.accept(this);
            if(Long.MAX_VALUE > executions + 1) executions++;
        }
    }

    public void cancel() {
        if(runnableID != null) {
            Bukkit.getScheduler().cancelTask((int) runnableID);
            runnableID = null;
        }
    }

}