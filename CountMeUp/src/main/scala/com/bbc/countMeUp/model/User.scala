package com.bbc.countMeUp.model

import java.util.UUID

case class User (
                override val id: UUID,
                name: String
                ) extends BaseModel
