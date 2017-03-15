package controllers

import play.api.Play.current
import play.api.cache.Cache
import play.api.libs.json._
import play.api.mvc.{Action, Controller, _}

class User extends Controller {

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

  def index = AuthenticatedAction { request =>

    val idToken = request.session.get("idToken").get
    val profile = Cache.getAs[JsValue](idToken + "profile").get
    Ok(views.html.user(Json.stringify(profile), profile))
  }
}
