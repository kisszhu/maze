package com.zhl.rules.dsl.filewatcher.java;

import lombok.SneakyThrows;
import org.apache.commons.collections.map.HashedMap;

import java.io.IOException;
import java.nio.file.*;
import java.util.Map;

/**
 * Created by zhuhailong-dc on 2018/4/7.
 * -
 */
public class FileWatchService implements AutoCloseable {

    private volatile WatchService watchService;

    private final Map<Path, WatchKey> pathWatchKeyMap = new HashedMap();

    @SneakyThrows
    public WatchService getWatchService() throws IOException {
        if (watchService == null) {
            synchronized (this) {
                if (watchService == null) { // 二级判断
                    watchService = FileSystems.getDefault().newWatchService();
                }
            }
        }
        return watchService;
    }

    public synchronized FileWatchService register(Path path) {
        try {
            pathWatchKeyMap.put(path, path.register(getWatchService(),
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_DELETE,
                    StandardWatchEventKinds.ENTRY_MODIFY));
        } catch (IOException ex) {

        }
        return this;
    }

    public synchronized void close(){
        try {
            if (watchService != null) {
                watchService.close();
                watchService = null;
            }
        }catch (IOException ex){

        }
    }

    public WatchKey take() throws IOException, InterruptedException {
        return getWatchService().take();
    }

    public synchronized void cancel(Path path) {
        WatchKey watchKey = pathWatchKeyMap.get(path);
        if (watchKey != null) {
            pathWatchKeyMap.remove(path);
            watchKey.cancel();
        }
    }
}
