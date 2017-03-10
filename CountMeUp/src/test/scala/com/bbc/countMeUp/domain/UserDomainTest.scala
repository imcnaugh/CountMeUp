package com.bbc.countMeUp.domain

import com.bbc.countMeUp.dao.impl.InMemoryUserDao
import org.scalatest.{FunSpec, Matchers}

class UserDomainTest extends FunSpec with Matchers{

  private val target = new UserDomain with InMemoryUserDao

  describe("testing"){
    it("testing"){
      target.addUser("testing")
      Console.println("hello world")
    }
  }

}
