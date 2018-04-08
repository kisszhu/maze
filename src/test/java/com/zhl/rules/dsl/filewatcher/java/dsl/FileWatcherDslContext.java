package com.zhl.rules.dsl.filewatcher.java.dsl;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import rx.Subscription;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhuhailong-dc on 2018/4/7.
 * -
 */
@Slf4j
@Getter
public class FileWatcherDslContext {

    public static final boolean recursively = true;
    public static final boolean to = !recursively;

    public static final FileType file = FileType.FILE;
    public static final FileType folder = FileType.FOLDER;

    public static final FilterType include = FilterType.INCLUDE;
    public static final FilterType exclude = FilterType.EXCLUDE;

    public static final NamePath name = NamePath.NAME;
    public static final NamePath path = NamePath.PATH;

    // 敏感的
    public static final boolean sensitive = false;
    public static final boolean insensitive = !sensitive;

    private final List<Watcher> watchers = new ArrayList<>();

    public Watcher start(boolean recursively) {
        System.out.printf("start {}to", recursively ? "recursively" : "");
        Watcher w = new Watcher();
        w.setRecursively(recursively);
        w.setStart(true);
        watchers.add(w);
        return w;
    }

    public Watcher stop(boolean to) {
        System.out.printf("stop to");
        Watcher w = new Watcher();
        w.setStart(false);
        watchers.add(w);
        return w;
    }

    public static Subscription async(Action0 action) {
        return Schedulers.io().createWorker().schedule(action);
    }

    public List<Watcher> getWatchers() {
        return watchers;
    }

}
