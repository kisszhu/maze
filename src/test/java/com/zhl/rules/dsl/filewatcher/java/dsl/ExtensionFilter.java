package com.zhl.rules.dsl.filewatcher.java.dsl;

import lombok.Value;

import java.util.Arrays;

/**
 * Created by zhuhailong-dc on 2018/4/7.
 * -
 */
@Value
public class ExtensionFilter implements FilterCondition {

    private String[] extensions;

    public ExtensionFilter(String[] extensions) {
        this.extensions = extensions;
    }


    @Override
    public boolean filter(String name) {
        String extName = getExtension(name);
        return Arrays.stream(extensions).anyMatch(extName::equalsIgnoreCase);
    }

    private String getExtension(String fileName) {
        int index = fileName.lastIndexOf('.');
        return index == -1 ? "" : fileName.substring(index + 1);
    }

}
