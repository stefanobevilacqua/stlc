package com.stefanobevilacqua.stlc.core.terms

data class RightOfPair(
  val pair: Term,
): Term() {
  override fun toString() = "$pair.right"
}