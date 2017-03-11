package com.bbc.countMeUp.dao.impl

import java.util.UUID

import com.bbc.countMeUp.model.{Candidate, Election, User, Vote}

import scala.collection.mutable

object DataStorage {
  var candidates: collection.mutable.Map[UUID, Candidate] = new mutable.HashMap[UUID, Candidate]
  var elections: collection.mutable.Map[UUID, Election] = new mutable.HashMap[UUID, Election]
  var users: collection.mutable.Map[UUID, User] = new mutable.HashMap[UUID, User]
  var votes: collection.mutable.Map[UUID, Vote] = new mutable.HashMap[UUID, Vote]
}
