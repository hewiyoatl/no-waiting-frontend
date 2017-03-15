package controllers

import javax.inject.Inject

import formatters.{RestaurantFormatter, UserRestInbound}
import play.api.Configuration
import play.api.Play.current
import play.api.cache.Cache
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json._
import play.api.libs.ws.{WSClient, WSResponse}
import play.api.mvc.{Action, Controller, _}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

case class RestaurantData(address1: Option[String],
                          address2: Option[String],
                          zipCode: Option[String],
                          state: Option[String],
                          city: Option[String],
                          country: Option[String],
                          phoneNumber: Option[String],
                          latitude: Option[String],
                          longitude: Option[String])

class RestaurantController @Inject()(config: Configuration, wsClient: WSClient) extends Controller {

  implicit val restWriter = RestaurantFormatter.RestaurantWriter

  val BACKEND_CONTEXT: String = "/noqueue"

  val restaurantForm = Form(
    mapping(
      "address_1" -> optional(text),
      "address_2" -> optional(text),
      "zip_code" -> optional(text),
      "state" -> optional(text),
      "city" -> optional(text),
      "country" -> optional(text),
      "phone_number" -> optional(text),
      "latitude" -> optional(text),
      "longitude" -> optional(text)
    )(RestaurantData.apply)(RestaurantData.unapply)
  )

  def AuthenticatedAction(f: Request[AnyContent] => Result): Action[AnyContent] = {

    Action { request =>
      (request.session.get("idToken").flatMap { idToken =>
        Cache.getAs[JsValue](idToken + "profile")
      } map { profile =>
        f(request)
      }).orElse {
        Some(Redirect(routes.Application.index()))
      }.get
    }
  }

  def index = Action { request =>

    //    val idToken = request.session.get("idToken").get
    //    val profile = Cache.getAs[JsValue](idToken + "profile").get

    Ok(views.html.restaurant(restaurantForm))
  }

  //  def index = AuthenticatedAction { request =>
  //
  //    val idToken = request.session.get("idToken").get
  //    val profile = Cache.getAs[JsValue](idToken + "profile").get
  //
  //    Ok(views.html.restaurant(restaurantForm))
  //  }

  def saveRestaurant = Action.async { implicit request =>

    val backendServiceBaseUrl = config.getString("backend.api.basepath").getOrElse("http://localhost:9000")
    val backendServiceUrl = backendServiceBaseUrl + s"""${BACKEND_CONTEXT}/v1/restaurants"""


    restaurantForm.bindFromRequest.fold(
      formWithErrors => {
        Future(BadRequest(views.html.restaurant(formWithErrors)))
      },
      restaurant => {

        val restaurantOutbound = UserRestInbound(
          None,
          restaurant.address1,
          restaurant.address2,
          restaurant.zipCode,
          restaurant.state,
          restaurant.city,
          restaurant.country,
          restaurant.phoneNumber,
          Some(1),
          restaurant.latitude.map(_.toFloat),
          restaurant.longitude.map(_.toFloat),
          None)

        val futureRestaurant = wsClient.url(backendServiceUrl)
          .post(Json.toJson(restaurantOutbound))

        val joinFutureCalls: Future[WSResponse] = for {
          f1ResultRestaurant <- futureRestaurant
        } yield f1ResultRestaurant

        joinFutureCalls.map {
          futureCalls => {

            Redirect(routes.Application.index())
          }
        }.recover {
          case e: Exception => BadRequest("")
        }
      }

    )
  }

}
