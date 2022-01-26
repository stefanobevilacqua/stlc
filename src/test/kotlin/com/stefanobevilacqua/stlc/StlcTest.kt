package com.stefanobevilacqua.stlc

import com.stefanobevilacqua.stlc.parser.typeCheck
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class StlcTest {

  @Test
  internal fun syllogism() {
    // If (A -> B) and (B -> C) then (A -> C)
    val expected = "(((A -> B) && (B -> C)) -> (A -> C))"

    val term = """
      fun (p: (A -> B) && (B -> C)) -> {
        let result: A -> C = fun (a: A) -> {
          let f: A -> B = p.left;
          let g: B -> C = p.right;
          let b: B = f(a);
          let c: C = g(b);
          c
        };
        result
      }
    """.trimIndent()

    val actual = typeCheck(term)

    assertEquals(expected, actual.toString())
  }

  @Test
  internal fun disjunction() {
    // If (W -> X) and (Y -> Z) then (W || Y) -> (X || Z)
    val expected = "(((W -> X), (Y -> Z)) -> ((W || Y) -> (X || Z)))"

    val term = """
      fun(wx: W -> X, yz: Y -> Z) -> {
        let l: W -> (X || Z) = fun(w: W) -> {
          let x: X = wx(w);
          left(x, Z)
        };
        let r: Y -> (X || Z) = fun(y: Y) -> {
          let z: Z = yz(y);
          right(X, z)
        };
        let result: (W || Y) -> (X || Z) = fun(wy: W || Y) -> wy.fold(l, r);
        result
    }
    """.trimIndent()

    val actual = typeCheck(term)

    assertEquals(expected, actual.toString())
  }

  @Test
  internal fun distributivity() {
    val expected = "((A || (B && C)) -> ((A || B) && (A || C)))"

    val term = """
      fun(h: A || (B && C)) -> {
        let t0: A -> (A || B) && (A || C) = fun(a: A) -> {
          let ab: A || B = left(a, B);
          let ac: A || C = left(a, C);
          let abac: (A || B) && (A || C) = pair(ab, ac);
          abac
        };
        let t1: B && C -> (A || B) && (A || C) = fun(bc: B && C) -> {
          let b: B = bc.left;
          let c: C = bc.right;
          let ab: A || B = right(A, b);
          let ac: A || C = right(A, c);
          let abac: (A || B) && (A || C) = pair(ab, ac);
          abac
        };
        let result: (A || B) && (A || C) = h.fold(t0, t1);
        result
      }
    """.trimIndent()

    val actual = typeCheck(term)

    assertEquals(expected, actual.toString())
  }
}