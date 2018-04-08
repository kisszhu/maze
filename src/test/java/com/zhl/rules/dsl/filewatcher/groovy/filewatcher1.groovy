package com.zhl.rules.dsl.filewatcher.groovy

import com.zhl.rules.dsl.filewatcher.java.FileWatcherExecutor

// FileWatcherDslContext类，这个类作为收集所有的DSL需求描述
// 主要用到Groovy的with关键字，它把fwctx对象隐藏起来，在with语句块中出现的方法和域来自fwctx对象
// 比如start 实际上来自fwctx.start
FileWatcherExecutor.execute { fwctx ->
    fwctx.with {

        // 可以省略小括号，相当于 fwctx.start(recursively)
        start recursively watch "E:/test/aa"   \
    filter include extension(
                "txt"
        ) filter exclude file name contains "123"   \
    on file modified { updatedFile ->
            async {
                println "file $updatedFile modifieddddd"
            }
        } on file modified { updatedFile ->
            println "open notepad"
            comamnd("notepad", updatedFile) run sync
        }

        start to watch "E:/test/bb"   \
    on file and folder updated { updatedFile, updatedType ->
            println "file $updatedFile $updatedType"
        }

    }
}

while (true) {

}