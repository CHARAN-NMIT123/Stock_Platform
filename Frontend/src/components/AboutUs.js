import { useState } from "react";
import { Star, Users, TrendingUp, Send } from "lucide-react";
import { useNavigate } from "react-router-dom";

const AboutUs = () => {
  const [feedback, setFeedback] = useState("");
  const navigate = useNavigate();

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log("Feedback submitted:", feedback);
    setFeedback("");
    alert("Thank you for your feedback!");
  };

  return (
    <div className="about-us animate__animated animate__fadeIn">
      <div className="bg-gradient-to-b from-primary to-primary-dark text-white py-5 mb-5 position-relative">
        <div className="container">
          {/* Home Button */}
          <button
            className="btn btn-primary position-absolute top-0 end-0 m-3"
            onClick={() => navigate("/home")}
          >
            Home
          </button>

          <h1 className="display-4 fw-bold text-center mb-4">
            About Speculator
          </h1>
          <p className="lead text-center mb-0">
            Empowering individuals with cutting-edge tools and insights for
            smarter financial decisions.
          </p>
        </div>
      </div>

      <div className="container">
        <div className="row mb-5">
          <div className="col-md-4 mb-4 mb-md-0">
            <div className="card h-100 shadow-sm hover-shadow transition-all">
              <div className="card-body text-center">
                <Star className="text-primary mb-3" size={40} />
                <h3 className="h4 mb-3 text-white">Expert Insights</h3>
                <p className="mb-0 text-secondary">
                  Access professional market analysis and expert recommendations
                  to make informed decisions.
                </p>
              </div>
            </div>
          </div>
          <div className="col-md-4 mb-4 mb-md-0">
            <div className="card h-100 shadow-sm hover-shadow transition-all">
              <div className="card-body text-center">
                <Users className="text-primary mb-3" size={40} />
                <h3 className="h4 mb-3 text-white">Community Driven</h3>
                <p className="mb-0 text-secondary">
                  Join a thriving community of traders and investors to share
                  insights and strategies.
                </p>
              </div>
            </div>
          </div>
          <div className="col-md-4">
            <div className="card h-100 shadow-sm hover-shadow transition-all">
              <div className="card-body text-center">
                <TrendingUp className="text-primary mb-3" size={40} />
                <h3 className="h4 mb-3 text-white">Advanced Analytics</h3>
                <p className="mb-0 text-secondary">
                  Leverage powerful tools for in-depth market analysis and
                  portfolio management.
                </p>
              </div>
            </div>
          </div>
        </div>

        <div className="row mb-5">
          <div className="col-lg-8 mx-auto">
            <h2 className="text-primary mb-4 text-center">Our Mission</h2>
            <p className="mb-4">
              At Speculator, we're on a mission to democratize finance and
              empower individuals with the tools and knowledge needed to
              navigate the complex world of investment. Founded by a team of
              finance experts and tech enthusiasts, we combine cutting-edge
              technology with deep market insights to provide you with a
              seamless trading experience.
            </p>
            <p className="mb-5">
              Whether you're a seasoned investor or just starting out,
              Speculator is designed to help you make informed decisions, manage
              risk effectively, and maximize your potential returns. Join us in
              shaping the future of personal finance and investment.
            </p>
          </div>
        </div>

        <div className="row mb-5">
          <div className="col-lg-8 mx-auto">
            <div className="card shadow">
              <div className="card-body">
                <h3 className="text-primary mb-4 text-center">
                  We'd Love to Hear From You!
                </h3>
                <form onSubmit={handleSubmit} className="feedback-form">
                  <div className="mb-3">
                    <label htmlFor="feedback" className="form-label">
                      Your Feedback
                    </label>
                    <textarea
                      id="feedback"
                      className="form-control"
                      value={feedback}
                      onChange={(e) => setFeedback(e.target.value)}
                      rows="4"
                      required
                      placeholder="Share your thoughts, suggestions, or experiences..."
                    ></textarea>
                  </div>
                  <div className="text-center">
                    <button type="submit" className="btn btn-primary btn-lg">
                      Submit Feedback <Send size={18} className="ms-2" />
                    </button>
                  </div>
                </form>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AboutUs;
