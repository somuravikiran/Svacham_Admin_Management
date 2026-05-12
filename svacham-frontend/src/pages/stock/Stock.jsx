import { useEffect, useState } from "react";

import {
    getAllStock,
    addStock,
    updateStock,
    deleteStock,
    getStockSummary
} from "../../services/stockService";

function Stock() {

    const [stocks, setStocks] = useState([]);

    const [summary, setSummary] = useState({});

    const [editingId, setEditingId] = useState(null);

    const [showForm, setShowForm] = useState(false);

    const [selectedStock, setSelectedStock] = useState(null);

    const [formData, setFormData] = useState({
        itemName: "",
        quantity: "",
        itemCategory: "",
        stockUnit: "",
        totalStock: "",
        usedStock: "",
        purchasePricePerUnit: "",
        sellingPricePerUnit: "",
        supplierName: "",
        stockAddedDate: "",
        notes: ""
    });

    useEffect(() => {
        loadStocks();
        loadSummary();
    }, []);

    const loadStocks = async () => {

        try {

            const response = await getAllStock();

            if (Array.isArray(response.data)) {
                setStocks(response.data);
            } else {
                setStocks([]);
            }

        } catch (error) {

            console.log(error);

            setStocks([]);
        }
    };

    const loadSummary = async () => {

        try {

            const response = await getStockSummary();

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
            itemName: "",
            quantity: "",
            itemCategory: "",
            stockUnit: "",
            totalStock: "",
            usedStock: "",
            purchasePricePerUnit: "",
            sellingPricePerUnit: "",
            supplierName: "",
            stockAddedDate: "",
            notes: ""
        });

        setEditingId(null);
    };

    const handleSubmit = async (e) => {

        e.preventDefault();

        try {

            if (editingId) {

                await updateStock(editingId, formData);

                alert("Stock Updated");

            } else {

                await addStock(formData);

                alert("Stock Added");
            }

            resetForm();

            setShowForm(false);

            loadStocks();

            loadSummary();

        } catch (error) {

            console.log(error);

            alert("Operation Failed");
        }
    };

    const handleEdit = (stock) => {

        setEditingId(stock.id);

        setShowForm(true);

        setFormData({
            itemName: stock.itemName,
            quantity: stock.quantity,
            itemCategory: stock.itemCategory,
            stockUnit: stock.stockUnit,
            totalStock: stock.totalStock,
            usedStock: stock.usedStock,
            purchasePricePerUnit: stock.purchasePricePerUnit,
            sellingPricePerUnit: stock.sellingPricePerUnit,
            supplierName: stock.supplierName,
            stockAddedDate: stock.stockAddedDate,
            notes: stock.notes
        });
    };

    const handleView = (stock) => {

        setSelectedStock(stock);
    };

    const handleDelete = async (id) => {

        try {

            await deleteStock(id);

            alert("Deleted Successfully");

            loadStocks();

            loadSummary();

        } catch (error) {

            console.log(error);
        }
    };

    return (

        <div className="container-fluid px-4 py-4 bg-light min-vh-100">

            {/* PAGE HEADER */}

            <div className="d-flex justify-content-between align-items-center mb-4">

                <h2 className="fw-bold text-dark m-0">
                    Stock Management
                </h2>

                <button
                    className="btn btn-primary px-4 py-2"
                    onClick={() => {
                        setShowForm(!showForm);
                        resetForm();
                    }}
                >
                    + Add Stock
                </button>

            </div>

            {/* SUMMARY CARDS */}

            <div className="row g-4 mb-4">

                <div className="col-md-3">

                    <div className="card border-0 shadow-sm rounded-3 p-3 h-100">

                        <h5 className="text-dark mb-3">
                            Total Stock
                        </h5>

                        <h2 className="fw-bold">
                            {summary.totalStockAvailable || 0}
                        </h2>

                    </div>

                </div>

                <div className="col-md-3">

                    <div className="card border-0 shadow-sm rounded-3 p-3 h-100">

                        <h5 className="text-dark mb-3">
                            Used Stock
                        </h5>

                        <h2 className="fw-bold">
                            {summary.totalUsedStock || 0}
                        </h2>

                    </div>

                </div>

                <div className="col-md-3">

                    <div className="card border-0 shadow-sm rounded-3 p-3 h-100">

                        <h5 className="text-dark mb-3">
                            Inventory Value
                        </h5>

                        <h2 className="fw-bold">
                            ₹ {summary.totalInventoryValue || 0}
                        </h2>

                    </div>

                </div>

                <div className="col-md-3">

                    <div className="card border-0 shadow-sm rounded-3 p-3 h-100">

                        <h5 className="text-dark mb-3">
                            Low Stock
                        </h5>

                        <h2 className="fw-bold">
                            {summary.lowStockItems || 0}
                        </h2>

                    </div>

                </div>

            </div>

            {/* VIEW PANEL */}

            {selectedStock && (

                <div className="card border-0 shadow-sm rounded-3 p-4 mb-4">

                    <div className="d-flex justify-content-between align-items-center mb-3">

                        <h4 className="fw-bold">
                            Stock Details
                        </h4>

                        <button
                            className="btn btn-secondary btn-sm"
                            onClick={() => setSelectedStock(null)}
                        >
                            Close
                        </button>

                    </div>

                    <div className="row">

                        <div className="col-md-6 mb-3">
                            <strong>Item Name:</strong> {selectedStock.itemName}
                        </div>

                        <div className="col-md-6 mb-3">
                            <strong>Category:</strong> {selectedStock.itemCategory}
                        </div>

                        <div className="col-md-6 mb-3">
                            <strong>Total Stock:</strong> {selectedStock.totalStock}
                        </div>

                        <div className="col-md-6 mb-3">
                            <strong>Used Stock:</strong> {selectedStock.usedStock}
                        </div>

                        <div className="col-md-6 mb-3">
                            <strong>Remaining Stock:</strong> {selectedStock.remainingStock}
                        </div>

                        <div className="col-md-6 mb-3">
                            <strong>Stock Unit:</strong> {selectedStock.stockUnit}
                        </div>

                        <div className="col-md-6 mb-3">
                            <strong>Purchase Price:</strong> ₹ {selectedStock.purchasePricePerUnit}
                        </div>

                        <div className="col-md-6 mb-3">
                            <strong>Selling Price:</strong> ₹ {selectedStock.sellingPricePerUnit}
                        </div>

                        <div className="col-md-6 mb-3">
                            <strong>Supplier:</strong> {selectedStock.supplierName}
                        </div>

                        <div className="col-md-6 mb-3">
                            <strong>Status:</strong> {selectedStock.status}
                        </div>

                        <div className="col-md-6 mb-3">
                            <strong>Added Date:</strong> {selectedStock.stockAddedDate}
                        </div>

                        <div className="col-md-12 mb-3">
                            <strong>Notes:</strong> {selectedStock.notes}
                        </div>

                    </div>

                </div>

            )}

            {/* FORM SECTION */}

            {showForm && (

                <div className="card border-0 shadow-sm rounded-3 p-4 mb-4">

                    <h4 className="fw-bold mb-4">

                        {editingId
                            ? "Update Stock"
                            : "Add Stock"}

                    </h4>

                    <form onSubmit={handleSubmit}>

                        <div className="row">

                            <div className="col-md-6 mb-3">

                                <input
                                    type="text"
                                    name="itemName"
                                    placeholder="Item Name"
                                    className="form-control"
                                    value={formData.itemName}
                                    onChange={handleChange}
                                />

                            </div>

                            <div className="col-md-6 mb-3">

                                <input
                                    type="number"
                                    name="quantity"
                                    placeholder="Quantity"
                                    className="form-control"
                                    value={formData.quantity}
                                    onChange={handleChange}
                                />

                            </div>

                            <div className="col-md-6 mb-3">

                                <input
                                    type="text"
                                    name="itemCategory"
                                    placeholder="Item Category"
                                    className="form-control"
                                    value={formData.itemCategory}
                                    onChange={handleChange}
                                />

                            </div>

                            <div className="col-md-6 mb-3">

                                <input
                                    type="text"
                                    name="stockUnit"
                                    placeholder="Stock Unit"
                                    className="form-control"
                                    value={formData.stockUnit}
                                    onChange={handleChange}
                                />

                            </div>

                            <div className="col-md-6 mb-3">

                                <input
                                    type="number"
                                    name="totalStock"
                                    placeholder="Total Stock"
                                    className="form-control"
                                    value={formData.totalStock}
                                    onChange={handleChange}
                                />

                            </div>

                            <div className="col-md-6 mb-3">

                                <input
                                    type="number"
                                    name="usedStock"
                                    placeholder="Used Stock"
                                    className="form-control"
                                    value={formData.usedStock}
                                    onChange={handleChange}
                                />

                            </div>

                            <div className="col-md-6 mb-3">

                                <input
                                    type="number"
                                    name="purchasePricePerUnit"
                                    placeholder="Purchase Price"
                                    className="form-control"
                                    value={formData.purchasePricePerUnit}
                                    onChange={handleChange}
                                />

                            </div>

                            <div className="col-md-6 mb-3">

                                <input
                                    type="number"
                                    name="sellingPricePerUnit"
                                    placeholder="Selling Price"
                                    className="form-control"
                                    value={formData.sellingPricePerUnit}
                                    onChange={handleChange}
                                />

                            </div>

                            <div className="col-md-6 mb-3">

                                <input
                                    type="text"
                                    name="supplierName"
                                    placeholder="Supplier Name"
                                    className="form-control"
                                    value={formData.supplierName}
                                    onChange={handleChange}
                                />

                            </div>

                            <div className="col-md-6 mb-3">

                                <input
                                    type="date"
                                    name="stockAddedDate"
                                    className="form-control"
                                    value={formData.stockAddedDate}
                                    onChange={handleChange}
                                />

                            </div>

                            <div className="col-md-12 mb-3">

                                <textarea
                                    name="notes"
                                    placeholder="Notes"
                                    className="form-control"
                                    rows="3"
                                    value={formData.notes}
                                    onChange={handleChange}
                                />

                            </div>

                        </div>

                        <button className="btn btn-primary px-4">

                            {editingId
                                ? "Update Stock"
                                : "Add Stock"}

                        </button>

                    </form>

                </div>

            )}

            {/* TABLE SECTION */}

            <div className="card border-0 shadow-sm rounded-3 p-4">

                <div className="table-responsive">

                    <table className="table align-middle">

                        <thead className="table-light">

                            <tr>

                                <th>ID</th>
                                <th>Item</th>
                                <th>Category</th>
                                <th>Total</th>
                                <th>Used</th>
                                <th>Remaining</th>
                                <th>Status</th>
                                <th>Supplier</th>
                                <th>Action</th>

                            </tr>

                        </thead>

                        <tbody>

                            {stocks.length > 0 ? (

                                stocks.map((stock) => (

                                    <tr key={stock.id}>

                                        <td>{stock.id}</td>

                                        <td>{stock.itemName}</td>

                                        <td>{stock.itemCategory}</td>

                                        <td>{stock.totalStock}</td>

                                        <td>{stock.usedStock}</td>

                                        <td>{stock.remainingStock}</td>

                                        <td>

                                            <span
                                                className={`badge ${
                                                    stock.status === "LOW"
                                                        ? "bg-danger"
                                                        : stock.status === "MEDIUM"
                                                        ? "bg-warning text-dark"
                                                        : "bg-success"
                                                }`}
                                            >
                                                {stock.status}
                                            </span>

                                        </td>

                                        <td>{stock.supplierName}</td>

                                        <td>

                                            <button
                                                className="btn btn-info btn-sm text-white me-2"
                                                onClick={() => handleView(stock)}
                                            >
                                                View
                                            </button>

                                            <button
                                                className="btn btn-warning btn-sm me-2"
                                                onClick={() => handleEdit(stock)}
                                            >
                                                Edit
                                            </button>

                                            <button
                                                className="btn btn-danger btn-sm"
                                                onClick={() => handleDelete(stock.id)}
                                            >
                                                Delete
                                            </button>

                                        </td>

                                    </tr>

                                ))

                            ) : (

                                <tr>

                                    <td colSpan="9" className="text-center py-4">
                                        No Stock Available
                                    </td>

                                </tr>

                            )}

                        </tbody>

                    </table>

                </div>

            </div>

        </div>
    );
}

export default Stock;