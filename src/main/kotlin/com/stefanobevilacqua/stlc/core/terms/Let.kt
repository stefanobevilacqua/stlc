package com.stefanobevilacqua.stlc.core.terms

import com.stefanobevilacqua.stlc.core.exceptions.AssignmentException
import com.stefanobevilacqua.stlc.core.types.Type

data class Let(
  val name: String,
  val type: Type,
  val value: Term,
  val next: Term,
): Term() {
  override fun toString() = "let $name: $type = $value; $next"

  override fun evaluateType(ctx: TypeContext): Type {
    val valueType = value.evaluateType(ctx)
    if (type == valueType) return next.evaluateType(ctx + (name to type))
    else throw AssignmentException(name, "$type", "$value", "$valueType")
  }
}