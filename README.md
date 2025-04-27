# DineReview

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Class Diagram](#class-diagram)
- [Technologies Used](#technologies-used)
- [Learning Objectives](#learning-objectives)
- [Getting Started](#getting-started)
- [Acknowledgments](#acknowledgments)

## Overview

We’re creating a web-based platform designed to help users:

- Discover local restaurants with ease
- Read genuine reviews from fellow diners
- Share their own dining experiences through detailed reviews and ratings

By combining comprehensive restaurant information with authentic user-generated content, our platform empowers users to make confident and informed dining choices.

## Features

- 🔎 Discover Local Restaurants based on cuisine, ratings, and location

- ✍️ Write Detailed Reviews with photos and star ratings

- 📸 Upload and Attach Photos to reviews or restaurant listings

- 🗺️ Geospatial Search: Find restaurants nearby using geolocation

- 🔐 Authentication and Authorization powered by Keycloak

- 🗂️ Advanced Search using Elasticsearch with fuzzy matching

- 🏷️ Filter Restaurants by cuisine type, rating, and proximity

- 📈 Manage Restaurants (Owners can add and edit their own restaurants)

- ✨ Edit Reviews within 48 hours of posting

- 🖼️ View Restaurant Details including operating hours, photos, maps, and reviews

## Class diagram
![image](https://github.com/user-attachments/assets/20c40e14-b898-4ab0-9c65-53444285993f)


## Technologies Used

- Backend: Java 21, Spring Boot 3

  - Spring Web (REST APIs)

  - Spring Security with OAuth2 Resource Server

  - Spring Data Elasticsearch

  - MapStruct + Lombok for mapping and reducing boilerplate

- Authentication: Keycloak 23 (OAuth2, OpenID Connect)

- Database/Search Engine: Elasticsearch 8

- Frontend: Next.js (React 18)

- Containerization: Docker, Docker Compose (for Elasticsearch, Kibana, and Keycloak)

- Other Tools: Maven, Node.js (for frontend), Kibana (for Elasticsearch management)

## Learning Objectives

By building the Restaurant Review Platform:

- 📚 Understand and apply domain-driven design by modeling real-world concepts like restaurants, reviews, addresses, photos, and users.

- 🛠️ Create a secure RESTful API with Spring Boot 3 and Spring Security using OAuth2 Resource Server and Keycloak.

- 🔎 Integrate Elasticsearch to enable full-text search, fuzzy matching, and geospatial queries.

- 📦 Design and implement nested documents (e.g., addresses inside restaurants, photos inside reviews) optimized for Elasticsearch storage and querying.

- 🏛️ Set up Dockerized services (Elasticsearch, Kibana, Keycloak) for seamless local development.

- 🧹 Implement clean architecture principles, separating entities, services, mappers, DTOs, and controllers.

- 🖼️ Handle file uploads securely and serve user-uploaded images through a well-designed photo upload/download service.

- 🔄 Map domain models to DTOs cleanly using MapStruct and Lombok.

- 🔐 Authenticate and authorize users for creating restaurants, writing reviews, and uploading photos.

- 🗺️ Use geolocation data to implement location-based restaurant search.

- 🚀 Deploy and run frontend and backend applications using modern tooling like Next.js (frontend) and Docker Compose (services).




## Getting Started

To get started with the Book Social Network project, follow the setup instructions in the respective directories:

- [Backend Setup Instructions](/)
- [Frontend Setup Instructions](/)


## Acknowledgments

Special thanks to the developers and maintainers of the technologies used in this project. Their hard work and dedication make projects like this possible.

