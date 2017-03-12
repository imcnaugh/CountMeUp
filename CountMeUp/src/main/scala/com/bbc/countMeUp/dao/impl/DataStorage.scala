package com.bbc.countMeUp.dao.impl

import org.mongodb.scala.{Document, MongoClient, MongoCollection, MongoDatabase}

object DataStorage {

  val mongoClient: MongoClient = MongoClient()
  val database: MongoDatabase = mongoClient.getDatabase("bbc")
  val collection: MongoCollection[Document] = database.getCollection[Document]("candidates")

  def getCollection(collectionName: String): MongoCollection[Document] = {
    database.getCollection[Document(collectionName)
  }
}
