package com.stefanobevilacqua.stlc.parser

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class TermVisitorTest {

  private val visitor = TermVisitor(TypeVisitor())

  @Test
  internal fun `valid unary lambda`() {
    val input = "fun(a: A) -> { a }"

    val parser = setup(input)
    val actual = visitor.visit(parser.term())

    assertEquals(input, actual.toString())
  }

  @Test
  internal fun `valid n-ary lambda`() {
    val input = "fun(a: A, b: B) -> { a }"

    val parser = setup(input)
    val actual = visitor.visit(parser.term())

    assertEquals(input, actual.toString())
  }

  @Test
  internal fun `valid function application`() {
    val input = "f(g(a), h(b))"

    val parser = setup(input)
    val actual = visitor.visit(parser.term())

    assertEquals(input, actual.toString())
  }

  @Test
  internal fun `valid pair`() {
    val input = "pair(x, y)"

    val parser = setup(input)
    val actual = visitor.visit(parser.term())

    assertEquals(input, actual.toString())
  }

  @Test
  internal fun `valid left of pair`() {
    val input = "pair(x, y).left"

    val parser = setup(input)
    val actual = visitor.visit(parser.term())

    assertEquals(input, actual.toString())
  }

  @Test
  internal fun `valid right of pair`() {
    val input = "pair(x, y).right"

    val parser = setup(input)
    val actual = visitor.visit(parser.term())

    assertEquals(input, actual.toString())
  }

  @Test
  internal fun `valid either left`() {
    val input = "left(a, B)"

    val parser = setup(input)
    val actual = visitor.visit(parser.term())

    assertEquals(input, actual.toString())
  }

  @Test
  internal fun `valid either right`() {
    val input = "right(A, b)"

    val parser = setup(input)
    val actual = visitor.visit(parser.term())

    assertEquals(input, actual.toString())
  }

  @Test
  internal fun `valid fold`() {
    val input = "left(a, B).fold(f, g)"

    val parser = setup(input)
    val actual = visitor.visit(parser.term())

    assertEquals(input, actual.toString())
  }

  @Test
  internal fun `valid let`() {
    val input = "let x: (A && B) = pair(a, b); x"

    val parser = setup(input)
    val actual = visitor.visit(parser.term())

    assertEquals(input, actual.toString())
  }

  @Test
  internal fun `invalid term`() {
    val parser = setup("fun(a: A)")

    parser.term()

    assertEquals(1, errorListener.errors)
  }
}