package com.bbc.countMeUp.model

import java.util.UUID

case class Election(
                     override val id: UUID,
                     candidates: Set[Candidate],

                     maxVotesPerUser: Int
                   ) extends BaseModel
