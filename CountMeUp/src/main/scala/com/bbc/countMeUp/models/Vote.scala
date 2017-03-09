package com.bbc.countMeUp.models

import java.util.UUID

case class Vote (
                override val id: UUID,
                userId: UUID,
                candidateId: UUID,
                electionId: UUID
                ) extends BaseModel
