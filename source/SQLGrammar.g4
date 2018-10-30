// Define a grammar called SQLGrammar
grammar SQLGrammar;

//-------------------------------INPUT IS EITHER A COMMAND OR A QUERY-----------------//

program : (query | command)*;

//-------------------------------QUERIES----------------------------------------------//

query  : relationName '<-' expr;         

relationName : identifier ;             

 expr : selection 
      | renaming
      | projection
      | union
      | difference
      | product
      | atomicExpr
      ;

atomicExpr : relationName
       	| selection
        | renaming
        | projection
        ;						

selection : 'select' '(' condition ')' expr 
		| '(' 'select' '(' condition ')' expr ')'
		;
renaming : 'rename' '(' attributeList ')' expr
		| '(' 'rename' '(' attributeList ')' expr ')'
		;
projection : 'project' '(' attributeList ')' expr
		| '(' 'project' '(' attributeList ')' expr ')'
		;
union : atomicExpr '+' atomicExpr
		| '(' atomicExpr '*' atomicExpr  ')'
		;
difference : atomicExpr '-' atomicExpr
		| '(' atomicExpr '*' atomicExpr  ')'
		;
product : atomicExpr '*' atomicExpr 
		| '(' atomicExpr '*' atomicExpr  ')'
		;

condition : conjunction ('||' conjunction)* ;
conjunction: comparison ('&&' comparison)* ;
comparison : operand OP operand;

OP : '==' | '!=' | '>' | '<' | '<=' | '>=' ;
operand : attributeName | literal;
attributeList : attributeName (',' attributeName)*;
attributeName : identifier;

literal : '"' identifier '"' | identifier | integer;
identifier : ALPHA((ALPHA | DIGIT))* | '"' ALPHA((ALPHA | DIGIT))* '"';
ALPHA : [a-zA-Z_]+ | '_'; 
DIGIT : [0-9]+ ;


//---------------------------------COMMANDS------------------------------------------//

command : opencmd
		| closecmd
		| writecmd
		| exitcmd
		| showcmd
		| createcmd
		| updatecmd
		| insertcmd
		| deletecmd
		;

opencmd : 'OPEN' relationName;
closecmd : 'CLOSE' relationName;
writecmd : 'WRITE' relationName;

exitcmd : 'EXIT';

showcmd : 'SHOW' atomicExpr;

createcmd : 'CREATE TABLE' relationName '(' typedAttributeList ')' 'PRIMARY KEY' '(' attributeList ')';

updatecmd : 'UPDATE' relationName 'SET' attributeName '=' literal (',' attributeName '=' literal)* 'WHERE' condition;

insertcmd : 'INSERT INTO' relationName 'VALUES FROM' '(' literal (',' literal)* ')'
          | 'INSERT INTO' relationName 'VALUES FROM RELATION' expr
          ;

deletecmd : 'DELETE FROM' relationName 'WHERE' condition;

//--------------------------------------HELPERS----------------------------------------//

typedAttributeList : attributeName type (',' attributeName type)* ;
type : 'VARCHAR' '(' integer ')' | 'INTEGER';
integer : DIGIT(DIGIT)*;

//--------------------------------------REMOVE WHITESPACE------------------------------//

WS : [ \t\r\n]+ -> skip ; // skip spaces, tabs, newlines