package com.bbc.countMeUp.dao.impl

import java.util.UUID

import com.bbc.countMeUp.dao.VoteDao
import com.bbc.countMeUp.model.Vote

import scala.collection.mutable

trait InMemoryVoteDao extends VoteDao{
  override def voteDao = new InMemVoteDao

  var votes: collection.mutable.Map[UUID, Vote] = new mutable.HashMap[UUID, Vote]

  class InMemVoteDao extends VoteDao{
    override def getVotesForElection(electionId: UUID): Iterable[Vote] = ???

    override def create(model: Vote): UUID = {
      votes.put(model.id, model) match {
        case None => model.id
        case _ => throw new Exception
      }
    }

    override def read(id: UUID): Option[Vote] = ???

    override def update(model: Vote): Unit = ???

    override def delete(id: UUID): Unit = ???
  }

}
