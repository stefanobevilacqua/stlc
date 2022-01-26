package com.stefanobevilacqua.stlc.core.terms

import com.stefanobevilacqua.stlc.core.exceptions.UnknownVariableException
import com.stefanobevilacqua.stlc.core.types.Type

data class Variable(
  val name: String,
): Term() {

  override fun toString() = name

  override fun evaluateType(ctx: TypeContext): Type =
    ctx[name] ?: throw UnknownVariableException(name)
}