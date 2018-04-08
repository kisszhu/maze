package com.zhl.rules.dsl.filewatcher.java;

import static java.util.stream.Collectors.partitioningBy;

import com.zhl.rules.dsl.filewatcher.java.dsl.*;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by zhuhailong-dc on 2018/4/6.
 * -
 */
public class FileWatcherExecutor {

    public static Subscription execute(Action1<FileWatcherDslContext> dslContextInitializer) {
        return run(dslContextInitializer).subscribe();
    }

    public static Observable<FileWatchEvent> run(Action1<FileWatcherDslContext> dslContextInitializer) {
        FileWatcherDslContext ctx = new FileWatcherDslContext();  // 创建DSL上下文对象（即Builder对象）
        dslContextInitializer.call(ctx); // 初始化DSL上下文对象（运行DSL从而完善Builder对象）
        return run(ctx);
    }

    public static Observable<FileWatchEvent> run(FileWatcherDslContext ctx) {
        return Observable.from(ctx.getWatchers()).filter(watcher -> watcher.isStart())
                .flatMap(FileWatcherExecutor::run);
    }

    public static Observable<FileWatchEvent> run(Watcher watcher) {
        try {
            List<Filter> includeFilters = watcher.getFilters().stream().collect(partitioningBy(filter -> filter.getFilterType() == FilterType.INCLUDE)).get(true);
            List<Filter> excludeFilters = watcher.getFilters().stream().collect(partitioningBy(filter -> filter.getFilterType() == FilterType.INCLUDE)).get(false);

            Observable<FileWatchEvent> fwe = new FileWatcher(Paths.get(watcher.getWatchedFile()), watcher.isRecursively()).asObservable()
                    .filter(evt -> includeFilters.isEmpty() || includeFilters.stream().anyMatch(filter -> matchFilter(evt, filter)))
                    .filter(evt -> !excludeFilters.stream().anyMatch(filter -> matchFilter(evt, filter)));

            for (Handler handler : watcher.getHandlers()) {
                fwe = fwe.doOnNext(evt -> {
                    if (isFileTypeMatch(evt, handler.getFileTypes()) && handler.getUpdateType().match(evt)) {
                        handler.getCode().call(evt.getPath().toString(), UpdateType.from(evt).toString().toLowerCase());
                    }
                });
            }
            return fwe;
        } catch (IOException ex) {

        }
        return null;
    }

    private static boolean matchFilter(FileWatchEvent evt, Filter filter) {
        return isFileTypeMatch(evt, filter.getFileTypes()) && filter.filter(evt);
    }

    private static boolean isFileTypeMatch(FileWatchEvent evt, List<FileType> fileTypes) {
        return (evt.isFile() && fileTypes.contains(FileType.FILE)) || (evt.isDirectory() && fileTypes.contains(FileType.FOLDER)) || evt.isDeleted();
    }

}
