package utils

import models._
import utils.FuelTypes.{DIESEL, GASOLINE}

object AdvertisementServiceUtility {

  val sortByFieldsList = List("id", "title", "price", "mileage")
  val sortByOrderList = List("asc", "desc")

  def checkSortingCriteria(sortBy: Option[String], sortOrder: Option[String]): Either[ErrorMessage, (String, String)] = {

    val maybeSortField = sortBy.map(x => sortByFieldsList.contains(x.toLowerCase()))
    val maybeSortOrder = sortOrder.map(x => sortByOrderList.contains(x.toLowerCase()))

    (maybeSortField, maybeSortOrder) match {

      case (None, None) => Right("id", "asc")
      case (Some(true), Some(true)) => Right(sortBy.get.toLowerCase(), sortOrder.get.toLowerCase())
      case (Some(true),_) => Right(sortBy.get.toLowerCase(),"asc")
      case (Some(false), _) => Left(ErrorMessage(buildCustomMessage(sortBy.get, sortByFieldsList)))
      case (_, Some(false)) => Left(ErrorMessage(buildCustomMessage(sortOrder.get, sortByOrderList)))
      case (_, _) => Right("id", "asc")
    }
  }

  def buildCustomMessage[T](param: String, paramsList: List[T]): String = {

    s"The query param ${param} is invalid. Allowed values are ${paramsList.mkString(",")}"
  }

  def checkFuelType(carModel: CarAdvertisementRequest): Either[ErrorResponse, CarAdvertisementRequest] = {

    carModel.fuel.toLowerCase match {
      case GASOLINE => Right(carModel)
      case DIESEL => Right(carModel)
      case _ => Left(ErrorResponse(400, s"Invalid fuel type. Allowed values are $GASOLINE and $DIESEL"))
    }
  }

  def verifyQueryParams(title: String, price: Int, mileage: Int) =
    title.nonEmpty && price > 0 && mileage > 0

  def sortData(data: Seq[CarAdvertisementModel],sortBy : String,sortOrder:String) = {
    (sortBy,sortOrder) match {
      case ("price","asc") => data.sortBy(_.price)
      case ("price","desc") => data.sortBy(_.price).reverse
      case ("mileage","asc") => data.sortBy(_.mileage)
      case ("mileage","desc") => data.sortBy(_.mileage).reverse
      case (_,"desc") => data.sortBy(_.id).reverse
      case _ => data.sortBy(_.id)
    }
  }

  implicit def toSQLTime(date: java.util.Date): java.sql.Timestamp = new java.sql.Timestamp(date.getTime)

}
