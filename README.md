
# Backend for flight booking made with Spring Boot


## Description
This project's aim is to create an API capable of searching flights based on different criteria specified by a the user.\
The project instead of being a monolithic RESTful API is based on different microserivices to better improve the mantainability and scalabilty of the API.\
Docker has implemented to ease the installation process.  

## Technologies used
List of the technologies used in this project:
- Spring Boot
- Docker
- PostgresSQL
- Zipkin
- OpenAPI
- Stripe
- Intellij

## Functionalities
This API supports the following operations:
- registration, login and logout of users
- creation and strorage of flights with their data
- search of availble flights
- creation and strorage of flight tickets
- payment through Stripe API


## Requirements
This application will require: 
- git 
- a recent version of Docker Desktop
- a `Stripe` account since the payments make use of their API


## Environment Variables
To run this project, you will need to add the following environment variables to `.env`:
- a 256 bit secret key
- the public key generated upon registering a `Stripe` account
- the secret key generated upon registering a `Stripe` account
- the secret key that encrypts the endpoint of the Stripe webhook
- the username of the user managing the database
- the password of the user managing the database
- the access token duration (default value = 30 min)
- the refresh token duration (default value = 24 h)
- the cross origin url (default value = "http://localhost:3000")


## Deployment
Make sure git and Docker are installed on your machine and clone the project with the following command:

```bash
  git clone https://github.com/AndreaVitti/FlightTicketBookingBackendAPI.git
```
Navigate to the project directory and deploy the project through the terminal:

```bash
  docker compose up -d
```
This will result in the generation of Docker images and containers for each microservice.\
To the get the webhook endpoint key open the 'stripeCli' container in docker and copy it from the logs to the env. file (to make the changes valid the containers will need to be reloaded so rerun the docker compose command).


## Testing
The endpoints of the API have been tested with the help of `Postman`.

## Security
The API has been secured by using a JWT on the API gateway from which every request has to pass through. If the JWT is valid then the API will provide the access token, which will be stored in memory and have a short duration, and a refresh token, which will be stored in a secure httponly cookie and be have a longer lifespan.  

## Usage
To checkout the project's CRUD operations yourserlf I highly suggest to install `Postman`, but any browser will do too. Zipkin can be access through "http://localhost:9412/zipkin/" to have a better understanding of the performance and request flows.

## Documentation
Use OpenAPI to organize and visualize all CRUD operations:
- "http://localhost:8555/swagger-ui/index.html" for the user microservice
- "http://localhost:8444/swagger-ui/index.html" for the flight microservice
- "http://localhost:8333/swagger-ui/index.html" for the ticket microservice
- "http://localhost:8222/swagger-ui/index.html" for the payment microservice
