package me.kubaw208.betterrunnableapi.structs;

/**
 * AUTOMATIC - Cancels task and automatically starts it on unpause.
 * <br><br>
 * MANUAL - Keeps task running, only isPaused boolean in a task is changed. You can implement your own logic while pausing in a task.
 *
 *
 */
public enum PauseType {

    AUTOMATIC,
    MANUAL

}