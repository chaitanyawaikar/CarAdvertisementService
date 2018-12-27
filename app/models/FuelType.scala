package models

sealed trait FuelType

case object Gasoline extends FuelType{
  override def toString: String = "Gasoline"
}

case object Diesel extends FuelType{
  override def toString: String = "Diesel"
}


