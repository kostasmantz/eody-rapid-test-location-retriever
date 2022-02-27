# EODY Rapid Test Location Retriever

## Table of Contents
- [Introduction](#introduction)
- [Technologies Used](#tech-stack)
- [Requirements](#requirements)
- [Quick Start](#quick-start)
- [API Documentation](#api-doc)

##Introduction <a name="introduction"/>
This is a simple server collecting data from [Eody's Official Site](https://eody.gov.gr/komy-testing-eody/)
In particular, Eody's site provides daily the sites conducting for free COVID-19 rapid tests. Those sites
are collected from the server, translated with [Here's Geocoding API](https://developer.here.com/products/geocoding-and-search)
to a latitude-longitude pair (so as they can be represented on a map) and lastly they are saved on a MongoDB.

The server also exposes an API with those data so as a front-end application can display that data on a map.
This is done in the single-page application developed [here](https://github.com/kostasmantz/eody-covid-map) with ReactJS.

The motivation behind this was to provide an easy way for someone to view the nearest COVID-19 rapid test sites
instead of having to scroll through the endless list provided by the official site.

##Technologies Used <a name="tech-stack"/>
Technologies used:
- Java 11
- Spring WebFlux
- Resilience4j RateLimiter
- MongoDB
- RabbitMQ
- Lombok
- Maven

##Requirements <a name="requirements"/>
The application has the following requirements to be built/run:
- Docker/Docker compose
- Java 11

## Quick Start <a name="quick-start"/>
To quickly start-up the server execute the following commands from the project's root. 
After this, you will have a fully-functioning server.

```bash
$ mvn package
$ docker compose --env-file .env.dev up -d
```
Hint: The docker compose command is for docker compose v2. For older versions use the respective docker-compose
command.

After this, you will have up-and-running the server, a mongodb instance and a rabbitmq instance.
No further configuration is needed.

##API Documentation requirements <a name="api-doc"/>
**URL** : `http://<base_url>:8080/api/places`

**Method** : `GET`

Request example:
GET `http://<base_url>:8080/api/places?from=2022-02-27T00:00:00.000&to=2022-02-27T23:59:00.000`

Response example:
```json
[
  {
    "id": "621a9f017ce9e0219f9dde5d",
    "title": "Δ. Καλλιθέας, είσοδος Δημαρχείου, 09:30-15:00",
    "latitude": "36.33683",
    "longitude": "28.17151",
    "creationDate": "2022-02-26T21:43:29.066Z",
    "effectiveDate": "2022-02-27T01:00:00Z"
  }
]
```
