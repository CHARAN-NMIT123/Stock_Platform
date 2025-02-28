import React, { useState } from "react";
import { X } from "lucide-react";

const UpdateCompany = ({ stock, onUpdate, onCancel, isUpdating }) => {
  const [updatedStock, setUpdatedStock] = useState(stock);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setUpdatedStock((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    onUpdate(updatedStock);
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-75 flex items-center justify-center z-50 animate__animated animate__fadeIn">
      <div className="bg-dark border border-primary p-6 rounded-lg w-full max-w-md shadow-lg animate-zoomIn">
        {/* Header */}
        <div className="flex justify-between items-center mb-6">
          <h2 className="text-xl font-semibold text-primary">Update Stock</h2>
          <button
            onClick={onCancel}
            className="text-gray-400 hover:text-gray-200 transition-colors p-1 rounded-full hover:bg-gray-700"
          >
            <X size={24} />
          </button>
        </div>

        {/* Form */}
        <form onSubmit={handleSubmit}>
          {/* Stock Name */}
          <div className="mb-4">
            <label
              htmlFor="name"
              className="block text-sm font-medium text-gray-400 mb-2"
            >
              Stock Name
            </label>
            <input
              type="text"
              id="name"
              name="name"
              value={updatedStock.name}
              onChange={handleInputChange}
              className="w-full px-4 py-2 bg-dark border border-primary rounded-md text-light focus:outline-none focus:border-blue-500 transition-colors"
              required
            />
          </div>

          {/* Stock Symbol */}
          <div className="mb-4">
            <label
              htmlFor="symbol"
              className="block text-sm font-medium text-gray-400 mb-2"
            >
              Stock Symbol
            </label>
            <input
              type="text"
              id="symbol"
              name="symbol"
              value={updatedStock.symbol}
              onChange={handleInputChange}
              className="w-full px-4 py-2 bg-dark border border-primary rounded-md text-light focus:outline-none focus:border-blue-500 transition-colors"
              required
            />
          </div>

          {/* Current Price */}
          <div className="mb-4">
            <label
              htmlFor="currentPrice"
              className="block text-sm font-medium text-gray-400 mb-2"
            >
              Current Price
            </label>
            <input
              type="number"
              id="open"
              name="open"
              value={updatedStock.open}
              onChange={handleInputChange}
              className="w-full px-4 py-2 bg-dark border border-primary rounded-md text-light focus:outline-none focus:border-blue-500 transition-colors"
              required
            />
          </div>
          <div className="mb-4">
            <label
              htmlFor="currentPrice"
              className="block text-sm font-medium text-gray-400 mb-2"
            >
              Last Price
            </label>
            <input
              type="number"
              id="last"
              name="last"
              value={updatedStock.last}
              onChange={handleInputChange}
              className="w-full px-4 py-2 bg-dark border border-primary rounded-md text-light focus:outline-none focus:border-blue-500 transition-colors"
              required
            />
          </div>

          {/* Total Shares */}
          <div className="mb-4">
            <label
              htmlFor="totalShares"
              className="block text-sm font-medium text-gray-400 mb-2"
            >
              Total Shares
            </label>
            <input
              type="number"
              id="totalShares"
              name="totalShares"
              value={updatedStock.totalShares}
              onChange={handleInputChange}
              className="w-full px-4 py-2 bg-dark border border-primary rounded-md text-light focus:outline-none focus:border-blue-500 transition-colors"
              required
            />
          </div>

          {/* Company Type */}
          <div className="mb-6">
            <label
              htmlFor="type"
              className="block text-sm font-medium text-gray-400 mb-2"
            >
              Company Type
            </label>
            <select
              id="type"
              name="type"
              value={updatedStock.type}
              onChange={handleInputChange}
              className="w-full px-4 py-2 bg-dark border border-primary rounded-md text-light focus:outline-none focus:border-blue-500 transition-colors"
              required
            >
              <option value="" disabled>
                Select Type of Company
              </option>
              <option value="construction">Construction</option>
              {/* <option value="construction">construction</option> */}
              <option value="technology">Technology</option>
              <option value="finance">Finance</option>
              <option value="Other">Other</option>
            </select>
          </div>

          {/* Buttons */}
          <div className="flex justify-end space-x-3">
            <button
              type="button"
              onClick={onCancel}
              className="btn btn-primary w-50"
              disabled={isUpdating}
            >
              Cancel
            </button>
            <button
              type="submit"
              className="btn btn-primary w-100"
              disabled={isUpdating}
            >
              {isUpdating ? "Updating..." : "Update Stock"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default UpdateCompany;
