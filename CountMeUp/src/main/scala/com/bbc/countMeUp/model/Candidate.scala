package com.bbc.countMeUp.model

import java.util.UUID

case class Candidate(
                      override val id: UUID,
                      name: String
                    ) extends BaseModel
