import React, { useState, useEffect } from "react";
import axios from "axios";

const API = "http://localhost:8080/api";

function App() {
  const [alerts, setAlerts]   = useState([]);
  const [stats,  setStats]    = useState({});
  const [loading, setLoading] = useState(true);

  const fetchData = async () => {
    try {
      const [a, s] = await Promise.all([
        axios.get(`${API}/alerts`),
        axios.get(`${API}/stats`)
      ]);
      setAlerts(a.data);
      setStats(s.data);
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
    const timer = setInterval(fetchData, 5000);
    return () => clearInterval(timer);
  }, []);

  return (
    <div style={{ fontFamily:"sans-serif", padding:"20px", background:"#0d1117", minHeight:"100vh", color:"#e6edf3" }}>
      <h1 style={{ color:"#58a6ff" }}>Fraud Detection Dashboard</h1>
      <div style={{ display:"flex", gap:"20px", marginBottom:"30px" }}>
        {[
          { label:"Total Alerts",    value: stats.total_alerts    || 0, color:"#f85149" },
          { label:"Open",            value: stats.open_alerts     || 0, color:"#d29922" },
          { label:"False Positives", value: stats.false_positives || 0, color:"#3fb950" },
        ].map(c => (
          <div key={c.label} style={{ background:"#161b22", border:"1px solid #30363d",
            borderRadius:"8px", padding:"20px 30px", textAlign:"center" }}>
            <div style={{ fontSize:"32px", fontWeight:"bold", color:c.color }}>{c.value}</div>
            <div style={{ fontSize:"12px", color:"#8b949e" }}>{c.label}</div>
          </div>
        ))}
      </div>
      {loading ? <p>Loading...</p> : (
        <table style={{ width:"100%", borderCollapse:"collapse" }}>
          <thead>
            <tr style={{ background:"#161b22" }}>
              {["TXN ID","User","Amount","Score","Confidence","Status","Time"].map(h => (
                <th key={h} style={{ padding:"10px", border:"1px solid #30363d", color:"#8b949e", fontSize:"12px" }}>{h}</th>
              ))}
            </tr>
          </thead>
          <tbody>
            {alerts.slice(0,50).map(a => (
              <tr key={a.id} style={{ background: a.confidence==="high" ? "#1a0a0a" : "#0d1117" }}>
                <td style={{ padding:"8px", border:"1px solid #21262d", fontSize:"11px", color:"#58a6ff" }}>
                  {a.txnId?.slice(0,8)}...
                </td>
                <td style={{ padding:"8px", border:"1px solid #21262d", fontSize:"11px" }}>
                  {a.userId?.slice(0,8)}...
                </td>
                <td style={{ padding:"8px", border:"1px solid #21262d" }}>₹{a.amount?.toFixed(2)}</td>
                <td style={{ padding:"8px", border:"1px solid #21262d", color:"#f85149" }}>
                  {a.fraudScore?.toFixed(4)}
                </td>
                <td style={{ padding:"8px", border:"1px solid #21262d" }}>
                  <span style={{ background: a.confidence==="high"?"#f85149":a.confidence==="medium"?"#d29922":"#3fb950",
                    color:"#fff", padding:"2px 8px", borderRadius:"12px", fontSize:"11px" }}>
                    {a.confidence}
                  </span>
                </td>
                <td style={{ padding:"8px", border:"1px solid #21262d", fontSize:"11px" }}>{a.status}</td>
                <td style={{ padding:"8px", border:"1px solid #21262d", fontSize:"10px", color:"#8b949e" }}>
                  {new Date(a.createdAt).toLocaleTimeString()}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}

export default App;