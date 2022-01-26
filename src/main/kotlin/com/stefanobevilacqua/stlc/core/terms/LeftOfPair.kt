package com.stefanobevilacqua.stlc.core.terms

import com.stefanobevilacqua.stlc.core.exceptions.NotAPairException
import com.stefanobevilacqua.stlc.core.types.Type
import com.stefanobevilacqua.stlc.core.types.Pair

data class LeftOfPair(
  val pair: Term,
): Term() {

  override fun toString() = "$pair.left"

  override fun evaluateType(ctx: TypeContext): Type =
    when (val type = pair.evaluateType(ctx)) {
      is Pair -> type.left
      else -> throw NotAPairException("$pair", "$type")
    }
}