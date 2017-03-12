package com.bbc.countMeUp.integrationTest

import com.bbc.countMeUp.dao.impl.MongoCandidateDao
import com.bbc.countMeUp.domain.CandidateDomain
import org.scalatest.{FunSpec, Matchers}

class CandidateIntTest extends FunSpec with Matchers {

  val candidateDomain = new CandidateDomain with MongoCandidateDao

  describe("Candidate round trip test"){
    it("I should be able to add a candidate, and read it back with no data loss"){
      val testCandidate = candidateDomain.addCandidate("test candidate")
      val readCandidate = candidateDomain.getCandidate(testCandidate.id)

      testCandidate should equal(readCandidate)
    }
  }

}
