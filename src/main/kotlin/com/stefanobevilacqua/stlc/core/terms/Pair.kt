package com.stefanobevilacqua.stlc.core.terms

import com.stefanobevilacqua.stlc.core.types.Type

private typealias PairType = com.stefanobevilacqua.stlc.core.types.Pair

data class Pair(
  val left: Term,
  val right: Term,
): Term() {

  override fun toString() = "pair($left, $right)"

  override fun evaluateType(ctx: TypeContext): Type = PairType(
    left = left.evaluateType(ctx),
    right = right.evaluateType(ctx)
  )
}