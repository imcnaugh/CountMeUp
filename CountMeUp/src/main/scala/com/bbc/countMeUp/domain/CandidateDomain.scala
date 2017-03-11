package com.bbc.countMeUp.domain

import java.util.UUID

import com.bbc.countMeUp.dao.CandidateDao
import com.bbc.countMeUp.domain.util.IdUtils
import com.bbc.countMeUp.model.Candidate

class CandidateDomain {
  this: CandidateDao =>

  def addCandidate(name: String): Candidate = {
    val newCandidate = Candidate(
      IdUtils.uniqueId(candidateDao.read),
      name
    )
    candidateDao.create(newCandidate)
    newCandidate
  }

  //TODO throw better exceptions
  @throws(classOf[Exception])
  def getCandidate(id: UUID): Candidate = {
    candidateDao.read(id) match {
      case c: Some[Candidate] => c.get
      case _ => throw new Exception
    }
  }

}
