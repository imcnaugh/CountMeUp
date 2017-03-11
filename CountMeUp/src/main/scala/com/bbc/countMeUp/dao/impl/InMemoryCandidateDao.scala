package com.bbc.countMeUp.dao.impl

import java.util.UUID

import com.bbc.countMeUp.dao.CandidateDao
import com.bbc.countMeUp.exception.{CreateException, EntityDoesNotExistException}
import com.bbc.countMeUp.model.Candidate

trait InMemoryCandidateDao extends CandidateDao{
  override def candidateDao = new InMemCandidateDao

  var candidates = DataStorage.candidates

  class InMemCandidateDao extends CandidateDao {
    override def create(model: Candidate): UUID = {
      candidates.put(model.id, model) match {
        case None => model.id
        case _ => throw new CreateException(model)
      }
    }

    override def read(id: UUID): Option[Candidate] = {
      candidates.get(id)
    }

    override def update(model: Candidate): Candidate = {
      candidates.put(model.id, model) match {
        case None => throw new EntityDoesNotExistException(model)
        case e: Some[Candidate] => e.get
      }
    }

    override def delete(id: UUID): Unit = {
      candidates.remove(id)
    }
  }
}
