package services

import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play._
import repository.CarAdvertisementRepository
import util.DataCreator._
import utils.AdvertisementConstants._

import scala.concurrent.Await
import scala.concurrent.Future.successful
import scala.concurrent.duration._

class CarAdvertisementServiceSpec extends PlaySpec with MockitoSugar {

  implicit val ec = scala.concurrent.ExecutionContext.Implicits.global
  val carAdvertisementRepository = mock[CarAdvertisementRepository]
  val adService = new CarAdvertisementService(carAdvertisementRepository)

  "CarAdvertisementService" should {

    "return CarAdvertisementModel for the given id" in {
      when(carAdvertisementRepository.getAdvertisementsById(5)) thenReturn (successful(Right(model1)))
      val actual = Await.result(adService.getAdvertisementsById(5), 1 second).right.get
      actual mustBe model1
    }

    "return list of models sorted by id and default sorting order" in {
      when(carAdvertisementRepository.getAllAdvertisements()) thenReturn (successful(Right(actualListOfModels)))
      val actual = Await.result(adService.getAllAdvertisements(), 2 seconds)
      actual.right.get mustBe actualListOfModels.sortBy(_.id)
    }

    "return list of models sorted by price in descending order" in {
      when(carAdvertisementRepository.getAllAdvertisements()) thenReturn (successful(Right(actualListOfModels)))
      val actual = Await.result(adService.getAllAdvertisements(Some("price"), Some("desc"), None), 2 seconds)
      actual.right.get mustBe actualListOfModels.sortBy(_.price).reverse
    }

    "return list of models sorted by mileage in default sorting order" in {

      when(carAdvertisementRepository.getAllAdvertisements()) thenReturn (successful(Right(actualListOfModels)))
      val actual = Await.result(adService.getAllAdvertisements(sortBy = Some("mileage")), 2 seconds)
      actual.right.get mustBe actualListOfModels.sortBy(_.mileage)
    }

    "return an error if it fails to get all advertisements" in {

      when(carAdvertisementRepository.getAllAdvertisements()) thenReturn (successful(Left(ERROR_FETCHING_CAR_ADVERTISEMENT)))

      val actual = Await.result(adService.getAllAdvertisements(sortBy = Some("mileage")), 2 seconds)
      actual.left.get mustBe ERROR_FETCHING_CAR_ADVERTISEMENT
    }

    "return an error for empty title in query params while creating advertisements" in {

      val actual = Await.result(adService.createAdvertisement(carAdvertisementRequest), 2 seconds)
      actual.left.get mustBe INVALID_REQUEST_PARAMS
    }

    "return an error for non positive price and mileage in query params while creating advertisements" in {
      val actual = Await.result(adService.createAdvertisement(carAdvertisementRequest.copy(title = "title", price = -11, mileage = -9)), 2 seconds)
      actual.left.get mustBe INVALID_REQUEST_PARAMS
    }

    "create car advertisement for given params and return success message" in {
      when(carAdvertisementRepository.createAdvertisement("Audi A8", "diesel", 1190, true, 12)).thenReturn(successful(Right(model1)))
      val actual = Await.result(adService.createAdvertisement(carAdvertisementRequest1), 2 seconds)
      actual.right.get mustBe CAR_ADVERT_CREATED_SUCCESSFULLY
    }

    "returns and error while create car advertisement" in {
      when(carAdvertisementRepository.createAdvertisement("Audi A8", "diesel", 1190, true, 12)).thenReturn(successful(Left(CAR_ADVERT_CREATED_SUCCESSFULLY)))
      val actual = Await.result(adService.createAdvertisement(carAdvertisementRequest1), 2 seconds)
      actual.left.get mustBe CAR_ADVERT_CREATED_SUCCESSFULLY
    }

    "return an error for empty title in query params while updating advertisements" in {

      val actual = Await.result(adService.updateAdvertisement(1, carAdvertisementRequest), 2 seconds)
      actual.left.get mustBe INVALID_REQUEST_PARAMS
    }

    "return an error for non positive price and mileage in query params while updating advertisements" in {
      val actual = Await.result(adService.updateAdvertisement(1, carAdvertisementRequest.copy(title = "title", price = -11, mileage = -9)), 2 seconds)
      actual.left.get mustBe INVALID_REQUEST_PARAMS
    }

    "update car advertisement for given params and return success message" in {
      when(carAdvertisementRepository.updateAdvertisement(1, "Audi A8", "diesel", 1190, true, 12)).thenReturn(successful(Right(CAR_ADVERT_UPDATED_SUCCESSFULLY)))
      val actual = Await.result(adService.updateAdvertisement(1, carAdvertisementRequest1), 2 seconds)
      actual.right.get mustBe CAR_ADVERT_UPDATED_SUCCESSFULLY
    }

    "returns and error while update car advertisement" in {
      when(carAdvertisementRepository.updateAdvertisement(1, "Audi A8", "diesel", 1190, true, 12)).thenReturn(successful(Left(CAR_ADVERT_UPDATED_SUCCESSFULLY)))
      val actual = Await.result(adService.updateAdvertisement(1, carAdvertisementRequest1), 2 seconds)
      actual.left.get mustBe CAR_ADVERT_UPDATED_SUCCESSFULLY
    }

    "return success message when deleting an advertisement by id" in {
      when(carAdvertisementRepository.deleteAdvertisement(1)).thenReturn(successful(Right(CAR_ADVERT_DELETED_SUCCESSFULLY)))
      val actual = Await.result(adService.deleteAdvertisement(id = 1), 2 seconds)
      actual.right.get mustBe CAR_ADVERT_DELETED_SUCCESSFULLY
    }

    "return error message when deleting an advertisement by id" in {
      when(carAdvertisementRepository.deleteAdvertisement(1)).thenReturn(successful(Left(ERROR_DELETING_CAR_ADVERTISEMENT)))
      val actual = Await.result(adService.deleteAdvertisement(id = 1), 2 seconds)
      actual.left.get mustBe ERROR_DELETING_CAR_ADVERTISEMENT
    }
  }
}
