package com.bbc.countMeUp.domain

import java.util.UUID

import com.bbc.countMeUp.dao.UserDao
import com.bbc.countMeUp.dao.impl.InMemoryUserDao
import com.bbc.countMeUp.model.User

class UserDomain {
  this: UserDao =>

  def addUser(name:String): User = {
    User(UUID.randomUUID(), "testing")
  }
}

object UserDomain{
  def apply(): UserDomain = new UserDomain with InMemoryUserDao
}
