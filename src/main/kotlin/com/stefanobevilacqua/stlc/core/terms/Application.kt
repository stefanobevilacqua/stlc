package com.stefanobevilacqua.stlc.core.terms

import com.stefanobevilacqua.stlc.core.exceptions.NotAFunctionException
import com.stefanobevilacqua.stlc.core.exceptions.ParamTypesException
import com.stefanobevilacqua.stlc.core.types.Type
import com.stefanobevilacqua.stlc.core.types.Function

data class Application(
  val function: Term,
  val parameters: List<Term>,
): Term() {
  override fun toString() = "$function(${parameters.joinToString(", ")})"

  override fun evaluateType(ctx: TypeContext): Type {

    val funType = function.evaluateType(ctx)
    val paramTypes = parameters.map { it.evaluateType(ctx) }

    return when (funType) {
      is Function ->
        if (funType.parameterTypes == paramTypes) funType.returnType
        else throw ParamTypesException(
          "$function",
          paramTypes.joinToString(", "),
          funType.parameterTypes.joinToString(", ")
        )
      else -> throw NotAFunctionException("$function", "$funType")
    }
  }
}