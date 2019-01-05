package util

import models.{CarAdvertisementModel, CarAdvertisementRequest}

object DataCreator {

  val model1 = CarAdvertisementModel(1, "Chevrolet Equinox", "gasoline", 100000, isNew = true, 12)
  val model2 = CarAdvertisementModel(2, "Audi A7", "diesel", 18371, isNew = true, 11)
  val model3 = CarAdvertisementModel(3, "BMW A1", "diesel", 9011, isNew = false, 20)
  val model4 = CarAdvertisementModel(4, "Toyota Camry", "gasoline", 3801, isNew = true, 22)

  val actualListOfModels = List(model4,model3,model2,model1)
  val carAdvertisementRequest = CarAdvertisementRequest("","gasoline",1000,true,11)
  val carAdvertisementRequest1 = CarAdvertisementRequest("Audi A8","diesel",1190,true,12)

}
