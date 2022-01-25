package com.stefanobevilacqua.stlc.core.terms

data class Application(
  val function: Term,
  val parameters: List<Term>,
): Term() {
  override fun toString() = "$function(${parameters.joinToString(", ")})"
}