package com.bbc.countMeUp.domain

import java.util.UUID

import com.bbc.countMeUp.dao.{ElectionDao, UserDao, VoteDao}
import com.bbc.countMeUp.model.User
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
}
