from fastapi import FastAPI
from pydantic import BaseModel
import joblib, numpy as np, os

app = FastAPI(title="Fraud Detection API")

MODEL_PATH  = "app/models/isolation_forest.pkl"
SCALER_PATH = "app/models/scaler.pkl"

model  = joblib.load(MODEL_PATH)  if os.path.exists(MODEL_PATH)  else None
scaler = joblib.load(SCALER_PATH) if os.path.exists(SCALER_PATH) else None

class Transaction(BaseModel):
    amount:      float
    hour:        int
    geo_risk:    int
    high_amount: int
    night_txn:   int

class FraudScore(BaseModel):
    score:     float
    is_fraud:  bool
    confidence: str

@app.get("/health")
def health():
    return {"status": "ok", "model_loaded": model is not None}

@app.post("/predict", response_model=FraudScore)
def predict(txn: Transaction):
    if model is None or scaler is None:
        return FraudScore(score=0.0, is_fraud=False, confidence="low")
    X = np.array([[txn.amount, txn.hour, txn.geo_risk, txn.high_amount, txn.night_txn]])
    X_scaled = scaler.transform(X)
    score     = float(model.decision_function(X_scaled)[0])
    threshold = -0.1
    is_fraud  = score < threshold
    confidence = "high" if abs(score) > 0.3 else "medium" if abs(score) > 0.1 else "low"
    return FraudScore(score=round(score, 4), is_fraud=is_fraud, confidence=confidence)

@app.get("/")
def root():
    return {"message": "Fraud Detection ML Service", "docs": "/docs"}
