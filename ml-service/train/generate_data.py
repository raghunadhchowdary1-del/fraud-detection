import pandas as pd
import numpy as np
from faker import Faker
import random, json
from datetime import datetime, timedelta

fake = Faker()
random.seed(42)
np.random.seed(42)

MERCHANT_CATEGORIES = ['grocery','electronics','restaurant','travel','online','pharmacy']

def generate_transactions(n=10000):
    records = []
    users = [fake.uuid4() for _ in range(200)]
    for i in range(n):
        is_fraud = random.random() < 0.02  # 2% fraud rate
        user_id  = random.choice(users)
        amount   = round(random.uniform(2000, 9000), 2) if is_fraud else round(random.uniform(5, 500), 2)
        lat      = round(random.uniform(-90, 90), 6)    if is_fraud else round(random.uniform(8, 14), 6)
        lon      = round(random.uniform(-180,180), 6)   if is_fraud else round(random.uniform(77, 80), 6)
        records.append({
            "txn_id":    fake.uuid4(),
            "user_id":   user_id,
            "amount":    amount,
            "merchant":  random.choice(MERCHANT_CATEGORIES),
            "lat":       lat,
            "lon":       lon,
            "timestamp": (datetime.now() - timedelta(seconds=random.randint(0, 86400))).isoformat(),
            "is_fraud":  int(is_fraud)
        })
    df = pd.DataFrame(records)
    df.to_csv("train/transactions.csv", index=False)
    print(f"Generated {n} transactions ({df.is_fraud.sum()} fraudulent)")
    return df

if __name__ == "__main__":
    generate_transactions()
