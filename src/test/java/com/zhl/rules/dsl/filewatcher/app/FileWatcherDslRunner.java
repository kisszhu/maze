package com.zhl.rules.dsl.filewatcher.app;

import com.zhl.rules.dsl.filewatcher.java.dsl.FileWatcherDslContext;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import lombok.SneakyThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import groovy.lang.Binding;
import org.codehaus.groovy.control.CompilerConfiguration;
import rx.Observable;


/**
 * Created by zhuhailong-dc on 2018/4/7.
 * -
 */
public class FileWatcherDslRunner {

    @SneakyThrows
    private static FileWatcherDslContext parse(Path dslPath) {
        try {
            FileWatcherDslContext context = new FileWatcherDslContext();
            Binding binding = new Binding();
            binding.setProperty("context", context);
            CompilerConfiguration configuration = new CompilerConfiguration();
            String dslText = new String(Files.readAllBytes(dslPath), "UTF-8");
            String groovyCode = String.format("context.with {%s}", dslText);
            Script dslScript = new GroovyShell(binding, configuration).parse(groovyCode);
            dslScript.run();
            return context;
        } catch (IOException ex) {
        }
        return null;
    }

    private static Observable<Path> getDslFiles(Path folder, int maxDepth) {
        try (Stream<Path> stream = Files.walk(folder, maxDepth)) {
            return Observable.from(stream.filter(Files::isRegularFile).filter(path -> path.toString().endsWith(".fw")).toArray(Path[]::new));
        } catch (IOException ex) {

        }
        return null;
    }

    public static Observable<FileWatcherDslContext> getDslContexts(Path dsl) {
        return Observable.just(dsl).flatMap(dslPath -> {
            if (Files.isDirectory(dslPath)) {
                return getDslFiles(dslPath, 1);
            } else {
                return Observable.just(dslPath);
            }
        }).map(FileWatcherDslRunner::parse);
    }

}
