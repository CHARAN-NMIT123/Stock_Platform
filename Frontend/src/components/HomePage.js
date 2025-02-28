import React, { useState, useEffect } from "react";
import { useNavigate, Routes, Route, useLocation } from "react-router-dom";
import {
  Eye,
  DollarSign,
  Briefcase,
  TrendingUp,
  BarChart2,
} from "lucide-react";
import axios from "axios";

import Navbar from "./Navbar";
import DashboardTab from "./DashboardTab";
import TradeTab from "./TradeTab";
import PortfoliosTab from "./PortfoliosTab";
import WatchlistTab from "./WatchlistTab";
import UpdateAccount from "./UpdateAccount";
import AccountDetails from "./AccountDetails";
import Stocks from "./Stocks";

const HomePage = () => {
  const [activeTab, setActiveTab] = useState("dashboard");
  // const [selectedPortfolio, setSelectedPortfolio] = useState(null)
  const [stocks, setStocks] = useState([]);
  const location = useLocation();
  const navigate = useNavigate();

  useEffect(() => {
    // const dummyStocks = [
    //   { stockId: 1, name: "Apple Inc.", symbol: "AAPL", type: "technology", open: 150.25, last: 151.5 },
    //   { stockId: 2, name: "Microsoft Corporation", symbol: "MSFT", type: "technology", open: 280.75, last: 282.0 },
    //   { stockId: 3, name: "Amazon.com, Inc.", symbol: "AMZN", type: "technology", open: 3380.0, last: 3395.5 },
    //   { stockId: 4, name: "Alphabet Inc.", symbol: "GOOGL", type: "technology", open: 2410.0, last: 2415.75 },
    //   { stockId: 5, name: "Tesla, Inc.", symbol: "TSLA", type: "technology", open: 690.5, last: 678.25 },
    //   { stockId: 6, name: "JPMorgan Chase & Co.", symbol: "JPM", type: "finance", open: 155.0, last: 156.75 },
    //   { stockId: 7, name: "Bank of America Corp", symbol: "BAC", type: "finance", open: 41.5, last: 41.75 },
    //   { stockId: 8, name: "Wells Fargo & Co", symbol: "WFC", type: "finance", open: 46.75, last: 46.25 },
    //   { stockId: 9, name: "Caterpillar Inc.", symbol: "CAT", type: "construction", open: 235.25, last: 239.0 },
    //   { stockId: 10, name: "Deere & Company", symbol: "DE", type: "construction", open: 355.0, last: 352.75 },
    // ]
    // setStocks(dummyStocks)

    // Uncomment to fetch data from an API
    const token = sessionStorage.getItem("token");
    axios
      .get("http://localhost:9091/api/stocks", {
        headers: {
          Authorization: `Bearer ${token}`, // Include the JWT token
        },
      })
      .then((response) => {
        setStocks(response.data);
      })
      .catch((error) => {
        console.error("Error fetching stock data:", error);
      });
    // Sync the active tab with the current URL
    const path = location.pathname;
    if (path.includes("/stocks")) {
      setActiveTab("stocks");
    } else if (path.includes("/trade")) {
      setActiveTab("trade");
    } else if (path.includes("/portfolios")) {
      setActiveTab("portfolios");
    } else if (path.includes("/watchlist")) {
      setActiveTab("watchlist");
    } else {
      setActiveTab("dashboard");
    }
  }, [location]);

  // const handleBackToPortfolios = () => {
  //   navigate("/home/portfolios")  // navigate back to the portfolios page
  // }
  const handleLogout = () => {
    const accountId = sessionStorage.getItem("accountId");

    if (accountId) {
      axios
        .post(`http://localhost:9091/v1/logout/${accountId}`)
        .then((response) => {
          console.log("Logout successful:", response.data);
          sessionStorage.clear();
          navigate("/login");
        })
        .catch((error) => {
          console.error("Logout failed:", error);
        });
    } else {
      console.error("No account ID found in session storage");
    }
  };

  // const renderTabContent = () => {
  //   switch (activeTab) {
  //     case "dashboard":
  //       return <DashboardTab />
  //     case "stocks":
  //       return <Stocks />
  //     case "trade":
  //       return <TradeTab />
  //     case "portfolios":
  //       return selectedPortfolio ? (
  //         <PortfolioDetails portfolio={selectedPortfolio} onBack={() => setSelectedPortfolio(null)} />
  //       ) : (
  //         <PortfoliosTab onSelectPortfolio={setSelectedPortfolio} />
  //       )
  //     case "watchlist":
  //       return <WatchlistTab />
  //     default:
  //       return <DashboardTab />
  //   }
  // }

  const onBack = () => {
    navigate("/home");
  };

  return (
    <div className="homepage bg-dark text-light min-vh-100">
      <Navbar handleLogout={handleLogout} stocks={stocks} />
      <div className="container-fluid mt-4">
        <div className="row">
          <div className="col-md-3 col-lg-2 mb-4">
            <div className="list-group">
              <button
                className={`list-group-item list-group-item-action ${
                  activeTab === "dashboard" ? "active" : ""
                }`}
                onClick={() => navigate("/home")}
              >
                <TrendingUp size={18} className="me-2" /> Dashboard
              </button>
              <button
                className={`list-group-item list-group-item-action ${
                  activeTab === "stocks" ? "active" : ""
                }`}
                onClick={() => navigate("/home/stocks")}
              >
                <BarChart2 size={18} className="me-2" /> Stocks
              </button>
              <button
                className={`list-group-item list-group-item-action ${
                  activeTab === "trade" ? "active" : ""
                }`}
                onClick={() => navigate("/home/trade")}
              >
                <DollarSign size={18} className="me-2" /> Trade
              </button>
              <button
                className={`list-group-item list-group-item-action ${
                  activeTab === "portfolios" ? "active" : ""
                }`}
                onClick={() => navigate("/home/portfolios")}
              >
                <Briefcase size={18} className="me-2" /> Portfolios
              </button>
              <button
                className={`list-group-item list-group-item-action ${
                  activeTab === "watchlist" ? "active" : ""
                }`}
                onClick={() => navigate("/home/watchlist")}
              >
                <Eye size={18} className="me-2" /> Watchlist
              </button>
            </div>
          </div>
          <div className="col-md-9 col-lg-10">
            <div className="card bg-dark border-0">
              <div className="card-body">
                <Routes>
                  <Route path="/" element={<DashboardTab stocks={stocks} />} />
                  <Route path="/stocks" element={<Stocks stocks={stocks} />} />
                  <Route path="/trade" element={<TradeTab stocks={stocks} />} />
                  <Route
                    path="/portfolios"
                    element={<PortfoliosTab onBack={onBack} stocks={stocks} />}
                  />
                  {/* <Route path="/portfolios/:id" element={<PortfolioDetails portfolio={selectedPortfolio} onBack={handleBackToPortfolios} />}/> */}
                  <Route
                    path="/watchlist"
                    element={<WatchlistTab stocks={stocks} />}
                  />
                  <Route path="/update-account" element={<UpdateAccount />} />
                  <Route path="/account-details" element={<AccountDetails />} />
                </Routes>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default HomePage;
