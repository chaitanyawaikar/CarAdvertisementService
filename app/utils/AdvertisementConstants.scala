package utils

object AdvertisementConstants {

  val CAR_ADVERT_CREATED_SUCCESSFULLY = "Car Advertisement created successfully with Id : "
  val ERROR_CREATING_CAR_ADVERTISEMENT = "There was some problem creating the advertisement."

  val CAR_ADVERT_UPDATED_SUCCESSFULLY = "Car Advertisement updated successfully"
  val ERROR_UPDATING_CAR_ADVERTISEMENT = "There was some problem updating the advertisement."

  val CAR_ADVERT_DELETED_SUCCESSFULLY = "Car Advertisement deleted successfully"
  val ERROR_DELETING_CAR_ADVERTISEMENT = "There was some problem deleting the advertisement."


  val ERROR_FETCHING_CAR_ADVERTISEMENT = "There was some problem while fetching the car advertisements."
  val CAR_ADVERTISEMENT_NOT_FOUND = "There was no advertisement found for the given id."

  val INTERNAL_EROR = "Internal server Error"
  val INVALID_JSON_INPUT = "Json input is invalid"
  val INVALID_REQUEST_PARAMS = "Request parameters are invalid. Expected :-> title:{Non Empty String} , price>0 and mileage>0"

  val TABLE_CREATED_SUCCESSFULLY = "Table CarAdvertisement has been created successfully. Please hit the /entries endpoint to insert dummy data"

  val DUMMY_DATA_INSERTED_SUCCESSFULLY = "Dummy data has been inserted successfully. Feel free to play with Play :)"
}

object FuelTypes{
  val GASOLINE = "gasoline"
  val DIESEL = "diesel"
}