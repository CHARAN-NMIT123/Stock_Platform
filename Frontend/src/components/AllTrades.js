import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import {
  ArrowLeft,
  DollarSign,
  Calendar,
  TrendingUp,
  TrendingDown,
  Clock,
  Tag,
} from "lucide-react";
import axios from "axios";

const AllTrades = () => {
  const [trades, setTrades] = useState([]);
  const [showAll, setShowAll] = useState(false);
  // const [fetchSuccess, setFetchSuccess] = useState(false);

  useEffect(() => {
    const fetchData = async () => {
      const accountId = sessionStorage.getItem("accountId");
      const token = sessionStorage.getItem("token");
      try {
        let response;
        if (showAll) {
          // Fetch all trades using Axios
          response = await axios.get(
            `http://localhost:9091/api/stock/${accountId}`,
            {
              headers: {
                Authorization: `Bearer ${token}`, // Include the JWT token
              },
            }
          );
        } else {
          // Fetch recent trades using Axios
          response = await axios.get(
            `http://localhost:9091/api/stock/trades/last5days/${accountId}`,
            {
              headers: {
                Authorization: `Bearer ${token}`, // Include the JWT token
              },
            }
          );
        }
        const tradesData = response.data.map((trade) => ({
          id: trade.orderId,
          symbol: trade.symbol,
          type: trade.transType,
          amount: trade.tradedAt,
          shares: trade.numShares,
          datetime: trade.dateOfOrder,
          typeOfPurchase: trade.typeOfPurchase,
          typeOfSell: trade.typeOfSell,
        }));
        setTrades(tradesData);
        // setFetchSuccess(true); // Set to true upon successful fetch
      } catch (error) {
        console.error("Error fetching trades:", error);
        // setFetchSuccess(false); // Set to false if there's an error fetching
      }
    };

    fetchData();
  }, [showAll]);

  const getTradeTypeInfo = (trade) => {
    if (trade.type === "buy" && trade.typeOfPurchase === "positionSizing") {
      return "Position Sizing";
    } else if (trade.type === "sell" && trade.typeOfSell === "stopLoss") {
      return "Stop Loss";
    } else {
      return "Market Plan";
    }
  };

  const getTypeColor = (type) => {
    switch (type) {
      case "Position Sizing":
        return "text-info";
      case "Stop Loss":
        return "text-warning";
      case "Market Plan":
        return "text-success";
      default:
        return "text-muted";
    }
  };

  return (
    <div className="all-trades animate__animated animate__fadeIn">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <Link to="/home" className="btn btn-primary mt-2">
          <ArrowLeft size={18} className="me-2" />
          Back to Home
        </Link>
        <h2 className="text-primary mb-0 me-3">All Trades</h2>
        <button
          className="btn btn-secondary"
          onClick={() => setShowAll(!showAll)}
        >
          {showAll ? "Show Recent" : "Show All"}
        </button>
      </div>

      {/* {fetchSuccess ? (
        <div className="alert alert-success" role="alert">
          All trades fetched successfully!
        </div>
      ) : (
        <div className="alert alert-danger" role="alert">
          Error fetching trades. Please try again.
        </div>
      )} */}

      <div className="card bg-dark">
        <div className="card-body">
          {trades.map((trade) => {
            const tradeType = getTradeTypeInfo(trade);
            return (
              <div
                key={trade.id}
                className="mb-3 p-3 rounded animate__animated animate__fadeInUp shadow-sm"
                style={{ backgroundColor: "rgba(74, 144, 226, 0.1)" }}
              >
                <div className="d-flex justify-content-between align-items-center mb-2">
                  <div>
                    <h5 className="mb-1 text-light">{trade.symbol}</h5>
                    <p className="mb-0 text-muted">
                      <Calendar size={14} className="me-1" />
                      {new Date(trade.datetime).toLocaleDateString("en-US", {
                        year: "numeric",
                        month: "long",
                        day: "numeric",
                      })}
                    </p>
                  </div>
                  <div className="text-end">
                    <h5
                      className={`mb-1 ${
                        trade.type === "buy" ? "text-success" : "text-danger"
                      }`}
                    >
                      {trade.type === "buy" ? (
                        <TrendingUp size={18} />
                      ) : (
                        <TrendingDown size={18} />
                      )}
                      {trade.type.toUpperCase()}
                    </h5>
                    <p className="mb-0 text-light">
                      <DollarSign size={14} className="me-1" />
                      {trade.amount !== undefined
                        ? trade.amount.toFixed(2)
                        : "N/A"}
                    </p>
                  </div>
                </div>
                <div className="d-flex justify-content-between align-items-center">
                  <p className="mb-0 text-muted">
                    <Clock size={14} className="me-1" />
                    {new Date(trade.datetime).toLocaleTimeString("en-US", {
                      hour: "numeric",
                      minute: "numeric",
                      hour12: true,
                    })}
                  </p>
                  <p className="mb-0 text-muted">Shares: {trade.shares}</p>
                  <p className="mb-0">
                    <Tag size={14} className="me-1" />
                    <span className={getTypeColor(tradeType)}>{tradeType}</span>
                  </p>
                </div>
              </div>
            );
          })}
        </div>
      </div>
    </div>
  );
};

export default AllTrades;
