package com.stefanobevilacqua.stlc.core.exceptions

data class UnknownVariableException(val name: String):
  TypeErrorException("Unknown symbol: $name")

data class NotAFunctionException(
  val function: String,
  val funType: String
): TypeErrorException("$function is $funType, not a Function")

data class ParamTypesException(
  val function: String,
  val actual: String,
  val expected: String
): TypeErrorException("$function expects parameter type $expected, not $actual")

data class NotAPairException(
  val pair: String,
  val type: String
): TypeErrorException("$pair is $type, not a Pair")

data class AssignmentException(
  val name: String,
  val type: String,
  val value: String,
  val valueType: String,
): TypeErrorException("$name is declared with type $type but has value $value that has type $valueType")

data class ReturnTypesException(
  val ifLeft: String,
  val ifRight: String,
  val ifLeftRT: String,
  val ifRightRT: String,
): TypeErrorException("$ifLeft and $ifRight should have the same return type but are different ($ifLeftRT and $ifRightRT)")

data class NotAnEitherException(
  val either: String,
  val type: String
): TypeErrorException("$either is $type, not an Either")

open class TypeErrorException(override val message: String): RuntimeException()