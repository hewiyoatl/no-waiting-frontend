package utilities

import com.aspose.words.SaveFormat
import com.kenshoo.play.metrics.Metrics
import com.zaxxer.hikari.HikariDataSource
import play.api.db.Database
import play.api.i18n.{MessagesApi, Lang}
import play.api.mvc._

object Util {

  /**
   * This variable is used to check the request param
   */
  val PRETTY = "pretty"

  val CALLER_INFO: String = "caller-info"

  val DOC_TEMPLATE_CONTEXT: String = "/docs"

  val DOC_INTERVIEW_CONTEXT: String = "/doc-ins"

  val DOC_HTML_GENERATOR_CONTEXT: String = "/doc-html"

  /**
   * Empty json
   */
  val EMPTY_JSON: String = "{}"

  /**
   * Utility method to validate that all the characters of a string are digits.
   *
   * @param value String to validate.
   * @return True if all characters are digits, false otherwise.
   */
  def isDigit(value: String): Boolean = value forall Character.isDigit

  /**
   * Related issues: https://groups.google.com/forum/#!topic/swagger-swaggersocket/TD0MWY09ESo
   * seems play filters still is in the works
   *
   * @param result
   * @return
   */
  def enableCors(result: Result) = result.withHeaders(
    "Access-Control-Allow-Origin" -> "*",
    "Access-Control-Allow-Methods" -> "PATCH, OPTIONS, GET, POST, PUT, DELETE, HEAD",
    "Access-Control-Allow-Headers" -> "Referrer, User-Agent, Cache-Control, Pragma, Date, Authorization, api_key, Accept, Content-Type, Origin, X-Json, X-Prototype-Version, X-Requested-With",
    "Access-Control-Allow-Credentials" -> "true",
    "Access-Control-Expose-Headers" -> "WWW-Authenticate, Server-Authorization, Location"
  )

  def initializeDBMetrics(db: Database, metrics: Metrics): Unit = {
    val dataSource : HikariDataSource = db.dataSource.asInstanceOf[HikariDataSource]
    if (dataSource.getMetricRegistry() == null) {
      dataSource.setMetricRegistry(metrics.defaultRegistry)
    }
  }

  def languageSupport(messagesApi: MessagesApi,
                      messageCode: String, param1: String)
                     (implicit request: RequestHeader): String = {
    val language: String =  request.acceptLanguages.map(_.code).headOption.getOrElse("en")
    messagesApi(messageCode, param1)(Lang(language))
  }

  def lines = scala.io.Source.fromFile("/opt/resources/play.properties").getLines().mkString

  def identityHeaderHardCode(implicit request: RequestHeader): Option[String] = {
    val apiHeader: String = s"""{"user":{"id":null,"accountMemberships":[]},"apiClient":null}"""
    val user: Option[String] = Some(apiHeader)
    user
  }

  def identityHeader(implicit request: RequestHeader): Option[String] = {
    val user: Option[String] = request.headers.get(CALLER_INFO)
    user
  }

  case class URLParts(urlProtocol: String, urlHost: String, urlPort: Int, urlPath: String)

  def getUrlParts(url: String): URLParts = {
    val urlExtract = new java.net.URL(url)
    URLParts(urlExtract.getProtocol, urlExtract.getHost, urlExtract.getPort, urlExtract.getPath)
  }

  /**
   * Expecting this string: path = /fr/fr/default.rl
   * Sending back this string: /fr/fr/interview#/{interview uuid}
   * @param path
   * @return
   */
  def removeRLPart(countryCode: String,
                   languageCode: String,
                   path: String,
                   interviewUuid: String,
                   templateUuid: String,
                   documentUuid: String): String = {
    val countryCodeLowerCase: String = countryCode.toLowerCase
    val languageCodeLowerCase: String = languageCode.toLowerCase
    val list: List[String] = path.split("/").toList
    if (list.size >= 2) {
      val removeEmptyString =
        if (list.headOption.getOrElse("") == "") {
          list.drop(1)
        } else {
          list
        }
      val param1: String = removeEmptyString.headOption.getOrElse(countryCodeLowerCase)
      val param2: String = removeEmptyString.tail.headOption.getOrElse(languageCodeLowerCase)
      s"""/$param1/$param2/interview/#/$interviewUuid?id=$templateUuid&document=$documentUuid"""
    } else s"""/$countryCodeLowerCase/$languageCodeLowerCase/interview/#/$interviewUuid?id=$templateUuid&document=$documentUuid"""
  }

  val supportedFormats = Map("pdf" -> SaveFormat.PDF, "doc" -> SaveFormat.DOC, "docx" -> SaveFormat.DOCX,
    "rtf" -> SaveFormat.RTF, "text" -> SaveFormat.TEXT, "png" -> SaveFormat.PNG, "jpeg" -> SaveFormat.JPEG)

  val supportedContentType = Map(SaveFormat.PDF -> "application/pdf", SaveFormat.DOC -> "application/msword",
    SaveFormat.DOCX -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
    SaveFormat.RTF -> "application/rtf", SaveFormat.TEXT -> "text/plain", SaveFormat.PNG -> "image/png",
    SaveFormat.JPEG -> "image/jpeg")
}
