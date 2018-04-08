package com.zhl.rules.dsl.filewatcher.java;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.SneakyThrows;
import lombok.Value;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;
import sun.security.krb5.internal.PAData;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhuhailong-dc on 2018/4/7.
 * -
 */
@Value
public class FileWatcher {
    Path path;
    // 用于判断是否允许递归查找文件
    boolean recursively;

    public FileWatcher(Path path, boolean recursively) {
        this.path = path;
        this.recursively = recursively;
    }

    /**
     * include the root path itself
     */
    private static List<Path> listDirsRecursively(Path path) throws IOException {
        List<Path> dirsToWatch = new ArrayList<>();
        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                dirsToWatch.add(dir);
                return FileVisitResult.CONTINUE;
            }
        });
        return dirsToWatch;
    }

    @SneakyThrows
    public Observable<FileWatchEvent> asObservable() throws IOException {
        if (!Files.exists(path)) {
            // 自定义业务异常
            throw new FileWatcherException("not exist path " + path);
        }

        long timeout = 1500; //ms
        Cache<Path, Long> pathLastModifiedTime = CacheBuilder
                .newBuilder()
                .expireAfterWrite(timeout, TimeUnit.MILLISECONDS)
                .build();

        boolean isWatchDir = Files.isDirectory(path);
        Path pathToWatch = isWatchDir ? path : path.getParent();

        FileWatchService watchService = new FileWatchService();

        return (isWatchDir && recursively ?
                Observable.from(listDirsRecursively(pathToWatch)) :
                Observable.just(pathToWatch)).reduce(watchService, (watcher, path) -> watcher.register(path))
                .flatMap(watcher -> Observable.create(subscriber -> watchFile(watcher, subscriber))
                        // 设置订阅的线程在哪个线程池中执行
                        // Schedulers.io() 返回一个Java标准的CacheThreadPool，带缓存的线程池
                        // 这样可以轻松解决take方法阻塞的问题。
                        .subscribeOn(Schedulers.io())
                        .cast(FileWatchEvent.class)
                )
                .filter(event -> isWatchDir || path.equals(event.getPath()))
                .filter(event -> event.exists() || event.isDeleted())
                .filter(event -> skipDuplicateEvent(pathLastModifiedTime, event, timeout))
                // doOnNext是响应订阅者onNext的响应逻辑
                .doOnNext(event -> updateWatchService(watchService, event))
                // doAfterTerminate是当观察者逻辑成功结束或者遇到错误时，即onCompleted或者onError时响应的逻辑
                // 它类似Java中的finally语句
                .doAfterTerminate(() -> watchService.close());
    }
    // 写到这里暂时还没有考虑用户在DSL中定义的过滤器和响应逻辑

    private void updateWatchService(final FileWatchService watchService, FileWatchEvent event) {
        if (event.isCreated() && event.isDirectory()) {
            watchService.register(event.getPath());
        } else if (event.isDeleted() && !event.exists()) {
            watchService.cancel(event.getPath());
        }
    }

    /**
     * fix Java's watch service issue that modify file event will send 2 to 3 times
     */
    private boolean skipDuplicateEvent(Cache<Path, Long> pathLastModifiedTimeCache,
                                       FileWatchEvent event, long periodInMilliSecond) {
        if (event.isModified() && event.isFile()) {
            Path path = event.getPath();
            long now = System.currentTimeMillis();
            synchronized (this) {
                Long lastModifiedTime = pathLastModifiedTimeCache.getIfPresent(path);
                if (lastModifiedTime == null || now - lastModifiedTime > periodInMilliSecond) {
                    pathLastModifiedTimeCache.put(path, now);
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return true;
        }
    }


    // 订阅事件
    // 定义订阅逻辑，当被观察者对象呗订阅时调用
    private void watchFile(FileWatchService watcher, Subscriber<? super FileWatchEvent> subscriber) {
        try {
            while (!subscriber.isUnsubscribed()) {
                WatchKey watchKey = watcher.take();
                for (WatchEvent event : watchKey.pollEvents()) {
                    Path watchablePath = (Path) watchKey.watchable();
                    Path path = watchablePath.resolve((Path) event.context());

                    // 用于响应每个事件的回调，出发订阅者的onNext事件
                    subscriber.onNext(new FileWatchEvent(path, event.kind()));
                }
                if (watchKey.isValid() && !watchKey.reset()) {
                    break;
                }
            }
            // 用于完成所有任务时的回调
            subscriber.onCompleted();
        } catch (Throwable e) {
            // 当有未处理的运行时异常抛出时onError方法被回调
            subscriber.onError(e);
        }
    }


}

