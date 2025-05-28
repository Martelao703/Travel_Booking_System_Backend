# Travel Booking System Backend  

## Overview  
This project is a backend application for a travel booking system, developed using **Java** and **Spring Boot** with a layered architecture pattern. It provides core business logic and API endpoints to support functionalities such as property management, user registration, booking creation, and browsing properties.

The project is currently in development and aims to enhance my backend development skills, with plans to expand into frontend development, with **Angular**, to build a complete full-stack solution.

## Application Description  
The backend serves as the foundation of a travel booking platform. It is designed to enable:  
- **User Management:** Register and manage user accounts.  
- **Property Listings:** Allow property owners to add, update, or remove listings, along with all its associated entities.
- **Booking System:** Provide users with the ability to search for properties, check availability, and create bookings for desired dates.  

The system is built using a **layered architecture pattern**, ensuring clear separation of concerns:  
- **Model Layer:** Represents core business entities such as `User`, `Booking`, and `Property`.  
- **Service Layer:** Encapsulates business logic, including validation, entity relationships, and complex operations.  
- **Controller Layer:** Exposes REST API endpoints for external interaction.  
- **DTO Layer:** Facilitates data transfer between API layers, maintaining separation between internal models and API formats.

The application also implements JWT-based authentication to secure API access and enforce role-based access control.

## Technologies and Tools Used  
- **Spring Boot:** Core framework for dependency injection, data management, and REST API creation.  
- **PostgreSQL:** Relational database used to store and manage persistent data.
- **Docker:** Ensures containerized deployment, maintaining consistency across development and production environments.  
- **Hibernate/JPA:** Simplifies database interaction using Object-Relational Mapping (ORM).  
- **Lombok:** Reduces boilerplate code for entities and DTOs with annotations like `@Data`.
- **MapStruct:** Automates object mapping between DTOs and Entities, reducing boilerplate code.

## Future Plans  
- **Frontend Development:** Expand the project into a full-stack solution using a modern frontend framework like React or Angular.    
- **Advanced Features:**  
  - Payment integration for booking confirmations.  
  - Enhanced property search with geolocation and filtering.  
  - Admin panel for user and property management.  

---

This project is a work in progress. Contributions, feedback, or suggestions are welcome!
