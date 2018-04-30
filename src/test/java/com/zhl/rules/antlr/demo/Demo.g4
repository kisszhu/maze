grammar Demo; // 定义文法的名字，必须与文法文件的名字相同

//parser
prog:stat
;
stat:expr|NEWLINE
;

/**
在Antlr中，算法的优先级需要通过文法规则的嵌套定义来实现，加减法的优先级低于乘除法，
表达式expr的定义由乘除法表达式multExpr和加减法算符('+'|'-')构成；
同理，括号的优先级高于乘除法，乘除法表达式multExpr通过原子操作数atom和乘除法算符('*'|'/')构成。
*/

expr:multExpr(('+'|'-')multExpr)*
;
multExpr:atom(('*'|'/')atom)*
;

atom:'('expr')'
    |INT
    |ID
;

/**
在Antlr种语法定义和词法定义通过规则的第一个字符来区别，规定语法定义符号的第一个字母小写，
而词法定义符号的第一个字母大写。

skip() 是词法分析器的一个方法
*/

//lexer
ID:('a'..'z'|'A'..'Z')+;
INT:'0'..'9'+;
NEWLINE:'\r'?'\n';
WS:(' '|'\t'|'\n'|'\r')+{skip();};
