import { Routes, Route, Navigate } from "react-router-dom";

import Login from "./pages/auth/Login";
import Register from "./pages/auth/Register";


import Dashboard from "./pages/dashboard/Dashboard";

import Clients from "./pages/clients/Clients";

import Orders from "./pages/orders/Orders";

import GstBills from "./pages/gst/GstBills";

import Salary from "./pages/salary/Salary";

import Spending from "./pages/spending/Spending";

import Stock from "./pages/stock/Stock";

import Navbar from "./components/Navbar";
import Sidebar from "./components/Sidebar";

function ProtectedLayout({ children }) {

    const token = localStorage.getItem("token");

    if (!token) {
        return <Navigate to="/login" />;
    }

    return (
        <div style={{ display: "flex" }}>

            <Sidebar />

            <div style={{ flex: 1 }}>

                <Navbar />

                <div className="p-4">
                    {children}
                </div>

            </div>

        </div>
    );
}

function AppRoutes() {

    return (

        <Routes>

            {/* AUTH */}

            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />

            {/* DASHBOARD */}

            <Route
                path="/dashboard"
                element={
                    <ProtectedLayout>
                        <Dashboard />
                    </ProtectedLayout>
                }
            />

            {/* CLIENTS */}

            <Route
                path="/clients"
                element={
                    <ProtectedLayout>
                        <Clients />
                    </ProtectedLayout>
                }
            />

         
            {/* ORDERS */}

            <Route
                path="/orders"
                element={
                    <ProtectedLayout>
                        <Orders />
                    </ProtectedLayout>
                }
            />

            {/* GST */}

            <Route
                path="/gst-bills"
                element={
                    <ProtectedLayout>
                        <GstBills />
                    </ProtectedLayout>
                }
            />

            {/* STOCK */}

            <Route
                path="/stock"
                element={
                    <ProtectedLayout>
                        <Stock />
                    </ProtectedLayout>
                }
            />

            {/* SALARY */}
            <Route 
                path="/salary"
                element={
                    <ProtectedLayout>
                        <Salary />
                    </ProtectedLayout>
                } 
            />

            {/* SPENDING */}

            <Route
                path="/spending"
                element={
                    <ProtectedLayout>
                        <Spending />
                    </ProtectedLayout>
                }
            />

            {/* DEFAULT */}

            <Route
                path="*"
                element={<Navigate to="/login" />}
            />

        </Routes>
    );
}

export default AppRoutes;