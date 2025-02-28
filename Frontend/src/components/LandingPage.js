import React, { useEffect, useRef } from "react";
import { Link } from "react-router-dom";

const LandingPage = () => {
  const canvasRef = useRef(null);

  useEffect(() => {
    const canvas = canvasRef.current;
    const ctx = canvas.getContext("2d");
    canvas.width = window.innerWidth;
    canvas.height = window.innerHeight;

    // Modern color palette
    const colors = {
      primary: "#6366f1", // Indigo
      secondary: "#ec4899", // Pink
      background: "#0f172a", // Navy
    };

    // Particle system
    class Particle {
      constructor() {
        this.reset();
        this.velocity = {
          x: (Math.random() - 0.5) * 0.5,
          y: (Math.random() - 0.5) * 0.5,
        };
      }

      reset() {
        this.x = Math.random() * canvas.width;
        this.y = Math.random() * canvas.height;
        this.size = Math.random() * 2 + 1;
        this.baseSize = this.size;
        this.color = Math.random() > 0.5 ? colors.primary : colors.secondary;
      }

      update() {
        this.x += this.velocity.x;
        this.y += this.velocity.y;

        // Keep particles within bounds
        if (this.x < 0 || this.x > canvas.width) this.velocity.x *= -1;
        if (this.y < 0 || this.y > canvas.height) this.velocity.y *= -1;

        // Size animation
        this.size = this.baseSize * (0.8 + Math.sin(Date.now() * 0.005) * 0.2);
      }

      draw() {
        ctx.beginPath();
        ctx.arc(this.x, this.y, this.size, 0, Math.PI * 2);
        ctx.fillStyle = this.color;
        ctx.fill();
      }
    }

    // Create particles
    const particles = Array.from({ length: 100 }, () => new Particle());

    // Mouse interaction
    const mouse = { x: null, y: null, radius: 100 };
    const handleMouseMove = (e) => {
      mouse.x = e.clientX;
      mouse.y = e.clientY;
    };
    canvas.addEventListener("mousemove", handleMouseMove);

    const animate = () => {
      ctx.fillStyle = colors.background;
      ctx.fillRect(0, 0, canvas.width, canvas.height);

      particles.forEach((particle) => {
        particle.update();

        // Mouse interaction
        const dx = mouse.x - particle.x;
        const dy = mouse.y - particle.y;
        const distance = Math.sqrt(dx * dx + dy * dy);
        if (distance < mouse.radius) {
          const force = (mouse.radius - distance) / mouse.radius;
          particle.x -= dx * force * 0.1;
          particle.y -= dy * force * 0.1;
        }

        particle.draw();

        // Draw connections between nearby particles
        particles.forEach((other) => {
          const dx = other.x - particle.x;
          const dy = other.y - particle.y;
          const distance = Math.sqrt(dx * dx + dy * dy);

          if (distance < 100) {
            ctx.beginPath();
            ctx.strokeStyle = particle.color;
            ctx.lineWidth = 0.2;
            ctx.globalAlpha = 1 - distance / 100;
            ctx.moveTo(particle.x, particle.y);
            ctx.lineTo(other.x, other.y);
            ctx.stroke();
          }
        });
      });

      ctx.globalAlpha = 1;
      requestAnimationFrame(animate);
    };

    animate();

    const handleResize = () => {
      canvas.width = window.innerWidth;
      canvas.height = window.innerHeight;
      particles.forEach((particle) => particle.reset());
    };

    window.addEventListener("resize", handleResize);
    return () => {
      window.removeEventListener("resize", handleResize);
      canvas.removeEventListener("mousemove", handleMouseMove);
    };
  }, []);

  return (
    <div className="landing-page vh-100 position-relative overflow-hidden">
      <canvas ref={canvasRef} className="background-animation" />

      <div className="position-absolute top-50 start-50 translate-middle text-center">
        <div className="glassmorphism p-5 rounded-4 shadow">
          <h1 className="display-1 mb-4 text-white fw-bold">
            Spec<span className="text-primary">ulator</span>
          </h1>
          <p className="lead text-light mb-4 opacity-75">
            Intelligent Investing for the Modern Era
          </p>
          <div className="d-flex gap-3 justify-content-center">
            <Link
              to="/signup"
              className="btn btn-lg btn-primary px-5 py-3 rounded-pill fw-bold hover-glow"
            >
              Get Started
            </Link>
            <Link
              to="/login"
              className="btn btn-lg btn-outline-light px-5 py-3 rounded-pill fw-bold"
            >
              Existing Account
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
};

export default LandingPage;
