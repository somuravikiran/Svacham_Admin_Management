// src/pages/auth/Register.jsx

import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { registerUser } from "../../services/authService";

function Register() {

    const navigate = useNavigate();

    const [formData, setFormData] = useState({
        fullName: "",
        email: "",
        password: ""
    });

    const handleChange = (e) => {

        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = async (e) => {

        e.preventDefault();

        try {

            await registerUser(formData);

            alert("Registration Successful");

            navigate("/");

        } catch (error) {

            alert("Registration Failed");
        }
    };

    return (

        <div
            className="d-flex justify-content-center align-items-center min-vh-100"
            style={{
                background: "linear-gradient(135deg, #0f172a, #1e293b)"
            }}
        >

            <div
                className="card border-0 shadow-lg p-5"
                style={{
                    width: "100%",
                    maxWidth: "450px",
                    borderRadius: "20px",
                    background: "rgba(255,255,255,0.95)",
                    backdropFilter: "blur(10px)"
                }}
            >

                {/* LOGO */}

                <div className="text-center mb-4">

                    <img
                        src="/logo.png"
                        alt="Logo"
                        style={{
                            width: "140px",
                            height: "auto",
                            objectFit: "contain"
                        }}
                    />

                </div>

                {/* TITLE */}

                <h2
                    className="text-center fw-bold mb-4"
                    style={{
                        color: "#0f172a"
                    }}
                >
                    Create Account
                </h2>

                {/* FORM */}

                <form onSubmit={handleSubmit}>

                    <div className="mb-3">

                        <input
                            type="text"
                            name="fullName"
                            className="form-control py-3"
                            placeholder="Full Name"
                            value={formData.fullName}
                            onChange={handleChange}
                            required
                            style={{
                                borderRadius: "12px"
                            }}
                        />

                    </div>

                    <div className="mb-3">

                        <input
                            type="email"
                            name="email"
                            className="form-control py-3"
                            placeholder="Email Address"
                            value={formData.email}
                            onChange={handleChange}
                            required
                            style={{
                                borderRadius: "12px"
                            }}
                        />

                    </div>

                    <div className="mb-4">

                        <input
                            type="password"
                            name="password"
                            className="form-control py-3"
                            placeholder="Password"
                            value={formData.password}
                            onChange={handleChange}
                            required
                            style={{
                                borderRadius: "12px"
                            }}
                        />

                    </div>

                    <button
                        type="submit"
                        className="btn btn-success w-100 py-3 mb-3"
                        style={{
                            borderRadius: "12px",
                            fontWeight: "600"
                        }}
                    >
                        Register
                    </button>

                    <Link
                        to="/"
                        className="btn btn-secondary w-100 py-3"
                        style={{
                            borderRadius: "12px",
                            fontWeight: "600"
                        }}
                    >
                        Back To Login
                    </Link>

                </form>

            </div>

        </div>
    );
}

export default Register;