import React, { useEffect } from "react";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { useLocation } from "react-router-dom";
import axios from "axios";
import { useFormik } from "formik";
import * as Yup from "yup";

const TradeTab = ({ stocks }) => {
  const location = useLocation();

  const formik = useFormik({
    initialValues: {
      tradeType: "buy",
      symbol: "",
      stockName: "",
      quantity: "",
      useRiskManagement: false,
      riskPerTrade: "",
      stopLoss: "",
    },
    validationSchema: Yup.object({
      symbol: Yup.string().required("Required"),
      stockName: Yup.string().required("Required"),
      quantity: Yup.number().when("useRiskManagement", {
        is: false,
        then: () => Yup.number().required("Required").positive("Must be greater than 0"),
        otherwise: () => Yup.number().notRequired(),
      }),
      riskPerTrade: Yup.number().when(["useRiskManagement", "tradeType"], {
        is: (useRiskManagement, tradeType) => useRiskManagement && tradeType === "buy",
        then: () => Yup.number().required("Required").positive("Risk must be positive"),
        otherwise: () => Yup.number().notRequired(),
      }),
      stopLoss: Yup.number().when("useRiskManagement", {
        is: true,
        then: () => Yup.number()
          .required("Required")
          .positive("StopLoss must be positive")
          .test(
            "max-stopLoss",
            function (value) {
              const { symbol } = this.parent;
              const stock = stocks.find((stock) => stock.symbol === symbol);
              if (stock) {
                return value < stock.open || this.createError({
                  message: `StopLoss cannot be more than or equal to stock's opening price of ${stock.open}`,
                });
              }
              return true;
            }
          ),
        otherwise: () => Yup.number().notRequired(),
      }),
    }),
    onSubmit: async (values) => {
      const stock = stocks.find(
        (stock) =>
          stock.symbol === values.symbol && stock.name === values.stockName
      );

      if (!stock) {
        alert("Invalid stock symbol or name. Please enter correct details.");
        return;
      }

      const id = stock.stockId;
      const token = sessionStorage.getItem("token");
      const price_per_share = stock.open;
      const total_price = stock.open * values.quantity;
      const accountId = sessionStorage.getItem("accountId");
      const unit_risk = (values.riskPerTrade / 100) * price_per_share;
      const basePayload = {
        stockId: id,
        transType: values.tradeType,
        symbol: values.symbol,
        stockName: values.stockName,
        numShares: values.quantity,
        accountId,
      };
      const r_basePayload = {
        stockId: id,
        transType: values.tradeType,
        symbol: values.symbol,
        stockName: values.stockName,
        numShares: 0,
        accountId,
      };
      const payloads = {
        buyWithoutRisk: {
          ...basePayload,
          typeOfPurchase: "marketplan",
          typeOfSell: "marketplan",
          riskPerTrade: 0,
          stopLoss: 0,
          entryPrice: total_price,
        },
        buyWithRisk: {
          ...r_basePayload,
          typeOfPurchase: "positionSizing",
          typeOfSell: "marketplan",
          riskPerTrade: unit_risk,
          stopLoss: values.stopLoss,
          entryPrice: price_per_share,
        },
        sellWithoutRisk: {
          ...basePayload,
          typeOfPurchase: "marketplan",
          typeOfSell: "marketplan",
          riskPerTrade: 0,
          stopLoss: 0,
          entryPrice: total_price,
        },
        sellWithRisk: {
          ...r_basePayload,
          typeOfPurchase: "marketplan",
          typeOfSell: "stoploss",
          riskPerTrade: 0,
          stopLoss: values.stopLoss,
          entryPrice: price_per_share,
        },
      };
      console.log(payloads);
      try {
        if (values.tradeType === "buy") {
          if (values.useRiskManagement) {
            await axios.post(
              "http://localhost:9090/api/orders/buy/positionSizing",
              payloads.buyWithRisk,
              {
                headers: {
                  Authorization: `Bearer ${token}`,
                },
              }
            );
          } else {
            await axios.post(
              "http://localhost:9090/api/orders/buy/MarketPlan",
              payloads.buyWithoutRisk,
              {
                headers: {
                  Authorization: `Bearer ${token}`,
                },
              }
            );
          }
        } else if (values.tradeType === "sell") {
          if (values.useRiskManagement) {
            await axios.post(
              "http://localhost:9090/api/orders/sell/stopLoss",
              payloads.sellWithRisk,
              {
                headers: {
                  Authorization: `Bearer ${token}`,
                },
              }
            );
          } else {
            await axios.post(
              "http://localhost:9090/api/orders/sell/MarketPlan",
              payloads.sellWithoutRisk,
              {
                headers: {
                  Authorization: `Bearer ${token}`,
                },
              }
            );
          }
        }
        toast.success("Trade successfully placed!");
      } catch (error) {
        toast.error("Failed to place order. Please try again.");
      }
    },
  });

  useEffect(() => {
    if (location.state) {
      formik.setFieldValue("tradeType", location.state.type);
      formik.setFieldValue("symbol", location.state.symbol);
      formik.setFieldValue("stockName", location.state.stockName);
    }
  }, [location.state]);

  return (
    <div className="trade animate__animated animate__fadeIn">
      <h2 className="mb-4 text-primary">Trade</h2>
      <form onSubmit={formik.handleSubmit}>
        <div className="mb-3">
          <div className="btn-group" role="group">
            <input
              type="radio"
              className="btn-check"
              name="tradeType"
              id="buyOption"
              value="buy"
              checked={formik.values.tradeType === "buy"}
              onChange={formik.handleChange}
            />
            <label className="btn btn-outline-primary" htmlFor="buyOption">
              Buy
            </label>
            <input
              type="radio"
              className="btn-check"
              name="tradeType"
              id="sellOption"
              value="sell"
              checked={formik.values.tradeType === "sell"}
              onChange={formik.handleChange}
            />
            <label className="btn btn-outline-primary" htmlFor="sellOption">
              Sell
            </label>
          </div>
        </div>
        <div className="mb-3">
          <input
            type="text"
            className="form-control"
            placeholder="Stock Symbol"
            name="symbol"
            value={formik.values.symbol}
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
            required
          />
          {formik.touched.symbol && formik.errors.symbol ? (
            <div className="text-danger">{formik.errors.symbol}</div>
          ) : null}
        </div>
        <div className="mb-3">
          <input
            type="text"
            className="form-control"
            placeholder="Stock Name"
            name="stockName"
            value={formik.values.stockName}
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
            required
          />
          {formik.touched.stockName && formik.errors.stockName ? (
            <div className="text-danger">{formik.errors.stockName}</div>
          ) : null}
        </div>
        {!formik.values.useRiskManagement && (
          <div className="mb-3">
            <input
              type="number"
              className="form-control"
              placeholder="Quantity"
              name="quantity"
              value={formik.values.quantity}
              onChange={formik.handleChange}
              onBlur={formik.handleBlur}
              required
            />
            {formik.touched.quantity && formik.errors.quantity ? (
              <div className="text-danger">{formik.errors.quantity}</div>
            ) : null}
          </div>
        )}
        <div className="mb-3 form-check">
          <input
            type="checkbox"
            className="form-check-input"
            id="useRiskManagement"
            name="useRiskManagement"
            checked={formik.values.useRiskManagement}
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
          />
          <label
            className="form-check-label text-light"
            htmlFor="useRiskManagement"
          >
            Use Risk Management
          </label>
        </div>
        {formik.values.useRiskManagement && (
          <>
            <div className="mb-3">
              <input
                type="number"
                className="form-control"
                placeholder="Stop Loss"
                name="stopLoss"
                value={formik.values.stopLoss}
                onChange={formik.handleChange}
                onBlur={formik.handleBlur}
              />
              {formik.touched.stopLoss && formik.errors.stopLoss ? (
                <div className="text-danger">{formik.errors.stopLoss}</div>
              ) : null}
            </div>
            {formik.values.tradeType === "buy" && (
              <div className="mb-3">
                <input
                  type="number"
                  className="form-control"
                  placeholder="Risk Per Trade (%)"
                  name="riskPerTrade"
                  value={formik.values.riskPerTrade}
                  onChange={formik.handleChange}
                  onBlur={formik.handleonBlur}
                  required
                />
                {formik.touched.riskPerTrade && formik.errors.riskPerTrade ? (
                  <div className="text-danger">{formik.errors.riskPerTrade}</div>
                ) : null}
              </div>
            )}
          </>
        )}
        <button type="submit" className="btn btn-primary">
          Place Order
        </button>
      </form>
      <ToastContainer />
    </div>
  );
  };
  
  export default TradeTab;