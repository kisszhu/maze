//Define a grammar called Hello
grammar Hello;
r : 'hello' ID; // match keyword hello followed by an identifier
ID : [a-z]+; // match lower-case identifiers
WS : [ \t\r\n]+ -> skip; // skip spaces, tabs, newlines

// 在rule “r”处选中“r”并右击鼠标后，点击“Test Rule r”
// 然后在左下方便可以输入“hello name”,在右下方框中便会显示对应的 Parse tree


