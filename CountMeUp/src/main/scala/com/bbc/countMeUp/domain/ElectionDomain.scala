package com.bbc.countMeUp.domain

import java.util.UUID

import com.bbc.countMeUp.dao.{CandidateDao, ElectionDao, VoteDao}
import com.bbc.countMeUp.model.Election

class ElectionDomain {
  this: ElectionDao with VoteDao with CandidateDao =>

  //TODO throw better exceptions
  @throws(classOf[Exception])
  def addElection(candidateIds: Set[UUID],
                  maxVotesPerUser: Int): Election = ???

  //TODO throw better exceptions
  @throws(classOf[Exception])
  def getElection(id: UUID): Election = ???

}
