package com.bbc.countMeUp.dao.impl

import java.util.UUID

import com.bbc.countMeUp.dao.ElectionDao
import com.bbc.countMeUp.model.Election

import scala.collection.mutable

trait InMemoryElectionDao extends ElectionDao{
  override def electionDao = new InMemElectionDao

  var elections: collection.mutable.Map[UUID, Election] = new mutable.HashMap[UUID, Election]

  class InMemElectionDao extends ElectionDao {
    override def create(model: Election): UUID = {
      elections.put(model.id, model) match {
        case None => model.id
        case _ => throw new Exception
      }
    }

    override def read(id: UUID): Option[Election] = {
      elections.get(id)
    }

    override def update(model: Election): Election = {
      elections.put(model.id, model) match {
        case None => throw new Exception
        case e: Some[Election] => e.get
      }
    }

    override def delete(id: UUID): Unit = {
      elections.remove(id)
    }
  }

}
