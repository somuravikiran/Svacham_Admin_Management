import { Link } from "react-router-dom";
import { useState } from "react";

function Sidebar() {

    const [isOpen, setIsOpen] = useState(false);

    const menuItems = [
        { name: "Dashboard", path: "/dashboard" },
        { name: "Clients", path: "/clients" },
        { name: "Orders", path: "/orders" },
        { name: "GST Bills", path: "/gst-bills" },
        { name: "Stock", path: "/stock" },
        { name: "Salary", path: "/salary" },
        { name: "Spending", path: "/spending" }
    ];

    return (

        <>

            {/* MENU BUTTON */}

            <button
                onMouseEnter={() => setIsOpen(true)}
                style={{
                    position: "fixed",
                    top: "20px",
                    left: "20px",
                    zIndex: 1001,
                    background: "#2563eb",
                    color: "white",
                    border: "none",
                    borderRadius: "10px",
                    padding: "10px 14px",
                    cursor: "pointer",
                    boxShadow: "0 4px 10px rgba(0,0,0,0.2)"
                }}
            >
                ☰
            </button>

            {/* SIDEBAR */}

            <div
                onMouseEnter={() => setIsOpen(true)}
                onMouseLeave={() => setIsOpen(false)}
                style={{
                    width: "240px",
                    height: "100vh",
                    background: "linear-gradient(180deg, #0f172a, #1e293b)",
                    color: "white",
                    padding: "20px",
                    position: "fixed",
                    top: 0,
                    left: isOpen ? "0" : "-260px",
                    transition: "0.4s ease",
                    boxShadow: "4px 0 15px rgba(0,0,0,0.3)",
                    zIndex: 1000
                }}
            >

                {/* LOGO */}

                <div
                    style={{
                        marginBottom: "35px",
                        textAlign: "center"
                    }}
                >

                    <img
                        src="/logo.png"
                        alt="SVACHAM Logo"
                        style={{
                            width: "150px",
                            borderRadius: "12px"
                        }}
                    />

                </div>

                {/* MENU ITEMS */}

                <div
                    style={{
                        display: "flex",
                        flexDirection: "column",
                        gap: "12px"
                    }}
                >

                    {menuItems.map((item, index) => (

                        <Link
                            key={index}
                            to={item.path}
                            style={{
                                color: "white",
                                textDecoration: "none",
                                padding: "12px 16px",
                                borderRadius: "12px",
                                background: "rgba(255,255,255,0.05)",
                                transition: "0.3s",
                                fontWeight: "500"
                            }}
                            onMouseOver={(e) => {
                                e.target.style.background = "#2563eb";
                                e.target.style.transform = "translateX(6px)";
                            }}
                            onMouseOut={(e) => {
                                e.target.style.background = "rgba(255,255,255,0.05)";
                                e.target.style.transform = "translateX(0px)";
                            }}
                        >
                            {item.name}
                        </Link>

                    ))}

                </div>

            </div>

        </>

    );
}

export default Sidebar;