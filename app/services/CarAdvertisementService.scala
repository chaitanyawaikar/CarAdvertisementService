package services

import javax.inject.Inject
import models.{CarAdvertisementModel, CarAdvertisementRequest}
import repository.CarAdvertisementRepository
import utils.AdvertisementConstants._
import utils.AdvertisementServiceUtility._

import scala.concurrent.{ExecutionContext, Future}


class CarAdvertisementService @Inject()(repo: CarAdvertisementRepository)(implicit ec: ExecutionContext) {

  def deleteAdvertisement(id: Int): Future[Either[String, String]] = {
    repo.deleteAdvertisement(id)
  }

  def getAllAdvertisements(sortBy: Option[String] = None, sortOrder: Option[String] = None, pageNumber: Option[Int] = None): Future[Either[String, Seq[CarAdvertisementModel]]] = {

    checkSortingCriteria(sortBy, sortOrder) match {

      case Right((sortBy, sortOrder)) =>
        repo.getAllAdvertisements().map {

          getResp =>
            getResp match {

              case Right(data) =>
                Right(sortData(data, sortBy, sortOrder))
              case Left(error) =>
                Left(error)
            }
        }
      case Left(errorMessage) => Future.successful(Left(errorMessage.errorMessage))
    }
  }

  def getAdvertisementsById(id: Int): Future[Either[String, CarAdvertisementModel]] = {
    repo.getAdvertisementsById(id)
  }

  def setup(): Future[Unit] = {
    repo.setup()
  }

  def setupData(): Future[Unit] = {
    repo.createEntries
  }

  def createAdvertisement(carModel: CarAdvertisementRequest): Future[Either[String, String]] = {
    if (verifyQueryParams(carModel.title, carModel.price, carModel.mileage)) {

      checkFuelType(carModel) match {

        case Right(fuelType) =>
          repo.createAdvertisement(carModel.title, carModel.fuel, carModel.price, carModel.isNew, carModel.mileage).map {

            createResponse =>
              createResponse match {
                case Right(data) => Right(CAR_ADVERT_CREATED_SUCCESSFULLY)
                case Left(error) => Left(error)
              }
          }
        case Left(errorResponse) =>
          Future.successful(Left(errorResponse.errorMessage))
      }
    }
    else
      Future.successful(Left(INVALID_REQUEST_PARAMS))
  }

  def updateAdvertisement(id: Int, carModel: CarAdvertisementRequest): Future[Either[String, String]] = {

    if (verifyQueryParams(carModel.title, carModel.price, carModel.mileage)) {
      checkFuelType(carModel) match {
        case Right(fuelType) =>
          repo.updateAdvertisement(id, carModel.title, carModel.fuel, carModel.price, carModel.isNew, carModel.mileage)
        case Left(errorResponse) =>
          Future.successful(Left(errorResponse.errorMessage))
      }
    }
    else
      Future.successful(Left(INVALID_REQUEST_PARAMS))
  }
}
