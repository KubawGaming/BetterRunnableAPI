# BetterRunnableAPI
<bold>[![](https://jitpack.io/v/KubawGaming/BetterRunnableAPI.svg)](https://jitpack.io/#KubawGaming/BetterRunnableAPI)</bold> <strong>Its project version used in gradle/maven</strong>

<br>
Small library based on the Bukkit API that allows you to easily create a task with additional implementations such as pausing.
The code was tested on minecraft version 1.20.4. I did not check compatibility with other versions!
After creating new instance of some BetterRunnable, task will start automaticly.
The predicted working of this API is on paper and his forks!

## Example of use:

```java
BetterTask exampleTask = new BetterRunnable(plugin, task -> {
    System.out.println("Task repeated " + task.getExecutions() + " times");

    // Executions is a built-in variable that is increased each time a task executes. It resets only when task is stopped by stop() method.
    if(task.getExecutions() > 50) task.stop();
}, 20); // 20 (ticks) = 1 second, current runnable will be executed every 1 second
```

The first example creates a synchronous task. You can also create a group of tasks that you can operate at once:

```java
BetterRunnableGroup tasksGroup = new BetterRunnableGroup(exampleTask);
BetterTask exampleTask2 = new BetterRunnable(plugin, tasksGroup, task -> {}, 20); // You can add tasks to group in constructor

tasksGroup.pauseAll(); // Pause all tasks in group
tasksGroup.unpauseAll(); // Unpause all tasks in group

// Stops all tasks in group. This method executes stop() method for all tasks. Stopping the task automatically removes it from group.
// You can use instead tasksGroup.stopAll(false); to prevent automatically removing tasks from all groups they are in.
tasksGroup.stopAll();
```

You can create asynchronous tasks as well. Note that the time in BetterAsyncRunnable runs in milliseconds, not ticks!

```java
BetterRunnableGroup tasksAsyncGroup = new BetterRunnableGroup();
BetterTask exampleTask3 = new BetterAsyncRunnable(plugin, tasksAsyncGroup, task -> {
    System.out.println("Async task repeated " + task.getExecutions() + " times");
}, 2000); // Repeating every 2 seconds

// Another way to add task to a group
tasksAsyncGroup.addTask(exampleTask3);

tasksAsyncGroup.pauseAll(); // Pause all tasks in group
tasksAsyncGroup.unpauseAll(); // Unpause all tasks in group
tasksAsyncGroup.stopAll(); // Stops all tasks in group
```

You can create delayed tasks that you can pause! Example:

```java
BetterTask exampleTask4 = new BetterDelayedRunnable(plugin, task -> {
    System.out.println("Hello from delayed task!");
}, 40); // Execute once after 2 seconds (including pause after 3 seconds)

exampleTask4.pause(); // Pause delayed task
new BetterDelayedRunnable(plugin, task -> exampleTask4.unpause(), 20); // Unpause after 1 second (20 ticks)
```

Similar to BetterAsyncRunnable, you can create an asynchronous delayed tasks that runs on milliseconds:

```java
BetterTask exampleTask5 = new BetterAsyncDelayedRunnable(plugin, task -> {
    System.out.println("Hello from delayed async task!");
}, 4000); // Execute once after 4 seconds
```

## Code refactoring

You can also split the code into parts using classes. Here is an example:

```java
public class CustomTimer implements Consumer<BetterTask> {

    @Override
    public void accept(BetterTask task) {
        if(task.getExecutions() > 20) {
            task.stop();
            return;
        }

        System.out.println("Counting: " + task.getExecutions());
    }

}
```

After creating such a class and completing it with the code you want, you can run the task like this:

```java
new BetterRunnable(
    plugin,
    new CustomTimer(),
    20
);

// You can use one class for a few types of tasks
new BetterDelayedRunnable(
    plugin,
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
