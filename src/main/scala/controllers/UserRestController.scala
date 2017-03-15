package controllers

import javax.inject.Inject

import formatters.{UserRestFormatter, RestaurantFormatter, UserRestInbound}
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

case class UserRestData(locationId: Option[Long],
                        firstName: Option[String],
                        lastName: Option[String],
                        phoneNumber: Option[String],
                        userName: Option[String],
                        password: Option[String])

class UserRestController @Inject()(config: Configuration, wsClient: WSClient) extends Controller {

  implicit val restUserWriter = UserRestFormatter.RestaurantWriter

  val BACKEND_CONTEXT: String = "/noqueue"

  val userRestForm = Form(
    mapping(
      "location_id" -> optional(longNumber),
      "first_name" -> optional(text),
      "last_name" -> optional(text),
      "phone_number" -> optional(text),
      "user_name" -> optional(text),
      "password" -> optional(text)
    )(UserRestData.apply)(UserRestData.unapply)
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

    Ok(views.html.rest_user(userRestForm))
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


    userRestForm.bindFromRequest.fold(
      formWithErrors => {
        Future(BadRequest(views.html.rest_user(formWithErrors)))
      },
      restaurant => {

        val restaurantOutbound = null
        //        val restaurantOutbound = UserRestInbound(
        //          None,
        //          restaurant.address1,
        //          restaurant.address2,
        //          restaurant.zipCode,
        //          restaurant.state,
        //          restaurant.city,
        //          restaurant.country,
        //          restaurant.phoneNumber,
        //          Some(1),
        //          restaurant.latitude.map(_.toFloat),
        //          restaurant.longitude.map(_.toFloat),
        //          None)

        val futureRestaurant = wsClient.url(backendServiceUrl)
          .post("")

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
