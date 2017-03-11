package com.bbc.countMeUp.domain

import java.util.UUID

import com.bbc.countMeUp.dao.CandidateDao
import com.bbc.countMeUp.model.Candidate

class CandidateDomain {
  this: CandidateDao =>

  def addCandidate(name: String): Candidate = ???

  //TODO throw better exceptions
  @throws(classOf[Exception])
  def getCandidate(id: UUID): Candidate = ???

}
