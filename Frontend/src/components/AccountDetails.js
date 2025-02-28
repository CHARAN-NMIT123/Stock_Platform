import React, { useState, useEffect } from "react";
import { User, Mail, AtSign } from "lucide-react";
import { Link } from "react-router-dom";
import axios from "axios";

const AccountDetails = () => {
  const [accountDetails, setAccountDetails] = useState({
    firstName: "",
    lastName: "",
    email: "",
    username: "",
  });
  const [loading, setLoading] = useState(true); // Loading state
  const [error, setError] = useState(null); // Error state

  useEffect(() => {
    // Fetch account details from the backend
    const fetchAccountDetails = async () => {
      try {
        // Get the JWT token from local storage
        const token = sessionStorage.getItem("token");
        const accountId = sessionStorage.getItem("accountId");

        // Make the request with the Authorization header
        const response = await axios.get(
          `http://localhost:9091/api/stocktrader/${accountId}`,
          {
            headers: {
              Authorization: `Bearer ${token}`, // Include the JWT token
            },
          }
        );

        // Map the backend response to the frontend state
        setAccountDetails({
          firstName: response.data.fname,
          lastName: response.data.lname,
          email: response.data.email,
          username: response.data.accountId,
        });

        setLoading(false); // Disable loading state
      } catch (error) {
        console.error("Failed to fetch account details:", error);
        setError("Failed to fetch account details. Please try again later."); // Set error message
        setLoading(false); // Disable loading state
      }
    };

    fetchAccountDetails(); // Call the function
  }, []);

  if (loading) {
    return (
      <div className="text-center text-light">Loading account details...</div>
    ); // Loading state
  }

  if (error) {
    return <div className="text-center text-danger">{error}</div>; // Error state
  }

  return (
    <div className="account-details animate__animated animate__fadeIn">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <Link to="/home" className="btn btn-primary">
          Home
        </Link>
        <h2 className="text-primary mb-0">Account Details</h2>
      </div>
      <div className="card bg-dark">
        <div className="card-body">
          {/* First Name */}
          <div className="mb-4 animate__animated animate__fadeInUp animate__delay-1s">
            <label className="form-label text-light">First Name</label>
            <div className="input-group">
              <span className="input-group-text bg-primary text-light">
                <User size={18} />
              </span>
              <input
                type="text"
                className="form-control bg-dark text-light"
                value={accountDetails.firstName}
                readOnly
              />
            </div>
          </div>

          {/* Last Name */}
          <div className="mb-4 animate__animated animate__fadeInUp animate__delay-2s">
            <label className="form-label text-light">Last Name</label>
            <div className="input-group">
              <span className="input-group-text bg-primary text-light">
                <User size={18} />
              </span>
              <input
                type="text"
                className="form-control bg-dark text-light"
                value={accountDetails.lastName}
                readOnly
              />
            </div>
          </div>

          {/* Email */}
          <div className="mb-4 animate__animated animate__fadeInUp animate__delay-3s">
            <label className="form-label text-light">Email</label>
            <div className="input-group">
              <span className="input-group-text bg-primary text-light">
                <Mail size={18} />
              </span>
              <input
                type="email"
                className="form-control bg-dark text-light"
                value={accountDetails.email}
                readOnly
              />
            </div>
          </div>

          {/* Username */}
          <div className="mb-4 animate__animated animate__fadeInUp animate__delay-4s">
            <label className="form-label text-light">Username</label>
            <div className="input-group">
              <span className="input-group-text bg-primary text-light">
                <AtSign size={18} />
              </span>
              <input
                type="text"
                className="form-control bg-dark text-light"
                value={accountDetails.username}
                readOnly
              />
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AccountDetails;
