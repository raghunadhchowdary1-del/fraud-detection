import pandas as pd
import numpy as np
from sklearn.ensemble import IsolationForest
from sklearn.preprocessing import StandardScaler
from sklearn.metrics import classification_report
import joblib, os

def engineer_features(df):
    df["hour"] = pd.to_datetime(df["timestamp"]).dt.hour
    df["geo_risk"] = ((df["lat"].abs() > 30) | (df["lon"].abs() > 100)).astype(int)
    df["high_amount"] = (df["amount"] > 1000).astype(int)
    df["night_txn"]   = ((df["hour"] < 6) | (df["hour"] > 22)).astype(int)
    return df[["amount","hour","geo_risk","high_amount","night_txn"]]

def train():
    df = pd.read_csv("train/transactions.csv")
    X  = engineer_features(df)
    y  = df["is_fraud"]

    scaler = StandardScaler()
    X_scaled = scaler.fit_transform(X)

    model = IsolationForest(contamination=0.02, random_state=42, n_estimators=200)
    model.fit(X_scaled)

    scores = model.decision_function(X_scaled)
    preds  = (scores < np.percentile(scores, 2)).astype(int)
    print(classification_report(y, preds, target_names=["legit","fraud"]))

    os.makedirs("app/models", exist_ok=True)
    joblib.dump(model,  "app/models/isolation_forest.pkl")
    joblib.dump(scaler, "app/models/scaler.pkl")
    print("Models saved to app/models/")

if __name__ == "__main__":
    train()
