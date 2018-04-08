package com.zhl.rules.dsl.filewatcher.app;

import com.zhl.rules.dsl.filewatcher.java.FileWatcherExecutor;
import rx.functions.Action1;

import static com.zhl.rules.dsl.filewatcher.java.dsl.FileWatcherDslContext.*;

/**
 * Created by zhuhailong-dc on 2018/4/7.
 * -
 */
public class DslWatcher {
    public static void watch(String dslPath, Action1<String> onUpdateDsl) {
        FileWatcherExecutor.execute(context -> context
                .start(to)
                .watch(dslPath)
                .filter(include)
                .file(name)
                .extension("fw")
                .on(file).modified(onUpdateDsl)
                .on(file).deleted(onUpdateDsl));
    }
}
