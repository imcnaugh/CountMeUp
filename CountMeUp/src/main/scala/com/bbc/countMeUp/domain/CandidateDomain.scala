package com.bbc.countMeUp.domain

import java.util.UUID

import com.bbc.countMeUp.dao.CandidateDao
import com.bbc.countMeUp.domain.util.IdUtils
import com.bbc.countMeUp.exception.EntityDoesNotExistException
import com.bbc.countMeUp.model.Candidate

class CandidateDomain {
  this: CandidateDao =>

  /**
    * Adds a candidate
    *
    * @param name
    * @return Candidate
    */
  def addCandidate(name: String): Candidate = {
    val newCandidate = Candidate(
      IdUtils.uniqueId(candidateDao.read),
      name
    )
    candidateDao.create(newCandidate)
    newCandidate
  }

  /**
    * Searches for a candidate
    *
    * @param id
    * @return Candidate
    */
  def getCandidate(id: UUID): Candidate = {
    candidateDao.read(id) match {
      case c: Some[Candidate] => c.get
      case _ => throw new EntityDoesNotExistException(id)
    }
  }

}
