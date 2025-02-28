import React, { useState, useEffect } from "react";

const SearchResults = ({
  searchQuery,
  filterType,
  onResultClick,
  onClose,
  className,
  stocks,
}) => {
  const [filteredStocks, setFilteredStocks] = useState([]);

  useEffect(() => {
    const filtered = stocks.filter((stock) => {
      const matchesSearch =
        stock.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
        stock.symbol.toLowerCase().includes(searchQuery.toLowerCase());
      const matchesFilter = filterType === "" || stock.type === filterType;
      return matchesSearch && matchesFilter;
    });
    setFilteredStocks(filtered);
  }, [searchQuery, filterType, stocks]);

  return (
    <div
      className={`search-results bg-dark border border-primary rounded p-3 position-absolute top-100 start-0 w-100 mt-2 ${className}`}
      style={{ zIndex: 1060 }}
    >
      <div className="d-flex justify-content-between align-items-center mb-2">
        <h6 className="text-primary mb-0">Search Results</h6>
        <button className="btn btn-sm btn-outline-primary" onClick={onClose}>
          Close
        </button>
      </div>
      {filteredStocks.length > 0 ? (
        <ul className="list-unstyled">
          {filteredStocks.map((stock) => (
            <li
              key={stock.stockId}
              className="cursor-pointer p-2 hover:bg-primary rounded"
              onClick={() => onResultClick(stock)}
            >
              <span className="text-light">{stock.name}</span>
              <span className="text-muted ml-2">({stock.symbol})</span>
            </li>
          ))}
        </ul>
      ) : (
        <p className="text-muted">No results found.</p>
      )}
    </div>
  );
};

export default SearchResults;
