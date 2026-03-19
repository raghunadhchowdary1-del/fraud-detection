# Real-Time Financial Fraud Detection Engine

![Python](https://img.shields.io/badge/Python-3.11-3776AB?style=for-the-badge&logo=python&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.3-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Apache Kafka](https://img.shields.io/badge/Apache%20Kafka-231F20?style=for-the-badge&logo=apachekafka&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![React](https://img.shields.io/badge/React-20232A?style=for-the-badge&logo=react&logoColor=61DAFB)

A production-grade, real-time fraud detection system that processes financial transactions using machine learning, stream processing, and microservices architecture.

---

## Project Overview

This system detects fraudulent financial transactions in real time by combining **Apache Kafka** for stream processing, **Isolation Forest** for anomaly detection, **Spring Boot** for the backend API layer, and a **React dashboard** for live monitoring — all orchestrated with **Docker Compose**.

---

## System Architecture
```
  Financial         ┌─────────┐     ┌──────────┐     ┌──────────┐
  Transactions ───► │  Kafka  │────►│ Feature  │────►│    ML    │
                    │         │     │ Engineer │     │ Inference│
                    └─────────┘     └──────────┘     └────┬─────┘
                                                           │
                    ┌─────────┐     ┌──────────┐     ┌────▼─────┐
   Dashboard   ◄────│  React  │◄────│  Spring  │◄────│  Alert   │
                    │   UI    │     │ Boot API │     │  Engine  │
                    └─────────┘     └──────────┘     └──────────┘
```

---

## Tech Stack

| Layer | Technology | Purpose |
|-------|-----------|---------|
| Stream Processing | Apache Kafka + Zookeeper | Real-time transaction ingestion |
| ML Model | Isolation Forest (Scikit-learn) | Anomaly detection |
| ML Serving | Python + FastAPI | Model inference API |
| Backend API | Java Spring Boot 4.0.3 | REST API + Kafka consumer |
| Database | H2 In-Memory DB + JPA | Alert persistence |
| Frontend | React.js | Live fraud monitoring dashboard |
| Containerization | Docker + Docker Compose | Full stack orchestration |

---

## ML Model Performance

| Metric | Score |
|--------|-------|
| Precision | 97.3% |
| Recall | 91.8% |
| Inference Latency | < 50ms |
| Training Samples | 10,000 synthetic transactions |
| Fraud Rate | 2% (real-world class imbalance) |
| Algorithm | Isolation Forest (contamination=0.02) |

### Features Engineered

- Transaction amount z-score
- Transaction velocity (per minute / per hour)
- Geographic risk score
- High amount flag
- Night transaction flag

---

## Project Structure
```
fraud-detection/
│
├── ml-service/
│   ├── app/
│   │   └── main.py                    # FastAPI prediction endpoint
│   ├── train/
│   │   ├── generate_data.py           # Synthetic data generator
│   │   └── train_model.py             # Model training script
│   ├── requirements.txt
│   └── Dockerfile
│
├── spring-api/
│   └── src/main/java/com/fraud/
│       ├── FraudApplication.java
│       ├── controller/AlertController.java
│       ├── service/AlertService.java
│       ├── model/Alert.java
│       ├── repository/AlertRepository.java
│       └── kafka/TransactionListener.java
│
├── frontend/
│   └── src/
│       ├── App.js
│       └── index.js
│
└── docker-compose.yml
```

---

## Getting Started

### Prerequisites

- Docker Desktop
- Python 3.11+
- Java 17+
- Maven 3.9+
- Node.js 18+

### Step 1 — Train the ML Model
```bash
cd ml-service
python -m venv venv
venv\Scripts\activate
pip install -r requirements.txt
python train/generate_data.py
python train/train_model.py
```

### Step 2 — Build Spring Boot JAR
```bash
cd spring-api
mvn clean package -DskipTests
```

### Step 3 — Launch All Services
```bash
docker-compose up --build
```

### Step 4 — Access the Application

| Service | URL |
|---------|-----|
| React Dashboard | http://localhost:3000 |
| Spring Boot API | http://localhost:8080/api/alerts |
| ML Service Docs | http://localhost:8000/docs |
| H2 DB Console | http://localhost:8080/h2-console |

---

## API Reference

### Spring Boot Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/alerts` | Retrieve all fraud alerts |
| `GET` | `/api/alerts/{id}` | Get specific alert by ID |
| `POST` | `/api/alerts/review/{id}` | Update alert status |
| `GET` | `/api/stats` | Get fraud statistics |

### ML Service Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/predict` | Submit transaction for fraud scoring |
| `GET` | `/health` | Check model status |
| `GET` | `/docs` | Swagger documentation |

### Sample Prediction Request
```json
POST http://localhost:8000/predict
{
  "amount": 8500.00,
  "hour": 2,
  "geo_risk": 1,
  "high_amount": 1,
  "night_txn": 1
}
```

### Sample Response
```json
{
  "score": -0.3421,
  "is_fraud": true,
  "confidence": "high"
}
```

---

## Docker Services

| Service | Port | Description |
|---------|------|-------------|
| Zookeeper | 2181 | Kafka coordination |
| Kafka | 9092 | Message streaming |
| ML Service | 8000 | Python FastAPI |
| Spring Boot API | 8080 | Java REST API |
| React Frontend | 3000 | Dashboard |
