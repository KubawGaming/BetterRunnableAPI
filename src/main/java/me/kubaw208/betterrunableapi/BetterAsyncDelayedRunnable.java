package me.kubaw208.betterrunableapi;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class BetterAsyncDelayedRunnable extends BetterRunnable {

    private int passedTimeInMilliseconds = 0;
    private long taskStartedTime = 0;

    public BetterAsyncDelayedRunnable(JavaPlugin plugin, Consumer<BetterRunnable> task, long delay) {
        super(plugin, task, delay, 0);
    }

    public BetterAsyncDelayedRunnable(JavaPlugin plugin, BetterRunnableGroup group, Consumer<BetterRunnable> task, long delay) {
        super(plugin, group, task, delay, 0);
    }

    @Override
    public void startTask() {
        cancel();
        runnableID = Bukkit.getAsyncScheduler().runDelayed(getPlugin(), scheduledTask -> run(), getDelay() - passedTimeInMilliseconds, TimeUnit.MILLISECONDS);
        taskStartedTime = System.currentTimeMillis();
    }

    @Override
    public void pause() {
        super.pause();
        cancel();
        passedTimeInMilliseconds += System.currentTimeMillis() - taskStartedTime;
    }

    @Override
    public void unpause() {
        super.unpause();
        startTask();
    }

}