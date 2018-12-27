package models

import play.api.libs.json.Json

case class CarAdvertisementRequest(title: String, fuel: String, price: Int, isNew: Boolean, mileage: Int)

object CarAdvertisementRequest {
  implicit val createCarAdvertisementRequestFormat = Json.reads[CarAdvertisementRequest]
}
