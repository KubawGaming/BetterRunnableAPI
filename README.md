# BetterRunnableAPI
Small library based on the Bukkit API that allows you to easily create a Task

## Example of use:

```java
BetterRunable exampleTask = new BetterRunable(plugin, task -> {
  System.out.println("Task repeated " + task.executions + " times");

  //Executions is a built-in variable that is added each time a Task executes
  if(task.executions > 50) task.cancel();
}, 20); //20 (ticks) = 1 second, current runnable will be executed every 1 second
```
You can also create a group of Tasks that you can operate at once:

```java
BetterRunnableGroup tasksGroup = new BetterRunnableGroup(exampleTask);
BetterRunable exampleTask2 = new BetterRunable(plugin, tasksGroup, task -> {}, 20); //You can add tasks to group in constructor

tasksGroup.pauseAll(); //Pause all tasks in group
tasksGroup.unpauseAll(); //Unpause all tasks in group
tasksGroup.cancelAll(); //Cancel all tasks in group
```

## Gradle repository:

```gradle
repositories {
  mavenCentral()
  maven { url 'https://jitpack.io' }
}

dependencies {
  implementation 'com.github.KubawGaming:BetterRunnableAPI:v1.0.0'
}
```
