package com.bbc.countMeUp.dao.util

import java.util.UUID

import com.bbc.countMeUp.model.BaseModel

class IdUtils {

}

object IdUtils {
  def uniqueId(readMethod: (UUID) => Option[BaseModel]): UUID = {
    var testId: UUID = UUID.randomUUID()
    while (readMethod(testId) != None) {
      testId = UUID.randomUUID()
    }
    testId
  }
}
