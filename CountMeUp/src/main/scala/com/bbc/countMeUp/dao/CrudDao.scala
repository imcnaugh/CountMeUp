package com.bbc.countMeUp.dao

import java.util.UUID

import com.bbc.countMeUp.model.BaseModel

/**
  * A simple CRUD (Create, Read, Update, Delete) Dao for BaseModel objects
  *
  * @tparam T concrete BaseModel object
  */
trait CrudDao[T <: BaseModel] {

  /**
    * Creates the model in the data store
    *
    * @param model model to be created
    * @return UUID
    */
  def create(model: T): UUID

  /**
    * Read an Option(object) from the data store
    * returns None if not found
    *
    * @param id Id of object
    * @return Option(T)
    */
  def read(id: UUID): Option[T]

  /**
    * Updates a model in the data store
    *
    * @param model model to be updated
    * @return Option(T)
    */
  def update(model: T): T

  /**
    * Removes an object from the data store
    *
    * @param id id of object
    */
  def delete(id: UUID)
}
