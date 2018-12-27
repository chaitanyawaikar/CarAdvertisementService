package controllers

import javax.inject.Inject
import play.api.Logger
import models.CarAdvertisementRequest._
import models._
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{Action, AnyContent, MessagesAbstractController, MessagesControllerComponents}
import services.CarAdvertisementService
import utils.AdvertisementConstants._

import scala.concurrent.{ExecutionContext, Future}

class CarAdvertisementServiceController @Inject()(service: CarAdvertisementService,
                                                  cc: MessagesControllerComponents
                                                 )(implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) {

  def getCarAdvertisements(sortBy: Option[String], sortOrder: Option[String], pageNumber: Option[Int]): Action[AnyContent] = Action.async { implicit request =>

    Logger.info("In getCarAdvertisements Of CarAdvertisementServiceController.")
    service.getAllAdvertisements(sortBy, sortOrder, pageNumber).map { advertisements =>
      advertisements match {
        case Left(error) =>
          BadRequest(Json.toJson(ErrorResponse(BAD_REQUEST, error)))
        case Right(data) =>
          Ok(Json.toJson(data))
      }
    }
  }

  def getCarAdvertisementsById(id: Int): Action[AnyContent] = Action.async { implicit request =>
    service.getAdvertisementsById(id).map { advertisements =>
      advertisements match {
        case Left(error) =>
          BadRequest(Json.toJson(ErrorResponse(BAD_REQUEST, error)))
        case Right(data) =>
          Ok(Json.toJson(data))
      }
    }
  }

  def setup = Action.async { implicit request =>
    service.setup().map {
      resp =>
        Ok(TABLE_CREATED_SUCCESSFULLY)
    }
  }

  def setupData= Action.async { implicit request =>
    service.setupData().map {
      resp =>
        Ok(DUMMY_DATA_INSERTED_SUCCESSFULLY)
    }
  }

  def createAdvertisement(): Action[JsValue] = Action.async(parse.json) { request =>

    request.body.validate[CarAdvertisementRequest].map {
      case carModel: CarAdvertisementRequest =>

        val createResponse = service.createAdvertisement(carModel)

        createResponse.map {
          resp =>
            resp match {
              case Right(data) => Ok(Json.toJson(SuccessResponse(OK, CAR_ADVERT_CREATED_SUCCESSFULLY)))
              case Left(error) =>
                BadRequest(Json.toJson(ErrorResponse(BAD_REQUEST, error)))
            }
        }.recover { case ex =>
          InternalServerError(Json.toJson(ErrorResponse(INTERNAL_SERVER_ERROR, "")))
        }
    }.recoverTotal {
      e => Future.successful(BadRequest(Json.toJson(ErrorResponse(BAD_REQUEST, INVALID_JSON_INPUT))))
    }
  }

  def updateAdvertisement(id: Int): Action[JsValue] = Action.async(parse.json) { request =>


    request.body.validate[CarAdvertisementRequest].map {
      case carModel: CarAdvertisementRequest =>

        val updateResponse = service.updateAdvertisement(id, carModel)
        updateResponse.map {
          resp =>
            resp match {
              case Right(data) => Ok(Json.toJson(SuccessResponse(OK, CAR_ADVERT_UPDATED_SUCCESSFULLY)))
              case Left(error) =>
                BadRequest(Json.toJson(ErrorResponse(BAD_REQUEST, error)))
            }
        }.recover { case ex =>
          InternalServerError(Json.toJson(ErrorResponse(INTERNAL_SERVER_ERROR, "")))
        }
    }.recoverTotal {
      e => Future.successful(BadRequest(Json.toJson(ErrorResponse(BAD_REQUEST, INVALID_JSON_INPUT))))
    }
  }

  def deleteAdvertisement(id: Int) = Action.async { implicit request =>

    service.deleteAdvertisement(id).map { advertisements =>
      advertisements match {
        case Left(error) =>
          BadRequest(Json.toJson(ErrorResponse(BAD_REQUEST, error)))
        case Right(data) =>
          Ok(Json.toJson(data))
      }
    }
  }
}