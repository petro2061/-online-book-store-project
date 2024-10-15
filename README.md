# 📗📘📙 Online Book Store 📗📘📙

Welcome to the **Online Book Store** project! This platform is designed to provide book enthusiasts with an easy and enjoyable way to browse, search, and purchase their favorite literary works online.

## 🔍 Project Overview
The main goal of our project is to provide even the most discerning book lovers with a seamless and enjoyable search experience. This platform allows users to access a wide range of literary works through a convenient online interface in just a few clicks. 

With a simple and user-friendly design, users can easily find the books they desire, add them to their cart, and manage their orders until delivery. Our administrators continuously update the book assortment, ensuring users embark on an exciting journey through their favorite books.

## ⚙️ Technologies
The project leverages the Spring Framework and a suite of modern technologies to deliver a robust and scalable application. Here's the complete list:

### Backend:
- Spring Boot
- Spring Security
- Spring Web
- Spring Data JPA
- JWT (JSON Web Tokens)
- Lombok
- MapStruct
- Swagger

### Database:
- MySQL
- Liquibase

### Containerization:
- Docker
- Docker Testcontainers (using MySQL)

## 🚀 Getting Started

### Prerequisites
Before you begin, ensure you have met the following requirements:
- **Java Development Kit (JDK)**: Version 17 or higher
- **Maven**: For building the project
- **Docker**: For containerization
- **MySQL**: Database setup (can be managed via Docker)
- **Git**: To clone the repository

### Installation

#### 1.Clone the Repository
```bash
git clone https://github.com/your-username/online-book-store.git
cd online-book-store
```
#### 2.Configure Environment Variables
Create a `.env` file in the root directory and add the necessary environment variables:
```env
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/bookstore
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=yourpassword
JWT_SECRET=your_jwt_secret_key
```
#### 3.Set Up the Database
Ensure MySQL is running and create a database named `bookstore`. You can use Docker to set up MySQL:
```bash
docker run --name mysql-bookstore -e MYSQL_ROOT_PASSWORD=yourpassword -e MYSQL_DATABASE=bookstore -p 3306:3306 -d mysql:8.0
```
#### 4.Apply Database Migrations
The project uses Liquibase for database migrations. Migrations will be applied automatically when the application starts.

### Running the Project

#### 1.Build the Project
```bash
mvn clean install
```
#### 2.Run the Application
```bash
mvn spring-boot:run
```
The application will start on http://localhost:8080.
#### 3.Access Swagger UI
For API documentation and testing, navigate to:
```bash
http://localhost:8080/swagger-ui.html
```
Since the project uses Spring Security, you'll need to log in with the following credentials:

##### Username: admin@gmail.com
##### Password: admin_12345!

Make sure to replace yourpassword with the actual password you set up in your environment variables.
