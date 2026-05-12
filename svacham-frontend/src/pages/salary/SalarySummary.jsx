import { useEffect, useState } from "react";
import axios from "axios";

function SalarySummary() {

    const [summary, setSummary] = useState({});

    const BASE_URL = "https://svacham-admin-management-2.onrender.com";

    const getToken = () => {
        return localStorage.getItem("token");
    };

    const loadSummary = async () => {

        try {

            const response = await axios.get(
                `${BASE_URL}/api/salary/summary`,
                {
                    headers: {
                        Authorization: `Bearer ${getToken()}`
                    }
                }
            );

            setSummary(response.data);

        } catch (error) {

            console.log(error);

            alert("Failed to load salary summary");
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

                        <h5>Total Salary</h5>

                        <h3>
                            ₹ {summary.totalSalaryPaid || 0}
                        </h3>

                    </div>

                </div>

                <div className="col-md-3 mb-3">

                    <div className="card shadow p-4 text-center">

                        <h5>Total Employees</h5>

                        <h3>
                            {summary.totalEmployees || 0}
                        </h3>

                    </div>

                </div>

                <div className="col-md-3 mb-3">

                    <div className="card shadow p-4 text-center">

                        <h5>Paid Salaries</h5>

                        <h3>
                            {summary.paidSalaries || 0}
                        </h3>

                    </div>

                </div>

                <div className="col-md-3 mb-3">

                    <div className="card shadow p-4 text-center">

                        <h5>Pending Salaries</h5>

                        <h3>
                            {summary.pendingSalaries || 0}
                        </h3>

                    </div>

                </div>

            </div>

        </div>
    );
}

export default SalarySummary;