package com.bbc.countMeUp.model

import java.util.UUID

/**
  * A user who can vote in elections
  *
  * @param id
  * @param name name of user
  */
case class User(
                 override val id: UUID,
                 name: String
               ) extends BaseModel
