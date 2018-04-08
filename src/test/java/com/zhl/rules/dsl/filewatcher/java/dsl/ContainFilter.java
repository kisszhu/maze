package com.zhl.rules.dsl.filewatcher.java.dsl;

import lombok.Value;

import java.util.Arrays;

/**
 * Created by zhuhailong-dc on 2018/4/7.
 * -
 */
@Value
public class ContainFilter implements FilterCondition {
    String[] values;
    boolean ignoreCase;

    public ContainFilter(String[] values, boolean ignoreCase) {
        this.values = values;
        this.ignoreCase = ignoreCase;
    }

    @Override
    public boolean filter(String nameOrPath) {
        return Arrays.stream(values).anyMatch(it ->
                ignoreCase ? nameOrPath.toLowerCase().contains(it.toLowerCase()) : nameOrPath.contains(it.toLowerCase()));
    }
}
