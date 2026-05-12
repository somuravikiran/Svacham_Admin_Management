// src/pages/dashboard/Dashboard.jsx

import React, { useEffect, useState } from "react";
import axios from "axios";
import "./Dashboard.css";

const Dashboard = () => {

    const BASE_URL = "https://svacham-admin-management-2.onrender.com";

    const token = localStorage.getItem("token");

    const headers = {
        Authorization: `Bearer ${token}`
    };

    const [clientSummary, setClientSummary] = useState({});
    const [orderSummary, setOrderSummary] = useState({});
    const [gstSummary, setGstSummary] = useState({});
    const [salarySummary, setSalarySummary] = useState({});
    const [spendingSummary, setSpendingSummary] = useState({});
    const [stockSummary, setStockSummary] = useState({});

    useEffect(() => {

        fetchData();

    }, []);

    const fetchData = async () => {

        try {

            // CLIENT
            const clientRes = await axios.get(
                `${BASE_URL}/api/clients/summary`,
                { headers }
            );

            setClientSummary(
                clientRes.data.data || clientRes.data
            );

            // ORDERS
            const orderRes = await axios.get(
                `${BASE_URL}/api/orders/summary`,
                { headers }
            );

            setOrderSummary(
                orderRes.data.data || orderRes.data
            );

            // GST
            const gstRes = await axios.get(
                `${BASE_URL}/api/gst-bills/summary`,
                { headers }
            );

            setGstSummary(
                gstRes.data.data || gstRes.data
            );

            // SALARY
            try {

                const salaryRes = await axios.get(
                    `${BASE_URL}/api/salary/summary`,
                    { headers }
                );

                setSalarySummary(
                    salaryRes.data.data || salaryRes.data
                );

            } catch (err) {

                console.log("Salary summary not found");
            }

            // SPENDING
            try {

                const spendingRes = await axios.get(
                    `${BASE_URL}/api/spending/summary`,
                    { headers }
                );

                setSpendingSummary(
                    spendingRes.data.data || spendingRes.data
                );

            } catch (err) {

                console.log("Spending summary not found");
            }

            // STOCK
            try {

                const stockRes = await axios.get(
                    `${BASE_URL}/api/stock/summary`,
                    { headers }
                );

                setStockSummary(
                    stockRes.data.data || stockRes.data
                );

            } catch (err) {

                console.log("Stock summary not found");
            }

        } catch (error) {

            console.log(error);

            alert("Dashboard Load Failed");
        }
    };

    return (

        <div className="dashboard">

            <h1 className="title">
                Business Dashboard
            </h1>

            <div className="cards">

                <div className="card client">
                    <h2>Clients</h2>
                    <h1>{clientSummary.totalClients || 0}</h1>
                    <p>
                        ₹ {clientSummary.totalBusinessAmount || 0}
                    </p>
                </div>

                <div className="card order">
                    <h2>Orders</h2>
                    <h1>{orderSummary.totalOrders || 0}</h1>
                    <p>
                        ₹ {orderSummary.totalRevenue || 0}
                    </p>
                </div>

                <div className="card gst">
                    <h2>GST Bills</h2>
                    <h1>{gstSummary.totalBills || 0}</h1>
                    <p>
                        ₹ {gstSummary.totalFinalAmount || 0}
                    </p>
                </div>

                <div className="card salary">
                    <h2>Salary</h2>
                    <h1>
                        ₹ {salarySummary.totalSalaryPaid || 0}
                    </h1>
                </div>

                <div className="card spending">
                    <h2>Spending</h2>
                    <h1>
                        ₹ {spendingSummary.totalSpending || 0}
                    </h1>
                </div>

                <div className="card stock">
                    <h2>Stock</h2>
                    <h1>
                        ₹ {stockSummary.totalStockValue || 0}
                    </h1>
                </div>

            </div>

        </div>
    );
};

export default Dashboard;