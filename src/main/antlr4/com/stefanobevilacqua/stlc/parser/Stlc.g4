grammar Stlc;

WS          : [ \n\r\t] -> skip;

FUN         : 'fun';
HYP         : 'hyp';
LET         : 'let';
PAIR        : 'pair';
LEFT        : 'left';
RIGHT       : 'right';
FOLD        : 'fold';

VALUENAME   : [a-z][a-z0-9_]*;
TYPENAME    : [A-Z][A-Z_]*;

type        : TYPENAME                          # baseType
            | '(' type ')'                      # groupType
            | type '&&' type                    # pairType
            | type '||' type                    # eitherType
            | <assoc=right> type '->' type      # unaryFuncType
            | <assoc=right> typelist '->' type  # naryFuncType
            ;

typelist    : '(' type (',' type)+ ')';

term        : VALUENAME                                     # variableTerm
            | (FUN | HYP) '(' funcParams ')' '->' funcBody  # lambdaTerm
            | LET VALUENAME ':' type '=' term ';' term      # letTerm
            | term '(' paramList ')'                        # applicationTerm
            | PAIR '(' term ',' term ')'                    # pairTerm
            | term '.' LEFT                                 # firstTerm
            | term '.' RIGHT                                # secondTerm
            | LEFT '(' term ',' type ')'                    # leftTerm
            | RIGHT '(' type ',' term ')'                   # rightTerm
            | term '.' FOLD '(' term ',' term ')'           # foldTerm
            ;

funcParams  : VALUENAME ':' type (',' VALUENAME ':' type)*;
funcBody    : term | '{' term '}';
paramList   : term (',' term)*;