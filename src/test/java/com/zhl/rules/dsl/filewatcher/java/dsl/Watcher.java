package com.zhl.rules.dsl.filewatcher.java.dsl;

import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhuhailong-dc on 2018/4/7.
 * -
 */
@Slf4j
@Data
public class Watcher {

    private boolean start;
    private boolean recursively;

    private String watchedFile = "";
    @Getter
    private final List<Handler> handlers = new ArrayList<>();
    @Getter
    private final List<Filter> filters = new ArrayList<>();

    public Watcher watch(String path) {
        System.out.printf("watch {}", path);
        watchedFile = path;
        return this;
    }

    public Handler on(FileType fileType) {
        System.out.printf("on {}", fileType);
        Handler handler = new Handler(this, fileType);
        handlers.add(handler);
        return handler;
    }

    public Filter filter(FilterType filterType) {
        System.out.printf("filter {}", filterType);
        Filter ft = new Filter(this, filterType);
        filters.add(ft);
        return ft;
    }


    public void setStart(boolean start) {
        this.start = start;
    }

    public void setRecursively(boolean recursively) {
        this.recursively = recursively;
    }

    public boolean isStart() {
        return start;
    }

    public boolean isRecursively() {
        return recursively;
    }

    public String getWatchedFile() {
        return watchedFile;
    }

    public List<Handler> getHandlers() {
        return handlers;
    }

    
    public List<Filter> getFilters() {
        return filters;
    }
}
