package controllers

import play.api._
import play.api.data._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.json.Json._
import dispatch._
import utils.dispatch.PlayJsonDispatchHttp._
import utils.neo4j.Neo4JRestService
import utils._
import org.apache.commons.codec.binary.Base64
import models._


object Application extends Controller {
  

  def base64Decode(in: String): Array[Byte] = (new Base64).decode(in.getBytes("UTF-8"))

  /*
   1. Check that apiKey is indeed correct
   2. Check that there is a user with username:password (basic authentication) for that apiKey
   3. Retrieve (create if it does not exist) an Entity node with name of "entity"
     3a. If "entity" is not given, retrieve the "Any" entity-node
   4. Check that there is no other object with given "UUID".
     4a. If "UUID" is not given, create one
     4b. If there is one, send an error
   5. Insert new Node
   6. Create a relationship from Node to Entity called "IS_A"
   7. Update index, called idxUUID, with UUID of entity
   8. Create/Update an index for all fields, named idx[EntityName][FieldName] with their respective value
   9. Add any additional relationships that may have been sent
   10. Return UUID to the newly created node
   */
  def create = Action(parse.json) { request =>
    var props:Map[String, JsValue] = Map[String, JsValue]()
    request.body match {
      case JsObject(fields) => { props = fields.toMap }
      case _ => {} // Ok("received something else: " + request.body + '\n')
    }

    if(!props.contains("UUID"))
      props.+("UUID" -> UniqueIdGenerator.uuid)

    if (!props.contains("entity"))
      props.+("entity" -> "unset")

    props.+("should" -> "appear")

    object neo extends Neo4JRestService

    val (node, data: JsObject) = Http(
      (neo.neoRestNode <<(stringify(toJson(props)), "application/json"))
        <:< Map("Accept" -> "application/json")
        >! {
        jsValue => ((jsValue \ "self").as[String], (jsValue \ "data").as[JsObject])
      })

//    neo.indexMap(props, "idxEntity", "entity", props.get("entity").get.toString)

    val auth = new String(base64Decode(request.headers.get("AUTHORIZATION").get.replaceFirst("Basic", "")))
    val split  = auth.split(":")
    val user = split(0)
    val pass = split(1)

    Ok("Yes - " + props + " / " + data.toString)
  }

  def read(uuid: String) = Action  {
    Ok("404")
  }

  def delete(uuid: String) = Action  {
    Ok("404")
  }

  def edit(uuid: String) = Action(parse.json) { request =>
    var props:Map[String, JsValue] = Map[String, JsValue]()
    request.body match {
      case JsObject(fields) => { props = fields.toMap }
      case _ => {} // Ok("received something else: " + request.body + '\n')
    }

    Ok("404")
  }

  def listEntities = Action {
    Ok("404")
  }

  def allOfEntity(entity: String) = Action {
    Ok("404")
  }

  def filteredEntity(entity: String) = Action(parse.json)  { request =>
    var props:Map[String, JsValue] = Map[String, JsValue]()
    request.body match {
      case JsObject(fields) => { props = fields.toMap }
      case _ => {} // Ok("received something else: " + request.body + '\n')
    }

    Ok("404")
  }


}