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

#### 1. Clone the Repository
```bash
git clone https://github.com/your-username/online-book-store.git
cd online-book-store
```
#### 2. Configure Environment Variables
Create a `.env` file in the root directory and add the necessary environment variables:
```env
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/bookstore
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=yourpassword
JWT_SECRET=your_jwt_secret_key
```
#### 3. Set Up the Database
Ensure MySQL is running and create a database named `bookstore`. You can use Docker to set up MySQL:
```bash
docker run --name mysql-bookstore -e MYSQL_ROOT_PASSWORD=yourpassword -e MYSQL_DATABASE=bookstore -p 3306:3306 -d mysql:8.0
```
#### 4. Apply Database Migrations
The project uses Liquibase for database migrations. Migrations will be applied automatically when the application starts.

### Running the Project

#### 1. Build the Project
```bash
mvn clean install
```
#### 2. Run the Application
```bash
mvn spring-boot:run
```
The application will start on http://localhost:8080.
#### 3. Access Swagger UI
For API documentation and testing, navigate to:
```bash
http://localhost:8080/swagger-ui.html
```
Since the project uses Spring Security, you'll need to log in with the following credentials:
**Username: admin@gmail.com**
**Password: admin_12345!**

Make sure to replace yourpassword with the actual password you set up in your environment variables.

## 📚 API Endpoints
The API is organized into several main categories: **Authentication**, **Book**, **Category**, **Order**, and **Shopping Cart**. Below is a detailed overview of each endpoint.

### 🔑 Authentication

#### Register a New User
- **Endpoint**: `POST /api/auth/registration`
- **Description**: Registers a new user. Accessible without any role.
- **Example Link**: [http://localhost:8080/api/auth/registration](http://localhost:8080/api/auth/registration)
- **Request Body**:
  ```json
  {
    "email": "bob@example.com",
    "password": "12345678",
    "repeatPassword": "12345678",
    "firstName": "Bob",
    "lastName": "Alison",
    "shippingAddress": "Bob's address"
  }
  ```
- **Response**:
  - **Status Code**: `200 Ok`
  - **Body**:
  ```json
  {
    "id": 1,
    "email": "bob@example.com",
    "firstName": "Bob",
    "lastName": "Alison",
    "shippingAddress": "Bob's address"
  }
  ```
#### User Login
- **Endpoint**: `POST /api/auth/login`
- **Description**: Logs in a registered user. Accessible for all users.
- **Example Link**: [http://localhost:8080/api/auth/login](http://localhost:8080/api/auth/login)
- **Request Body**:
  ```json
  {
    "email": "bob@example.com",
    "password": "12345678"
  }
  ```
- **Response**:
  - **Status Code**: `200 Ok`
  - **Body**:
   ```json
  {
  "token": "your_jwt_token_here"
  }
  ```
### 📖 Book

#### Get All Books
- **Endpoint**: `GET /api/books`
- **Description**: Returns a list of all stored books. Accessible for roles **User** and **Admin**.
- **Example Link**: [http://localhost:8080/api/books](http://localhost:8080/api/books)
- **Response**:
  - **Status Code**: `200 OK`
  - **Body** (example):
  ```json
  [
    {
      "id": 1,
      "title": "The Great Gatsby",
      "author": "F. Scott Fitzgerald",
      "price": 10.99,
      "category": "Fiction",
      "description": "A classic novel about the American dream."
      "categoriesIds": [1]
    }
  ]
  ```
#### Get Book by ID
- **Endpoint**: `GET /api/books/{id}`
- **Description**: Returns a book by the specified ID. Accessible for roles **User** and **Admin**.
- **Example Link**: [http://localhost:8080/api/books/1](http://localhost:8080/api/books/1)
- **Response**:
  - **Status Code**: `200 OK`
  - **Body** (example):
 ```json
{
  "id": 1,
  "title": "White Fang",
  "author": "Jack London",
  "isbn": "00000000000001",
  "price": 19.90,
  "description": "Book about adventure",
  "coverImage": "http://example.com/whiteFang.jpg",
  "categoryIds": [1]
}
```
#### Search Books
- **Endpoint**: `GET /api/books/search`
- **Description**: Searches books using specified parameters. Accessible for roles **User** and **Admin**.
- **Example Link**: [http://localhost:8080/api/books/search?titles=White%20Fang&author=Jack%20London](http://localhost:8080/api/books/search?titles=White%20Fang&author=Jack%20London)
- **Response**:
  - **Status Code**: `200 OK`
  - **Body** (example):
```json
[
  {
    "id": 1,
    "title": "White Fang",
    "author": "Jack London",
    "isbn": "00000000000001",
    "price": 19.90,
    "description": "Book about adventure",
    "coverImage": "http://example.com/whiteFang.jpg",
    "categoryIds": [1]
  },
]
```
#### Create a New Book
- **Endpoint**: `POST /api/books`
- **Description**: Creates a new book in the database. Accessible for role **Admin**. **WARNING! Before adding a book, the corresponding category must be added**
- **Example Link**: http://localhost:8080/api/books
- **Request Body**:
```json
{
  "title": "New Book",
  "author": "New Author",
  "isbn": "00000000000002",
  "price": 29.99,
  "description": "New description",
  "coverImage": "https://example.com/newbook-cover-image.jpg"
  "categoriesIds": [1]
}
```
- **Response**:
  - **Status Code**: `200 Ok`
  - **Body** (example):
```json
{
  "id": 1,
  "title": "New Book",
  "author": "New Author",
  "isbn": "00000000000002",
  "price": 29.99,
  "description": "New description",
  "coverImage": "https://example.com/newbook-cover-image.jpg",
  "categoryIds": [1]
}
```
#### Update a Book
- **Endpoint**: `PUT /api/books/{id}`
- **Description**: Updates the book with the specified ID. Accessible for role **Admin**.
- **Example Link**: [http://localhost:8080/api/books/1](http://localhost:8080/api/books/1)
- **Request Body**:
```json
{
  "title": "Shantaram",
  "author": "Greg David Roberts",
  "isbn": "00000000000003",
  "price": 27.90,
  "description": "Book about adventure",
  "coverImage": "https://example.com/shantaram-cover-image.jpg"
   "categoryIds": [1]
}
```
- **Response**:
  - **Status Code**: `200 Ok`
  - **Body** (example):
```json
{
  "id": 1,
  "title": "Shantaram",
  "author": "Greg David Roberts",
  "isbn": "00000000000003",
  "price": 27.90,
  "description": "Book about adventure",
  "coverImage": "https://example.com/shantaram-cover-image.jpg",
  "categoryIds": [1]
}
```
#### Delete a Book
- **Endpoint**: `DELETE /api/books/{id}`
- **Description**: Soft-deletes a book with the specified ID from the database. Accessible for role **Admin**.
- **Example Link**: [http://localhost:8080/api/books/1](http://localhost:8080/api/books/1)
- **Response**:
  - **Status Code**: `204 No content`

### 📜 Category

#### Create a New Category
- **Endpoint**: `POST /api/categories`
- **Description**: Creates a new category. Accessible for role **Admin**.
- **Example Link**: [http://localhost:8080/api/categories](http://localhost:8080/api/categories)
- **Request Body**:
```json
{
  "name": "Adventures",
  "description": "Books about adventures"
}
```
- **Response**:
  - **Status Code**: `200 Ok`
  - **Body** (example):
```json
{
  "id": 1,
  "name": "Adventures",
  "description": "Books about adventures"
}
```
#### Get All Categories
- **Endpoint**: `GET /api/categories`
- **Description**: Returns a list of all categories from the database. Accessible for roles **User** and **Admin**.
- **Example Link**: [http://localhost:8080/api/categories](http://localhost:8080/api/categories)
- **Response**:
  - **Status Code**: `200 Ok`
  - **Body** (example):
```json
[
  {
    "id": 1,
    "name": "Adventures",
    "description": "Books about adventures"
  },
  {
    "id": 2,
    "name": "History",
    "description": "Books describing historical periods"
  }
]
```
#### Get Books by Category
- **Endpoint**: `GET /api/categories/{id}/books`
- **Description**: Returns a list of books that belong to a specific category. Accessible for roles **User**.
- **Example Link**: [http://localhost:8080/api/categories/1/books](http://localhost:8080/api/categories/1/books)
- **Response**:
  - **Status Code**: `200 Ok`
  - **Body** (example):
```json
[
  {
    "id": 1,
    "title": "White Fang",
    "author": "Jack London",
    "isbn": "00000000000001",
    "price": 19.90,
    "description": "Book about adventure",
    "coverImage": "http://example.com/whiteFang.jpg"
  },
  {
    "id": 3,
    "title": "Shantaram",
    "author": "Greg David Roberts",
    "isbn": "00000000000003",
    "price": 27.90,
    "description": "Book about adventure",
    "coverImage": "https://example.com/shantaram-cover-image.jpg"
  }
]
```
#### Update a Category
- **Endpoint**: `PUT /api/categories/{id}`
- **Description**: Updates the category with the specified ID. Accessible for role **Admin**.
- **Example Link**: [http://localhost:8080/api/categories/1](http://localhost:8080/api/categories/1)
- **Request Body**:
```json
[
  {
  "name": "Updated Category",
  "description": "Updated description"
  }
]
```
- **Response**:
  - **Status Code**: `200 Ok`
  - **Body** (example):
```json
[
  {
  "id": 1,
  "name": "Updated Category",
  "description": "Updated description"
  }
]
```
#### Delete a Category
- **Endpoint**: `DELETE /api/categories/{id}`
- **Description**: Soft-deletes the category with the specified ID. Accessible for role **Admin**.
- **Example Link**: [http://localhost:8080/api/categories/1](http://localhost:8080/api/categories/1)
- **Response**:
  - **Status Code**: `204 No content`

 ### 🧾 Order

#### Get All Orders
- **Endpoint**: `GET /api/orders`
- **Description**: Returns a list of all orders. Accessible for roles **User** and **Admin**.
- **Example Link**: [http://localhost:8080/api/orders](http://localhost:8080/api/orders)
- **Response**:
  - **Status Code**: `200 Ok`
  - **Body** (example):
```json
[
  {
    "id": 1,
    "userId": 1,
    "orderItems": [
      {
        "id": 1,
        "bookId": 1,
        "quantity": 1
      },
      {
        "id": 2,
        "bookId": 2,
        "quantity": 2
      }
    ],
    "orderDate": "2024-03-08T17:53:05",
    "total": 46.80,
    "status": "PENDING"
  }
]
```
#### Get Order Items
- **Endpoint**: `GET /api/orders/{orderId}/items`
- **Description**: Returns a list of items in a specific order. Accessible for roles **User** and **Admin**.
- **Example Link**: [http://localhost:8080/api/orders/1/items](http://localhost:8080/api/orders/1/items)
- **Response**:
  - **Status Code**: `200 Ok`
  - **Body** (example):
```json
[
  {
    "id": 1,
    "bookId": 1,
    "quantity": 1
  },
  {
    "id": 2,
    "bookId": 2,
    "quantity": 2
  }
]
```
#### Get Specific Order Item
- **Endpoint**: `GET /api/orders/{orderId}/items/{itemId}`
- **Description**: Returns a specific item from a specific order. Accessible for roles **User** and **Admin**.
- **Example Link**: [http://localhost:8080/api/orders/1/items/2](http://localhost:8080/api/orders/1/items/2)
- **Response**:
  - **Status Code**: `200 Ok`
  - **Body** (example):
```json
{
  "id": 2,
  "bookId": 2,
  "quantity": 2
}
```
#### Create a New Order
- **Endpoint**: `POST /api/orders`
- **Description**: Creates a new order. Accessible for roles **User** and **Admin**.
- **Example Link**: [http://localhost:8080/api/orders](http://localhost:8080/api/orders)
- **Request Body**:
```json
[
  {
  "shippingAddress": "Some address"
  }
]
```
- **Response**:
  - **Status Code**: `200 Ok`
  - **Body** (example):
```json
[
  {
  "id": 1,
  "userId": 1,
  "orderItems": [
    {
      "id": 1,
      "bookId": 1,
      "quantity": 1
    },
    {
      "id": 2,
      "bookId": 2,
      "quantity": 2
    }
  ],
  "orderDate": "2024-03-08T17:53:05",
  "total": 46.80,
  "status": "PENDING"
  }
]
```
#### Update Order Status
- **Endpoint**: `PATCH /api/orders/{id}`
- **Description**: Updates the status of an order. Accessible for role **Admin**.
- **Example Link**: [http://localhost:8080/api/orders/1](http://localhost:8080/api/orders/1)
- **Request Body**:
```json
[
  {
  "status": "DELIVERED"
  }
]
```
- **Response**:
  - **Status Code**: `200 Ok`
  - **Body** (example):
```json
[
  {
  "id": 1,
  "userId": 1,
  "orderItems": [
    {
      "id": 1,
      "bookId": 1,
      "quantity": 1
    },
    {
      "id": 2,
      "bookId": 2,
      "quantity": 2
    }
  ],
  "orderDate": "2024-03-08T17:53:05",
  "total": 46.80,
  "status": "DELIVERED"
}
]
```
 ### 🛒 Shopping Cart

#### Get Shopping Cart
- **Endpoint**: `GET /api/cart`
- **Description**: Returns the shopping cart of the logged-in user.
- **Example Link**: [http://localhost:8080/api/cart](http://localhost:8080/api/cart)
- **Response**:
  - **Status Code**: `200 Ok`
  - **Body** (example):
```json
[
  {
  "id": 2,
  "userId": 2,
  "cartItemsIds": [3, 4]
  }
]
```
