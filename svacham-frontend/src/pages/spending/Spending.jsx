// src/pages/spending/Spending.jsx

import { useEffect, useState } from "react";

import {
    getAllSpendings,
    addSpending,
    deleteSpending,
    updateSpending,
    getSpendingSummary
} from "../../services/spendingService";

function Spending() {

    const [spendings, setSpendings] = useState([]);
    const [summary, setSummary] = useState({});
    const [editingId, setEditingId] = useState(null);
    const [selectedSpending, setSelectedSpending] = useState(null);
    const [showForm, setShowForm] = useState(false);

    const [formData, setFormData] = useState({
        expenseTitle: "",
        expenseCategory: "",
        description: "",
        amount: "",
        spentDate: "",
        vendorName: "",
        paymentMode: "",
        status: ""
    });

    useEffect(() => {
        loadSpendings();
        loadSummary();
    }, []);

    const loadSpendings = async () => {

        try {

            const response = await getAllSpendings();

            if (Array.isArray(response.data)) {
                setSpendings(response.data);
            } else {
                setSpendings([]);
            }

        } catch (error) {
            console.log(error);
            setSpendings([]);
        }
    };

    const loadSummary = async () => {

        try {

            const response = await getSpendingSummary();

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

    const resetForm = () => {

        setFormData({
            expenseTitle: "",
            expenseCategory: "",
            description: "",
            amount: "",
            spentDate: "",
            vendorName: "",
            paymentMode: "",
            status: ""
        });

        setEditingId(null);
    };

    const handleSubmit = async (e) => {

        e.preventDefault();

        try {

            if (editingId) {

                await updateSpending(editingId, formData);

                alert("Spending Updated");

            } else {

                await addSpending(formData);

                alert("Spending Added");
            }

            resetForm();

            setShowForm(false);

            loadSpendings();

            loadSummary();

        } catch (error) {

            console.log(error);

            alert("Operation Failed");
        }
    };

    const handleEdit = (spending) => {

        setEditingId(spending.id);

        setShowForm(true);

        setFormData({
            expenseTitle: spending.expenseTitle,
            expenseCategory: spending.expenseCategory,
            description: spending.description,
            amount: spending.amount,
            spentDate: spending.spentDate,
            vendorName: spending.vendorName,
            paymentMode: spending.paymentMode,
            status: spending.status
        });
    };

    const handleView = (spending) => {
        setSelectedSpending(spending);
    };

    const handleDelete = async (id) => {

        try {

            await deleteSpending(id);

            alert("Deleted Successfully");

            loadSpendings();

            loadSummary();

        } catch (error) {

            console.log(error);
        }
    };

    return (

        <div className="container mt-4">

            {/* HEADER */}

            <div className="d-flex justify-content-between align-items-center mb-4">

                <h2 className="fw-bold">
                    Spending Management
                </h2>

                <button
                    className="btn btn-primary px-4 py-2"
                    onClick={() => {
                        setShowForm(!showForm);
                        resetForm();
                    }}
                >
                    + Add Spending
                </button>

            </div>

            {/* SUMMARY CARDS */}

            <div className="row mb-4">

                <div className="col-md-3 mb-3">

                    <div className="card shadow-sm border-0 p-4">

                        <h5>Total Expense</h5>

                        <h2>₹ {summary.totalExpense || 0}</h2>

                    </div>

                </div>

                <div className="col-md-3 mb-3">

                    <div className="card shadow-sm border-0 p-4">

                        <h5>Paid Expense</h5>

                        <h2>₹ {summary.paidExpense || 0}</h2>

                    </div>

                </div>

                <div className="col-md-3 mb-3">

                    <div className="card shadow-sm border-0 p-4">

                        <h5>Pending Expense</h5>

                        <h2>₹ {summary.pendingExpense || 0}</h2>

                    </div>

                </div>

                <div className="col-md-3 mb-3">

                    <div className="card shadow-sm border-0 p-4">

                        <h5>Transactions</h5>

                        <h2>{summary.totalTransactions || 0}</h2>

                    </div>

                </div>

            </div>

            {/* VIEW PANEL */}

            {selectedSpending && (

                <div className="card p-4 shadow-sm border-0 mb-4">

                    <h4 className="mb-3">
                        Spending Details
                    </h4>

                    <div className="row">

                        <div className="col-md-6 mb-2">
                            <strong>Title:</strong> {selectedSpending.expenseTitle}
                        </div>

                        <div className="col-md-6 mb-2">
                            <strong>Category:</strong> {selectedSpending.expenseCategory}
                        </div>

                        <div className="col-md-6 mb-2">
                            <strong>Amount:</strong> ₹ {selectedSpending.amount}
                        </div>

                        <div className="col-md-6 mb-2">
                            <strong>Vendor:</strong> {selectedSpending.vendorName}
                        </div>

                        <div className="col-md-6 mb-2">
                            <strong>Status:</strong> {selectedSpending.status}
                        </div>

                        <div className="col-md-6 mb-2">
                            <strong>Date:</strong> {selectedSpending.spentDate}
                        </div>

                    </div>

                    <button
                        className="btn btn-secondary btn-sm mt-3"
                        onClick={() => setSelectedSpending(null)}
                    >
                        Close
                    </button>

                </div>

            )}

            {/* FORM */}

            {showForm && (

                <div className="card shadow-sm border-0 p-4 mb-4">

                    <h4 className="mb-4">

                        {editingId
                            ? "Update Spending"
                            : "Add Spending"}

                    </h4>

                    <form onSubmit={handleSubmit}>

                        <div className="row">

                            <div className="col-md-6 mb-3">

                                <input
                                    type="text"
                                    name="expenseTitle"
                                    placeholder="Expense Title"
                                    className="form-control"
                                    value={formData.expenseTitle}
                                    onChange={handleChange}
                                />

                            </div>

                            <div className="col-md-6 mb-3">

                                <input
                                    type="text"
                                    name="expenseCategory"
                                    placeholder="Expense Category"
                                    className="form-control"
                                    value={formData.expenseCategory}
                                    onChange={handleChange}
                                />

                            </div>

                            <div className="col-md-6 mb-3">

                                <input
                                    type="number"
                                    name="amount"
                                    placeholder="Amount"
                                    className="form-control"
                                    value={formData.amount}
                                    onChange={handleChange}
                                />

                            </div>

                            <div className="col-md-6 mb-3">

                                <input
                                    type="date"
                                    name="spentDate"
                                    className="form-control"
                                    value={formData.spentDate}
                                    onChange={handleChange}
                                />

                            </div>

                            <div className="col-md-6 mb-3">

                                <input
                                    type="text"
                                    name="vendorName"
                                    placeholder="Vendor Name"
                                    className="form-control"
                                    value={formData.vendorName}
                                    onChange={handleChange}
                                />

                            </div>

                            <div className="col-md-6 mb-3">

                                <input
                                    type="text"
                                    name="paymentMode"
                                    placeholder="Payment Mode"
                                    className="form-control"
                                    value={formData.paymentMode}
                                    onChange={handleChange}
                                />

                            </div>

                            <div className="col-md-6 mb-3">

                                <select
                                    name="status"
                                    className="form-control"
                                    value={formData.status}
                                    onChange={handleChange}
                                >
                                    <option value="">Select Status</option>
                                    <option value="PAID">PAID</option>
                                    <option value="PENDING">PENDING</option>
                                    <option value="CANCELLED">CANCELLED</option>
                                </select>

                            </div>

                            <div className="col-md-12 mb-3">

                                <textarea
                                    name="description"
                                    placeholder="Description"
                                    className="form-control"
                                    rows="3"
                                    value={formData.description}
                                    onChange={handleChange}
                                />

                            </div>

                        </div>

                        <button className="btn btn-primary px-4">

                            {editingId
                                ? "Update Spending"
                                : "Add Spending"}

                        </button>

                    </form>

                </div>

            )}

            {/* TABLE */}

            <div className="card shadow-sm border-0 p-4">

                <div className="table-responsive">

                    <table className="table table-bordered align-middle">

                        <thead>

                            <tr>

                                <th>ID</th>
                                <th>Title</th>
                                <th>Category</th>
                                <th>Amount</th>
                                <th>Status</th>
                                <th>Vendor</th>
                                <th>Date</th>
                                <th>Action</th>

                            </tr>

                        </thead>

                        <tbody>

                            {spendings.map((spending) => (

                                <tr key={spending.id}>

                                    <td>{spending.id}</td>

                                    <td>{spending.expenseTitle}</td>

                                    <td>{spending.expenseCategory}</td>

                                    <td>₹ {spending.amount}</td>

                                    <td>{spending.status}</td>

                                    <td>{spending.vendorName}</td>

                                    <td>{spending.spentDate}</td>

                                    <td>

                                        <button
                                            className="btn btn-info btn-sm me-2 text-white"
                                            onClick={() => handleView(spending)}
                                        >
                                            View
                                        </button>

                                        <button
                                            className="btn btn-warning btn-sm me-2"
                                            onClick={() => handleEdit(spending)}
                                        >
                                            Update
                                        </button>

                                        <button
                                            className="btn btn-danger btn-sm"
                                            onClick={() => handleDelete(spending.id)}
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

        </div>
    );
}

export default Spending;