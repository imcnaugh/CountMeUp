package com.bbc.countMeUp.dao.impl

import java.util.UUID

import com.bbc.countMeUp.dao.UserDao
import com.bbc.countMeUp.model.User

trait InMemoryUserDao extends UserDao{
  override def userDao = new InMemUserDao

  class InMemUserDao extends UserDao{
    override def create(model: User): UUID = ???

    override def read(id: UUID): Option[User] = ???

    override def update(model: User): User = ???

    override def delete(id: UUID): Unit = ???
  }

}
