package com.bbc.countMeUp.domain

import java.util.UUID

import com.bbc.countMeUp.dao.{ElectionDao, UserDao, VoteDao}
import com.bbc.countMeUp.model.{Candidate, Election, User, Vote}
import org.scalatest.{FunSpec, Matchers}
import org.mockito.Mockito._
import org.mockito.Matchers._
import org.mockito.invocation.InvocationOnMock
import org.mockito.stubbing.Answer


class UserDomainTest extends FunSpec with Matchers{

  trait MockUserDao extends UserDao {
    val userDao = org.scalatest.mockito.MockitoSugar.mock[UserDao]
  }

  trait MockVoteDao extends VoteDao{
    val voteDao = org.scalatest.mockito.MockitoSugar.mock[VoteDao]
  }

  trait MockElectionDao extends ElectionDao {
    val electionDao = org.scalatest.mockito.MockitoSugar.mock[ElectionDao]
  }

  val domain = new UserDomain with MockUserDao with MockVoteDao with MockElectionDao

  describe("add user tests"){
    it("user should be assigned a unique id, and inserted properly"){
      val name = "testing"

      when(domain.userDao.create(User((any[UUID]), name))).thenAnswer(new Answer[UUID] {
        override def answer(invocation: InvocationOnMock): UUID = invocation.getArguments()(0).asInstanceOf[User].id})

      when(domain.userDao.read(any[UUID])).thenReturn(None)

      val newUser = domain.addUser(name)
      newUser.id should not be(None)
      newUser.name should equal(name)
    }
  }

  describe("get user tests"){
    it("looking up a user should suceed if the user exist"){
      val id = UUID.randomUUID()
      val name = "test user"
      when(domain.userDao.read(id)).thenReturn(Option[User](User(id = id, name = name)))

      User(id = id, name = name) should equal(domain.getUser(id))
    }

    it("looking up a user should fail if the user does not exist"){
      val id = UUID.randomUUID()
      when(domain.userDao.read(id)).thenReturn(None)

      intercept[Exception] {
        domain.getUser(id)
      }
    }
  }

  describe("voting tests"){
    it("A user should be able to vote if they have not reached the limit for an election, and the candidate is valid"){
      val electionId = UUID.randomUUID()
      val candidateId = UUID.randomUUID()
      val userId = UUID.randomUUID()
      when(domain.electionDao.read(electionId)).thenReturn(
        Option(Election(
          id = electionId,
          candidates = Set(Candidate(
            id = candidateId,
            name = "test candidate")),
          maxVotesPerUser = 1)))
      when(domain.voteDao.getVoteCountForElectionAndUser(electionId, userId)).thenReturn(0)
      when(domain.userDao.read(userId)).thenReturn(Option(User(id = userId, name = "test user")))
      when(domain.voteDao.read(any[UUID])).thenReturn(None)

      val vote = domain.voteInElection(userId, electionId, candidateId)

      verify(domain.voteDao, times(1)).create(any[Vote])
      vote.id should not be(None)
      vote.electionId should equal(electionId)
      vote.candidateId should equal(candidateId)
    }
  }
}
