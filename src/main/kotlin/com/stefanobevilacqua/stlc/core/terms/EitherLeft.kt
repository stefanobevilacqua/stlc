package com.stefanobevilacqua.stlc.core.terms

import com.stefanobevilacqua.stlc.core.types.Type

data class EitherLeft(
  val term: Term,
  val type: Type,
): Term() {
  override fun toString() = "left($term, $type)"
}