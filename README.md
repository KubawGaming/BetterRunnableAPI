# BetterRunnableAPI
<bold>[![](https://jitpack.io/v/KubawGaming/BetterRunnableAPI.svg)](https://jitpack.io/#KubawGaming/BetterRunnableAPI)</bold> <strong>Its project version used in gradle/maven</strong>

<br>
Small library based on the Bukkit API that allows you to easily create a Task.
The code was tested on minecraft version 1.20.4. I did not check compatibility with other versions!
After creating new instance of some BetterRunnable, task will start automaticly.

## Example of use:

```java
BetterRunnable exampleTask = new BetterRunnable(plugin, task -> {
    System.out.println("Task repeated " + task.executions + " times");

    // Executions is a built-in variable that is added each time a Task executes
    if(task.executions > 50) task.cancel();
}, 20); // 20 (ticks) = 1 second, current runnable will be executed every 1 second
```

The first example creates a synchronous task. This means that you can, for example, change the Minecraft world in it
You can also create a group of Tasks that you can operate at once:

```java
BetterRunnableGroup tasksGroup = new BetterRunnableGroup(exampleTask);
BetterRunnable exampleTask2 = new BetterRunnable(plugin, tasksGroup, task -> {}, 20); // You can add tasks to group in constructor

tasksGroup.pauseAll(); // Pause all tasks in group
tasksGroup.unpauseAll(); // Unpause all tasks in group
tasksGroup.cancelAll(); // Cancel all tasks in group
```

You can create async task as well. Note that the time in BetterAsyncRunnable runs in milliseconds, not ticks!

```java
BetterRunnableGroup tasksAsyncGroup = new BetterRunnableGroup();
BetterAsyncRunnable exampleTask3 = new BetterAsyncRunnable(plugin, tasksAsyncGroup, task -> {
    System.out.println("Async task repeated " + task.executions + " times");
}, 2000); // Repeating every 2 seconds

//Another way to add task to a group
tasksAsyncGroup.addTask(exampleTask3);

tasksAsyncGroup.pauseAll(); // Pause all tasks in group
tasksAsyncGroup.unpauseAll(); // Unpause all tasks in group
tasksAsyncGroup.cancelAll(); // Cancel all tasks in group
```

You can create delayed task that you can pause! Example:

```java
BetterRunnableGroup delayedTasksGroup = new BetterRunnableGroup();
BetterDelayedRunnable exampleTask4 = new BetterDelayedRunnable(plugin, delayedTasksGroup, task -> {
    System.out.println("Hello from delayed task!");
            
    task.pause(); // Pause delayed task
    new BetterDelayedRunnable(plugin, task2 -> task.unpause(), 20); // Unpause after 1 second (20 ticks)
}, 40); // Execute once after 2 seconds (including pause after 3 seconds)
```

Similar to BetterAsyncRunnable, you can create an asynchronous delayed task that runs on milliseconds:

```java
//If you do not want to add a task to a group simply insert null
BetterAsyncDelayedRunnable exampleTask5 = new BetterAsyncDelayedRunnable(plugin, null, task -> {
    System.out.println("Hello from delayed async task!");
}, 4000); // Execute once after 4 seconds
```

## Code refactoring

You can also split the code into parts using classes. Here is an example:

```java
public class CustomTimer implements Consumer<BetterRunnable> {

    @Override
    public void accept(BetterRunnable betterRunnable) {
        if(++betterRunnable.executions > 20) {
            betterRunnable.cancel();
            return;
        }

        System.out.println("Counting: " + betterRunnable.executions);
    }

}
```

After creating such a class and completing it with the code you want, you can run the task like this:

```java
new BetterRunnable(
    plugin,
    null, // You can add your task to BetterRunnableGroup
    new CustomTimer(),
    20
);
```

## Gradle:

```gradle
repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.KubawGaming:BetterRunnableAPI:VERSION_HERE'
}
```

## Maven:

```html
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>

<dependency>
    <groupId>com.github.KubawGaming</groupId>
    <artifactId>BetterRunnableAPI</artifactId>
    <version>VERSION_HERE</version>
</dependency>
```
