package com.bbc.countMeUp.dao.impl

import java.util.UUID

import com.bbc.countMeUp.dao.CandidateDao
import com.bbc.countMeUp.model.Candidate

import scala.collection.mutable

trait InMemoryCandidateDao extends CandidateDao{
  override def candidateDao = new InMemCandidateDao

  var candidates: collection.mutable.Map[UUID, Candidate] = new mutable.HashMap[UUID, Candidate]

  class InMemCandidateDao extends CandidateDao {
    override def create(model: Candidate): UUID = {
      candidates.put(model.id, model) match {
        case None => model.id
        case _ => throw new Exception
      }
    }

    override def read(id: UUID): Option[Candidate] = {
      candidates.get(id)
    }

    override def update(model: Candidate): Candidate = {
      candidates.put(model.id, model) match {
        case None => throw new Exception
        case e: Some[Candidate] => e.get
      }
    }

    override def delete(id: UUID): Unit = {
      candidates.remove(id)
    }
  }

}
