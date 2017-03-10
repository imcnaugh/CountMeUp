package com.bbc.countMeUp.dao

import com.bbc.countMeUp.model.Election

trait ElectionDao {

  def electionDao: ElectionDao

  trait ElectionDao extends CrudDao[Election]{

  }

}
