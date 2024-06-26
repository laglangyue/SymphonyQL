package symphony
package parser

import scala.util.*
import org.parboiled2.*
import org.parboiled2.Rule.*
import symphony.parser.*
import symphony.parser.SymphonyQLError.ParsingError
import symphony.parser.adt.*
import symphony.parser.parsers.*

object SymphonyQLParser {

  private def documentParser(input: ParserInput): DefinitionParser = new DefinitionParser(input)

  // ========================================Parser API===================================================================
  def parseQuery(query: String): Either[ParsingError, Document] = {
    val input        = ParserInput(query)
    val sourceMapper = SourceMapper(query)
    val parser       = SymphonyQLParser.documentParser(input)
    parser.document.run() match
      case Failure(parseError: ParseError) =>
        val str = parseError.format(parser)
        Left(ParsingError(str, Some(sourceMapper.getLocation(parseError.position))))
      case Failure(exception)              =>
        Left(ParsingError(s"Query parsing error", innerThrowable = Some(exception)))
      case Success(value)                  => Right(Document(value.definitions, SourceMapper(query)))
  }

  def check(query: String): Option[String] = {
    val input  = ParserInput(query)
    val parser = SymphonyQLParser.documentParser(input)
    parser.document.run() match
      case Failure(parseError: ParseError) => Some(parseError.format(parser))
      case Failure(exception)              => Some(exception.getMessage)
      case Success(_)                      => None
  }

  def parseInputValue(query: String): Either[ParsingError, SymphonyQLInputValue] = {
    val input        = ParserInput(query)
    val parser       = SymphonyQLParser.documentParser(input)
    val sourceMapper = SourceMapper(query)
    parser.value.run() match
      case Failure(parseError: ParseError) =>
        val str = parseError.format(parser)
        Left(ParsingError(str, Some(sourceMapper.getLocation(parseError.position))))
      case Failure(exception)              => Left(ParsingError(s"InputValue parsing error", innerThrowable = Some(exception)))
      case Success(value)                  => Right(value)
  }
}
