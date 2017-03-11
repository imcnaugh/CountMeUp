package com.bbc.countMeUp.domain

import java.util.UUID

import com.bbc.countMeUp.dao.{ElectionDao, UserDao, VoteDao}
import com.bbc.countMeUp.domain.util.IdUtils
import com.bbc.countMeUp.exception.{EntityDoesNotExistException, ReachedElectionVoteLimitException}
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

  def getUser(id: UUID): User = {
    userDao.read(id) match {
      case u: Some[User] => u.get
      case _ => throw new EntityDoesNotExistException(id)
    }
  }

  def voteInElection(
                      userId: UUID,
                      electionId: UUID,
                      candidateId: UUID): Vote = {
    val election = electionDao.read(electionId).getOrElse(throw new Exception)
    val userVoteCountInElection = voteDao.getVoteCountForElectionAndUser(electionId, userId)

    //validate user exists
    if(userDao.read(userId) == None){
      throw new EntityDoesNotExistException(userId)
    }
    //Election not found
    if(election.maxVotesPerUser <= userVoteCountInElection){
      throw new ReachedElectionVoteLimitException
    }
    // Candidate is not in this election
    if(election.candidates.find(c => c.id == candidateId) == None){
      throw new EntityDoesNotExistException(candidateId)
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
