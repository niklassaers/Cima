package controllers

import play.api._
import play.api.data._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.json.Json._
import play.api.data.Forms._
import play.api.mvc.BodyParsers._

import utils.dispatch.PlayJsonDispatchHttp._
import utils.neo4j.Neo4JRestService
import org.apache.commons.codec.binary.Base64

import models._
import utils._


/*
case class AuthenticatedRequest[A](
  val user: AuthUser, request: Request[A]
) extends WrappedRequest(request) {

def Authenticated[A](p: BodyParser[A])(f: AuthenticatedRequest[A] => Result) = {
  Action(p) { request =>
    request.session.get("user").flatMap(u => AuthUser.find(u)).map { user =>
      f(AuthenticatedRequest(user, request))
    }.getOrElse(Unauthorized)
  }
}

def Authenticated(f: AuthenticatedRequest[AnyContent] => Result): Action[AnyContent]  = {
  Authenticated(parse.anyContent)(f)
}

}
*/

object Auth  {

  /*
  def Authenticated[A](action: AuthUser => Action[A]): Action[A] = {

    // Let's define an helper function to retrieve a User
    def getUser(request: RequestHeader): Option[AuthUser] = {
      Option(new AuthUser("Joe"))
    }

    // Wrap the original BodyParser with authentication
    val authenticatedBodyParser = parse.using { request =>
      getUser(request).map(u => action(u).parser).getOrElse {
        parse.error(Unauthorized)
      }
    }

    // Now let's define the new Action
    Action(authenticatedBodyParser) { request =>
      getUser(request).map(u => action(u)(request)).getOrElse {
        Unauthorized
      }
    }

  }
  */

  def base64Decode(in: String): Array[Byte] = (new Base64).decode(in.getBytes("UTF-8"))

  //  trait Secured {
    def IsAuthenticated(block: => String => Request[AnyContent] => Result) = {
      Security.Authenticated(RetrieveUser, HandleUnauthorized) { user =>
        Action { request =>
          block(user)(request)
        }
      }
    }

    def RetrieveUser(request: RequestHeader) = {

      val auth = new String(base64Decode(request.headers.get("AUTHORIZATION").get.replaceFirst("Basic", "")))
      val split  = auth.split(":")
      val user = split(0)
      val pass = split(1)
      Option(user)
    }

    def HandleUnauthorized(request: RequestHeader) = {
      Results.Forbidden
    }

    /**
     * Check if the connected user is a member of this project.
     */
    def IsMemberOf(project: Long)(f: => String => Request[AnyContent] => Result) = IsAuthenticated { user => request =>
//      if(Project.isMember(project, user)) {
        f(user)(request)
/*      } else {
        Results.Forbidden
      }*/
    }

    /**
     * Check if the connected user is a owner of this task.
     */
    def IsOwnerOf(task: Long)(f: => String => Request[AnyContent] => Result) = IsAuthenticated { user => request =>
//      if(Task.isOwner(task, user)) {
        f(user)(request)
/*      } else {
        Results.Forbidden
      }*/
    }

    /**
     * Check that the API key is correct
     */
    def APIKey(apiKey: String)(f: => String => Request[AnyContent] => Result) = IsAuthenticated { user => request =>

      if(apiKey == "123")
        f(user)(request)
      else
        Results.Forbidden
    }

//    }
}