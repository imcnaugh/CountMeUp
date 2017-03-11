package com.bbc.countMeUp.dao.impl

import java.util.UUID

import com.bbc.countMeUp.dao.ElectionDao
import com.bbc.countMeUp.exception.{EntityAlreadyExistsException, EntityDoesNotExistException}
import com.bbc.countMeUp.model.Election

trait InMemoryElectionDao extends ElectionDao{
  override def electionDao = new InMemElectionDao

  var elections = DataStorage.elections

  class InMemElectionDao extends ElectionDao {
    override def create(model: Election): UUID = {
      elections.put(model.id, model) match {
        case None => model.id
        case _ => throw new EntityAlreadyExistsException(model)
      }
    }

    override def read(id: UUID): Option[Election] = {
      elections.get(id)
    }

    override def update(model: Election): Election = {
      elections.put(model.id, model) match {
        case None => throw new EntityDoesNotExistException(model.id)
        case e: Some[Election] => e.get
      }
    }

    override def delete(id: UUID): Unit = {
      elections.remove(id)
    }
  }

}
