package com.bbc.countMeUp.dao

import com.bbc.countMeUp.model.User

trait UserDao {
  def userDao: UserDao

  trait UserDao extends CrudDao[User]{

  }

}
