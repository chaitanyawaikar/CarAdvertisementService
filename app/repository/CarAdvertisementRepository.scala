package repository

import javax.inject.{Inject, Singleton}
import models._
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import utils.AdvertisementConstants._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

@Singleton
class CarAdvertisementRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {


  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private class CarAdvertisement(tag: Tag) extends Table[CarAdvertisementModel](tag, "CAR_ADVERTISEMENT") {

    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def title = column[String]("title")

    def fuel = column[String]("fuel")

    def price = column[Int]("price")

    def isNew = column[Boolean]("isNew")

    def mileage = column[Int]("mileage")

    def * = (id, title, fuel, price, isNew, mileage) <> ((CarAdvertisementModel.apply _).tupled, CarAdvertisementModel.unapply)
  }

  private val carAdvertisement = TableQuery[CarAdvertisement]

  def getAllAdvertisements(): Future[Either[String, Seq[CarAdvertisementModel]]] = {
    Try {
      db.run {
        carAdvertisement.result
      }
    } match {
      case Success(data) => data.map(Right(_))
      case Failure(error) => Future.successful(Left(ERROR_FETCHING_CAR_ADVERTISEMENT))
    }

  }


  def getAdvertisementsById(id: Int): Future[Either[String, CarAdvertisementModel]] = {
    Try {
      db.run {
        carAdvertisement.result
      }
    } match {
      case Success(value) =>
        value.map {
          getResp =>
            getResp.find(_.id == id) match {
              case None => Left(CAR_ADVERTISEMENT_NOT_FOUND)
              case Some(value) => Right(value)
            }
        }
      case Failure(error) => Future.successful(Left(ERROR_FETCHING_CAR_ADVERTISEMENT))
    }

  }

  def setup(): Future[Unit] = {
    val schema = carAdvertisement.schema
    db.run(DBIO.seq(
      schema.create))
  }

  def createAdvertisement(title: String, fuel: String, price: Int, isNew: Boolean, mileage: Int): Future[Either[String, CarAdvertisementModel]] = {
    Try {
      db.run {
        (carAdvertisement.map(p => (p.title, p.fuel, p.price, p.isNew, p.mileage))
          returning carAdvertisement.map(_.id)
          into ((model, id) => CarAdvertisementModel(id, model._1, model._2, model._3, model._4, model._5))
          ) += (title, fuel, price, isNew, mileage)
      }
    } match {
      case Success(value) =>
        value.map(Right(_))
      case Failure(error) => Future.successful(Left(ERROR_CREATING_CAR_ADVERTISEMENT))
    }
  }

  def updateAdvertisement(id: Int, title: String, fuel: String, price: Int, isNew: Boolean, mileage: Int): Future[Either[String, String]] = {
    Try {
      db.run {
        carAdvertisement.filter(_.id === id).update(CarAdvertisementModel(id, title, fuel, price, isNew, mileage))
      }
    } match {
      case Success(value) =>
        value.map {
          updateResp =>
            updateResp match {
              case 0 =>
                Left(ERROR_UPDATING_CAR_ADVERTISEMENT)
              case _ =>
                Right(CAR_ADVERT_UPDATED_SUCCESSFULLY)
            }
        }
      case Failure(error) => Future.successful(Left(ERROR_UPDATING_CAR_ADVERTISEMENT))
    }
  }

  def deleteAdvertisement(id: Int): Future[Either[String, String]] = {
    getAdvertisementsById(id).flatMap {
      getResp =>
        getResp match {
          case Left(error) =>
            Future.successful(Left(error))
          case Right(model) =>
            db.run {
              carAdvertisement.filter(_.id === id).delete
            }.map {
              affectedRows =>
                affectedRows match {
                  case 0 => Left(ERROR_DELETING_CAR_ADVERTISEMENT)
                  case _ => Right(CAR_ADVERT_DELETED_SUCCESSFULLY)
                }
            }
        }
    }
  }

  def createEntries: Future[Unit] = {
    val setup: DBIOAction[Unit, NoStream, Effect.Write with Effect.Transactional] = DBIO.seq(
      carAdvertisement ++= Seq(
        CarAdvertisementModel(1, "Chevrolet Equinox", "gasoline", 100000, true, 12),
        CarAdvertisementModel(2, "Toyota Corolla", "gasoline", 2000, true, 22),
        CarAdvertisementModel(3, "Toyota Camry", "gasoline", 2500, false, 20),
        CarAdvertisementModel(4, "Mercedes-Benz Maybach Exelero", "diesel", 200000, true, 15),
        CarAdvertisementModel(5, "Lamborghini Veneno.", "diesel", 999000000, true, 14),
        CarAdvertisementModel(6, "W Motors Lykan Hypersport", "diesel", 128129, true, 5),
        CarAdvertisementModel(7, "Bugatti Chiron", "diesel", 250000, true, 10),
        CarAdvertisementModel(8, "Corolla", "gasoline", 2000, true, 11)
      )
    ).transactionally
    db.run(setup)
  }

}
