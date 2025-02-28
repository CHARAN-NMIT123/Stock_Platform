import React, { useState, useEffect } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { ArrowUp, ArrowDown } from "lucide-react";
import { MdOutlineShoppingCart, MdOutlineAttachMoney } from "react-icons/md"; // Import new icons
import { Button } from "react-bootstrap";
import { ShoppingCart, DollarSign } from "lucide-react";

const Stocks = ({ stocks }) => {
  const [selectedStock, setSelectedStock] = useState(null);
  const location = useLocation();
  const navigate = useNavigate();

  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const stockId = params.get("id");
    if (stockId) {
      const stock = stocks.find((s) => s.stockId === Number.parseInt(stockId));
      setSelectedStock(stock);
    } else {
      setSelectedStock(null);
    }
  }, [location.search, stocks]);

  const handleTrade = (type, stock) => {
    navigate(`/home/trade`, {
      state: { type, symbol: stock.symbol, stockName: stock.name },
    });
  };

  const renderStockCard = (stock) => {
    const priceChange = stock.last - stock.open;
    const priceChangePercent = (priceChange / stock.open) * 100;
    const isPositive = priceChange >= 0;

    return (
      <div key={stock.stockId} className="col-md-4 mb-4">
        <div className="card bg-dark border-primary h-100">
          <div className="card-body d-flex flex-column">
            <h5 className="card-title text-primary">{stock.name}</h5>
            <h6 className="card-subtitle mb-2 text-muted">{stock.symbol}</h6>
            <div className="mt-auto">
              <p className="card-text mb-0">
                <span className="text-muted">Open:</span>{" "}
                <span className="text-light">${stock.open.toFixed(2)}</span>
              </p>
              <p className="card-text mb-0">
                <span className="text-muted">Last:</span>{" "}
                <span className="text-light">${stock.last.toFixed(2)}</span>
              </p>
              <p
                className={`card-text ${
                  isPositive ? "text-success" : "text-danger"
                }`}
              >
                {isPositive ? <ArrowUp size={16} /> : <ArrowDown size={16} />}$
                {Math.abs(priceChange).toFixed(2)} (
                {priceChangePercent.toFixed(2)}%)
              </p>
              <div className="d-flex justify-content-around mt-3">
                <button className="btn btn-success d-flex align-items-center" onClick={() => handleTrade("buy", stock)}>
                  <MdOutlineShoppingCart className="me-2" /> Buy
                </button>
                <button className="btn btn-danger d-flex align-items-center" onClick={() => handleTrade("sell", stock)}>
                  <MdOutlineAttachMoney className="me-2" /> Sell
                </button>
                {/* <Button
                  variant="primary"
                  className="custom-buy-button"
                  onClick={() => handleTrade("buy", stock)}
                >
                  <ShoppingCart size={18} className="me-2" /> Buy
                </Button>

                <Button
                  variant="danger"
                  className="custom-sell-button"
                  onClick={() => handleTrade("sell", stock)}
                >
                  <DollarSign size={18} className="me-2" /> Sell
                </Button> */}
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  };

  const renderStockList = () => (
    <div className="row">{stocks.map(renderStockCard)}</div>
  );

  const renderSelectedStock = () => renderStockCard(selectedStock);

  return (
    <div className="stocks animate__animated animate__fadeIn">
      <h2 className="mb-4 text-primary">Stocks</h2>
      {selectedStock ? renderSelectedStock() : renderStockList()}
    </div>
  );
};

export default Stocks;
