package com.stefanobevilacqua.stlc.parser

import com.stefanobevilacqua.stlc.core.types.*
import com.stefanobevilacqua.stlc.core.types.Function

class TypeVisitor: StlcBaseVisitor<Type>() {

  override fun visitBaseType(ctx: StlcParser.BaseTypeContext): Type =
    Base(ctx.TYPENAME().text)

  override fun visitGroupType(ctx: StlcParser.GroupTypeContext): Type =
    visit(ctx.type())

  override fun visitPairType(ctx: StlcParser.PairTypeContext): Type {
    val (left, right) = ctx.type().map { visit(it) }
    return Pair(left, right)
  }

  override fun visitEitherType(ctx: StlcParser.EitherTypeContext): Type {
    val (left, right) = ctx.type().map { visit(it) }
    return Either(left, right)
  }

  override fun visitUnaryFuncType(ctx: StlcParser.UnaryFuncTypeContext): Type {
    val (paramType, returnType) = ctx.type().map { visit(it) }
    return Function(listOf(paramType), returnType)
  }

  override fun visitNaryFuncType(ctx: StlcParser.NaryFuncTypeContext): Type {
    val inTypes = ctx.typelist().type().map { visit(it) }
    val outType = visit(ctx.type())
    return Function(inTypes, outType)
  }
}