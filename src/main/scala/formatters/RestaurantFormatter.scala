package formatters

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import play.api.libs.functional.syntax._
import play.api.libs.json._

case class RestaurantInbound(id: Option[Long],
                             address1: Option[String],
                             address2: Option[String],
                             zipCode: Option[String],
                             state: Option[String],
                             city: Option[String],
                             country: Option[String],
                             phoneNumber: Option[String],
                             restaurantId: Option[Long],
                             latitude: Option[Float],
                             longitude: Option[Float],
                             createdTimestamp: Option[DateTime])

object RestaurantFormatter {

  val dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"

  val jodaDateWrites: Writes[DateTime] = new Writes[DateTime] {
    def writes(d: DateTime): JsValue = JsString(d.toString())
  }

  val jodaDateReads = Reads[DateTime](js =>
    js.validate[String].map[DateTime](dtString =>
      DateTime.parse(dtString, DateTimeFormat.forPattern(dateFormat))
    )
  )

  val RestaurantWriter: Writes[UserRestInbound] = (
    (JsPath \ "restaurant_id").writeNullable[Long] and
      (JsPath \ "address_1").writeNullable[String] and
      (JsPath \ "address_2").writeNullable[String] and
      (JsPath \ "zip_code").writeNullable[String] and
      (JsPath \ "state").writeNullable[String] and
      (JsPath \ "city").writeNullable[String] and
      (JsPath \ "country").writeNullable[String] and
      (JsPath \ "phone_number").writeNullable[String] and
      (JsPath \ "restaurant_id").writeNullable[Long] and
      (JsPath \ "latitude").writeNullable[Float] and
      (JsPath \ "longitude").writeNullable[Float] and
      (JsPath \ "created_timestamp").writeNullable[DateTime](jodaDateWrites)
    )(unlift(UserRestInbound.unapply))

  //  val RestaurantWriter: Writes[RestaurantOutbound] = (
  //    (JsPath \ "restaurant_id").writeNullable[Long] and
  //      (JsPath \ "address_1").writeNullable[String] and
  //      (JsPath \ "address_2").writeNullable[String] and
  //      (JsPath \ "zip_code").writeNullable[String] and
  //      (JsPath \ "state").writeNullable[String] and
  //      (JsPath \ "city").writeNullable[String] and
  //      (JsPath \ "country").writeNullable[String] and
  //      (JsPath \ "phone_number").writeNullable[String] and
  //      (JsPath \ "latitude").writeNullable[Float] and
  //      (JsPath \ "longitude").writeNullable[Float] and
  //      (JsPath \ "created_timestamp").writeNullable[DateTime](jodaDateWrites)
  //    )(unlift(RestaurantOutbound.unapply))
}

