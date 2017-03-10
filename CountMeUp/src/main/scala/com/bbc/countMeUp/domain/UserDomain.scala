package com.bbc.countMeUp.domain

import java.util.UUID

import com.bbc.countMeUp.model.User

trait UserDomain {
  def userDomain: UserDomain

  trait UserDomain {
    def addUser(name: String): UUID
    def getUser(id: UUID): User
  }

}
