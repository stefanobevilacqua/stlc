package com.stefanobevilacqua.stlc.core.types

sealed class Type

data class Base(val name: String): Type() {
  override fun toString() = name
}

data class Either(val left: Type, val right: Type): Type() {
  override fun toString() = "($left || $right)"
}

data class Function(
  val parameterTypes: List<Type>,
  val returnType: Type
): Type() {
  override fun toString() = "(${parameterTypes.asString()} -> $returnType)"

  private fun List<Type>.asString(): String {
    val string = joinToString(", ")
    return if(this.size > 1) "($string)"
    else return string
  }
}

data class Pair(val left: Type, val right: Type): Type() {
  override fun toString() = "($left && $right)"
}