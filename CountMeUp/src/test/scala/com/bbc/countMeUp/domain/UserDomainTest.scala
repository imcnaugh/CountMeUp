package com.bbc.countMeUp.domain

import com.bbc.countMeUp.domain.impl.UserDomainImpl
import org.scalatest.{FunSpec, Matchers}

class UserDomainTest extends FunSpec with Matchers{

  private class UserDomainTest {
    this: UserDomain =>
  }

  private val target = new UserDomainTest with UserDomainImpl

}
