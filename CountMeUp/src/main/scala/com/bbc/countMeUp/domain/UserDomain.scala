package com.bbc.countMeUp.domain

import java.util.UUID

import com.bbc.countMeUp.dao.UserDao
import com.bbc.countMeUp.dao.util.IdUtils
import com.bbc.countMeUp.model.{BaseModel, User}

class UserDomain {
  this: UserDao =>

  def addUser(name:String): User = {
    val newUser = User(
      IdUtils.uniqueId(userDao.read),
      name)
    userDao.create(newUser)
    newUser
  }

  def getUser(id: UUID): User = {
    User(UUID.randomUUID(), "")
  }
}
