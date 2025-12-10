🌦️ ClimaStream – Automated Weather ETL System

A real-time Weather Data Extraction, Transformation & Loading system built using Java, MySQL, OkHttp, Jackson & OpenWeatherMap API.

🚀 Project Overview

ClimaStream is an automated ETL pipeline that fetches live weather data from the OpenWeatherMap API, processes the JSON response, and stores the structured weather readings into a MySQL database at regular time intervals.

This project demonstrates skills in:
✔ Java (Core + OOP)
✔ API integration
✔ JSON parsing using Jackson
✔ JDBC + MySQL
✔ ETL pipeline logic
✔ Thread scheduling
✔ Real-time data processing
✔ Clean modular architecture

📌 Features

🌍 Fetches real-time weather data for multiple cities

🔄 Automated ETL pipeline runs every few minutes

🧹 Cleans and transforms API response

🗄 Stores data into MySQL table weather_readings

⚙ Includes DAO, DTO, client & util layers

📦 Built as a Maven project with proper dependencies

🔐 Uses secure API keys from application.properties

🗂️ Project Structure
ClimaStream/
│
├── src/
│   ├── com.climastream.client/        # API client (OkHttp)
│   ├── com.climastream.dao/           # Database operations (JDBC)
│   ├── com.climastream.dto/           # Data Transfer Objects
│   ├── com.climastream.etl/           # ETL Worker / Scheduler
│   ├── com.climastream.util/          # DB connection utilities
│   ├── application.properties          # API keys & DB credentials
│   └── Main.java                       # Entry point
│
├── pom.xml                             # Maven dependencies
└── README.md                           # Project documentation

⚙️ Technologies Used
Component	Technology
Language	Java 17 / 21
Dependencies	OkHttp, Jackson, MySQL JDBC, HikariCP
Database	MySQL
Architecture	ETL (Extract–Transform–Load)
Tools	Eclipse IDE, Maven
🔧 Setup Instructions
1️⃣ Clone the Repository
git clone https://github.com/MohamedSilar/ClimaStream.git
cd ClimaStream

2️⃣ Configure application.properties
# MySQL
db.url=jdbc:mysql://localhost:3306/climastream?useSSL=false&serverTimezone=UTC
db.user=root
db.password=YOUR_PASSWORD

# OpenWeatherMap
owm.api.key=YOUR_API_KEY
owm.base.url=https://api.openweathermap.org/data/2.5/weather

# ETL
etl.poll.interval.seconds=900
etl.cities=London,Delhi,New York

3️⃣ Create MySQL Table
CREATE TABLE weather_readings (
  id INT AUTO_INCREMENT PRIMARY KEY,
  city VARCHAR(100),
  country VARCHAR(50),
  timestamp TIMESTAMP,
  temp DOUBLE,
  feels_like DOUBLE,
  humidity INT,
  pressure INT,
  windspeed DOUBLE,
  weather_desc VARCHAR(100)
);

4️⃣ Run the Application

In Eclipse / CMD:

java Main

📸 Sample Output
[INFO] Fetching weather for: London
[INFO] Weather saved to database.
[INFO] Fetching weather for: Delhi
[INFO] Weather saved to database.

🧩 Future Enhancements

Add a dashboard UI for visualizing data

Implement batch processing

Add Redis cache for API optimization

Build REST API endpoints

Integrate e-mail/SMS alerts

👤 Author

Mohamed Silar
📧 mohamedsilar26@gmail.com

💼 https://github.com/MohamedSilar
