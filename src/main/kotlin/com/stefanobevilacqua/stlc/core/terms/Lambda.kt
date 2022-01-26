package com.stefanobevilacqua.stlc.core.terms

import com.stefanobevilacqua.stlc.core.types.Type
import com.stefanobevilacqua.stlc.core.types.Function

data class Lambda(
  val parameters: List<Parameter>,
  val body: Term,
): Term() {

  data class Parameter(val name: String, val type: Type) {
    override fun toString(): String = "$name: $type"
  }

  override fun toString() = "fun(${parameters.joinToString(", ")}) -> { $body }"

  override fun evaluateType(ctx: TypeContext): Type = Function(
    parameterTypes = parameters.map { it.type },
    returnType = body.evaluateType(ctx + parameters.toMap())
  )

  private fun List<Parameter>.toMap() = associate { (it.name to it.type) }
}