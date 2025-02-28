import React from "react";
import { Link } from "react-router-dom";
import { useFormik } from "formik";
import * as Yup from "yup";
import axios from "axios";

const UpdateAccount = () => {
  // Validation schema using Yup (username and password fields removed)
  const validationSchema = Yup.object({
    firstName: Yup.string()
      .min(3, "First name must be at least 3 characters")
      .required("First name is required"),
    lastName: Yup.string()
      .min(3, "Last name must be at least 3 characters")
      .required("Last name is required"),
    email: Yup.string()
      .email("Invalid email address")
      .matches(/@cognizant\.com$/, "Only @cognizant.com emails are allowed")
      .required("Email is required"),
  });

  // Formik hook (username and password fields removed from initial values)
  const formik = useFormik({
    initialValues: {
      firstName: "",
      lastName: "",
      email: "",
    },
    validationSchema,
    onSubmit: async (values, { resetForm }) => {
      try {
        // Set loading state to true
        formik.setSubmitting(true);

        const backendPayLoad = {
          fname: values.firstName,
          lname: values.lastName,
          email: values.email,
        };

        const token = sessionStorage.getItem("token");
        const accountId = sessionStorage.getItem("accountId");
        // Send the updated data to the backend using Axios
        const response = await axios.put(
          `http://localhost:9091/api/stocktrader/update/${accountId}`, // Update endpoint
          backendPayLoad, // Payload
          {
            headers: {
              Authorization: `Bearer ${token}`, // Include the JWT token
            },
          }
        );

        // Log the response (for debugging)
        console.log("Account updated successfully:", response.data);

        // Display success message
        alert("Account updated successfully!");

        // Reset the form fields
        resetForm();
      } catch (error) {
        // Handle errors (e.g., display error message)
        console.error(
          "Failed to update account:",
          error.response?.data || error.message
        );
        alert("Failed to update account. Please try again.");
      } finally {
        // Set loading state to false
        formik.setSubmitting(false);
      }
    },
  });

  return (
    <div className="update-account animate__animated animate__fadeIn">
      <Link
        to="/home"
        className="btn btn-primary position-absolute top-0 end-0 m-3"
      >
        Home
      </Link>
      <h2 className="mb-4 text-primary">Update Account</h2>

      <form onSubmit={formik.handleSubmit}>
        {/* First Name */}
        <div className="mb-3">
          <label htmlFor="firstName" className="form-label">
            First Name
          </label>
          <input
            type="text"
            className={`form-control ${
              formik.touched.firstName && formik.errors.firstName
                ? "is-invalid"
                : ""
            }`}
            id="firstName"
            name="firstName"
            value={formik.values.firstName}
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
          />
          {formik.touched.firstName && formik.errors.firstName ? (
            <div className="invalid-feedback">{formik.errors.firstName}</div>
          ) : null}
        </div>

        {/* Last Name */}
        <div className="mb-3">
          <label htmlFor="lastName" className="form-label">
            Last Name
          </label>
          <input
            type="text"
            className={`form-control ${
              formik.touched.lastName && formik.errors.lastName
                ? "is-invalid"
                : ""
            }`}
            id="lastName"
            name="lastName"
            value={formik.values.lastName}
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
          />
          {formik.touched.lastName && formik.errors.lastName ? (
            <div className="invalid-feedback">{formik.errors.lastName}</div>
          ) : null}
        </div>

        {/* Email */}
        <div className="mb-3">
          <label htmlFor="email" className="form-label">
            Email address
          </label>
          <input
            type="email"
            className={`form-control ${
              formik.touched.email && formik.errors.email ? "is-invalid" : ""
            }`}
            id="email"
            name="email"
            value={formik.values.email}
            onChange={formik.handleChange}
            onBlur={formik.handleBlur}
          />
          {formik.touched.email && formik.errors.email ? (
            <div className="invalid-feedback">{formik.errors.email}</div>
          ) : null}
        </div>

        {/* Submit Button */}
        <button
          type="submit"
          className="btn btn-primary"
          disabled={formik.isSubmitting} // Disable button while submitting
        >
          {formik.isSubmitting ? "Updating..." : "Update"}
        </button>
      </form>
    </div>
  );
};

export default UpdateAccount;
