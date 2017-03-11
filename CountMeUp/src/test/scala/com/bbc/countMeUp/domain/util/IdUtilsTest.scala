package com.bbc.countMeUp.domain.util

import java.util.UUID

import com.bbc.countMeUp.dao.CrudDao
import com.bbc.countMeUp.model.{BaseModel, User}
import org.scalatest.{FunSpec, Matchers}
import org.mockito.Matchers.any
import org.mockito.Mockito.{times, verify, when}

class IdUtilsTest extends FunSpec with Matchers{

  val mockDao = org.scalatest.mockito.MockitoSugar.mock[CrudDao[BaseModel]]

  describe("Id utils tests"){
    it("should keep looking for a unique id when the first id it checks is taken"){
      when(mockDao.read(any[UUID])).thenReturn(Option(User(UUID.randomUUID(), "")), None)
      val id = IdUtils.uniqueId(mockDao.read)

      verify(mockDao, times(2)).read(any[UUID])
    }
  }
}
