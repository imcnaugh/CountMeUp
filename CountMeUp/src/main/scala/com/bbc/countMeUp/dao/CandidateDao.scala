package com.bbc.countMeUp.dao

import com.bbc.countMeUp.model.Candidate

trait CandidateDao {

  def candidateDao: CandidateDao

  trait CandidateDao extends CrudDao[Candidate]{

  }

}
