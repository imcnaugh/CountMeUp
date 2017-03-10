package com.bbc.countMeUp.domain

import java.util.UUID

import com.bbc.countMeUp.dao.UserDao
import com.bbc.countMeUp.model.User

class UserDomain {
  this: UserDao =>

  def addUser(name:String): User = {
    User(UUID.randomUUID(), "")
  }

  def getUser(id: UUID): User = {
    User(UUID.randomUUID(), "")
  }
}
