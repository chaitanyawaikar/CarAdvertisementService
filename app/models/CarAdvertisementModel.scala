package models

import play.api.libs.json.Json

case class CarAdvertisementModel(id: Int, title: String, fuel: String, price: Int, isNew: Boolean, mileage: Int)


object CarAdvertisementModel {

  implicit val carAdvertisementModelFormat = Json.format[CarAdvertisementModel]

}
