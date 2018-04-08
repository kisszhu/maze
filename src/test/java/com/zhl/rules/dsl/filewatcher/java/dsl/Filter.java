package com.zhl.rules.dsl.filewatcher.java.dsl;

import com.zhl.rules.dsl.filewatcher.java.FileWatchEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.definition.rule.Watch;
import rx.functions.Func2;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by zhuhailong-dc on 2018/4/7.
 * -
 */
//@RequiredArgsConstructor
@Slf4j
public class Filter {

    @Getter
    private final Watcher watcher;
    @Getter
    private final FilterType filterType;

    public Filter(Watcher watcher, FilterType filterType) {
        this.watcher = watcher;
        this.filterType = filterType;
    }

    @Getter
    private FilterCondition filterCondition;

    @Getter
    private List<FileType> fileTypes = Arrays.asList(FileType.FILE, FileType.FOLDER);

    @Getter
    private NamePath nameOrPath = NamePath.NAME;

    @Getter
    private boolean ignoreCase = false;

    public Filter by(FileType fileType) {
        System.out.printf("by {}", fileType);
        fileTypes = Arrays.asList(fileType);
        return this;
    }

    public Filter by(NamePath nameOrPath) {
        System.out.printf("by {}", nameOrPath);
        this.nameOrPath = nameOrPath;
        return this;
    }

    public Filter folder(NamePath nameOrPath) {
        return by(FileType.FOLDER).by(nameOrPath);
    }

    public Filter file(NamePath nameOrPath) {
        return by(FileType.FILE).by(nameOrPath);
    }

    public Filter cases(boolean ignoreCase) {
        System.out.printf("cases {}", ignoreCase ? "insensitive" : "sensitive");
        this.ignoreCase = ignoreCase;
        return this;
    }

    // 扩展名
    public Watcher extension(String... extensions) {
        System.out.printf("extension {}" + Arrays.toString(extensions));
        filterCondition = new ExtensionFilter(extensions);
        return watcher;
    }

    public Watcher when(Func2<String, String, Boolean> condition) {
        System.out.printf("when condition");
        filterCondition = new ConditionFilter(condition);
        return watcher;
    }

    public Watcher equalsTo(String... values) {
        System.out.printf("equals to {}", Arrays.toString(values));
        filterCondition = new EqualFilter(values, ignoreCase);
        return watcher;
    }

    public Watcher startsWith(String... values) {
        System.out.printf("starts with {}", Arrays.toString(values));
        filterCondition = new StartFilter(values, ignoreCase);
        return watcher;
    }

    public Watcher endsWith(String... values) {
        System.out.printf("ends with {}" + Arrays.toString(values));
        filterCondition = new EndFilter(values, ignoreCase);
        return watcher;
    }

    public Watcher contains(String... values) {
        System.out.printf("contains {}", Arrays.toString(values));
        filterCondition = new ContainFilter(values, ignoreCase);
        return watcher;
    }

    public Watcher matches(String pattern) {
        System.out.printf("matches{}", pattern);
        filterCondition = new MatchFilter(Pattern.compile(pattern, ignoreCase ? Pattern.CASE_INSENSITIVE : 0));
        return watcher;
    }

    public boolean filter(FileWatchEvent evt) {
        if (filterCondition == null) {
            return true;
        }
        return filterCondition.filter(evt, this);
    }

    public NamePath getNameOrPath() {
        return nameOrPath;
    }

    public FilterType getFilterType() {
        return filterType;
    }


    public List<FileType> getFileTypes() {
        return fileTypes;
    }
}






















