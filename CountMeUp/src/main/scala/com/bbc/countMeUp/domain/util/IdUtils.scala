package com.bbc.countMeUp.domain.util

import java.util.UUID

import com.bbc.countMeUp.model.BaseModel

class IdUtils {

}

object IdUtils {


  /**
    * Returns a unique id for an object
    *
    * @param readMethod CrudDao read method
    * @return UUID
    */
  def uniqueId(readMethod: (UUID) => Option[BaseModel]): UUID = {
    var testId: UUID = UUID.randomUUID()
    while (readMethod(testId) != None) {
      testId = UUID.randomUUID()
    }
    testId
  }
}
