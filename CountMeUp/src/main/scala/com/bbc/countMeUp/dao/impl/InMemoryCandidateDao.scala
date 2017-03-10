package com.bbc.countMeUp.dao.impl

import java.util.UUID

import com.bbc.countMeUp.dao.CandidateDao
import com.bbc.countMeUp.model.Candidate

trait InMemoryCandidateDao extends CandidateDao{
  override def candidateDao = new InMemCandidateDao

  class InMemCandidateDao extends CandidateDao {
    override def create(model: Candidate): UUID = ???

    override def read(id: UUID): Option[Candidate] = ???

    override def update(model: Candidate): Candidate = ???

    override def delete(id: UUID): Unit = ???
  }

}
