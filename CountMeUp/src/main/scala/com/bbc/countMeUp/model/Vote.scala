package com.bbc.countMeUp.model

import java.util.UUID

/**
  * a vote cast by a user
  *
  * @param id
  * @param userId
  * @param candidateId
  * @param electionId
  */
case class Vote(
                 override val id: UUID,
                 userId: UUID,
                 candidateId: UUID,
                 electionId: UUID
               ) extends BaseModel
