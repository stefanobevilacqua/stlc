package com.stefanobevilacqua.stlc.parser

import com.stefanobevilacqua.stlc.core.types.Type
import org.antlr.v4.runtime.*

val errorListener = TestErrorListener(0)

fun setup(input: String): StlcParser {
  val stream = CharStreams.fromString(input)
  val lexer = StlcLexer(stream)
  val parser = StlcParser(CommonTokenStream(lexer))
  parser.removeErrorListeners()
  parser.addErrorListener(errorListener)
  return parser
}

fun typeCheck(term: String, typeContext: Map<String, Type> = emptyMap() ): Type {
  val parser = setup(term)
  val visitor = TermVisitor(TypeVisitor())
  return visitor.visit(parser.term()).evaluateType(typeContext)
}

data class TestErrorListener(var errors: Int): BaseErrorListener() {
  override fun syntaxError(
    recognizer: Recognizer<*, *>?,
    offendingSymbol: Any?,
    line: Int,
    charPositionInLine: Int,
    msg: String?,
    e: RecognitionException?
  ) {
    errors++
    super.syntaxError(recognizer, offendingSymbol, line, charPositionInLine, msg, e)
  }
}