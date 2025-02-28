import React from "react";
import { PlusCircle } from "lucide-react";
import { useFormik } from "formik";
import * as Yup from "yup";
import axios from "axios";

const CreateCompany = ({ fetchStocks }) => {
  // Validation schema using Yup
  const validationSchema = Yup.object({
    name: Yup.string().required("Stock Name is required"),
    symbol: Yup.string()
      .min(3, "Symbol must be at least 3 characters")
      .required("Stock Symbol is required"),
    sharesToRelease: Yup.number()
      .min(1, "Shares to Release must be greater than 0")
      .required("Shares to Release is required"),
    companyType: Yup.string().required("Company Type is required"),
    entryPrice: Yup.number()
      .min(0.01, "Entry Price must be greater than 0")
      .required("Entry Price is required"),
  });

  // Formik hook
  const formik = useFormik({
    initialValues: {
      name: "",
      symbol: "",
      sharesToRelease: "",
      companyType: "",
      entryPrice: "",
    },
    validationSchema,
    onSubmit: async (values) => {
      const backendPayload = {
        name: values.name,
        symbol: values.symbol,
        totalShares: values.sharesToRelease,
        type: values.companyType,
        open: values.entryPrice,
        last: 0,
      };
      const token = sessionStorage.getItem("token");
      formik.setSubmitting(true);
      try {
        // Send the payload to the backend using Axios
        const response = await axios.post(
          "http://localhost:9091/api/stocks/companies/new",
          backendPayload,
          {
            headers: {
              Authorization: `Bearer ${token}`, // Include the JWT token
            },
          }
        );

        // Log the response (for debugging)
        console.log("Stock registration successful:", response.data);

        // Call the parent function (if needed)

        fetchStocks();
        // Reset the form
        formik.resetForm();
      } catch (error) {
        // Handle errors (e.g., display error message)
        console.error(
          "Stock registration failed:",
          error.response?.data || error.message
        );
      } finally {
        formik.setSubmitting(false);
      }
    },
  });

  return (
    <div className="card bg-dark border-primary mb-4">
      <div className="card-body">
        <h5 className="card-title text-primary">
          <PlusCircle size={20} className="me-2" />
          New Stock Registration
        </h5>
        <form onSubmit={formik.handleSubmit}>
          {/* Stock Name */}
          <div className="mb-3">
            <input
              type="text"
              className={`form-control bg-dark text-light border-primary ${
                formik.touched.name && formik.errors.name ? "is-invalid" : ""
              }`}
              placeholder="Stock Name"
              name="name"
              value={formik.values.name}
              onChange={formik.handleChange}
              onBlur={formik.handleBlur}
            />
            {formik.touched.name && formik.errors.name ? (
              <div className="invalid-feedback">{formik.errors.name}</div>
            ) : null}
          </div>

          {/* Stock Symbol */}
          <div className="mb-3">
            <input
              type="text"
              className={`form-control bg-dark text-light border-primary ${
                formik.touched.symbol && formik.errors.symbol
                  ? "is-invalid"
                  : ""
              }`}
              placeholder="Stock Symbol"
              name="symbol"
              value={formik.values.symbol}
              onChange={formik.handleChange}
              onBlur={formik.handleBlur}
            />
            {formik.touched.symbol && formik.errors.symbol ? (
              <div className="invalid-feedback">{formik.errors.symbol}</div>
            ) : null}
          </div>

          {/* Number of Shares to Release */}
          <div className="mb-3">
            <input
              type="number"
              className={`form-control bg-dark text-light border-primary ${
                formik.touched.sharesToRelease && formik.errors.sharesToRelease
                  ? "is-invalid"
                  : ""
              }`}
              placeholder="Number of Shares to Release"
              name="sharesToRelease"
              value={formik.values.sharesToRelease}
              onChange={formik.handleChange}
              onBlur={formik.handleBlur}
            />
            {formik.touched.sharesToRelease && formik.errors.sharesToRelease ? (
              <div className="invalid-feedback">
                {formik.errors.sharesToRelease}
              </div>
            ) : null}
          </div>

          {/* Type of Company */}
          <div className="mb-3">
            <div className="input-group position-relative">
              <select
                className={`form-select bg-dark text-light border-primary pe-5 ${
                  formik.touched.companyType && formik.errors.companyType
                    ? "is-invalid"
                    : ""
                }`}
                name="companyType"
                value={formik.values.companyType}
                onChange={formik.handleChange}
                onBlur={formik.handleBlur}
              >
                <option value="" disabled>
                  Select Type of Company
                </option>
                <option value="construction">Construction</option>
                <option value="technology">Technology</option>
                <option value="finance">Finance</option>
                <option value="Other">Other</option>
              </select>
              <span className="position-absolute end-0 top-0 bottom-0 d-flex align-items-center pe-3 bg-transparent">
                <i className="bi bi-chevron-down text-light"></i>
              </span>
            </div>
            {formik.touched.companyType && formik.errors.companyType ? (
              <div className="invalid-feedback">
                {formik.errors.companyType}
              </div>
            ) : null}
          </div>

          {/* Entry Price */}
          <div className="mb-3">
            <input
              type="number"
              className={`form-control bg-dark text-light border-primary ${
                formik.touched.entryPrice && formik.errors.entryPrice
                  ? "is-invalid"
                  : ""
              }`}
              placeholder="Entry Price"
              name="entryPrice"
              value={formik.values.entryPrice}
              onChange={formik.handleChange}
              onBlur={formik.handleBlur}
            />
            {formik.touched.entryPrice && formik.errors.entryPrice ? (
              <div className="invalid-feedback">{formik.errors.entryPrice}</div>
            ) : null}
          </div>

          {/* Submit Button */}
          <button
            type="submit"
            className="btn btn-primary w-100"
            disabled={formik.isSubmitting}
          >
            {formik.isSubmitting ? "Submitting..." : "Submit"}
          </button>
        </form>
      </div>
    </div>
  );
};

export default CreateCompany;
