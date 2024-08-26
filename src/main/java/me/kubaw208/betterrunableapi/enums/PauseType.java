package me.kubaw208.betterrunableapi.enums;

public enum PauseType {

    /** Cancels task and automatically starts it on unpause. */
    AUTOMATIC,

    /** Keeps task running, only isPaused boolean in task is changed. You can implement your own logic while pausing in task. */
    MANUAL

}