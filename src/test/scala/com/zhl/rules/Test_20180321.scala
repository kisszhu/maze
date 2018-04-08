package com.zhl.rules

import java.io.File
import java.nio.file.FileSystem

import org.apache.hadoop.fs.{FileSystem, Path}
import java.io.File

// import hammurabi.RuleEngine
import org.apache.hadoop.conf.Configuration

/**
  * Created by zhuhailong-dc on 2018/3/21.
  * 规则引擎（rule engine）是指将复杂的业务逻辑抽象成规则，然后使用特定的算法（比如Rete）
  * 对规则进行求值等操作
  * 特点：
  * 1.脚本化，允许用类Python的脚本语言或DSL来描述规则
  * 2.动态化，实时动态地加载规则脚本，规则的修改能实时的反馈于服务系统
  * 快速的执行速度
  *
  * Scala 方案：
  * 为了做到脚本化与动态化，Scala化的规则引擎将会把规则作为Scala Script独立出来，采用动态编译来加载。
  * 第三方Scala库hammurabi与scalascriptengine完美地契合了需求。其中，hammurabi是规则引擎的具体实现，
  * scalascriptengine用于对scala文件做动态编译。虽然hammurabi没有wiki，但是DZone上有一篇介绍使用的文章。
  * https://dzone.com/articles/hammurabi-scala-rule-engine
  *
  */
object Test_20180321 {

  import com.googlecode.scalascriptengine.ScalaScriptEngine;

  def loadRules[T](sourceDir: String, className: String): T = {
    val sse = ScalaScriptEngine.onChangeRefresh(new File(sourceDir))
    // delete all compiled classes (i.e. from previous runs)
    sse.deleteAllClassesInOutputDirectory()
    sse.refresh
    sse.newInstance[T](className)
  }
}

//import hammurabi.Rule

trait RulesTrait {
//  val rules: Set[Rule]
}

case class User(uid: String, apps: Array[String]) {
  val tags = scala.collection.mutable.Set.empty[String]

  override def toString: String = "uid: %s, app: %s, tags: %s".format(uid, apps.mkString, tags)

}

// Scala Script所描述的业务规则集合（用于给用户打标签）如下：
//import hammurabi.Rule
//import hammurabi.Rule._

//class TagRules extends RulesTrait {
//
//  override val rules: Set[Rule] = Set(
//    rule("add 母婴 Tag") let {
//      val u = any(kindOf[User])
//      when {
//        u.apps.mkString.matches(".*(孕|宝宝|育儿).*")
//      } then {
//        u.tags += "母婴"
//      }
//    },
//    rule("add 大学生 Tag") let {
//      val u = any(kindOf[User])
//      when {
//        u.apps.mkString.matches(".*(四级|六级|大学).*")
//      } then {
//        u.tags += "大学生"
//      }
//    }
//  )
//}


