package com.stefanobevilacqua.stlc.parser

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class TypeVisitorTest {

  private val visitor = TypeVisitor()

  @Test
  internal fun `valid type`() {
    val input = "((A -> B), (W || X)) -> (Y && Z)"
    val parser = setup(input)

    val actual = visitor.visit(parser.type())

    assertEquals("($input)", actual.toString())
  }

  @Test
  internal fun `invalid type`() {
    val parser = setup("A && || B")

    parser.type()

    assertEquals(1, errorListener.errors)
  }
}

