package utils

/**
 * Created with IntelliJ IDEA.
 * User: niklas
 * Date: 4/4/12
 * Time: 6:57 PM
 * To change this template use File | Settings | File Templates.
 */


import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue
import java.util.UUID
import scala.actors._
import scala.actors.Actor._
import scala.collection.mutable.Queue

object UniqueIdGenerator{
  val uuids  = new Queue[UUID] //new LinkedBlockingQueue[UUID]()

  populate();

  def getId: UUID = {
    //val id = uuids.take()
    val id = uuids.dequeue
    //repoulate if empty
    if(uuids.size==0){
      populate()
    }
    return id
  }

  def uuid: String = {
    return UniqueIdGenerator.getId.toString
  }

  def populate() = {
    println("populating...")
    for( i <- 1 to 1000){
      //uuids.put(UUID.randomUUID())//
      uuids.enqueue(UUID.randomUUID())
    }
  }
}
