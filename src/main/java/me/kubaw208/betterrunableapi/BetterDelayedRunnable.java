package me.kubaw208.betterrunableapi;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Consumer;

public class BetterDelayedRunnable extends BetterRunnable {

    private int passedTimeInTicks = 0;
    private int lostMillisecondsWhilePausing = 0; //Saves milliseconds that is lost caused ticks accuracy so that in case of multiple pauses no more than 1 tick is lost
    private long taskStartedTime = 0;

    public BetterDelayedRunnable(JavaPlugin plugin, Consumer<BetterRunnable> task, long delay) {
        super(plugin, task, delay, 0);
    }

    public BetterDelayedRunnable(JavaPlugin plugin, BetterRunnableGroup group, Consumer<BetterRunnable> task, long delay) {
        super(plugin, group, task, delay, 0);
    }

    @Override
    public void startTask() {
        cancel();
        runnableID = Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), this::run, getDelay() - passedTimeInTicks);
        taskStartedTime = System.currentTimeMillis();
    }

    @Override
    public void pause() {
        super.pause();
        cancel();

        lostMillisecondsWhilePausing += (System.currentTimeMillis() - taskStartedTime) % 50;
        passedTimeInTicks += (int) (System.currentTimeMillis() - taskStartedTime) / 50;

        if(lostMillisecondsWhilePausing >= 50) {
            passedTimeInTicks++;
            lostMillisecondsWhilePausing -= 50;
        }
    }

    @Override
    public void unpause() {
        super.unpause();
        startTask();
    }

}