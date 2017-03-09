package com.bbc.countMeUp.dao

import java.util.UUID

import com.bbc.countMeUp.model.BaseModel

trait CrudDao[T <: BaseModel]{
  def create(model: T): UUID,
  def read(id: UUID): Option[T],
  def update(model: T),
  def delete(id: UUID)
}
