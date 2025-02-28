import React, { useState, useEffect } from "react";
import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer,
} from "recharts";
import axios from "axios";

// Dummy stock data for testing
const stockList = [
  { stockId: 1, name: "Apple Inc.", symbol: "AAPL", open: 150.25, last: 151.5 },
  {
    stockId: 2,
    name: "Microsoft Corp.",
    symbol: "MSFT",
    open: 280.75,
    last: 282.0,
  },
  {
    stockId: 3,
    name: "Amazon.com Inc.",
    symbol: "AMZN",
    open: 3380.0,
    last: 3395.5,
  },
  {
    stockId: 4,
    name: "Alphabet Inc.",
    symbol: "GOOGL",
    open: 2410.0,
    last: 2415.75,
  },
  { stockId: 5, name: "Tesla Inc.", symbol: "TSLA", open: 690.5, last: 678.25 },
  {
    stockId: 6,
    name: "JPMorgan Chase",
    symbol: "JPM",
    open: 155.0,
    last: 156.75,
  },
  {
    stockId: 7,
    name: "Bank of America",
    symbol: "BAC",
    open: 41.5,
    last: 41.75,
  },
  { stockId: 8, name: "Wells Fargo", symbol: "WFC", open: 46.75, last: 46.25 },
  {
    stockId: 9,
    name: "Caterpillar Inc.",
    symbol: "CAT",
    open: 235.25,
    last: 239.0,
  },
  {
    stockId: 10,
    name: "Deere & Company",
    symbol: "DE",
    open: 355.0,
    last: 352.75,
  },
];

const DashboardTab = ({ stocks = stockList }) => {
  // Passing stock data as a prop (defaulting to dummy data)
  const [selectedStocks, setSelectedStocks] = useState([]);
  const [stockData, setStockData] = useState([]);
  const [gainers, setGainers] = useState([]);
  const [losers, setLosers] = useState([]);
  const [accountName, setAccountName] = useState("John Doe"); // Dummy account name for now

  const getRandomPrice = (basePrice) => {
    return +(basePrice * (0.65 + Math.random() * 0.7)).toFixed(2);
  };

  const getDistinctColor = (index) => {
    const colors = ["#FF0000", "#00FF00", "#b507b5"]; // Red, Green, Purple
    return colors[index % colors.length]; // Cycle through the three colors
  };

  useEffect(() => {
    // Commenting out fetching logic for now
    const token = sessionStorage.getItem("token");
    const accountId = sessionStorage.getItem("accountId");
    axios
      .get(`http://localhost:9091/api/stocktrader/${accountId}`, {
        headers: {
          Authorization: `Bearer ${token}`, // Include the JWT token
        },
      }) // Replace with your actual endpoint
      .then((response) => {
        setAccountName(response.data.fname); // Assuming accountName is part of the response
      })
      .catch((error) => {
        console.error("Error fetching account details:", error);
      });

    // Randomly select 3 stocks for the graph from the passed stocks prop
    const shuffledStocks = [...stocks].sort(() => 0.5 - Math.random());
    const chosenStocks = shuffledStocks.slice(0, 3);

    // Generate monthly data for all stocks with fluctuating prices
    const months = [
      "Jan",
      "Feb",
      "Mar",
      "Apr",
      "May",
      "Jun",
      "Jul",
      "Aug",
      "Sep",
      "Oct",
      "Nov",
      "Dec",
    ];
    const generatedData = months.map((month) => {
      let entry = { name: month };
      stocks.forEach((stock) => {
        entry[stock.symbol] = getRandomPrice(Math.max(stock.open, stock.last));
      });
      return entry;
    });

    setSelectedStocks(chosenStocks);
    setStockData(generatedData);

    // Determine gainers and losers (based on all stocks, not just the selected ones)
    const stockChanges = stocks.map((stock) => {
      const firstPrice = generatedData[0][stock.symbol];
      const lastPrice = generatedData[generatedData.length - 1][stock.symbol];
      const changePercent = (
        ((lastPrice - firstPrice) / firstPrice) *
        100
      ).toFixed(2);
      return { symbol: stock.symbol, name: stock.name, change: changePercent };
    });

    // Sort by gainers and losers
    const sortedChanges = [...stockChanges].sort((a, b) => b.change - a.change);
    setGainers(sortedChanges.slice(0, 3));
    setLosers(sortedChanges.slice(-3));
  }, [stocks]); // Add stocks as a dependency in case props change

  return (
    <div className="dashboard animate__animated animate__fadeIn">
      <h2 className="mb-4 text-primary">Dashboard</h2>
      {/* Displaying the welcome message with account name */}
      <div className="welcome-message text-light mb-4">
        <h4>Welcome, {accountName}</h4>
      </div>
      <div className="row">
        <div className="col-md-8 mb-4">
          <div className="card bg-dark">
            <div className="card-body">
              <h5 className="card-title text-primary">Stock Performance</h5>
              <ResponsiveContainer width="100%" height={300}>
                <LineChart
                  data={stockData}
                  animationDuration={3000}
                  animationEasing="ease-in-out"
                >
                  <CartesianGrid strokeDasharray="3 3" stroke="#444" />
                  <XAxis dataKey="name" stroke="#888" />
                  <YAxis stroke="#888" />
                  <Tooltip
                    contentStyle={{ backgroundColor: "#333", border: "none" }}
                  />
                  <Legend />
                  {selectedStocks.map((stock, index) => (
                    <Line
                      key={stock.symbol}
                      type="monotone"
                      dataKey={stock.symbol}
                      stroke={getDistinctColor(index)}
                      activeDot={{ r: 8 }}
                      isAnimationActive={true}
                    />
                  ))}
                </LineChart>
              </ResponsiveContainer>
            </div>
          </div>
        </div>
        <div className="col-md-4 mb-4">
          <div className="card bg-dark mt-4">
            <div className="card-body">
              <h5 className="card-title text-primary">Latest Market News</h5>
              <ul className="list-unstyled text-light">
                <li className="mb-2">
                  Tech stocks surge on positive earnings reports
                </li>
                <li className="mb-2">
                  Oil prices stabilize after recent volatility
                </li>
                <li className="mb-2">
                  Federal Reserve hints at potential rate hike
                </li>
              </ul>
            </div>
          </div>
        </div>
      </div>
      {/* Gainers & Losers */}
      <div className="row">
        <div className="col-md-6 mb-4">
          <div className="card bg-dark">
            <div className="card-body">
              <h5 className="card-title text-primary">Top Gainers</h5>
              <ul className="list-unstyled text-light">
                {gainers.map((stock) => (
                  <li key={stock.symbol}>
                    {stock.symbol} - {stock.name}{" "}
                    <span className="text-success">+{stock.change}%</span>
                  </li>
                ))}
              </ul>
            </div>
          </div>
        </div>
        <div className="col-md-6 mb-4">
          <div className="card bg-dark">
            <div className="card-body">
              <h5 className="card-title text-primary">Top Losers</h5>
              <ul className="list-unstyled text-light">
                {losers.map((stock) => (
                  <li key={stock.symbol}>
                    {stock.symbol} - {stock.name}{" "}
                    <span className="text-danger">{stock.change}%</span>
                  </li>
                ))}
              </ul>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default DashboardTab;
