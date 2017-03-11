package com.bbc.countMeUp.domain

import java.util.UUID

import com.bbc.countMeUp.dao.{ElectionDao, UserDao, VoteDao}
import com.bbc.countMeUp.dao.util.IdUtils
import com.bbc.countMeUp.model.User

class UserDomain {
  this: UserDao with VoteDao with ElectionDao =>

  def addUser(name:String): User = {
    val newUser = User(
      IdUtils.uniqueId(userDao.read),
      name)
    userDao.create(newUser)
    newUser
  }

  //TODO throw better exceptions
  @throws(classOf[Exception])
  def getUser(id: UUID): User = {
    userDao.read(id) match {
      case u: Some[User] => u.get
      case _ => throw new Exception
    }
  }
}
