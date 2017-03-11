package com.bbc.countMeUp.dao.impl

import java.util.UUID

import com.bbc.countMeUp.dao.UserDao
import com.bbc.countMeUp.exception.{EntityAlreadyExistsException, EntityDoesNotExistException}
import com.bbc.countMeUp.model.User

trait InMemoryUserDao extends UserDao {
  override def userDao = new InMemUserDao

  var users = DataStorage.users

  class InMemUserDao extends UserDao {
    override def create(model: User): UUID = {
      users.put(model.id, model) match {
        case None => model.id
        case _ => throw new EntityAlreadyExistsException(model)
      }
    }

    override def read(id: UUID): Option[User] = {
      users.get(id)
    }

    override def update(model: User): User = {
      users.put(model.id, model) match {
        case None => throw new EntityDoesNotExistException(model.id)
        case u: Some[User] => u.get
      }
    }

    override def delete(id: UUID): Unit = {
      users.remove(id)
    }
  }

}
