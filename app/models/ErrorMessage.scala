package models

import play.api.libs.json.{JsObject, Json, OFormat, Writes}

case class ErrorMessage(errorMessage: String)

case class ErrorResponse(statusCode : Int, errorMessage: String)
case class SuccessResponse(statusCode : Int, successMessage: String)

object ErrorResponse{

  implicit val errorResponseWrites = new Writes[ErrorResponse] {
    def writes(errorResponse: ErrorResponse): JsObject = Json.obj(
      "statusCode" -> errorResponse.statusCode,
      "errorMessage" -> errorResponse.errorMessage
    )
  }}

object SuccessResponse{

  implicit val successResponseWrites = new Writes[SuccessResponse] {
    def writes(successResponse: SuccessResponse): JsObject = Json.obj(
      "statusCode" -> successResponse.statusCode,
      "successMessage" -> successResponse.successMessage
    )
  }}
