package com.stefanobevilacqua.stlc.core.terms

import com.stefanobevilacqua.stlc.core.types.Either
import com.stefanobevilacqua.stlc.core.types.Type

data class EitherRight(
  val type: Type,
  val term: Term,
): Term() {
  override fun toString() = "right($type, $term)"

  override fun evaluateType(ctx: TypeContext): Type = Either(
    left = type,
    right = term.evaluateType(ctx)
  )
}