package com.bbc.countMeUp.domain.impl

import java.util.UUID

import com.bbc.countMeUp.domain.UserDomain
import com.bbc.countMeUp.model.User

trait UserDomainImpl extends UserDomain{
  override def userDomain: UserDomain = new UserDomainImpl

  class UserDomainImpl extends UserDomain{
    override def addUser(name: String): UUID = ???

    override def getUser(id: UUID): User = ???
  }
}
