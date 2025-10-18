# Restaurant Order & Table Management System

A comprehensive multi-tenant restaurant management system built with Java 21.0.7 and Spring Boot 3.5.6 using microservices architecture.

## 🏗️ Architecture Overview

This system consists of 8 microservices that work together to provide a complete restaurant management solution:

- **API Gateway** (Port 8080): Request routing and JWT validation
- **Auth Service** (Port 8081): User authentication and JWT token management
- **Tenant Service** (Port 8082): Multi-tenancy with schema-per-tenant
- **Menu Service** (Port 8083): Menu items and categories management
- **Table Service** (Port 8084): Table management and reservations
- **Order Service** (Port 8085): Order processing and management
- **Billing Service** (Port 8086): Bill generation and payment processing
- **Kitchen Service** (Port 8087): Real-time kitchen order updates via WebSocket

## 🚀 Key Features

- **Multi-tenant Architecture**: Schema-per-tenant isolation
- **JWT Authentication**: Secure API access with role-based authorization
- **Real-time Updates**: WebSocket-based kitchen order notifications
- **Microservices**: Scalable and maintainable service architecture
- **PostgreSQL**: Robust data persistence with multi-schema support
- **Docker Support**: Easy deployment with Docker Compose

## 🛠️ Technology Stack

- **Java**: 21.0.7
- **Spring Boot**: 3.5.6
- **Spring Cloud Gateway**: API Gateway
- **Spring Security**: Authentication & Authorization
- **Spring Data JPA**: Data persistence
- **Spring WebSocket**: Real-time communication
- **PostgreSQL**: Database
- **JWT**: Token-based authentication
- **Docker**: Containerization
- **Maven**: Build tool

## 📋 Prerequisites

- Java 21.0.7 or higher
- Maven 3.6+
- Docker and Docker Compose
- PostgreSQL 15+ (if running locally without Docker)

## 🚀 Quick Start

### 1. Clone the Repository

```bash
git clone <repository-url>
cd restaurant-management-system
```

### 2. Build All Services

```bash
mvn clean install
```

### 3. Run with Docker Compose

```bash
docker-compose up -d
```

This will start:
- PostgreSQL database
- All 8 microservices
- API Gateway

### 4. Verify Services

Check that all services are running:

```bash
docker-compose ps
```

## 🔧 Manual Setup (Without Docker)

### 1. Database Setup

Create PostgreSQL database:

```sql
CREATE DATABASE restaurant_main;
CREATE USER restaurant_user WITH PASSWORD 'restaurant_password';
GRANT ALL PRIVILEGES ON DATABASE restaurant_main TO restaurant_user;
```

### 2. Run Services

Start each service individually:

```bash
# Terminal 1 - Auth Service
cd auth-service
mvn spring-boot:run

# Terminal 2 - Tenant Service
cd tenant-service
mvn spring-boot:run

# Terminal 3 - Menu Service
cd menu-service
mvn spring-boot:run

# Terminal 4 - Table Service
cd table-service
mvn spring-boot:run

# Terminal 5 - Order Service
cd order-service
mvn spring-boot:run

# Terminal 6 - Billing Service
cd billing-service
mvn spring-boot:run

# Terminal 7 - Kitchen Service
cd kitchen-service
mvn spring-boot:run

# Terminal 8 - API Gateway
cd api-gateway
mvn spring-boot:run
```

## 📚 API Documentation

### Authentication Endpoints

**Base URL**: `http://localhost:8080/api/auth`

- `POST /login` - User login
- `POST /register` - User registration
- `GET /me` - Get current user info

### Tenant Management

**Base URL**: `http://localhost:8080/api/tenants`

- `POST /` - Create new tenant
- `GET /` - Get all tenants
- `GET /{id}` - Get tenant by ID
- `PUT /{id}` - Update tenant
- `DELETE /{id}` - Delete tenant

### Menu Management

**Base URL**: `http://localhost:8080/api/menu`

- `POST /items` - Create menu item
- `GET /items` - Get all menu items
- `GET /items/{id}` - Get menu item by ID
- `GET /items/category/{category}` - Get items by category
- `GET /items/available` - Get available items
- `PUT /items/{id}` - Update menu item
- `DELETE /items/{id}` - Delete menu item

### Table Management

**Base URL**: `http://localhost:8080/api/tables`

- `POST /` - Create table
- `GET /` - Get all tables
- `GET /{id}` - Get table by ID
- `GET /available` - Get available tables
- `PUT /{id}` - Update table
- `PUT /{id}/reserve` - Reserve table
- `PUT /{id}/release` - Release table
- `DELETE /{id}` - Delete table

### Order Management

**Base URL**: `http://localhost:8080/api/orders`

- `POST /` - Create order
- `GET /` - Get all orders
- `GET /{id}` - Get order by ID
- `GET /table/{tableId}` - Get orders by table
- `GET /status/{status}` - Get orders by status
- `PUT /{id}` - Update order
- `PUT /{id}/status/{status}` - Update order status
- `DELETE /{id}` - Delete order

### Billing Management

**Base URL**: `http://localhost:8080/api/billing`

- `POST /bills` - Create bill
- `GET /bills` - Get all bills
- `GET /bills/{id}` - Get bill by ID
- `GET /bills/order/{orderId}` - Get bill by order
- `PUT /bills/{id}` - Update bill
- `PUT /bills/{id}/pay` - Process payment
- `DELETE /bills/{id}` - Delete bill

### Kitchen Management

**Base URL**: `http://localhost:8080/api/kitchen`

- `POST /orders` - Create kitchen order
- `GET /orders` - Get all kitchen orders
- `GET /orders/{id}` - Get kitchen order by ID
- `PUT /orders/{id}` - Update kitchen order
- `PUT /orders/{id}/status/{status}` - Update kitchen order status

## 🔌 WebSocket Endpoints

**Kitchen Service WebSocket**: `ws://localhost:8087/ws/orders`

Subscribe to kitchen order updates:
```javascript
const socket = new SockJS('http://localhost:8087/ws/orders');
const stompClient = Stomp.over(socket);

stompClient.connect({}, function(frame) {
    stompClient.subscribe('/topic/kitchen-orders', function(message) {
        const kitchenOrder = JSON.parse(message.body);
        console.log('Kitchen order update:', kitchenOrder);
    });
});
```

## 🔐 Authentication Flow

1. **Register User**: `POST /api/auth/register`
2. **Login**: `POST /api/auth/login` - Returns JWT token
3. **Use Token**: Include `Authorization: Bearer <token>` header in requests

### Example Login Request

```json
{
    "username": "admin",
    "password": "password123"
}
```

### Example Login Response

```json
{
    "token": "eyJhbGciOiJIUzUxMiJ9...",
    "type": "Bearer",
    "id": 1,
    "username": "admin",
    "email": "admin@restaurant.com",
    "role": "ADMIN",
    "tenantId": 1
}
```

## 🏢 Multi-Tenancy

The system supports multiple restaurants (tenants) with complete data isolation:

1. **Create Tenant**: Each tenant gets a unique schema
2. **Schema Isolation**: All tenant data is stored in separate schemas
3. **User Association**: Users are associated with specific tenants
4. **Data Segregation**: Complete separation of restaurant data

### Tenant Creation Flow

1. Create tenant via `POST /api/tenants`
2. System creates dedicated schema
3. Creates necessary tables in tenant schema
4. Users can be associated with the tenant

## 📊 Database Schema

### Main Schema (`restaurant_main`)
- `users` - System users
- `tenants` - Restaurant tenants

### Tenant Schemas (`tenant_<name>`)
- `menu_items` - Menu items
- `tables` - Restaurant tables
- `orders` - Customer orders
- `order_items` - Order line items
- `bills` - Generated bills
- `kitchen_orders` - Kitchen order tracking

## 🐳 Docker Commands

### Build and Run

```bash
# Build all services
docker-compose build

# Start all services
docker-compose up -d

# View logs
docker-compose logs -f

# Stop all services
docker-compose down

# Stop and remove volumes
docker-compose down -v
```

### Individual Service Management

```bash
# Build specific service
docker build -t restaurant-auth-service ./auth-service

# Run specific service
docker run -p 8081:8081 restaurant-auth-service
```

## 🧪 Testing

### Manual Testing

1. **Create Tenant**:
```bash
curl -X POST http://localhost:8080/api/tenants \
  -H "Content-Type: application/json" \
  -d '{"name": "Test Restaurant", "schemaName": "test_restaurant", "plan": "BASIC"}'
```

2. **Register User**:
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username": "admin", "email": "admin@test.com", "password": "password123", "role": "ADMIN", "tenantId": 1}'
```

3. **Login**:
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "admin", "password": "password123"}'
```

4. **Create Menu Item** (with JWT token):
```bash
curl -X POST http://localhost:8080/api/menu/items \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <your-jwt-token>" \
  -d '{"name": "Pizza Margherita", "description": "Classic pizza", "category": "Pizza", "price": 12.99, "available": true}'
```

## 🔧 Configuration

### Environment Variables

- `SPRING_DATASOURCE_URL`: Database connection URL
- `SPRING_DATASOURCE_USERNAME`: Database username
- `SPRING_DATASOURCE_PASSWORD`: Database password
- `JWT_SECRET`: JWT signing secret
- `JWT_EXPIRATION`: JWT token expiration time

### Service Ports

- API Gateway: 8080
- Auth Service: 8081
- Tenant Service: 8082
- Menu Service: 8083
- Table Service: 8084
- Order Service: 8085
- Billing Service: 8086
- Kitchen Service: 8087
- PostgreSQL: 5432

## 🚨 Troubleshooting

### Common Issues

1. **Port Conflicts**: Ensure ports 8080-8087 and 5432 are available
2. **Database Connection**: Verify PostgreSQL is running and accessible
3. **JWT Issues**: Check JWT secret configuration across services
4. **Service Discovery**: Ensure API Gateway can reach all services

### Logs

```bash
# View all service logs
docker-compose logs

# View specific service logs
docker-compose logs auth-service

# Follow logs in real-time
docker-compose logs -f api-gateway
```

## 📈 Monitoring

### Health Checks

Each service provides health check endpoints:
- `GET /actuator/health` - Service health status

### Metrics

Spring Boot Actuator provides metrics endpoints:
- `GET /actuator/metrics` - Service metrics
- `GET /actuator/info` - Service information

## 🔄 Development Workflow

1. **Local Development**: Run services individually for development
2. **Testing**: Use Docker Compose for integration testing
3. **Production**: Deploy using Docker containers

## 📝 License

This project is licensed under the MIT License.

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests
5. Submit a pull request

## 📞 Support

For support and questions, please create an issue in the repository.

---

**Happy Coding! 🍕🍔🍰**
