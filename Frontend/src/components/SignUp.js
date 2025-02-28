// import React, { useState, useEffect, useRef } from "react";
// import { Link, useNavigate } from "react-router-dom"; // Import useNavigate for redirection
// import { CSSTransition } from "react-transition-group";
// import { useFormik } from "formik";
// import * as Yup from "yup";
// import axios from "axios";
// import { FaEye, FaEyeSlash } from "react-icons/fa"; // Importing the eye icons

// const SignUp = () => {
//   const [showForm, setShowForm] = useState(false);
//   const [loading, setLoading] = useState(false); // Loading state
//   const [success, setSuccess] = useState(false); // Success state
//   const [showPassword, setShowPassword] = useState(false); // State for password visibility
//   const nodeRef = useRef(null); // Ref for CSSTransition
//   const navigate = useNavigate(); // Hook for navigation

//   useEffect(() => {
//     // Show form after component mounts
//     setShowForm(true);
//   }, []);

//   // Validation schema using Yup
//   const validationSchema = Yup.object({
//     fname: Yup.string()
//       .min(3, "First name must be at least 3 characters")
//       .required("First name is required"),
//     lname: Yup.string()
//       .min(3, "Last name must be at least 3 characters")
//       .required("Last name is required"),
//     email: Yup.string()
//       .email("Invalid email address")
//       .matches(/@cognizant\.com$/, "Only @cognizant.com emails are allowed")
//       .required("Email is required"),
//     username: Yup.string().required("Username is required"),
//     password: Yup.string()
//       .min(6, "Password must be at least 6 characters")
//       .required("Password is required"),
//   });

//   // Formik hook
//   const formik = useFormik({
//     initialValues: {
//       fname: "",
//       lname: "",
//       email: "",
//       username: "",
//       password: "",
//     },
//     validationSchema,
//     onSubmit: async (values) => {
//       const backendPayLoad = {
//         username: values.username,
//         password: values.password,
//         fname: values.fname,
//         lname: values.lname,
//         email: values.email,
//         roles: "TRADER",
//       };

//       try {
//         setLoading(true); // Enable loading state
//         const response = await axios.post(
//           "http://localhost:9091/v1/signup",
//           backendPayLoad
//         );
//         console.log("Signup successful", response.data);

//         // Set success state and show success message
//         setSuccess(true);

//         // Redirect to login page after 2 seconds
//         setTimeout(() => {
//           navigate("/login"); // Redirect to login page
//         }, 2000);
//       } catch (error) {
//         console.error("Signup failed", error.response?.data || error.message);
//         alert("Signup failed. Please try again."); // Show error message
//       } finally {
//         setLoading(false); // Disable loading state
//       }
//     },
//   });

//   // Function to toggle password visibility
//   const togglePasswordVisibility = () => {
//     setShowPassword(!showPassword);
//   };

//   return (
//     <div
//       className="container-fluid vh-100 d-flex justify-content-center align-items-center bg-dark"
//       style={{
//         background: "linear-gradient(135deg, #000000, #1D2671)",
//       }}
//     >
//       <Link
//         to="/"
//         className="position-absolute top-0 start-0 m-4 text-light home-link"
//       >
//         <i className="fas fa-home me-2"></i>Home
//       </Link>
//       <CSSTransition
//         in={showForm}
//         timeout={300}
//         classNames="fade"
//         unmountOnExit
//         nodeRef={nodeRef}
//       >
//         <div
//           ref={nodeRef}
//           className="card bg-dark text-light shadow-lg"
//           style={{ width: "25rem" }}
//         >
//           <div className="card-body">
//             <h2 className="card-title text-center mb-4 text-primary">
//               Sign Up for Speculator
//             </h2>

//             {/* Success Message */}
//             {success && (
//               <div className="alert alert-success text-center" role="alert">
//                 Account created successfully! Redirecting to login...
//               </div>
//             )}

//             <form onSubmit={formik.handleSubmit}>
//               <div className="mb-3">
//                 <label htmlFor="fname" className="form-label">
//                   First Name
//                 </label>
//                 <input
//                   type="text"
//                   id="fname"
//                   className="form-control bg-dark text-light border-primary animate__animated animate__fadeInUp animate__faster"
//                   name="fname"
//                   placeholder="Enter your first name"
//                   value={formik.values.fname}
//                   onChange={formik.handleChange}
//                   onBlur={formik.handleBlur}
//                 />
//                 {formik.touched.fname && formik.errors.fname ? (
//                   <div className="text-danger">{formik.errors.fname}</div>
//                 ) : null}
//               </div>
//               <div className="mb-3">
//                 <label htmlFor="lname" className="form-label">
//                   Last Name
//                 </label>
//                 <input
//                   type="text"
//                   id="lname"
//                   className="form-control bg-dark text-light border-primary animate__animated animate__fadeInUp animate__faster"
//                   name="lname"
//                   placeholder="Enter your last name"
//                   value={formik.values.lname}
//                   onChange={formik.handleChange}
//                   onBlur={formik.handleBlur}
//                 />
//                 {formik.touched.lname && formik.errors.lname ? (
//                   <div className="text-danger">{formik.errors.lname}</div>
//                 ) : null}
//               </div>
//               <div className="mb-3">
//                 <label htmlFor="email" className="form-label">
//                   Email
//                 </label>
//                 <input
//                   type="email"
//                   id="email"
//                   className="form-control bg-dark text-light border-primary animate__animated animate__fadeInUp animate__faster"
//                   name="email"
//                   placeholder="Enter your email"
//                   value={formik.values.email}
//                   onChange={formik.handleChange}
//                   onBlur={formik.handleBlur}
//                 />
//                 {formik.touched.email && formik.errors.email ? (
//                   <div className="text-danger">{formik.errors.email}</div>
//                 ) : null}
//               </div>
//               <div className="mb-3">
//                 <label htmlFor="username" className="form-label">
//                   Username
//                 </label>
//                 <input
//                   type="text"
//                   id="username"
//                   className="form-control bg-dark text-light border-primary animate__animated animate__fadeInUp animate__faster"
//                   name="username"
//                   placeholder="Choose a username"
//                   value={formik.values.username}
//                   onChange={formik.handleChange}
//                   onBlur={formik.handleBlur}
//                 />
//                 {formik.touched.username && formik.errors.username ? (
//                   <div className="text-danger">{formik.errors.username}</div>
//                 ) : null}
//               </div>
//               <div className="mb-3">
//                 <label htmlFor="password" className="form-label">
//                   Password
//                 </label>
//                 <div className="input-group">
//                   <input
//                     type={showPassword ? "text" : "password"}
//                     id="password"
//                     className="form-control bg-dark text-light border-primary animate__animated animate__fadeInUp animate__faster"
//                     name="password"
//                     placeholder="Create a password"
//                     value={formik.values.password}
//                     onChange={formik.handleChange}
//                     onBlur={formik.handleBlur}
//                   />
//                   <button
//                     type="button"
//                     className="btn btn-outline-primary password-toggle"
//                     onClick={togglePasswordVisibility}
//                   >
//                     {showPassword ? (
//                       <FaEyeSlash className="icon-white" />
//                     ) : (
//                       <FaEye className="icon-white" />
//                     )}
//                   </button>
//                 </div>
//                 {formik.touched.password && formik.errors.password ? (
//                   <div className="text-danger">{formik.errors.password}</div>
//                 ) : null}
//               </div>
//               <div className="d-grid">
//                 <button
//                   type="submit"
//                   className="btn btn-primary animate__animated animate__fadeInUp animate__faster"
//                   disabled={loading} // Disable button while loading
//                 >
//                   {loading ? "Signing Up..." : "Sign Up"}
//                 </button>
//               </div>
//             </form>
//             <p className="text-center mt-3">
//               Already have an account?{" "}
//               <Link to="/login" className="text-primary">
//                 Login
//               </Link>
//             </p>
//           </div>
//         </div>
//       </CSSTransition>
//     </div>
//   );
// };

// export default SignUp;
import React, { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom"; // Import useNavigate for redirection
import { useFormik } from "formik";
import * as Yup from "yup";
import axios from "axios";
import { FaEye, FaEyeSlash } from "react-icons/fa"; // Importing the eye icons

const SignUp = () => {
  const [showForm, setShowForm] = useState(false);
  const [loading, setLoading] = useState(false); // Loading state
  const [success, setSuccess] = useState(false); // Success state
  const [showPassword, setShowPassword] = useState(false); // State for password visibility
  const navigate = useNavigate(); // Hook for navigation

  useEffect(() => {
    // Show form after component mounts
    setShowForm(true);
  }, []);

  // Validation schema using Yup
  const validationSchema = Yup.object({
    fname: Yup.string()
      .min(3, "First name must be at least 3 characters")
      .required("First name is required"),
    lname: Yup.string()
      .min(3, "Last name must be at least 3 characters")
      .required("Last name is required"),
    email: Yup.string()
      .email("Invalid email address")
      .matches(/@cognizant\.com$/, "Only @cognizant.com emails are allowed")
      .required("Email is required"),
    username: Yup.string().required("Username is required"),
    password: Yup.string()
      .min(6, "Password must be at least 6 characters")
      .required("Password is required"),
  });

  // Formik hook
  const formik = useFormik({
    initialValues: {
      fname: "",
      lname: "",
      email: "",
      username: "",
      password: "",
    },
    validationSchema,
    onSubmit: async (values) => {
      const backendPayLoad = {
        username: values.username,
        password: values.password,
        fname: values.fname,
        lname: values.lname,
        email: values.email,
        roles: "TRADER",
      };

      try {
        setLoading(true); // Enable loading state
        const response = await axios.post(
          "http://localhost:9091/v1/signup",
          backendPayLoad
        );
        console.log("Signup successful", response.data);

        // Set success state and show success message
        setSuccess(true);

        // Redirect to login page after 2 seconds
        setTimeout(() => {
          navigate("/login"); // Redirect to login page
        }, 2000);
      } catch (error) {
        console.error("Signup failed", error.response?.data || error.message);
        alert("Signup failed. Please try again."); // Show error message
      } finally {
        setLoading(false); // Disable loading state
      }
    },
  });

  // Function to toggle password visibility
  const togglePasswordVisibility = () => {
    setShowPassword(!showPassword);
  };

  return (
    <div
      className="container-fluid vh-100 d-flex justify-content-center align-items-center bg-dark"
      style={{
        background: "linear-gradient(135deg, #000000, #1D2671)",
      }}
    >
      <Link
        to="/"
        className="position-absolute top-0 start-0 m-4 text-light home-link"
      >
        <i className="fas fa-home me-2"></i>Home
      </Link>
      {showForm && (
        <div
          className="card bg-dark text-light shadow-lg animate__animated animate__fadeIn"
          style={{ width: "25rem" }}
        >
          <div className="card-body">
            <h2 className="card-title text-center mb-4 text-primary">
              Sign Up for Speculator
            </h2>

            {/* Success Message */}
            {success && (
              <div className="alert alert-success text-center" role="alert">
                Account created successfully! Redirecting to login...
              </div>
            )}

            <form onSubmit={formik.handleSubmit}>
              <div className="mb-3">
                <label htmlFor="fname" className="form-label">
                  First Name
                </label>
                <input
                  type="text"
                  id="fname"
                  className="form-control bg-dark text-light border-primary animate__animated animate__fadeInUp animate__faster"
                  name="fname"
                  placeholder="Enter your first name"
                  value={formik.values.fname}
                  onChange={formik.handleChange}
                  onBlur={formik.handleBlur}
                />
                {formik.touched.fname && formik.errors.fname ? (
                  <div className="text-danger">{formik.errors.fname}</div>
                ) : null}
              </div>
              <div className="mb-3">
                <label htmlFor="lname" className="form-label">
                  Last Name
                </label>
                <input
                  type="text"
                  id="lname"
                  className="form-control bg-dark text-light border-primary animate__animated animate__fadeInUp animate__faster"
                  name="lname"
                  placeholder="Enter your last name"
                  value={formik.values.lname}
                  onChange={formik.handleChange}
                  onBlur={formik.handleBlur}
                />
                {formik.touched.lname && formik.errors.lname ? (
                  <div className="text-danger">{formik.errors.lname}</div>
                ) : null}
              </div>
              <div className="mb-3">
                <label htmlFor="email" className="form-label">
                  Email
                </label>
                <input
                  type="email"
                  id="email"
                  className="form-control bg-dark text-light border-primary animate__animated animate__fadeInUp animate__faster"
                  name="email"
                  placeholder="Enter your email"
                  value={formik.values.email}
                  onChange={formik.handleChange}
                  onBlur={formik.handleBlur}
                />
                {formik.touched.email && formik.errors.email ? (
                  <div className="text-danger">{formik.errors.email}</div>
                ) : null}
              </div>
              <div className="mb-3">
                <label htmlFor="username" className="form-label">
                  Username
                </label>
                <input
                  type="text"
                  id="username"
                  className="form-control bg-dark text-light border-primary animate__animated animate__fadeInUp animate__faster"
                  name="username"
                  placeholder="Choose a username"
                  onChange={formik.handleChange}
                  onBlur={formik.handleBlur}
                />
                {formik.touched.username && formik.errors.username ? (
                  <div className="text-danger">{formik.errors.username}</div>
                ) : null}
              </div>
              <div className="mb-3">
                <label htmlFor="password" className="form-label">
                  Password
                </label>
                <div className="input-group">
                  <input
                    type={showPassword ? "text" : "password"}
                    id="password"
                    className="form-control bg-dark text-light border-primary animate__animated animate__fadeInUp animate__faster"
                    name="password"
                    placeholder="Create a password"
                    value={formik.values.password}
                    onChange={formik.handleChange}
                    onBlur={formik.handleBlur}
                  />
                  <button
                    type="button"
                    className="btn btn-outline-primary password-toggle"
                    onClick={togglePasswordVisibility}
                  >
                    {showPassword ? (
                      <FaEyeSlash className="icon-white" />
                    ) : (
                      <FaEye className="icon-white" />
                    )}
                  </button>
                </div>
                {formik.touched.password && formik.errors.password ? (
                  <div className="text-danger">{formik.errors.password}</div>
                ) : null}
              </div>
              <div className="d-grid">
                <button
                  type="submit"
                  className="btn btn-primary animate__animated animate__fadeInUp animate__faster"
                  disabled={loading} // Disable button while loading
                >
                  {loading ? "Signing Up..." : "Sign Up"}
                </button>
              </div>
            </form>
            <p className="text-center mt-3">
              Already have an account?{" "}
              <Link to="/login" className="text-primary">
                Login
              </Link>
            </p>
          </div>
        </div>
      )}
    </div>
  );
};

export default SignUp;