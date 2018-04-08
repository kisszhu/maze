package com.zhl.rules.dsl.filewatcher.java.dsl;

import com.zhl.compile.frontend.symbols.Array;

import java.util.Arrays;

/**
 * Created by zhuhailong-dc on 2018/4/7.
 * -
 */
public class StartFilter implements FilterCondition {
    String[] values;
    boolean ignoreCase;

    public StartFilter(String[] values, boolean ignoreCase) {
        this.values = values;
        this.ignoreCase = ignoreCase;
    }

    @Override
    public boolean filter(String nameOrPath) {
        return Arrays.stream(values).anyMatch(it ->
                ignoreCase ? nameOrPath.toLowerCase().startsWith(it.toLowerCase()) : nameOrPath.startsWith(it.toLowerCase()));
    }
}
