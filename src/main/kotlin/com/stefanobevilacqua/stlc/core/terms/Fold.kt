package com.stefanobevilacqua.stlc.core.terms

import com.stefanobevilacqua.stlc.core.exceptions.NotAFunctionException
import com.stefanobevilacqua.stlc.core.exceptions.NotAnEitherException
import com.stefanobevilacqua.stlc.core.exceptions.ParamTypesException
import com.stefanobevilacqua.stlc.core.exceptions.ReturnTypesException
import com.stefanobevilacqua.stlc.core.types.Either
import com.stefanobevilacqua.stlc.core.types.Function
import com.stefanobevilacqua.stlc.core.types.Type
import com.stefanobevilacqua.stlc.core.types.asString

data class Fold(
  val either: Term,
  val ifLeft: Term,
  val ifRight: Term,
): Term() {
  override fun toString() = "$either.fold($ifLeft, $ifRight)"

  override fun evaluateType(ctx: TypeContext): Type {
    val eitherType = either.evaluateType(ctx)
    val ifLeftType = ifLeft.evaluateType(ctx)
    val ifRightType = ifRight.evaluateType(ctx)
    return when (eitherType) {
      is Either -> {
        if (ifLeftType !is Function) throw NotAFunctionException("$ifLeft", "$ifLeftType")
        if (ifRightType !is Function) throw NotAFunctionException("$ifRight", "$ifRightType")
        if (ifLeftType.parameterTypes != listOf(eitherType.left)) throw ParamTypesException("$ifLeft", ifLeftType.parameterTypes.asString(), "${eitherType.left}")
        if (ifRightType.parameterTypes != listOf(eitherType.right)) throw ParamTypesException("$ifRight", ifRightType.parameterTypes.asString(), "${eitherType.right}")
        if (ifLeftType.returnType != ifRightType.returnType) throw ReturnTypesException("$ifLeft", "$ifRight", "${ifLeftType.returnType}", "${ifRightType.returnType}")
        ifLeftType.returnType
      }
      else -> throw NotAnEitherException("$either", "$eitherType")
    }
  }
}
