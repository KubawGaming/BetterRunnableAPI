package me.kubaw208.betterrunableapi;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Consumer;

public class BetterLaterRunnable extends BetterRunnable {

    public BetterLaterRunnable(JavaPlugin plugin, Consumer<BetterRunnable> task, long delay) {
        super(plugin, task, delay, 0);
    }

    public BetterLaterRunnable(JavaPlugin plugin, BetterRunnableGroup group, Consumer<BetterRunnable> task, long delay) {
        super(plugin, group, task, delay, 0);
    }

    @Override
    public void startTask() {
        cancel();
        runnableID = Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), this::run, getDelay());
    }

}