# 📘 Parking Lot Management System

A comprehensive parking lot management system built with Spring Boot that handles parking lot operations, slot management, and vehicle entry/exit tracking.

## 🔧 Prerequisites

- Java 21 installed
- Gradle installed (wrapper included)
- Docker (optional, if you want Postgres instead of H2)
- Google OAuth2 credentials set up (client ID/secret in application.yml)

## ▶️ Running the Application

```bash
# Clone the repo
git clone https://github.com/amit10987/parking-lot.git
cd parking-lot

# Build
./gradlew clean build

# Run
./gradlew bootRun
```

The app starts at:
👉 http://localhost:8080

## 🔑 Authentication

Uses Google OAuth2 for login.

You must obtain a valid Bearer token (Access Token or ID Token with email claim).

Pass it in all requests as:
```
Authorization: Bearer <ACCESS_TOKEN>
```

## 🚪 H2 Database Console

Visit: http://localhost:8080/h2-console

- **JDBC URL:** `jdbc:h2:mem:parkingdb`
- **User:** `sa`
- **Password:** (empty)

## 🛠️ REST API Endpoints

### Admin Endpoints

#### 1. Add Parking Lot
Creates a new parking lot in the system.

```http
POST /api/admin/lots
Content-Type: application/x-www-form-urlencoded
Authorization: Bearer <ACCESS_TOKEN>

location=Dubai&floors=3
```

**Parameters:**
- `location` (string): Location of the parking lot
- `floors` (number): Number of floors in the parking lot

---

#### 2. Add Parking Slots
Adds parking slots to an existing parking lot.

```http
POST /api/admin/slots
Content-Type: application/x-www-form-urlencoded
Authorization: Bearer <ACCESS_TOKEN>

lotId=1&type=TRUCK&floor=1
```

**Parameters:**
- `lotId` (number): ID of the parking lot
- `type` (string): Vehicle type (CAR, TRUCK, MOTORCYCLE, etc.)
- `floor` (number): Floor number where the slot is located

---

#### 3. Get All Slots
Retrieves all parking slots in the system.

```http
GET /api/admin/slots
Authorization: Bearer <ACCESS_TOKEN>
```

**Response:** Returns a list of all parking slots with their current status.

---

### Parking Operations

#### 4. Vehicle Entry
Records a vehicle entering the parking lot and assigns a parking slot.

```http
POST /api/parking/entry
Content-Type: application/x-www-form-urlencoded
Authorization: Bearer <ACCESS_TOKEN>

plateNo=DL8CAF5030&type=TRUCK&gateId=1
```

**Parameters:**
- `plateNo` (string): Vehicle license plate number
- `type` (string): Vehicle type (CAR, TRUCK, MOTORCYCLE, etc.)
- `gateId` (number): Entry gate identifier

**Response:** Returns a parking ticket with ticket ID and assigned slot information.

---

#### 5. Vehicle Exit
Processes a vehicle exit and calculates parking charges.

```http
POST /api/parking/exit
Content-Type: application/x-www-form-urlencoded
Authorization: Bearer <ACCESS_TOKEN>

ticketId=2
```

**Parameters:**
- `ticketId` (number): Parking ticket ID received during entry

**Response:** Returns exit details including parking duration and charges.

---

## 🎯 Usage Flow

1. **Setup:** Admin creates parking lots using the `/api/admin/lots` endpoint
2. **Configuration:** Admin adds parking slots using the `/api/admin/slots` endpoint
3. **Entry:** Vehicles enter through `/api/parking/entry` and receive a ticket
4. **Monitoring:** Admin can view all slots via `/api/admin/slots`
5. **Exit:** Vehicles exit through `/api/parking/exit` using their ticket ID

## 📊 Supported Vehicle Types

- `CAR`
- `TRUCK`
- `MOTORCYCLE`
- (Add other types as supported by your system)

## 🔐 OAuth2 Configuration

The system uses Google OAuth2 with the following scopes:
- `openid`
- `profile`
- `email`

**Redirect URI:** `http://localhost:8080/login/oauth2/code/google`

Then update your `application.yml` to use PostgreSQL instead of H2.

## 🧪 Testing with Postman

Import the provided `parking-lot.postman_collection.json` file into Postman to test all endpoints. The collection includes:

- Pre-configured OAuth2 authentication
- Sample request bodies
- All endpoint configurations

## 🏗️ System Features & Requirements Coverage

### ✅ **Important Cases Handled**

**1. Vehicle Types Management**
- Extensible vehicle types (CAR, TRUCK, MOTORCYCLE) via enum
- Slot compatibility validation (trucks can't use car slots)
- Type-specific pricing rules

**2. Smart Slot Allocation**
- **Strategy Pattern**: 4 allocation strategies (Nearest, FirstAvailable, Random, LevelWise)
- **Multi-Gate Support**: Handles multiple entry gates consistently
- **Concurrency Safety**: Pessimistic database locking prevents double-allocation

**3. Ticket Generation**
- Complete ticket info (vehicle, slot, entry time, gate)
- **Duplicate Prevention**: Validates existing active tickets for same license plate
- Unique auto-generated ticket IDs

**4. Payment & Exit Processing**
- **Dynamic Pricing**: Duration + vehicle type based (BIKE: ₹5/hr, CAR: ₹10/hr, TRUCK: ₹20/hr)
- **Flexible Rules**: First 2 hours free, hourly charges after
- **Atomic Processing**: Payment failure prevents slot release
- Cannot exit without successful payment

**5. Capacity Management**
- Finite slot tracking with real-time availability
- Graceful "full lot" rejection with proper messages
- Multi-floor support with floor-wise allocation

**6. Concurrency & Thread Safety**
- **Database-level locking** for slot allocation
- **Transaction management** ensures data consistency
- Handles multiple simultaneous entry/exit operations

**7. Security & Authentication**
- **Google OAuth2** integration with JWT tokens
- **Role-based access**: ADMIN (manage lots/slots) vs USER (park/pay)
- Protected API endpoints with proper authorization

## 🔧 Technical Highlights

- **Domain-Driven Design** with clean separation of concerns
- **Strategy Pattern** for extensible slot allocation algorithms
- **Pessimistic Locking** prevents race conditions in concurrent scenarios
- **Global Exception Handling** for consistent error responses
- **Atomic Transactions** ensure data integrity across operations

## 📝 Notes

- All admin endpoints require ADMIN role authentication
- Vehicle types should match the supported enum values (CAR, TRUCK, MOTORCYCLE)
- Ticket IDs are generated automatically during vehicle entry
- System tracks parking duration for accurate billing
- Pessimistic locking ensures thread-safe slot allocation
- Payment must be completed before slot is released