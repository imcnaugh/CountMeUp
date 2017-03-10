package com.bbc.countMeUp.domain

import java.util.UUID

import com.bbc.countMeUp.dao.UserDao
import com.bbc.countMeUp.model.User
import org.scalatest.{FunSpec, Matchers}
import org.mockito.Mockito._
import org.mockito.Matchers._


class UserDomainTest extends FunSpec with Matchers{

  trait MockUserDao extends UserDao {
    val userDao = org.scalatest.mockito.MockitoSugar.mock[UserDao]
  }

  private val target = new UserDomain with MockUserDao

  describe("add user tests"){
    it("user should be assigned a unique id, and inserted properly"){
      val name = "testing"
      val id = UUID.randomUUID()

      when(target.userDao.create(User((any[UUID]), name))).thenReturn(id)
      User(id, name) should equal(target.addUser(name))

    }
  }

}
