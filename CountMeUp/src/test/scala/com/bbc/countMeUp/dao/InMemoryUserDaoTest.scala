package com.bbc.countMeUp.dao

import java.util.UUID

import com.bbc.countMeUp.dao.impl.InMemoryUserDao
import com.bbc.countMeUp.model.User
import org.scalatest.{FunSpec, Matchers}

class InMemoryUserDaoTest extends FunSpec with Matchers {

  private class InMemoryUserDaoTest{
    this: InMemoryUserDao =>
  }

  private val target = new InMemoryUserDaoTest with InMemoryUserDao

  val testUser = User(
    id = UUID.randomUUID(),
    name = "test user"
  )

  describe("create user test"){
    it("creating a user should not throw an exception when ID is unique"){
      val newId = UUID.randomUUID()
      target.userDao.create(testUser.copy(id = newId)) should equal(newId)
    }

    it("creating a user with a non unique id should throw an exception"){
      target.userDao.create(testUser)
      intercept[Exception]{
        target.userDao.create(testUser)
      }
    }
  }

  describe("read user test"){
    it("attempting to read a user that does not exist should return None"){
      target.userDao.read(UUID.randomUUID()) should equal (None)
    }

    it("reading a user from an ID should return that user"){
      val userId = UUID.randomUUID()
      val readUser = testUser.copy(id = userId)
      target.userDao.create(readUser)

      target.userDao.read(userId).get should equal(readUser)
    }
  }

  describe("update user test"){
    it("updating a user should reflect the updates when read"){
      val userId = UUID.randomUUID()
      val initialUser = testUser.copy(id = userId)
      target.userDao.create(initialUser)
      target.userDao.read(userId).get.name should equal(initialUser.name)

      val updateUser = initialUser.copy(name = "newName")
      target.userDao.update(updateUser)

      target.userDao.read(userId).get should equal(updateUser)

    }

    it("updating a user that does not exist should throw an exception"){
      intercept[Exception]{
        target.userDao.update(testUser.copy(id = UUID.randomUUID()))
      }
    }
  }

  describe("delete user test"){
    it("should be able to delete a user that exists") {
      //hard deletes for right now
      val userId = UUID.randomUUID()
      val deleteUser = testUser.copy(id = userId)
      target.userDao.create(deleteUser)

      target.userDao.read(userId).get should equal(deleteUser)

      target.userDao.delete(userId)

      target.userDao.read(userId) should equal(None)
    }
}
