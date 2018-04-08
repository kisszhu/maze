package com.zhl.rules.dsl.filewatcher.java.dsl;

import com.zhl.rules.dsl.filewatcher.java.FileWatchEvent;

import java.nio.file.Path;

/**
 * Created by zhuhailong-dc on 2018/4/7.
 * -
 */
public interface FilterCondition {

    default boolean filter(FileWatchEvent evt, Filter filter) {
        return filter(evt.getPath(), filter.getNameOrPath());
    }

    default boolean filter(Path path, NamePath namePath) {
        return filter(namePath == NamePath.NAME ? path.getFileName().toString() : path.toString());
    }

    boolean filter(String nameOrPath);

}
