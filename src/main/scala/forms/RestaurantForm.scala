package forms

import play.api.data.Form
import play.api.data.Forms._


case class RestaurantData(address1: Option[String],
                          address2: Option[String],
                          zipCode: Option[String],
                          state: Option[String],
                          city: Option[String],
                          country: Option[String],
                          phoneNumber: Option[String],
                          latitude: Option[Long],
                          longitude: Option[Long])

class RestaurantForm {

  val restaurantForm = Form(
    mapping(
      "address_1" -> optional(text),
      "address_2" -> optional(text),
      "zip_code" -> optional(text),
      "state" -> optional(text),
      "city" -> optional(text),
      "country" -> optional(text),
      "phone_number" -> optional(text),
      "latitude" -> optional(longNumber),
      "longitude" -> optional(longNumber)
    )(RestaurantData.apply)(RestaurantData.unapply)
  )


}
