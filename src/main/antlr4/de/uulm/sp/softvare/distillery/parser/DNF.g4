grammar DNF;

file
   : (dnf NEWLINE*)* EOF
   ;

dnf
   : SPACE* (product SPACE* SEMICOLON SPACE*)* product SPACE*
   ;

product
   : OBRACKET SPACE* (literal SPACE* COMMA SPACE*)* literal SPACE* CBRACKET
   ;

literal
   : CHARACTER+
   ;

CHARACTER
   : [0-9A-Za-z]
   ;

OBRACKET
   : '['
   ;

CBRACKET
   : ']'
   ;

SEMICOLON
   : ';'
   ;

COMMA
   : ','
   ;

SPACE
   : ' ' -> skip
   ;

NEWLINE
   : '\r'? '\n' -> skip
   ;

