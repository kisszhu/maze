package com.zhl.rules.dsl.filewatcher.java.dsl;

import com.zhl.rules.dsl.filewatcher.java.FileWatchEvent;
import lombok.Value;
import rx.functions.Func2;

import java.nio.file.Path;

/**
 * Created by zhuhailong-dc on 2018/4/7.
 * -
 */
@Value
public class ConditionFilter implements FilterCondition {

    private Func2<String, String, Boolean> condition;

    public ConditionFilter(Func2<String, String, Boolean> condition) {
        this.condition = condition;
    }

    @Override
    public boolean filter(FileWatchEvent evt, Filter filter) {
        return condition.call(evt.getPath().toString(), UpdateType.from(evt).toString().toLowerCase());
    }

    @Override
    public boolean filter(Path path, NamePath namePath) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean filter(String nameOrPath) {
        throw new UnsupportedOperationException();
    }
}
