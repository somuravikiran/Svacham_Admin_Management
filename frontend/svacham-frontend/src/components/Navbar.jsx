import { useNavigate } from "react-router-dom";

function Navbar() {

    const navigate = useNavigate();

    const logout = () => {
        localStorage.removeItem("token");
        navigate("/login");
    };

    return (
        <div
            style={{
                height: "65px",
                background: "linear-gradient(90deg, #1e3c72, #2a5298)",
                display: "flex",
                justifyContent: "space-between",
                alignItems: "center",
                padding: "0 25px",
                color: "white",
                boxShadow: "0 4px 12px rgba(0,0,0,0.15)",
                position: "sticky",
                top: 0,
                zIndex: 1000
            }}
        >
            <div
                style={{
                    marginBottom: "25px",
                    textAlign: "center",
                    marginLeft: "2cm"
                }}
            >

                <img
                    src="/logo.png"
                    alt="SVACHAM Logo"
                    style={{
                        width: "140px",
                        height: "auto",
                        objectFit: "contain"
                    }}
                />

            </div>

            <button
                onClick={logout}
                style={{
                    background: "#ff4d4f",
                    border: "none",
                    padding: "10px 18px",
                    color: "white",
                    borderRadius: "8px",
                    cursor: "pointer",
                    fontWeight: "bold",
                    transition: "0.3s"
                }}
                onMouseOver={(e) => (e.target.style.background = "#d9363e")}
                onMouseOut={(e) => (e.target.style.background = "#ff4d4f")}
            >
                Logout
            </button>
        </div>
    );
}

export default Navbar;