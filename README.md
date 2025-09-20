📘 Parking Lot Management System
🔧 Prerequisites

Java 21 installed

Gradle installed (wrapper included)

Docker (optional, if you want Postgres instead of H2)

Google OAuth2 credentials set up (client ID/secret in application.yml)

▶️ Running the Application
# Clone the repo
git clone https://github.com/amit10987/parking-lot.git
cd parking-lot

# Build
./gradlew clean build

# Run
./gradlew bootRun


The app starts at:
👉 http://localhost:8080

🔑 Authentication

Uses Google OAuth2 for login.

You must obtain a valid Bearer token (Access Token or ID Token with email claim).

Pass it in all requests as:

Authorization: Bearer <ACCESS_TOKEN>

🚪 H2 Database Console

Visit: http://localhost:8080/h2-console

JDBC URL: jdbc:h2:mem:parkingdb

User: sa

Password: (empty)

🚗 API Endpoints (with cURL)
1. 🏢 Create Parking Lot (Admin only)
   curl -X POST "http://localhost:8080/api/admin/lots" \
   -H "Authorization: Bearer <ACCESS_TOKEN>" \
   -H "Content-Type: application/x-www-form-urlencoded" \
   -d "location=Dubai" \
   -d "floors=3"

2. ➕ Add Slot (Admin only)
   curl -X POST "http://localhost:8080/api/admin/slots" \
   -H "Authorization: Bearer <ACCESS_TOKEN>" \
   -H "Content-Type: application/x-www-form-urlencoded" \
   -d "lotId=1" \
   -d "type=CAR" \
   -d "floor=1"

3. 📋 View All Slots (Admin only)
   curl -X GET "http://localhost:8080/api/admin/slots" \
   -H "Authorization: Bearer <ACCESS_TOKEN>"

4. 🚙 Vehicle Entry (User)
   curl --location "http://localhost:8080/api/parking/entry" \
   --header "Authorization: Bearer <ACCESS_TOKEN>" \
   --header "Content-Type: application/x-www-form-urlencoded" \
   --data-urlencode "plateNo=DL8CAF5030" \
   --data-urlencode "type=CAR" \
   --data-urlencode "gateId=1"

5. 🅿️ Vehicle Exit + Payment (User)
   curl --location "http://localhost:8080/api/parking/exit" \
   --header "Authorization: Bearer <ACCESS_TOKEN>" \
   --header "Content-Type: application/x-www-form-urlencoded" \
   --data-urlencode "plateNo=DL8CAF5030" \
   --data-urlencode "amount=150.00"

6. 🎟 Get Active Ticket by Plate (User)
   curl --location "http://localhost:8080/api/parking/ticket/DL8CAF5030" \
   --header "Authorization: Bearer <ACCESS_TOKEN>"