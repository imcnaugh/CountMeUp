package com.bbc.countMeUp.model

import java.util.UUID

/**
  * A base model for anything that can be stored in the data store
  */
trait BaseModel {
  /**
    * Id of the object
    */
  val id: UUID
}
