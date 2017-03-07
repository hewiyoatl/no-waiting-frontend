package controllers

import helpers.Auth0Config
import play.api.mvc.{Action, Controller}

class Application extends Controller {

  def index = Action {
    Ok(views.html.index(Auth0Config.get()))
  }
}
