package com.stefanobevilacqua.stlc.core.terms

import com.stefanobevilacqua.stlc.core.types.Type

data class Let(
  val name: String,
  val type: Type,
  val value: Term,
  val next: Term,
): Term() {
  override fun toString() = "let $name: $type = $value; $next"
}