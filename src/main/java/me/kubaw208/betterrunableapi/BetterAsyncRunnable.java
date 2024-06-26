package me.kubaw208.betterrunableapi;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Consumer;

public class BetterAsyncRunnable extends BetterRunnable {

    public BetterAsyncRunnable(JavaPlugin plugin, Consumer<BetterRunnable> task, long interval) {
        super(plugin, task, interval);
    }

    public BetterAsyncRunnable(JavaPlugin plugin, Consumer<BetterRunnable> task, long delay, long interval) {
        super(plugin, task, delay, interval);
    }

    public BetterAsyncRunnable(JavaPlugin plugin, BetterRunnableGroup group, Consumer<BetterRunnable> task, long delay, long interval) {
        super(plugin, group, task, delay, interval);
    }

    public BetterAsyncRunnable(JavaPlugin plugin, BetterRunnableGroup group, Consumer<BetterRunnable> task, long interval) {
        super(plugin, group, task, interval);
    }

    @Override
    public void run() {
        if(!isPaused()) {
            getTask().accept(this);
            if(Long.MAX_VALUE > executions + 1) executions++;
        }
    }

}
