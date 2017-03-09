package com.bbc.countMeUp.dao.impl

import java.util.UUID

import com.bbc.countMeUp.dao.VoteDao
import com.bbc.countMeUp.model.Vote

trait InMemoryVoteDao extends VoteDao{
  override def voteDao = new InMemVoteDao

  class InMemVoteDao extends VoteDao{
    override def getVotesForElection(electionId: UUID): Iterable[Vote] = ???

    override def create(model: Vote): UUID = ???

    override def read(id: UUID): Option[Vote] = ???

    override def update(model: Vote): Unit = ???

    override def delete(id: UUID): Unit = ???
  }

}
