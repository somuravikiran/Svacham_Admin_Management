import { useEffect, useState } from "react";
import axios from "axios";

function StockSummary() {

    const [summary, setSummary] = useState({});

    const BASE_URL = "https://svacham-admin-management-2.onrender.com";

    const getToken = () => {
        return localStorage.getItem("token");
    };

    const loadSummary = async () => {

        try {

            const response = await axios.get(
                `${BASE_URL}/api/stock/summary`,
                {
                    headers: {
                        Authorization: `Bearer ${getToken()}`
                    }
                }
            );

            setSummary(response.data);

        } catch (error) {

            console.log(error);

            alert("Failed to load stock summary");
        }
    };

    useEffect(() => {

        loadSummary();

    }, []);

    return (

        <div className="container mt-4">

            <div className="row">

                <div className="col-md-3 mb-3">

                    <div className="card shadow p-4 text-center">

                        <h5>Total Stock</h5>

                        <h3>
                            {summary.totalStockAvailable || 0}
                        </h3>

                    </div>

                </div>

                <div className="col-md-3 mb-3">

                    <div className="card shadow p-4 text-center">

                        <h5>Used Stock</h5>

                        <h3>
                            {summary.totalUsedStock || 0}
                        </h3>

                    </div>

                </div>

                <div className="col-md-3 mb-3">

                    <div className="card shadow p-4 text-center">

                        <h5>Inventory Value</h5>

                        <h3>
                            ₹ {summary.totalInventoryValue || 0}
                        </h3>

                    </div>

                </div>

                <div className="col-md-3 mb-3">

                    <div className="card shadow p-4 text-center">

                        <h5>Low Stock Items</h5>

                        <h3>
                            {summary.lowStockItems || 0}
                        </h3>

                    </div>

                </div>

            </div>

        </div>
    );
}

export default StockSummary;