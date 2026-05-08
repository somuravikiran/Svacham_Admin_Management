import { useEffect, useState } from "react";

import {
    addSalary,
    getAllSalaries,
    deleteSalary,
    getSalarySummary
} from "../../services/salaryService";

function Salary() {

    const [salaryList, setSalaryList] = useState([]);
    const [summary, setSummary] = useState({});
    const [showForm, setShowForm] = useState(false);

    // ✅ NEW STATES ADDED
    const [selectedSalary, setSelectedSalary] = useState(null);
    const [isEditMode, setIsEditMode] = useState(false);

    const [formData, setFormData] = useState({
        employeeName: "",
        employeeRole: "",
        monthlySalary: "",
        paidAmount: "",
        salaryMonth: "",
        paymentDate: "",
        paymentMode: "",
        notes: ""
    });

    useEffect(() => {
        loadSalaries();
        loadSummary();
    }, []);

    const loadSalaries = async () => {
        try {
            const response = await getAllSalaries();
            if (Array.isArray(response.data)) {
                setSalaryList(response.data);
            } else {
                setSalaryList([]);
            }
        } catch (error) {
            console.log(error);
            setSalaryList([]);
        }
    };

    const loadSummary = async () => {
        try {
            const response = await getSalarySummary();
            setSummary(response.data);
        } catch (error) {
            console.log(error);
        }
    };

    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            await addSalary(formData);

            alert("Salary Added Successfully");

            resetForm();

            setShowForm(false);

            loadSalaries();
            loadSummary();

        } catch (error) {
            console.log(error);
            alert("Failed To Add Salary");
        }
    };

    // ✅ UPDATE HANDLER
    const handleUpdate = async (e) => {
        e.preventDefault();

        try {
            await addSalary(formData); // replace with update API if available

            alert("Salary Updated Successfully");

            resetForm();

            setIsEditMode(false);
            setShowForm(false);

            loadSalaries();
            loadSummary();

        } catch (error) {
            console.log(error);
            alert("Failed To Update Salary");
        }
    };

    const resetForm = () => {
        setFormData({
            employeeName: "",
            employeeRole: "",
            monthlySalary: "",
            paidAmount: "",
            salaryMonth: "",
            paymentDate: "",
            paymentMode: "",
            notes: ""
        });
        setSelectedSalary(null);
    };

    const handleDelete = async (id) => {
        try {
            await deleteSalary(id);
            loadSalaries();
            loadSummary();
        } catch (error) {
            console.log(error);
        }
    };

    // ✅ VIEW
    const handleView = (salary) => {
        setSelectedSalary(salary);
        setIsEditMode(false);
    };

    // ✅ EDIT
    const handleEdit = (salary) => {
        setFormData(salary);
        setSelectedSalary(salary);
        setIsEditMode(true);
        setShowForm(true);
    };

    return (

        <div className="container mt-4">

            {/* HEADER */}
            <div className="d-flex justify-content-between align-items-center mb-4">
                <h2>Salary Management</h2>

                <button
                    className="btn btn-primary"
                    onClick={() => setShowForm(!showForm)}
                >
                    + Add Salary
                </button>
            </div>

            {/* SUMMARY CARDS */}
            <div className="row mb-4">

                <div className="col-md-3">
                    <div className="card p-3 shadow">
                        <h6>Total Employees</h6>
                        <h4>{summary.totalEmployees}</h4>
                    </div>
                </div>

                <div className="col-md-3">
                    <div className="card p-3 shadow">
                        <h6>Total Salary</h6>
                        <h4>{summary.totalMonthlySalary}</h4>
                    </div>
                </div>

                <div className="col-md-3">
                    <div className="card p-3 shadow">
                        <h6>Total Paid</h6>
                        <h4>{summary.totalPaidSalary}</h4>
                    </div>
                </div>

                <div className="col-md-3">
                    <div className="card p-3 shadow">
                        <h6>Total Pending</h6>
                        <h4>{summary.totalPendingSalary}</h4>
                    </div>
                </div>

            </div>

            {/* VIEW SECTION */}
            {selectedSalary && !isEditMode && (
                <div className="card p-3 mb-3 bg-light">
                    <h5>Salary Details</h5>

                    <p><b>Employee:</b> {selectedSalary.employeeName}</p>
                    <p><b>Role:</b> {selectedSalary.employeeRole}</p>
                    <p><b>Salary:</b> {selectedSalary.monthlySalary}</p>
                    <p><b>Paid:</b> {selectedSalary.paidAmount}</p>
                    <p><b>Status:</b> {selectedSalary.status}</p>

                    <button
                        className="btn btn-secondary btn-sm"
                        onClick={() => setSelectedSalary(null)}
                    >
                        Close
                    </button>
                </div>
            )}

            {/* FORM */}
            {showForm && (
                <div className="card p-4 mb-4">

                    <h4>{isEditMode ? "Update Salary" : "Add Salary"}</h4>

                    <form onSubmit={isEditMode ? handleUpdate : handleSubmit}>

                        <div className="row">

                            <div className="col-md-6 mb-3">
                                <input
                                    className="form-control"
                                    name="employeeName"
                                    placeholder="Employee Name"
                                    value={formData.employeeName}
                                    onChange={handleChange}
                                    required
                                />
                            </div>

                            <div className="col-md-6 mb-3">
                                <input
                                    className="form-control"
                                    name="employeeRole"
                                    placeholder="Employee Role"
                                    value={formData.employeeRole}
                                    onChange={handleChange}
                                    required
                                />
                            </div>

                            <div className="col-md-6 mb-3">
                                <input
                                    className="form-control"
                                    name="monthlySalary"
                                    placeholder="Monthly Salary"
                                    value={formData.monthlySalary}
                                    onChange={handleChange}
                                    required
                                />
                            </div>

                            <div className="col-md-6 mb-3">
                                <input
                                    className="form-control"
                                    name="paidAmount"
                                    placeholder="Paid Amount"
                                    value={formData.paidAmount}
                                    onChange={handleChange}
                                    required
                                />
                            </div>

                            <div className="col-md-6 mb-3">
                                <input
                                    className="form-control"
                                    name="salaryMonth"
                                    placeholder="Salary Month"
                                    value={formData.salaryMonth}
                                    onChange={handleChange}
                                    required
                                />
                            </div>

                            <div className="col-md-6 mb-3">
                                <input
                                    type="date"
                                    className="form-control"
                                    name="paymentDate"
                                    value={formData.paymentDate}
                                    onChange={handleChange}
                                    required
                                />
                            </div>

                            <div className="col-md-6 mb-3">
                                <select
                                    className="form-control"
                                    name="paymentMode"
                                    value={formData.paymentMode}
                                    onChange={handleChange}
                                >
                                    <option value="">Payment Mode</option>
                                    <option value="CASH">CASH</option>
                                    <option value="UPI">UPI</option>
                                    <option value="BANK">BANK</option>
                                </select>
                            </div>

                            <div className="col-md-12 mb-3">
                                <textarea
                                    className="form-control"
                                    name="notes"
                                    placeholder="Notes"
                                    value={formData.notes}
                                    onChange={handleChange}
                                />
                            </div>

                        </div>

                        <button className="btn btn-success me-2">
                            {isEditMode ? "Update" : "Save"}
                        </button>

                        <button
                            type="button"
                            className="btn btn-secondary"
                            onClick={() => setShowForm(false)}
                        >
                            Cancel
                        </button>

                    </form>
                </div>
            )}

            {/* TABLE */}
            <div className="card p-4">

                <table className="table table-bordered">

                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Employee</th>
                            <th>Role</th>
                            <th>Salary</th>
                            <th>Paid</th>
                            <th>Balance</th>
                            <th>Status</th>
                            <th>Action</th>
                        </tr>
                    </thead>

                    <tbody>
                        {Array.isArray(salaryList) &&
                            salaryList.map((salary) => (
                                <tr key={salary.id}>
                                    <td>{salary.id}</td>
                                    <td>{salary.employeeName}</td>
                                    <td>{salary.employeeRole}</td>
                                    <td>{salary.monthlySalary}</td>
                                    <td>{salary.paidAmount}</td>
                                    <td>{salary.balanceAmount}</td>
                                    <td>{salary.status}</td>

                                    <td>
                                        <button
                                            className="btn btn-info btn-sm me-2"
                                            onClick={() => handleView(salary)}
                                        >
                                            View
                                        </button>

                                        <button
                                            className="btn btn-warning btn-sm me-2"
                                            onClick={() => handleEdit(salary)}
                                        >
                                            Update
                                        </button>

                                        <button
                                            className="btn btn-danger btn-sm"
                                            onClick={() => handleDelete(salary.id)}
                                        >
                                            Delete
                                        </button>
                                    </td>
                                </tr>
                            ))}
                    </tbody>

                </table>

            </div>

        </div>
    );
}

export default Salary;