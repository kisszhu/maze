package com.zhl.rules.dsl.filewatcher.java.dsl;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import rx.functions.Action1;
import rx.functions.Action2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhuhailong-dc on 2018/4/7.
 * -
 */
@Slf4j
public class Handler {
    @Getter
    private final Watcher watcher;

    @Getter
    private final List<FileType> fileTypes = new ArrayList<>();

    @Getter
    private UpdateType updateType;


    @Getter
    private Action2<String, String> code;

    public Handler(Watcher watcher, FileType fileType) {
        this.watcher = watcher;
        fileTypes.add(fileType);
    }

    public Handler add(FileType fileType) {
        System.out.printf("and {}", fileType);
        fileTypes.add(fileType);
        return this;
    }


    //TODO don't understand
    private Watcher execute1(Action1<String> code) {
        return execute((updatedFile, updatedType) -> code.call(updatedFile));
    }

    private Watcher execute(Action2<String, String> code) {
        this.code = code;
        return watcher;
    }

    public Watcher updated(Action2<String, String> code) {
        updateType = UpdateType.UPDATED;
        System.out.printf(updateType.toString());
        return execute(code);
    }

    public Watcher modified(Action1<String> code) {
        updateType = UpdateType.MODIFIED;
        System.out.printf(updateType.toString());
        return execute1(code);
    }

    public Watcher created(Action1<String> code) {
        updateType = UpdateType.CREATED;
        System.out.printf(updateType.toString());
        return execute1(code);
    }

    public Watcher deleted(Action1<String> code) {
        updateType = UpdateType.DELETED;
        System.out.printf(updateType.toString());
        return execute1(code);
    }


    public UpdateType getUpdateType() {
        return updateType;
    }


    public Action2<String, String> getCode() {
        return code;
    }


    public List<FileType> getFileTypes() {
        return fileTypes;
    }
}
