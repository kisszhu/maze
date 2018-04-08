package com.zhl.rules.dsl.filewatcher.java.dsl;

import lombok.Value;

import java.util.regex.Pattern;

/**
 * Created by zhuhailong-dc on 2018/4/7.
 * -
 */
@Value
public class MatchFilter implements FilterCondition {
    Pattern pattern;

    public MatchFilter(Pattern pattern) {
        this.pattern = pattern;
    }

    @Override
    public boolean filter(String nameOrPath) {
        return pattern.matcher(nameOrPath).matches();
    }
}
