package services

import models.CarAdvertisementModel
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play._
import repository.CarAdvertisementRepository

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

class CarAdvertisementServiceSpec extends PlaySpec with MockitoSugar {

  implicit val ec = scala.concurrent.ExecutionContext.Implicits.global
  val carAdvertisementRepository = mock[CarAdvertisementRepository]
  val adService = new CarAdvertisementService(carAdvertisementRepository)

  val model = CarAdvertisementModel(1, "Chevrolet Equinox", "gasoline", 100000, true, 12)

  "CarAdvertisementService" should {

    "return CarAdvertisementModel for the given id" in {
      when(carAdvertisementRepository.getAdvertisementsById(5)) thenReturn (Future.successful(Right(model)))
      val actual = Await.result(adService.getAdvertisementsById(5), 1 second)
      actual mustBe model
    }
  }

}
