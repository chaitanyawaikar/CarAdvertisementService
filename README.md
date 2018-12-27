# Car Advertisement Service #

This project is a car advertisement services that exposes different endpoints to create, modify, view and delete car advertisements. 

Tech stack details 
1) Play application
2) In memory database H2
3) Slick ORM
4) Scalatest and Mockito
----------
### How to setup the application ?

For simplicity, two endpoints have been created
##### /setup #### 
To create the table CarAdvertisement. Please hit this endpoint only once as it will throw a DBException stating that the table already exits (saves developer time in running the scripts and is OS agnostic :) ) 
 
##### /create-entries
To insert dummy data in the application

----------
##### Other Endpoints

 ##### GET Apis
i) /ads api will fetch all the advertisements available. Default sorting is by id. Other sorting options are by price and mileage

ii)/ads/:id will fetch an advertisement for a particular id 

 ##### POST Api
 This api will create a new car advertisement.
 
 
 ##### PUT Api
 This api will modify a new car advertisement given its id.
 
 ##### DELETE Api
 This api will delete a car advertisement given its id
 
 ----------
 ----------
 ##### Tech debt due to time constraint
 
 1) Add exhaustive test cases (unit, functional) :-> Sorry for not adding them :(
  
 2) Add logging and prometheus as a dependency for better monitoring
 
 3) Swagger documentation of apis
 
 4) Picking up messages from message file using MessagesApi in Controller (Internationalization with Messages)
 
 5) Replacing integer id with GUID
 
 6) Add date for ads and provide sorting on that also.