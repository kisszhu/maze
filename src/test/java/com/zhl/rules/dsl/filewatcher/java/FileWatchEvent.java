package com.zhl.rules.dsl.filewatcher.java;

import lombok.Value;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;

/**
 * Created by zhuhailong-dc on 2018/4/7.
 * 一般情况下，会将 path kind 当做方法的参数传入进去
 */
@Value
public class FileWatchEvent {

    Path path;
    WatchEvent.Kind<?> kind;

    public FileWatchEvent(Path path, WatchEvent.Kind<?> kind) {
        this.path = path;
        this.kind = kind;
    }

    public boolean isModified() {
        return kind == StandardWatchEventKinds.ENTRY_MODIFY;
    }

    public boolean isCreated() {
        return kind == StandardWatchEventKinds.ENTRY_CREATE;
    }

    public boolean isDeleted() {
        return kind == StandardWatchEventKinds.ENTRY_DELETE;
    }

    public boolean exists() {
        return Files.exists(path);
    }

    public boolean isDirectory() {
        return Files.isDirectory(path);
    }

    public boolean isFile() {
        return exists() && !isDirectory();
    }


    public Path getPath() {
        return path;
    }

//    public void setPath(Path path) {
//        this.path = path;
//    }

    public WatchEvent.Kind<?> getKind() {
        return kind;
    }

//    public void setKind(WatchEvent.Kind<?> kind) {
//        this.kind = kind;
//    }
}
