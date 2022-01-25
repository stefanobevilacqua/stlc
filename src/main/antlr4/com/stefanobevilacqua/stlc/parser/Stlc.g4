grammar Stlc;

WS          : [ \n\r\t] -> skip;
TYPENAME    : [A-Z][A-Z_]*;

type        : TYPENAME                          # baseType
            | '(' type ')'                      # groupType
            | type '&&' type                    # pairType
            | type '||' type                    # eitherType
            | <assoc=right> type '->' type      # unaryFuncType
            | <assoc=right> typelist '->' type  # naryFuncType
            ;

typelist    : '(' type (',' type)+ ')';