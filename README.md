# Trade Nest - E-Commerce API

Trade Nest is an e-commerce API built with JWT authentication, complex data models, and Stripe payment integration.  
It supports user authentication, product and cart management, and secure checkout — designed for scalable, real-world use.

---

## Features

- **User Authentication**  
  Secure signup and login with JWT tokens.

- **Product Management**  
  Admin panel to add, update, and manage products, pricing, and inventory.

- **Shopping Cart**  
  Users can add or remove products from their carts.

- **Product Browsing**  
  View and search products with filtering capabilities.

- **Checkout & Payments**  
  Stripe integration for seamless and secure payments.

- **Role-Based Access Control**  
  Separate user and admin roles with enforced authorization.
- 
---

## Technology Stack

- **Backend:** Java Spring Boot
- **Authentication:** JWT (JSON Web Tokens)
- **Database:** MySQL
- **Payments:** Stripe API
- **API Testing:** Postman

---

## Getting Started

### Prerequisites

- Java 17+
- Maven
- MySQL
- Stripe account and API keys

### Installation

1. Clone the repository:  
   `git clone https://github.com/PatrickNandom/tradenest-api.git`

2. Configure database and Stripe credentials in your `application.properties` or environment variables.

3. Build and run the application:  
   `./mvnw spring-boot:run` or `./gradlew bootRun`

---

## API Endpoints Overview

- **Auth**  
  `POST /api/v1/auth/register` — Register a new user  
  `POST /api/v1/auth/login` — Login and receive JWT token

- **Products**  
  `GET /api/v1/products` — List and search products  
  `POST /api/v1/products` — Admin only: Add new product  
  `PUT /api/v1/products/{id}` — Admin only: Update product  
  `DELETE /api/v1/products/{id}` — Admin only: Delete product

- **Cart**  
  `POST /api/v1/cart` — Add product to cart  
  `DELETE /api/v1/cart/{productId}` — Remove product from cart  
  `GET /api/v1/cart` — View user’s cart

- **Checkout**  
  `POST /api/v1/checkout` — Process Stripe payment and complete order

---

## Security

- JWT secures all protected endpoints.
- Role-based access control with Spring Security annotations.

---

## Notes

- Keep Stripe API keys private; do not commit to version control.
- Extend data models to suit your business requirements.

---

## License

MIT License © Nandom Patrick Molshakat

---

## Contact

For questions or support, open an issue or reach out at [patricknandom82@gmail.com].
