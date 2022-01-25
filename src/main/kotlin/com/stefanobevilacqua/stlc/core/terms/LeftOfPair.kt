package com.stefanobevilacqua.stlc.core.terms

data class LeftOfPair(
  val pair: Term,
): Term() {

  override fun toString() = "$pair.left"
}