package com.stefanobevilacqua.stlc.core.terms

import com.stefanobevilacqua.stlc.core.exceptions.*
import com.stefanobevilacqua.stlc.core.types.Base
import com.stefanobevilacqua.stlc.parser.TermVisitor
import com.stefanobevilacqua.stlc.parser.TypeVisitor
import com.stefanobevilacqua.stlc.parser.setup
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class TermTest {

  private val visitor = TermVisitor(TypeVisitor())

  @Test
  internal fun `variable type evaluation`() {
    val input = "a"

    val parser =  setup(input)
    val term = visitor.visit(parser.term())
    val context = mapOf("a" to Base("A"))

    val actual = term.evaluateType(context)

    assertEquals("A", actual.toString())
  }

  @Test
  internal fun `unary function type evaluation`() {
    val input = "fun(a: A) -> a"

    val parser =  setup(input)
    val term = visitor.visit(parser.term())
    val context: TypeContext = emptyMap()

    val actual = term.evaluateType(context)

    assertEquals("(A -> A)", actual.toString())
  }

  @Test
  internal fun `n-ary function type evaluation`() {
    val input = "fun(a: A, b: B) -> a"

    val parser =  setup(input)
    val term = visitor.visit(parser.term())
    val context: TypeContext = emptyMap()

    val actual = term.evaluateType(context)

    assertEquals("((A, B) -> A)", actual.toString())
  }

  @Test
  internal fun `function application type evaluation`() {
    val input = "fun(f: A -> B, a: A) -> f(a)"

    val parser =  setup(input)
    val term = visitor.visit(parser.term())
    val context: TypeContext = emptyMap()

    val actual = term.evaluateType(context)

    assertEquals("(((A -> B), A) -> B)", actual.toString())
  }

  @Test
  internal fun `pair type evaluation`() {
    val input = "fun(a: A, b: B) -> pair(a, b)"

    val parser =  setup(input)
    val term = visitor.visit(parser.term())
    val context: TypeContext = emptyMap()

    val actual = term.evaluateType(context)

    assertEquals("((A, B) -> (A && B))", actual.toString())
  }

  @Test
  internal fun `left of pair type evaluation`() {
    val input = "fun(p: (A && B)) -> p.left"

    val parser =  setup(input)
    val term = visitor.visit(parser.term())
    val context: TypeContext = emptyMap()

    val actual = term.evaluateType(context)

    assertEquals("((A && B) -> A)", actual.toString())
  }

  @Test
  internal fun `right of pair type evaluation`() {
    val input = "fun(p: (A && B)) -> p.right"

    val parser =  setup(input)
    val term = visitor.visit(parser.term())
    val context: TypeContext = emptyMap()

    val actual = term.evaluateType(context)

    assertEquals("((A && B) -> B)", actual.toString())
  }

  @Test
  internal fun `either left type evaluation`() {
    val input = "fun(a: A) -> left(a, B)"

    val parser =  setup(input)
    val term = visitor.visit(parser.term())
    val context: TypeContext = emptyMap()

    val actual = term.evaluateType(context)

    assertEquals("(A -> (A || B))", actual.toString())
  }

  @Test
  internal fun `either right type evaluation`() {
    val input = "fun(b: B) -> right(A, b)"

    val parser =  setup(input)
    val term = visitor.visit(parser.term())
    val context: TypeContext = emptyMap()

    val actual = term.evaluateType(context)

    assertEquals("(B -> (A || B))", actual.toString())
  }

  @Test
  internal fun `fold type evaluation`() {
    val input = "fun(e: (A || B), f: A -> C, g: B -> C) -> e.fold(f, g)"

    val parser =  setup(input)
    val term = visitor.visit(parser.term())
    val context: TypeContext = emptyMap()

    val actual = term.evaluateType(context)

    assertEquals("(((A || B), (A -> C), (B -> C)) -> C)", actual.toString())
  }

  @Test
  internal fun `let type evaluation`() {
    val input = "fun(a: A, b: B) -> let p: (A && B) = pair(a, b); p.left"

    val parser =  setup(input)
    val term = visitor.visit(parser.term())
    val context: TypeContext = emptyMap()

    val actual = term.evaluateType(context)

    assertEquals("((A, B) -> A)", actual.toString())
  }

  // Error test cases

  @Test
  internal fun `error - unknown variable`() {
    val input = "x"

    val parser =  setup(input)
    val term = visitor.visit(parser.term())
    val context: TypeContext = emptyMap()

    val actual: TypeErrorException = assertThrows { term.evaluateType(context) }
    assertEquals(UnknownVariableException("x"), actual)
  }

  @Test
  internal fun `error - application - not a function`() {
    val input = "fun(f: (A && B), x: A) -> f(x)"

    val parser =  setup(input)
    val term = visitor.visit(parser.term())
    val context: TypeContext = emptyMap()

    val actual: TypeErrorException = assertThrows { term.evaluateType(context) }
    assertEquals(NotAFunctionException("f", "(A && B)"), actual)
  }

  @Test
  internal fun `error - application - wrong parameter types`() {
    val input = "fun(f: (A -> B), x: B) -> f(x)"

    val parser =  setup(input)
    val term = visitor.visit(parser.term())
    val context: TypeContext = emptyMap()

    val actual: TypeErrorException = assertThrows { term.evaluateType(context) }
    assertEquals(ParamTypesException("f", "B", "A"), actual)
  }

  @Test
  internal fun `error - pair - left`() {
    val input = "fun(p: (A -> B)) -> p.left"

    val parser =  setup(input)
    val term = visitor.visit(parser.term())
    val context: TypeContext = emptyMap()

    val actual: TypeErrorException = assertThrows { term.evaluateType(context) }
    assertEquals(NotAPairException("p", "(A -> B)"), actual)
  }

  @Test
  internal fun `error - pair - right`() {
    val input = "fun(p: (A -> B)) -> p.right"

    val parser =  setup(input)
    val term = visitor.visit(parser.term())
    val context: TypeContext = emptyMap()

    val actual: TypeErrorException = assertThrows { term.evaluateType(context) }
    assertEquals(NotAPairException("p", "(A -> B)"), actual)
  }

  @Test
  internal fun `error - let - assignment`() {
    val input = "fun(a: A) -> let b: B = a; a"

    val parser =  setup(input)
    val term = visitor.visit(parser.term())
    val context: TypeContext = emptyMap()

    val actual: TypeErrorException = assertThrows { term.evaluateType(context) }
    assertEquals(AssignmentException("b", "B", "a", "A"), actual)
  }

  @Test
  internal fun `error - fold - left is not a function`() {
    val input = "fun(e: (A || B), f: A, g: (B -> C)) -> e.fold(f, g)"

    val parser =  setup(input)
    val term = visitor.visit(parser.term())
    val context: TypeContext = emptyMap()

    val actual: TypeErrorException = assertThrows { term.evaluateType(context) }
    assertEquals(NotAFunctionException("f", "A"), actual)
  }

  @Test
  internal fun `error - fold - right is not a function`() {
    val input = "fun(e: (A || B), f: (A -> B), g: B) -> e.fold(f, g)"

    val parser =  setup(input)
    val term = visitor.visit(parser.term())
    val context: TypeContext = emptyMap()

    val actual: TypeErrorException = assertThrows { term.evaluateType(context) }
    assertEquals(NotAFunctionException("g", "B"), actual)
  }

  @Test
  internal fun `error - fold - wrong left function parameter type`() {
    val input = "fun(e: (A || B), f: (B -> C), g: (B -> C)) -> e.fold(f, g)"

    val parser =  setup(input)
    val term = visitor.visit(parser.term())
    val context: TypeContext = emptyMap()

    val actual: TypeErrorException = assertThrows { term.evaluateType(context) }
    assertEquals(ParamTypesException("f", "B", "A"), actual)
  }

  @Test
  internal fun `error - fold - wrong right function parameter type`() {
    val input = "fun(e: (A || B), f: (A -> C), g: (A -> C)) -> e.fold(f, g)"

    val parser =  setup(input)
    val term = visitor.visit(parser.term())
    val context: TypeContext = emptyMap()

    val actual: TypeErrorException = assertThrows { term.evaluateType(context) }
    assertEquals(ParamTypesException("g", "A", "B"), actual)
  }

  @Test
  internal fun `error - fold - wrong functions return types`() {
    val input = "fun(e: (A || B), f: (A -> C), g: (B -> D)) -> e.fold(f, g)"

    val parser =  setup(input)
    val term = visitor.visit(parser.term())
    val context: TypeContext = emptyMap()

    val actual: TypeErrorException = assertThrows { term.evaluateType(context) }
    assertEquals(ReturnTypesException("f", "g", "C", "D"), actual)
  }

  @Test
  internal fun `error - fold - not an either`() {
    val input = "fun(e: (A && B), f: (A -> C), g: (B -> C)) -> e.fold(f, g)"

    val parser =  setup(input)
    val term = visitor.visit(parser.term())
    val context: TypeContext = emptyMap()

    val actual: TypeErrorException = assertThrows { term.evaluateType(context) }
    assertEquals(NotAnEitherException("e", "(A && B)"), actual)
  }
}