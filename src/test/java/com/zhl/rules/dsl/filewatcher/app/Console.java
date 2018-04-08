package com.zhl.rules.dsl.filewatcher.app;

import com.zhl.rules.dsl.filewatcher.java.FileWatchEvent;
import com.zhl.rules.dsl.filewatcher.java.FileWatcherExecutor;
import lombok.Getter;
import lombok.SneakyThrows;
import rx.Observable;
import rx.Subscription;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;

/**
 * Created by zhuhailong-dc on 2018/4/7.
 * -
 */
public class Console {

    @Getter
    private final String path;

    Subscription subscription;

    public Console(String path) {
        this.path = path;
    }

    @SneakyThrows
    public static void main(String[] args) {
        Console console = new Console("src/test/java/com/zhl/rules/dsl/filewatcher/groovy/test2.fw");//args.length > 0 ? args[0] : ".");
        DslWatcher.watch(console.getPath(), updatedFile -> console.restart());

        console.start();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            do {
                System.out.println("Input 'exit' to exit: ");
            } while (!"exit".equalsIgnoreCase(br.readLine()));
        } catch (IOException ex) {

        }
        console.stop();
    }

    public void start() {
        subscription = Observable.just(path)
                .map(strPath -> Paths.get(strPath).toAbsolutePath())
                .flatMap(FileWatcherDslRunner::getDslContexts)
                .flatMap(FileWatcherExecutor::run)
                .subscribe(this::onNext, this::onError);
    }

    private void stop() {
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }

    private void restart() {
        stop();
        start();
    }

    private void onNext(FileWatchEvent e) {

    }

    public String getPath() {
        return path;
    }

    private void onError(Throwable e) {
        System.err.println(e.getMessage());
    }
}