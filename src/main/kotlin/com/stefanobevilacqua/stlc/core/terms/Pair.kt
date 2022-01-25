package com.stefanobevilacqua.stlc.core.terms

data class Pair(
  val left: Term,
  val right: Term,
): Term() {

  override fun toString() = "pair($left, $right)"
}