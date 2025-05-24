
# Backend for flight booking made with Spring Boot


## Description
This project's aim is to create an API capable of searching flights based on different criteria specified by a the user.\
The project instead of being a monolithic RESTful API is based on different microserivices to better improve the mantainability and scalabilty of the API.\
Docker has implemented to ease the installation process.  


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
- the username of the user managing the database
- the password of the user managing the database


## Deployment
Make sure git and Docker are installed on your machine and clone the project with the following command:

```bash
  git clone https://github.com/AndreaVitti/FlightTicketBookingBackendAPI.git
```
Navigate to the project directory and deploy the project through the terminal:

```bash
  docker compose up -d
```
This will result in the generation of Docker images and containers for each microservice.

## Testing
The endpoints of the API have been tested with the help of `Postman`.

## Usage
To use the project I highly suggest to install `Postman` and proceed to check the various possible CRUD operations or use simply any browser.
