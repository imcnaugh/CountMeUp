package com.bbc.countMeUp.models

import java.util.UUID

case class User (
                override val id: UUID,
                name: String
                ) extends BaseModel
