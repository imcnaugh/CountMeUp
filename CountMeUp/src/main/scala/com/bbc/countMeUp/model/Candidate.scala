package com.bbc.countMeUp.model

import java.util.UUID

/**
  * a candidate that can run in any election
  *
  * @param id
  * @param name name of the candidate
  */
case class Candidate(
                      override val id: UUID,
                      name: String
                    ) extends BaseModel
