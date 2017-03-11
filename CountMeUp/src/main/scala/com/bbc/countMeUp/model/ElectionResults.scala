package com.bbc.countMeUp.model

import java.util.UUID

case class ElectionResults (
                           electionId: UUID,
                           maxVotesPerUser: Int,
                           results: Set[CandidateTally]
                           )

case class CandidateTally(
                         candidate: Candidate,
                         votes: Int
                         )
