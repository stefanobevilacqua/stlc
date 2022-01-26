package com.stefanobevilacqua.stlc.core.terms

import com.stefanobevilacqua.stlc.core.types.Type

typealias TypeContext = Map<String, Type>

abstract class Term {
  abstract fun evaluateType(ctx: TypeContext): Type
}