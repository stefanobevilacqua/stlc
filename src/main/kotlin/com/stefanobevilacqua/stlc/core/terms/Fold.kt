package com.stefanobevilacqua.stlc.core.terms

data class Fold(
  val either: Term,
  val ifLeft: Term,
  val ifRight: Term,
): Term() {
  override fun toString() = "$either.fold($ifLeft, $ifRight)"
}
