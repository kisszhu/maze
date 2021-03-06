package com.zhl.rules.dsl.filewatcher.java.dsl;

import lombok.Value;

import java.util.Arrays;

/**
 * Created by zhuhailong-dc on 2018/4/7.
 * -
 */
@Value
public class EqualFilter implements FilterCondition {
    String[] values;
    boolean ignoreCase;

    public EqualFilter(String[] values, boolean ignoreCase) {
        this.values = values;
        this.ignoreCase = ignoreCase;
    }

    @Override
    public boolean filter(String name) {
        return Arrays.stream(values).anyMatch(it -> ignoreCase ? name.equalsIgnoreCase(it) : name.equals(it));
    }
}
