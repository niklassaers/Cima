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

  object neo extends Neo4JRestService


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
  def create = Auth.APIKey("123") { username => request =>

    var props:Map[String, JsValue] = Map[String, JsValue]()
    request.body.asJson  match {
      case None => {}
      case Some(x) => {
        x match {
          case JsObject(fields) => { props = fields.toMap }
          case _ => {} // Ok("received something else: " + request.body + '\n')
        }
      }
    }

    if(!props.contains("UUID")) {
      props += "UUID" -> toJson(UniqueIdGenerator.uuid)
    }

    if(!props.contains("entity"))
      props += "entity" -> toJson("unset")

    props += "should" -> toJson("appear")

    props += "username" -> toJson(username)

    val (nodeURI: String, data: JsObject) = Http(
      (neo.neoRestNode <<(stringify(toJson(props)), "application/json"))
        <:< Map("Accept" -> "application/json")
        >! {
        jsValue => ((jsValue \ "self").as[String], (jsValue \ "data").as[JsObject])
      })

    var ret = neo.indexNodeURI(nodeURI, "idxEntity", "entity", props.get("entity").get.toString)
    ret = neo.indexNodeURI(nodeURI, "idxUUIDs", "UUID", props.get("UUID").get.toString)

//    val entityNodeURL = neo.findNodeFromIndex("idxEntity", "entity", props.get("entity").get.toString)
//    neo.createRelationshipForURIs(createRelationship, "IS_A", entityNodeURL)

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

  def error = Action {
    Ok("404") // NotFound
//    notFound("This was not what you were expecting")
  }

}
