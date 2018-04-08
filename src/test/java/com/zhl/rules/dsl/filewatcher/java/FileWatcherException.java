package com.zhl.rules.dsl.filewatcher.java;

import org.apache.camel.component.file.FileEndpoint;

/**
 * Created by zhuhailong-dc on 2018/4/7.
 * -
 */
public class FileWatcherException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public FileWatcherException() {
        super();
    }

    public FileWatcherException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public FileWatcherException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileWatcherException(String message) {
        super(message);
    }

    public FileWatcherException(Throwable cause) {
        super(cause);
    }

}
