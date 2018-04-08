package com.zhl.rules.dsl.filewatcher.test;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.collections.map.HashedMap;
import org.kie.api.definition.rule.Watch;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zhuhailong-dc on 2018/4/7.
 * -
 */
public class FileWatcher1 {

    private static List<Path> getDirsToWatch(Path path) throws IOException {
        List<Path> dirsToWatch = new ArrayList<>();
        // 递归得到每一个目录
        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes atts) throws IOException {
                dirsToWatch.add(dir);
                return FileVisitResult.CONTINUE;
            }
        });
        return dirsToWatch;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        try (WatchService watcher = FileSystems.getDefault().newWatchService()) {
            Path root = Paths.get("E:\\abc");
            List<Path> dirsToWatch = getDirsToWatch(root);

            Map<Path, WatchKey> pathWatchKeyMap = new HashedMap();
            for (Path path : dirsToWatch) {
                WatchKey watchKey = path.register(watcher,
                        StandardWatchEventKinds.ENTRY_CREATE,
                        StandardWatchEventKinds.ENTRY_DELETE,
                        StandardWatchEventKinds.ENTRY_MODIFY
                );
                pathWatchKeyMap.put(path, watchKey);
            }

            Map<WatchEvent.Kind<?>, String> fileUpdateTypes = ImmutableMap.of(
                    StandardWatchEventKinds.ENTRY_CREATE, "created",
                    StandardWatchEventKinds.ENTRY_DELETE, "deleted",
                    StandardWatchEventKinds.ENTRY_MODIFY, "modified"
            );

            while (true) {
                // watcher.take() 会阻塞地监听文件的变化，一旦文件发生变化，它会返回WatchKey对象
                WatchKey key = watcher.take();
                for (WatchEvent<?> event : key.pollEvents()) {
                    Path watchablePath = (Path) key.watchable();
                    Path path = watchablePath.resolve((Path) event.context());
                    WatchEvent.Kind<?> kind = event.kind();

                    String fileType = Files.isDirectory(path) ? "Folder" : "File";
                    String updateType = fileUpdateTypes.getOrDefault(event.kind(), "unknown");
                    System.out.printf("%s %s to %s\n", fileType, updateType, path.toString());

                    if (kind == StandardWatchEventKinds.ENTRY_CREATE && Files.isDirectory(path)) {
                        WatchKey watchKey = path.register(watcher,
                                StandardWatchEventKinds.ENTRY_CREATE,
                                StandardWatchEventKinds.ENTRY_DELETE,
                                StandardWatchEventKinds.ENTRY_MODIFY
                        );
                        pathWatchKeyMap.put(path, watchKey);
                    } else if (kind == StandardWatchEventKinds.ENTRY_DELETE && !Files.exists(path)) {
                        WatchKey watchKey = pathWatchKeyMap.get(path);
                        if (watchKey != null) {
                            pathWatchKeyMap.remove(path);
                            watchKey.cancel();
                            System.out.println("watchKey.cancel()");
                        }
                    }
                }
                System.out.println("key.isValid : " + key.isValid());
                if (key.isValid() && !key.reset()) {
                    System.out.printf("!key.reset()");
                    break;
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            System.out.printf("Exit");
        }
    }
}
