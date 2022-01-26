package com.stefanobevilacqua.stlc.core.terms

import com.stefanobevilacqua.stlc.core.types.Either
import com.stefanobevilacqua.stlc.core.types.Type

data class EitherLeft(
  val term: Term,
  val type: Type,
): Term() {
  override fun toString() = "left($term, $type)"

  override fun evaluateType(ctx: TypeContext): Type = Either(
    left = term.evaluateType(ctx),
    right = type
  )
}