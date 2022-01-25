package com.stefanobevilacqua.stlc.core.terms

import com.stefanobevilacqua.stlc.core.types.Type

data class EitherRight(
  val type: Type,
  val term: Term,
): Term() {
  override fun toString() = "right($type, $term)"
}