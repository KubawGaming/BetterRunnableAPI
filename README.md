# BetterRunnableAPI
Small library based on the Bukkit API that allows you to easily create a Task.
The code was tested on minecraft version 1.20.4. I did not check compatibility with other versions!

## Example of use:

```java
BetterRunnable exampleTask = new BetterRunnable(plugin, task -> {
  System.out.println("Task repeated " + task.executions + " times");

  //Executions is a built-in variable that is added each time a Task executes
  if(task.executions > 50) task.cancel();
}, 20); //20 (ticks) = 1 second, current runnable will be executed every 1 second
```

The first example creates a synchronous task. This means that you can, for example, change the Minecraft world in it
You can also create a group of Tasks that you can operate at once:

```java
BetterRunnableGroup tasksGroup = new BetterRunnableGroup(exampleTask);
BetterRunnable exampleTask2 = new BetterRunnable(plugin, tasksGroup, task -> {}, 20); //You can add tasks to group in constructor

tasksGroup.pauseAll(); //Pause all tasks in group
tasksGroup.unpauseAll(); //Unpause all tasks in group
tasksGroup.cancelAll(); //Cancel all tasks in group
```

You can create async task as well. Note that the time in BetterAsyncRunnable runs in milliseconds, not ticks!

```java
BetterRunnableGroup tasksAsyncGroup = new BetterRunnableGroup();
BetterAsyncRunnable exampleTask3 = new BetterAsyncRunnable(plugin, tasksAsyncGroup, task -> {
  System.out.println("Async task repeated " + task.executions + " times");
}, 2000); //Repeating every 2 seconds

//Another way to add task to a group
tasksAsyncGroup.addTask(exampleTask3);

tasksAsyncGroup.pauseAll(); //Pause all tasks in group
tasksAsyncGroup.unpauseAll(); //Unpause all tasks in group
tasksAsyncGroup.cancelAll(); //Cancel all tasks in group
```

## Gradle repository:

```gradle
repositories {
  mavenCentral()
  maven { url 'https://jitpack.io' }
}

dependencies {
  implementation 'com.github.KubawGaming:BetterRunnableAPI:v1.0.2'
}
```
