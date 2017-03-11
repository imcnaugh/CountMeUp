package com.bbc.countMeUp.model

import java.util.UUID

/**
  * an election that candidates can run in, and user can vote on
  *
  * @param id
  * @param candidates candidates running in the election
  * @param maxVotesPerUser maximum amount of times a user can vote in the election
  */
case class Election(
                     override val id: UUID,
                     candidates: Set[Candidate],

                     maxVotesPerUser: Int
                   ) extends BaseModel
