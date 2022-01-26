package com.stefanobevilacqua.stlc.core.terms

import com.stefanobevilacqua.stlc.core.exceptions.*
import com.stefanobevilacqua.stlc.core.types.Base
import com.stefanobevilacqua.stlc.parser.TermVisitor
import com.stefanobevilacqua.stlc.parser.TypeVisitor
import com.stefanobevilacqua.stlc.parser.setup
import com.stefanobevilacqua.stlc.parser.typeCheck
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class TermTest {

  private val visitor = TermVisitor(TypeVisitor())

  @Test
  internal fun `variable type evaluation`() {
    val input = "a"
    val context = mapOf("a" to Base("A"))

    val actual = typeCheck(input, context)

    assertEquals("A", actual.toString())
  }

  @Test
  internal fun `unary function type evaluation`() {
    val input = "fun(a: A) -> a"

    val actual = typeCheck(input)

    assertEquals("(A -> A)", actual.toString())
  }

  @Test
  internal fun `n-ary function type evaluation`() {
    val input = "fun(a: A, b: B) -> a"

    val actual = typeCheck(input)

    assertEquals("((A, B) -> A)", actual.toString())
  }

  @Test
  internal fun `function application type evaluation`() {
    val input = "fun(f: A -> B, a: A) -> f(a)"

    val actual = typeCheck(input)

    assertEquals("(((A -> B), A) -> B)", actual.toString())
  }

  @Test
  internal fun `pair type evaluation`() {
    val input = "fun(a: A, b: B) -> pair(a, b)"

    val actual = typeCheck(input)

    assertEquals("((A, B) -> (A && B))", actual.toString())
  }

  @Test
  internal fun `left of pair type evaluation`() {
    val input = "fun(p: (A && B)) -> p.left"

    val actual = typeCheck(input)

    assertEquals("((A && B) -> A)", actual.toString())
  }

  @Test
  internal fun `right of pair type evaluation`() {
    val input = "fun(p: (A && B)) -> p.right"

    val actual = typeCheck(input)

    assertEquals("((A && B) -> B)", actual.toString())
  }

  @Test
  internal fun `either left type evaluation`() {
    val input = "fun(a: A) -> left(a, B)"

    val actual = typeCheck(input)

    assertEquals("(A -> (A || B))", actual.toString())
  }

  @Test
  internal fun `either right type evaluation`() {
    val input = "fun(b: B) -> right(A, b)"

    val actual = typeCheck(input)

    assertEquals("(B -> (A || B))", actual.toString())
  }

  @Test
  internal fun `fold type evaluation`() {
    val input = "fun(e: (A || B), f: A -> C, g: B -> C) -> e.fold(f, g)"

    val actual = typeCheck(input)

    assertEquals("(((A || B), (A -> C), (B -> C)) -> C)", actual.toString())
  }

  @Test
  internal fun `let type evaluation`() {
    val input = "fun(a: A, b: B) -> let p: (A && B) = pair(a, b); p.left"

    val actual = typeCheck(input)

    assertEquals("((A, B) -> A)", actual.toString())
  }

  // Error test cases

  @Test
  internal fun `error - unknown variable`() {
    val input = "x"

    val actual: TypeErrorException = assertThrows { typeCheck(input) }
    assertEquals(UnknownVariableException("x"), actual)
  }

  @Test
  internal fun `error - application - not a function`() {
    val input = "fun(f: (A && B), x: A) -> f(x)"

    val actual: TypeErrorException = assertThrows { typeCheck(input) }
    assertEquals(NotAFunctionException("f", "(A && B)"), actual)
  }

  @Test
  internal fun `error - application - wrong parameter types`() {
    val input = "fun(f: (A -> B), x: B) -> f(x)"

    val actual: TypeErrorException = assertThrows { typeCheck(input) }
    assertEquals(ParamTypesException("f", "B", "A"), actual)
  }

  @Test
  internal fun `error - pair - left`() {
    val input = "fun(p: (A -> B)) -> p.left"

    val actual: TypeErrorException = assertThrows { typeCheck(input) }
    assertEquals(NotAPairException("p", "(A -> B)"), actual)
  }

  @Test
  internal fun `error - pair - right`() {
    val input = "fun(p: (A -> B)) -> p.right"

    val actual: TypeErrorException = assertThrows { typeCheck(input) }
    assertEquals(NotAPairException("p", "(A -> B)"), actual)
  }

  @Test
  internal fun `error - let - assignment`() {
    val input = "fun(a: A) -> let b: B = a; a"

    val actual: TypeErrorException = assertThrows { typeCheck(input) }
    assertEquals(AssignmentException("b", "B", "a", "A"), actual)
  }

  @Test
  internal fun `error - fold - left is not a function`() {
    val input = "fun(e: (A || B), f: A, g: (B -> C)) -> e.fold(f, g)"

    val actual: TypeErrorException = assertThrows { typeCheck(input) }
    assertEquals(NotAFunctionException("f", "A"), actual)
  }

  @Test
  internal fun `error - fold - right is not a function`() {
    val input = "fun(e: (A || B), f: (A -> B), g: B) -> e.fold(f, g)"

    val actual: TypeErrorException = assertThrows { typeCheck(input) }
    assertEquals(NotAFunctionException("g", "B"), actual)
  }

  @Test
  internal fun `error - fold - wrong left function parameter type`() {
    val input = "fun(e: (A || B), f: (B -> C), g: (B -> C)) -> e.fold(f, g)"

    val actual: TypeErrorException = assertThrows { typeCheck(input) }
    assertEquals(ParamTypesException("f", "B", "A"), actual)
  }

  @Test
  internal fun `error - fold - wrong right function parameter type`() {
    val input = "fun(e: (A || B), f: (A -> C), g: (A -> C)) -> e.fold(f, g)"

    val actual: TypeErrorException = assertThrows { typeCheck(input) }
    assertEquals(ParamTypesException("g", "A", "B"), actual)
  }

  @Test
  internal fun `error - fold - wrong functions return types`() {
    val input = "fun(e: (A || B), f: (A -> C), g: (B -> D)) -> e.fold(f, g)"

    val actual: TypeErrorException = assertThrows { typeCheck(input) }
    assertEquals(ReturnTypesException("f", "g", "C", "D"), actual)
  }

  @Test
  internal fun `error - fold - not an either`() {
    val input = "fun(e: (A && B), f: (A -> C), g: (B -> C)) -> e.fold(f, g)"

    val actual: TypeErrorException = assertThrows { typeCheck(input) }
    assertEquals(NotAnEitherException("e", "(A && B)"), actual)
  }
}