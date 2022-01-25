package com.stefanobevilacqua.stlc.core.terms

data class Variable(
  val name: String,
): Term() {

  override fun toString() = name
}