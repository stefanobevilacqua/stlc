package com.stefanobevilacqua.stlc.parser

import com.stefanobevilacqua.stlc.core.terms.*

data class TermVisitor(
  private val typeVisitor: TypeVisitor,
): StlcBaseVisitor<Term>() {
  override fun visitVariableTerm(ctx: StlcParser.VariableTermContext): Term =
    Variable(ctx.VALUENAME().text)

  override fun visitLambdaTerm(ctx: StlcParser.LambdaTermContext): Term {
    val paramNames = ctx.funcParams().VALUENAME().map { it.text }
    val paramTypes = ctx.funcParams().type().map { typeVisitor.visit(it) }
    val paramList = paramNames.zip(paramTypes).map {
      Lambda.Parameter(it.first, it.second)
    }
    return Lambda(paramList, visit(ctx.funcBody().term()))
  }

  override fun visitLetTerm(ctx: StlcParser.LetTermContext): Term {
    val (value, next) = ctx.term().map { visit(it) }
    val type = typeVisitor.visit(ctx.type())
    return Let(ctx.VALUENAME().text, type, value, next)
  }

  override fun visitApplicationTerm(ctx: StlcParser.ApplicationTermContext): Term {
    val function = visit(ctx.term())
    val params = ctx.paramList().term().map { visit(it) }
    return Application(function, params)
  }

  override fun visitPairTerm(ctx: StlcParser.PairTermContext): Term {
    val (left, right) = ctx.term().map { visit(it) }
    return Pair(left, right)
  }

  override fun visitFirstTerm(ctx: StlcParser.FirstTermContext): Term =
    LeftOfPair(visit(ctx.term()))

  override fun visitSecondTerm(ctx: StlcParser.SecondTermContext): Term =
    RightOfPair(visit(ctx.term()))

  override fun visitLeftTerm(ctx: StlcParser.LeftTermContext): Term =
    EitherLeft(visit(ctx.term()), typeVisitor.visit(ctx.type()))

  override fun visitRightTerm(ctx: StlcParser.RightTermContext): Term =
    EitherRight(typeVisitor.visit(ctx.type()), visit(ctx.term()))

  override fun visitFoldTerm(ctx: StlcParser.FoldTermContext): Term {
    val (either, ifLeft, ifRight) = ctx.term().map { visit(it) }
    return Fold(either, ifLeft, ifRight)
  }
}