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

## 🧪 Testing with Postman

Import the provided `parking-lot.postman_collection.json` file into Postman to test all endpoints. The collection includes:

- Pre-configured OAuth2 authentication
- Sample request bodies
- All endpoint configurations

## 📝 Notes

- All admin endpoints require proper authentication
- Vehicle types should match the supported types in your system
- Ticket IDs are generated automatically during vehicle entry
- The system tracks parking duration for billing purposes