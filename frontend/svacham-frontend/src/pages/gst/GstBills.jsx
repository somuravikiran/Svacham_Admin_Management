import { useEffect, useState } from "react";

import {
    createBill,
    deleteBill,
    getAllBills,
    getBillById,
    getBillSummary,
    updateBill
} from "../../services/gstBillService";

function GstBills() {

    const [bills, setBills] = useState([]);
    const [summary, setSummary] = useState({});
    const [isEdit, setIsEdit] = useState(false);
    const [viewBill, setViewBill] = useState(null);
    const [showForm, setShowForm] = useState(false);

    const [bill, setBill] = useState({
        id: "",
        vendorName: "",
        billNumber: "",
        itemPurchased: "",
        billAmount: "",
        gstPercent: "",
        gstAmount: "",
        totalAmount: "",
        billDate: "",
        paymentMode: "",
        status: "",
        notes: ""
    });

    const loadBills = async () => {
        try {
            const response = await getAllBills();
            setBills(response.data.data);
        } catch (error) {
            console.log(error);
            alert("Failed to load bills");
        }
    };

    const loadSummary = async () => {
        try {
            const response = await getBillSummary();
            setSummary(response.data.data);
        } catch (error) {
            console.log(error);
        }
    };

    useEffect(() => {
        loadBills();
        loadSummary();
    }, []);

    const calculateAmounts = (updatedBill) => {

        const billAmount = Number(updatedBill.billAmount) || 0;
        const gstPercent = Number(updatedBill.gstPercent) || 0;

        const gstAmount = (billAmount * gstPercent) / 100;
        const totalAmount = billAmount + gstAmount;

        setBill({
            ...updatedBill,
            gstAmount,
            totalAmount
        });
    };

    const handleChange = (e) => {

        const updatedBill = {
            ...bill,
            [e.target.name]: e.target.value
        };

        if (e.target.name === "billAmount" || e.target.name === "gstPercent") {
            calculateAmounts(updatedBill);
        } else {
            setBill(updatedBill);
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {

            if (isEdit) {
                await updateBill(bill.id, bill);
                alert("GST Bill Updated");
            } else {
                await createBill(bill);
                alert("GST Bill Added");
            }

            resetForm();
            setShowForm(false);
            loadBills();
            loadSummary();

        } catch (error) {
            console.log(error);
            alert("Operation Failed");
        }
    };

    const handleEdit = (data) => {
        setBill(data);
        setIsEdit(true);
        setShowForm(true);
    };

    const handleDelete = async (id) => {
        try {
            await deleteBill(id);
            loadBills();
            loadSummary();
        } catch (error) {
            console.log(error);
        }
    };

    const handleView = async (id) => {
        const res = await getBillById(id);
        setViewBill(res.data.data);
    };

    const resetForm = () => {
        setBill({
            id: "",
            vendorName: "",
            billNumber: "",
            itemPurchased: "",
            billAmount: "",
            gstPercent: "",
            gstAmount: "",
            totalAmount: "",
            billDate: "",
            paymentMode: "",
            status: "",
            notes: ""
        });
        setIsEdit(false);
    };

    return (

        <div className="container mt-4">

            {/* HEADER */}
            <div className="d-flex justify-content-between align-items-center mb-4">
                <h2>GST Bills Management</h2>

                <button
                    className="btn btn-primary"
                    onClick={() => {
                        setShowForm(!showForm);
                        resetForm();
                    }}
                >
                    + Add GST Bill
                </button>
            </div>

            {/* SUMMARY CARDS */}
            <div className="row mb-4">

                <div className="col-md-4">
                    <div className="card shadow p-3">
                        <h5>Total Bills</h5>
                        <h3>{summary.totalBills}</h3>
                    </div>
                </div>

                <div className="col-md-4">
                    <div className="card shadow p-3">
                        <h5>Total GST</h5>
                        <h3>₹ {summary.totalGstAmount}</h3>
                    </div>
                </div>

                <div className="col-md-4">
                    <div className="card shadow p-3">
                        <h5>Total Amount</h5>
                        <h3>₹ {summary.totalFinalAmount}</h3>
                    </div>
                </div>

            </div>

            {/* FORM (ONLY SHOW ON BUTTON CLICK) */}
            {showForm && (
                <div className="card shadow p-4 mb-4">

                    <h4>{isEdit ? "Update GST Bill" : "Add GST Bill"}</h4>

                    <form onSubmit={handleSubmit}>

                        <div className="row">

                            <div className="col-md-6 mb-3">
                                <input className="form-control" name="vendorName"
                                    placeholder="Vendor Name"
                                    value={bill.vendorName}
                                    onChange={handleChange}
                                />
                            </div>

                            <div className="col-md-6 mb-3">
                                <input className="form-control" name="billNumber"
                                    placeholder="Bill Number"
                                    value={bill.billNumber}
                                    onChange={handleChange}
                                />
                            </div>

                            <div className="col-md-6 mb-3">
                                <input className="form-control" name="itemPurchased"
                                    placeholder="Item Purchased"
                                    value={bill.itemPurchased}
                                    onChange={handleChange}
                                />
                            </div>

                            <div className="col-md-6 mb-3">
                                <input type="date" className="form-control"
                                    name="billDate"
                                    value={bill.billDate}
                                    onChange={handleChange}
                                />
                            </div>

                            <div className="col-md-4 mb-3">
                                <input className="form-control" name="billAmount"
                                    placeholder="Bill Amount"
                                    value={bill.billAmount}
                                    onChange={handleChange}
                                />
                            </div>

                            <div className="col-md-4 mb-3">
                                <input className="form-control" name="gstPercent"
                                    placeholder="GST %"
                                    value={bill.gstPercent}
                                    onChange={handleChange}
                                />
                            </div>

                            <div className="col-md-4 mb-3">
                                <input className="form-control"
                                    value={bill.gstAmount}
                                    readOnly
                                    placeholder="GST Amount"
                                />
                            </div>

                            <div className="col-md-6 mb-3">
                                <input className="form-control"
                                    value={bill.totalAmount}
                                    readOnly
                                    placeholder="Total Amount"
                                />
                            </div>

                            <div className="col-md-6 mb-3">
                                <select className="form-control"
                                    name="status"
                                    value={bill.status}
                                    onChange={handleChange}
                                >
                                    <option value="">Status</option>
                                    <option value="PAID">PAID</option>
                                    <option value="PENDING">PENDING</option>
                                    <option value="PARTIAL">PARTIAL</option>
                                </select>
                            </div>

                        </div>

                        <button className="btn btn-success me-2">
                            {isEdit ? "Update" : "Save"}
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
            <div className="card shadow p-3">

                <table className="table table-bordered">

                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Vendor</th>
                            <th>Bill No</th>
                            <th>Total</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>

                    <tbody>
                        {bills.map((b) => (
                            <tr key={b.id}>
                                <td>{b.id}</td>
                                <td>{b.vendorName}</td>
                                <td>{b.billNumber}</td>
                                <td>₹ {b.totalAmount}</td>
                                <td>{b.status}</td>
                                <td>

                                    <button className="btn btn-info btn-sm me-2"
                                        onClick={() => handleView(b.id)}>
                                        View
                                    </button>

                                    <button className="btn btn-warning btn-sm me-2"
                                        onClick={() => handleEdit(b)}>
                                        Edit
                                    </button>

                                    <button className="btn btn-danger btn-sm"
                                        onClick={() => handleDelete(b.id)}>
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

export default GstBills;