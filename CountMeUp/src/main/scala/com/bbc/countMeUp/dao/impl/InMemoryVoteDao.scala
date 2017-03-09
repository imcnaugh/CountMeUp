package com.bbc.countMeUp.dao.impl

import java.util.UUID

import com.bbc.countMeUp.dao.VoteDao
import com.bbc.countMeUp.model.Vote

import scala.collection.mutable

trait InMemoryVoteDao extends VoteDao{
  override def voteDao = new InMemVoteDao

  var votes: collection.mutable.Map[UUID, Vote] = new mutable.HashMap[UUID, Vote]

  class InMemVoteDao extends VoteDao{
    override def getVotesForElection(electionId: UUID): mutable.Map[UUID, Vote] = {
      votes.filter(v => v._2.electionId == electionId)
    }

    override def getVoteCountForElection(electionId: UUID): Int = {
      votes.count(v => v._2.electionId == electionId)
    }

    override def getVoteCountForElectionAndUser(electionId: UUID, userId: UUID): Int = {
      votes.count(v => v._2.electionId == electionId && v._2.userId == userId)
    }

    override def getVoteCountForElectionAndCandidate(electionId: UUID, candidateId: UUID): Int = {
      votes.count(v => v._2.electionId == electionId && v._2.candidateId == candidateId)
    }

    override def create(model: Vote): UUID = {
      votes.put(model.id, model) match {
        case None => model.id
        case _ => throw new Exception
      }
    }

    override def read(id: UUID): Option[Vote] = {
      votes.get(id)
    }

    override def update(model: Vote): Vote = {
      votes.put(model.id, model) match {
        case None => throw new Exception
        case v: Some[Vote] => v.get
      }
    }

    override def delete(id: UUID): Unit = {
      votes.remove(id)
    }
  }

}
