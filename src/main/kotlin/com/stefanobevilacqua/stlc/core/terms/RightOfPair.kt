package com.stefanobevilacqua.stlc.core.terms

import com.stefanobevilacqua.stlc.core.exceptions.NotAPairException
import com.stefanobevilacqua.stlc.core.types.Type
import com.stefanobevilacqua.stlc.core.types.Pair

data class RightOfPair(
  val pair: Term,
): Term() {
  override fun toString() = "$pair.right"

  override fun evaluateType(ctx: TypeContext): Type =
    when (val type = pair.evaluateType(ctx)) {
      is Pair -> type.right
      else -> throw NotAPairException("$pair", "$type")
    }
}