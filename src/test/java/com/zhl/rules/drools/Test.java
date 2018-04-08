package com.zhl.rules.drools;


// org.drools.builder.KnowledgeBuilderFactory

import org.kie.api.io.ResourceType;
import org.kie.internal.KnowledgeBase;
import org.kie.internal.KnowledgeBaseFactory;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderError;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.runtime.StatefulKnowledgeSession;

import java.util.Iterator;

/**
 * Created by zhuhailong-dc on 2018/3/23.
 * -
 */
public class Test {
    public static class User {
        private int money; // 手中的钱
        private int kp; // 空瓶数
        private int totals; // 喝掉的瓶数

        public int getMoney() {
            return money;
        }

        public void setMoney(int money) {
            this.money = money;
        }

        public int getKp() {
            return kp;
        }

        public void setKp(int kp) {
            this.kp = kp;
        }

        public int getTotals() {
            return totals;
        }

        public void setTotals(int totals) {
            this.totals = totals;
        }
    }

    public static void main(String[] args) throws Exception {
        KnowledgeBuilder kbuilder= KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(ResourceFactory.newClassPathResource("com/zhl/rules/drools/a.drl"), ResourceType.DRL);
        if(kbuilder.hasErrors()){
            System.out.println("规则错误：");
            Iterator<KnowledgeBuilderError> it=kbuilder.getErrors().iterator();
            while(it.hasNext()) System.out.println(it.next());
            return;
        }
        KnowledgeBase kb= KnowledgeBaseFactory.newKnowledgeBase();
        kb.addKnowledgePackages(kbuilder.getKnowledgePackages());
        StatefulKnowledgeSession s=kb.newStatefulKnowledgeSession();
        // 这个传递数据的对象叫做Fact对象
        /**
         * 当一个Java Bean插入到workingMemory中，规则使用的是原有对象的引用，规则通过对fact对象的读写，
         * 规则中，可以动态的往当前workingMemory中插入删除新的fact对象
         */
        User user=new User();
        user.setMoney(50);
        // 通过insert方法设置初始数据，并通过后续的代码进行规则调用进行计算
        s.insert(user);
        s.fireAllRules();
        s.dispose();
    }
}
