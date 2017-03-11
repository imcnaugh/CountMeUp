package com.bbc.countMeUp.model

import java.util.UUID

/**
  * Election results object
  *
  * @param electionId
  * @param maxVotesPerUser
  * @param results Set[CandidateTally] has each candidate and there total current votes
  */
case class ElectionResults (
                           electionId: UUID,
                           maxVotesPerUser: Int,
                           results: Set[CandidateTally]
                           )

/**
  *
  * @param candidate
  * @param votes current votes
  */
case class CandidateTally(
                         candidate: Candidate,
                         votes: Int
                         )
