package models

/**
 * Created with IntelliJ IDEA.
 * User: niklas
 * Date: 4/5/12
 * Time: 12:09 AM
 */

case class AuthUser (user: String) {

  def find(param: Any) = {
      new AuthUser("Joe")
  }
}
