import React, { useState, useEffect } from "react";
import { Plus, X, Trash2 } from "lucide-react";

const WatchlistTab = ({ stocks }) => {
  const [watchlist1, setWatchlist1] = useState([]);
  const [watchlist2, setWatchlist2] = useState([]);
  const [newSymbol, setNewSymbol] = useState("");
  const [showAddStock, setShowAddStock] = useState(false);
  const [isLoaded, setIsLoaded] = useState(false);
  const [activeWatchlist, setActiveWatchlist] = useState(1); // 1 for watchlist1, 2 for watchlist2

  // Function to get current user ID from session storage
  const getCurrentUserID = () => {
    return sessionStorage.getItem("accountId");
  };

  // Load watchlists from localStorage on component mount
  useEffect(() => {
    const currentUserID = getCurrentUserID();
    if (currentUserID) {
      const storedUserWatchlist1 = localStorage.getItem(
        `watchlist1_${currentUserID}`
      );
      const storedUserWatchlist2 = localStorage.getItem(
        `watchlist2_${currentUserID}`
      );
      if (storedUserWatchlist1) {
        setWatchlist1(JSON.parse(storedUserWatchlist1));
      }
      if (storedUserWatchlist2) {
        setWatchlist2(JSON.parse(storedUserWatchlist2));
      }
      setIsLoaded(true);
    }
  }, []);

  // Save watchlists to localStorage, but only after the initial load
  useEffect(() => {
    const currentUserID = getCurrentUserID();
    if (isLoaded && currentUserID) {
      localStorage.setItem(
        `watchlist1_${currentUserID}`,
        JSON.stringify(watchlist1)
      );
      localStorage.setItem(
        `watchlist2_${currentUserID}`,
        JSON.stringify(watchlist2)
      );
    }
  }, [watchlist1, watchlist2, isLoaded]);

  const handleAddToWatchlist = (symbol) => {
    const stock = stocks.find((s) => s.symbol === symbol);
    if (stock) {
      if (activeWatchlist === 1) {
        if (!watchlist1.some((s) => s.symbol === symbol)) {
          setWatchlist1((prev) => [...prev, stock]);
        }
      } else {
        if (!watchlist2.some((s) => s.symbol === symbol)) {
          setWatchlist2((prev) => [...prev, stock]);
        }
      }
      setNewSymbol("");
      setShowAddStock(false);
    }
  };

  const handleDeleteFromWatchlist = (symbol) => {
    if (activeWatchlist === 1) {
      setWatchlist1((prev) => prev.filter((stock) => stock.symbol !== symbol));
    } else {
      setWatchlist2((prev) => prev.filter((stock) => stock.symbol !== symbol));
    }
  };

  const calculatePriceChange = (stock) => {
    const priceChange = stock.last - stock.open;
    const priceChangePercent = (priceChange / stock.open) * 100;
    return {
      priceChange,
      priceChangePercent,
      isPositive: priceChange >= 0,
    };
  };

  const activeWatchlistData = activeWatchlist === 1 ? watchlist1 : watchlist2;

  return (
    <div className="watchlist animate__animated animate__fadeIn">
      <h2 className="mb-4 text-primary">Watchlist</h2>
      <div className="mb-3">
        <button
          className={`btn btn-outline-primary me-2 ${
            activeWatchlist === 1 ? "active" : ""
          }`}
          onClick={() => setActiveWatchlist(1)}
        >
          Watchlist 1
        </button>
        <button
          className={`btn btn-outline-primary ${
            activeWatchlist === 2 ? "active" : ""
          }`}
          onClick={() => setActiveWatchlist(2)}
        >
          Watchlist 2
        </button>
      </div>
      <div className="table-responsive">
        <table className="table table-dark table-hover">
          <thead>
            <tr>
              <th>Symbol</th>
              <th>Name</th>
              <th>Price</th>
              <th>Change</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
            {activeWatchlistData.map((stock) => {
              const { priceChangePercent, isPositive } =
                calculatePriceChange(stock);

              return (
                <tr key={stock.symbol}>
                  <td>{stock.symbol}</td>
                  <td>{stock.name}</td>
                  <td>${stock.last.toFixed(2)}</td>
                  <td className={isPositive ? "text-success" : "text-danger"}>
                    {isPositive ? "+" : ""}
                    {priceChangePercent.toFixed(2)}%
                  </td>
                  <td>
                    <button
                      className="btn btn-sm btn-outline-danger"
                      onClick={() => handleDeleteFromWatchlist(stock.symbol)}
                    >
                      <Trash2 size={16} />
                    </button>
                  </td>
                </tr>
              );
            })}
          </tbody>
        </table>
      </div>
      <div className="mt-4">
        {showAddStock ? (
          <>
            <div className="input-group mb-2">
              <input
                type="text"
                className="form-control"
                placeholder="Search Stock Symbol"
                value={newSymbol}
                onChange={(e) => setNewSymbol(e.target.value)}
              />
              <button
                onClick={() => setShowAddStock(false)}
                className="btn btn-outline-secondary"
                type="button"
              >
                <X size={18} />
              </button>
            </div>
            <div className="list-group">
              {stocks
                .filter(
                  (s) =>
                    s.symbol.toLowerCase().includes(newSymbol.toLowerCase()) &&
                    !activeWatchlistData.some(
                      (stock) => stock.symbol === s.symbol
                    )
                )
                .map((stock, index) => (
                  <button
                    key={index}
                    className="list-group-item list-group-item-action"
                    onClick={() => handleAddToWatchlist(stock.symbol)}
                  >
                    {stock.symbol} - {stock.name}
                  </button>
                ))}
            </div>
          </>
        ) : (
          <button
            onClick={() => setShowAddStock(true)}
            className="btn btn-primary"
          >
            <Plus size={18} className="me-2" /> Add to Watchlist
          </button>
        )}
      </div>
    </div>
  );
};

export default WatchlistTab;
