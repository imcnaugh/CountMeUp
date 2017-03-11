package com.bbc.countMeUp.domain

import java.util.UUID

import com.bbc.countMeUp.dao.{ElectionDao, UserDao, VoteDao}
import com.bbc.countMeUp.domain.util.IdUtils
import com.bbc.countMeUp.model.{User, Vote}

class UserDomain {
  this: UserDao with VoteDao with ElectionDao =>

  def addUser(name:String): User = {
    val newUser = User(
      IdUtils.uniqueId(userDao.read),
      name)
    userDao.create(newUser)
    newUser
  }

  //TODO throw better exceptions
  @throws(classOf[Exception])
  def getUser(id: UUID): User = {
    userDao.read(id) match {
      case u: Some[User] => u.get
      case _ => throw new Exception
    }
  }

  //TODO throw better exceptions
  @throws(classOf[Exception])
  def voteInElection(
                      userId: UUID,
                      electionId: UUID,
                      candidateId: UUID): Vote = {
    val election = electionDao.read(electionId).getOrElse(throw new Exception)
    val userVoteCountInElection = voteDao.getVoteCountForElectionAndUser(electionId, userId)

    //validate user exists
    if(userDao.read(userId) == None){
      throw new Exception
    }
    //Election not found
    if(election.maxVotesPerUser <= userVoteCountInElection){
      throw new Exception
    }
    // Candidate is not in this election
    if(election.candidates.find(c => c.id == candidateId) == None){
      throw new Exception
    }

    val newVote = Vote(
      id = IdUtils.uniqueId(voteDao.read),
      userId = userId,
      electionId = electionId,
      candidateId = candidateId
    )

    voteDao.create(newVote)
    newVote
  }
}
