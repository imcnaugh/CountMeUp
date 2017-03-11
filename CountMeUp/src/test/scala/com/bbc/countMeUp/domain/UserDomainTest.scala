package com.bbc.countMeUp.domain

import java.util.UUID

import com.bbc.countMeUp.dao.UserDao
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

  describe("add user tests"){
    it("user should be assigned a unique id, and inserted properly"){
      val domain = new UserDomain with MockUserDao
      val name = "testing"

      when(domain.userDao.create(User((any[UUID]), name))).thenAnswer(new Answer[UUID] {
        override def answer(invocation: InvocationOnMock): UUID = invocation.getArguments()(0).asInstanceOf[User].id})

      when(domain.userDao.read(any[UUID])).thenReturn(None)

      val newUser = domain.addUser(name)
      newUser.id should not be(None)
      newUser.name should equal(name)
    }
  }
}
