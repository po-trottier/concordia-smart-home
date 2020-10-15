package com.concordia.smarthomesimulator.dataModels;

import java.util.Date;

public class LogEntry {
    private final String component;
    private final String message;
    private final LogImportance importance;
    private final Date dateTime;

    /**
     * Instantiates a new Log entry.
     *
     * @param component  the component
     * @param message    the message
     * @param importance the importance
     */
    public LogEntry(String component, String message, LogImportance importance) {
        this.component = component;
        this.message = message;
        this.importance = importance;
        this.dateTime = new Date();
    }

    /**
     * Gets component.
     *
     * @return the component
     */
    public String getComponent() {
        return component;
    }

    /**
     * Gets message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets importance.
     *
     * @return the importance
     */
    public LogImportance getImportance() {
        return importance;
    }

    /**
     * Gets date time.
     *
     * @return the date time
     */
    public Date getDateTime() {
        return dateTime;
    }
}
