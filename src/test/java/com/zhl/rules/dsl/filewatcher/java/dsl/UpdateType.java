package com.zhl.rules.dsl.filewatcher.java.dsl;

import com.zhl.rules.dsl.filewatcher.java.FileWatchEvent;
import com.zhl.rules.dsl.filewatcher.java.FileWatcherException;

import java.util.Arrays;
import java.util.function.Predicate;

/**
 * Created by zhuhailong-dc on 2018/4/7.
 * -
 */
public enum UpdateType {

    CREATED(evt -> evt.isCreated()),
    MODIFIED(evt -> evt.isModified()),
    DELETED(evt -> evt.isDeleted()),
    UPDATED(evt -> true);

    private final Predicate<FileWatchEvent> matcher;

    private UpdateType(Predicate<FileWatchEvent> matcher) {
        this.matcher = matcher;
    }

    public boolean match(FileWatchEvent evt) {
        return matcher.test(evt);
    }

    public static UpdateType from(FileWatchEvent evt) {
        return Arrays.stream(values())
                .filter(it -> it.match(evt))
                .findFirst()
                .orElseThrow(() -> new FileWatcherException("Cannot find event kind " + evt.getKind()));
    }
}
