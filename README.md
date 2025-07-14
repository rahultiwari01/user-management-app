# User Management REST API

A comprehensive RESTful API for user management, built with Spring Boot and secured with JWT (JSON Web Tokens). This application provides a complete set of endpoints for user authentication, registration, and administration.

## Features

- **JWT-based Authentication**: Secure endpoints using JSON Web Tokens.
- **User Management**: Full CRUD (Create, Read, Update, Delete) operations for users.
- **Search Functionality**: Search for users by username, full name, or email.
- **Password Management**: Securely update user passwords.
- **Role-based access control** can be easily integrated with Spring Security.

## Technologies Used

- **Java 11+**
- **Spring Boot**: For building the application.
- **Spring Security**: For authentication and authorization.
- **Spring Data JPA**: For data persistence.
- **jjwt**: For creating and parsing JSON Web Tokens.
- **Maven**: For project dependency management.
- **H2**: 

## Getting Started

### Prerequisites

- JDK 11 or later
- Maven or Gradle
- A database of your choice (e.g., PostgreSQL, H2)

### Installation & Setup

1.  **Clone the repository:**
    ```sh
    git clone git@github.com:rahultiwari01/user-management-app.git
    cd user-management-app
    ```

2.  **Configure the application:**
    Open `src/main/resources/application.properties` and configure your database connection details and JWT secret.

    ```properties
    # Database Configuration
    spring.datasource.url=jdbc:h2:mem:user-management-app;DB_CLOSE_ON_EXIT=FALSE
    spring.datasource.driver-class-name=org.h2.Driver
    spring.datasource.username=sa
    spring.datasource.password=
    spring.h2.console.enabled=true

    # JWT Secret Key
    jwt.secret.key=your-super-secret-key-that-is-long-and-secure
    ```

3.  **Build the project:**
    ```sh
    # Using Maven
    mvn clean install

    # Using Gradle
    ./gradlew build
    ```

4.  **Run the application:**
    ```sh
    java -jar target/user-management-app-0.0.1-SNAPSHOT.jar
    ```
    The application will start on `http://localhost:8080`.

## API Endpoints

The base URL for all endpoints is `/api`.

### Authentication (`/api/auth`)

| Method | Endpoint  | Description                                                   |
| :----- | :-------- | :------------------------------------------------------------ |
| `POST` | `/login`  | Authenticates a user and returns a JWT token.                 |
| `POST` | `/logout` | Logs out the current user (requires token).                   |
| `GET`  | `/status` | Checks if the current user is authenticated (requires token). |
| `GET`  | `/info`   | Provides information about authentication endpoints.          |

**Sample Login Request Body:**
```json
{
    "username": "admin",
    "password": "password"
}
```

### User Management (`/api/users`)

These endpoints require a valid JWT token in the `Authorization: Bearer <token>` header.

| Method   | Endpoint                  | Description                   |
| :------- | :------------------------ | :---------------------------- |
| `GET`    | `/`                       | Get a list of all users.      |
| `POST`   | `/`                       | Create a new user.            |
| `GET`    | `/{id}`                   | Get a user by their ID.       |
| `PUT`    | `/{id}`                   | Update a user's details.      |
| `DELETE` | `/{id}`                   | Delete a user by their ID.    |
| `PUT`    | `/{id}/password`          | Update a user's password.     |
| `GET`    | `/search/email?q={email}` | Search for users by email.    |
| `GET`    | `/search/name?q={name}`   | Search for users by name.     |
| `GET`    | `/search/username?q={q}`  | Get a user by their username. |

### Test Endpoints (`/api/test`)

> **Warning:** This endpoint is intended for development and testing purposes only. It should be disabled or removed in a production environment.

| Method | Endpoint              | Description                                    |
| :----- | :-------------------- | :--------------------------------------------- |
| `GET`  | `/generate-passwords` | Generates BCrypt-hashed passwords for testing. |

Alternatively, you can use the `GeneratePasswords.java` utility to generate hashed passwords from the command line.

## Usage

1.  **Register/Create a user** by sending a `POST` request to `/api/users`.
2.  **Login** by sending a `POST` request to `/api/auth/login` with the user's credentials to receive a JWT.
3.  **Access protected endpoints** by including the JWT in the `Authorization` header of your requests:
    ```
    Authorization: Bearer <your_jwt_token>
    ```

## License

This project is licensed under the MIT License - see the LICENSE file for details.