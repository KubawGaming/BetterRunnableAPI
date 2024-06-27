package me.kubaw208.betterrunableapi;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Consumer;

public class BetterLaterRunnable extends BetterRunnable {

    public BetterLaterRunnable(JavaPlugin plugin, Consumer<BetterRunnable> task, long interval) {
        super(plugin, task, interval);
    }

    public BetterLaterRunnable(JavaPlugin plugin, Consumer<BetterRunnable> task, long delay, long interval) {
        super(plugin, task, delay, interval);
    }

    public BetterLaterRunnable(JavaPlugin plugin, BetterRunnableGroup group, Consumer<BetterRunnable> task, long delay, long interval) {
        super(plugin, group, task, delay, interval);
    }

    public BetterLaterRunnable(JavaPlugin plugin, BetterRunnableGroup group, Consumer<BetterRunnable> task, long interval) {
        super(plugin, group, task, interval);
    }

    @Override
    public void startTask() {
        cancel();
        runnableID = Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), this::run, getDelay());
    }

}