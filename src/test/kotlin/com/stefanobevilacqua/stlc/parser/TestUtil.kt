package com.stefanobevilacqua.stlc.parser

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

data class TestErrorListener(var errors: Int): BaseErrorListener() {
  override fun syntaxError(
    recognizer: Recognizer<*, *>?,
    offendingSymbol: Any?,
    line: Int,
    charPositionInLine: Int,
    msg: String?,
    e: RecognitionException
  ) {
    errors++
    super.syntaxError(recognizer, offendingSymbol, line, charPositionInLine, msg, e)
    throw e
  }
}