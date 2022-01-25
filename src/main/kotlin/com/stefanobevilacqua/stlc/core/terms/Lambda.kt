package com.stefanobevilacqua.stlc.core.terms

import com.stefanobevilacqua.stlc.core.types.Type

data class Lambda(
  val parameters: List<Parameter>,
  val body: Term,
): Term() {

  data class Parameter(val name: String, val type: Type) {
    override fun toString(): String = "$name: $type"
  }

  override fun toString() = "fun(${parameters.joinToString(", ")}) -> { $body }"
}