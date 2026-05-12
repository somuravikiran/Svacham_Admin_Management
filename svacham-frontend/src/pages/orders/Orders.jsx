import { useEffect, useState } from "react";
import axios from "axios";


const BASE_URL = "https://svacham-admin-management-2.onrender.com";

function Orders() {

    const [orders, setOrders] = useState([]);

    const [showForm, setShowForm] = useState(false);

    const [editingId, setEditingId] = useState(null);

    const [viewOrder, setViewOrder] = useState(null);

    const token = localStorage.getItem("token");

    const [formData, setFormData] = useState({
        clientId: "",
        clientName: "",
        orderDate: "",
        deliveryDate: "",
        paidAmount: "",
        items: [
            {
                pickleType: "",
                packSizeKg: "",
                quantity: "",
                unitPrice: ""
            }
        ]
    });

    // ================= LOAD ORDERS =================

    const loadOrders = async () => {

        try {

            const response = await axios.get(
                `${BASE_URL}/api/orders/all`,
                {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                }
            );

            if (Array.isArray(response.data)) {
                setOrders(response.data);
            } else {
                setOrders([]);
            }

        } catch (error) {

            console.log(error);

            alert("Failed to load orders");
        }
    };

    useEffect(() => {
        loadOrders();
    }, []);

    // ================= FORM HANDLE =================

    const handleChange = (e) => {

        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    const handleItemChange = (index, e) => {

        const updatedItems = [...formData.items];

        updatedItems[index][e.target.name] = e.target.value;

        setFormData({
            ...formData,
            items: updatedItems
        });
    };

    const addItem = () => {

        setFormData({
            ...formData,
            items: [
                ...formData.items,
                {
                    pickleType: "",
                    packSizeKg: "",
                    quantity: "",
                    unitPrice: ""
                }
            ]
        });
    };

    // ================= SAVE ORDER =================

    const handleSubmit = async (e) => {

        e.preventDefault();

        try {

            if (editingId) {

                await axios.put(
                    `${BASE_URL}/api/orders/update/${editingId}`,
                    formData,
                    {
                        headers: {
                            Authorization: `Bearer ${token}`
                        }
                    }
                );

                alert("Order Updated");

            } else {

                await axios.post(
                    `${BASE_URL}/api/orders/add`,
                    formData,
                    {
                        headers: {
                            Authorization: `Bearer ${token}`
                        }
                    }
                );

                alert("Order Added");
            }

            resetForm();

            loadOrders();

        } catch (error) {

            console.log(error);

            alert("Save Failed");
        }
    };

    // ================= DELETE =================

    const handleDelete = async (id) => {

        try {

            await axios.delete(
                `${BASE_URL}/api/orders/delete/${id}`,
                {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                }
            );

            alert("Order Deleted");

            loadOrders();

        } catch (error) {

            console.log(error);

            alert("Delete Failed");
        }
    };

    // ================= EDIT =================

    const handleEdit = (order) => {

        setEditingId(order.orderId);

        setFormData({
            clientId: order.clientId,
            clientName: order.clientName,
            orderDate: order.orderDate,
            deliveryDate: order.deliveryDate,
            paidAmount: order.paidAmount,
            items: order.items || [
                {
                    pickleType: "",
                    packSizeKg: "",
                    quantity: "",
                    unitPrice: ""
                }
            ]
        });

        setShowForm(true);

        window.scrollTo(0, 0);
    };

    // ================= VIEW =================

    const handleView = (order) => {
        setViewOrder(order);
    };

    // ================= RESET =================

    const resetForm = () => {

        setEditingId(null);

        setShowForm(false);

        setFormData({
            clientId: "",
            clientName: "",
            orderDate: "",
            deliveryDate: "",
            paidAmount: "",
            items: [
                {
                    pickleType: "",
                    packSizeKg: "",
                    quantity: "",
                    unitPrice: ""
                }
            ]
        });
    };

    // ================= TOTAL SUMMARY =================

    const totalRevenue = orders.reduce(
        (sum, order) => sum + (order.totalAmount || 0),
        0
    );

    const totalPending = orders.reduce(
        (sum, order) => sum + (order.pendingAmount || 0),
        0
    );

    return (

        <div className="container mt-4">

            {/* ================= HEADER ================= */}

            <div className="d-flex justify-content-between align-items-center mb-4">

                <h2>Orders Management</h2>

                <button
                    className="btn btn-primary"
                    onClick={() => {
                        resetForm();
                        setShowForm(!showForm);
                    }}
                >
                    {showForm ? "Close Form" : "Add Order"}
                </button>

            </div>

            {/* ================= SUMMARY ================= */}

            <div className="row mb-4">

                <div className="col-md-4">
                    <div className="card p-3 shadow-sm">
                        <h5>Total Orders</h5>
                        <h3>{orders.length}</h3>
                    </div>
                </div>

                <div className="col-md-4">
                    <div className="card p-3 shadow-sm">
                        <h5>Total Revenue</h5>
                        <h3>₹ {totalRevenue}</h3>
                    </div>
                </div>

                <div className="col-md-4">
                    <div className="card p-3 shadow-sm">
                        <h5>Total Pending</h5>
                        <h3>₹ {totalPending}</h3>
                    </div>
                </div>

            </div>

            {/* ================= FORM ================= */}

            {
                showForm && (

                    <div className="card p-4 mb-5 shadow">

                        <h4 className="mb-4">
                            {editingId ? "Edit Order" : "Add Order"}
                        </h4>

                        <form onSubmit={handleSubmit}>

                            <div className="row">

                                <div className="col-md-6 mb-3">
                                    <input
                                        type="number"
                                        name="clientId"
                                        placeholder="Client ID"
                                        className="form-control"
                                        value={formData.clientId}
                                        onChange={handleChange}
                                    />
                                </div>

                                <div className="col-md-6 mb-3">
                                    <input
                                        type="text"
                                        name="clientName"
                                        placeholder="Client Name"
                                        className="form-control"
                                        value={formData.clientName}
                                        onChange={handleChange}
                                    />
                                </div>

                                <div className="col-md-6 mb-3">
                                    <input
                                        type="date"
                                        name="orderDate"
                                        className="form-control"
                                        value={formData.orderDate}
                                        onChange={handleChange}
                                    />
                                </div>

                                <div className="col-md-6 mb-3">
                                    <input
                                        type="date"
                                        name="deliveryDate"
                                        className="form-control"
                                        value={formData.deliveryDate}
                                        onChange={handleChange}
                                    />
                                </div>

                                <div className="col-md-6 mb-3">
                                    <input
                                        type="number"
                                        name="paidAmount"
                                        placeholder="Paid Amount"
                                        className="form-control"
                                        value={formData.paidAmount}
                                        onChange={handleChange}
                                    />
                                </div>

                            </div>

                            <hr />

                            <h5>Order Items</h5>

                            {
                                formData.items.map((item, index) => (

                                    <div
                                        key={index}
                                        className="border rounded p-3 mb-3"
                                    >

                                        <div className="row">

                                            <div className="col-md-3 mb-2">
                                                <input
                                                    type="text"
                                                    name="pickleType"
                                                    placeholder="Pickle Type"
                                                    className="form-control"
                                                    value={item.pickleType}
                                                    onChange={(e) =>
                                                        handleItemChange(index, e)
                                                    }
                                                />
                                            </div>

                                            <div className="col-md-3 mb-2">
                                                <input
                                                    type="number"
                                                    name="packSizeKg"
                                                    placeholder="Pack Size"
                                                    className="form-control"
                                                    value={item.packSizeKg}
                                                    onChange={(e) =>
                                                        handleItemChange(index, e)
                                                    }
                                                />
                                            </div>

                                            <div className="col-md-3 mb-2">
                                                <input
                                                    type="number"
                                                    name="quantity"
                                                    placeholder="Quantity"
                                                    className="form-control"
                                                    value={item.quantity}
                                                    onChange={(e) =>
                                                        handleItemChange(index, e)
                                                    }
                                                />
                                            </div>

                                            <div className="col-md-3 mb-2">
                                                <input
                                                    type="number"
                                                    name="unitPrice"
                                                    placeholder="Unit Price"
                                                    className="form-control"
                                                    value={item.unitPrice}
                                                    onChange={(e) =>
                                                        handleItemChange(index, e)
                                                    }
                                                />
                                            </div>

                                        </div>

                                    </div>
                                ))
                            }

                            <button
                                type="button"
                                className="btn btn-secondary me-3"
                                onClick={addItem}
                            >
                                Add Item
                            </button>

                            <button className="btn btn-success">
                                {editingId ? "Update Order" : "Save Order"}
                            </button>

                        </form>

                    </div>
                )
            }

            {/* ================= TABLE ================= */}

            <div className="card p-4 shadow">

                <h4 className="mb-4">All Orders</h4>

                <table className="table table-bordered table-hover">

                    <thead className="table-dark">

                        <tr>
                            <th>ID</th>
                            <th>Client</th>
                            <th>Total</th>
                            <th>Paid</th>
                            <th>Pending</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>

                    </thead>

                    <tbody>

                        {
                            orders.length > 0 ? (

                                orders.map((order) => (

                                    <tr key={order.orderId}>

                                        <td>{order.orderId}</td>

                                        <td>{order.clientName}</td>

                                        <td>₹ {order.totalAmount}</td>

                                        <td>₹ {order.paidAmount}</td>

                                        <td>₹ {order.pendingAmount}</td>

                                        <td>{order.orderStatus}</td>

                                        <td>

                                            <button
                                                className="btn btn-info btn-sm me-2"
                                                onClick={() => handleView(order)}
                                            >
                                                View
                                            </button>

                                            <button
                                                className="btn btn-warning btn-sm me-2"
                                                onClick={() => handleEdit(order)}
                                            >
                                                Edit
                                            </button>

                                            <button
                                                className="btn btn-danger btn-sm"
                                                onClick={() =>
                                                    handleDelete(order.orderId)
                                                }
                                            >
                                                Delete
                                            </button>

                                        </td>

                                    </tr>
                                ))

                            ) : (

                                <tr>
                                    <td colSpan="7" className="text-center">
                                        No Orders Found
                                    </td>
                                </tr>
                            )
                        }

                    </tbody>

                </table>

            </div>

            {/* ================= VIEW MODAL ================= */}

            {
                viewOrder && (

                    <div
                        className="modal d-block"
                        style={{
                            backgroundColor: "rgba(0,0,0,0.5)"
                        }}
                    >

                        <div className="modal-dialog modal-lg">

                            <div className="modal-content">

                                <div className="modal-header">

                                    <h4>Order Details</h4>

                                    <button
                                        className="btn-close"
                                        onClick={() => setViewOrder(null)}
                                    ></button>

                                </div>

                                <div className="modal-body">

                                    <p>
                                        <strong>Client:</strong>{" "}
                                        {viewOrder.clientName}
                                    </p>

                                    <p>
                                        <strong>Order Date:</strong>{" "}
                                        {viewOrder.orderDate}
                                    </p>

                                    <p>
                                        <strong>Delivery Date:</strong>{" "}
                                        {viewOrder.deliveryDate}
                                    </p>

                                    <p>
                                        <strong>Total Amount:</strong> ₹{" "}
                                        {viewOrder.totalAmount}
                                    </p>

                                    <p>
                                        <strong>Paid Amount:</strong> ₹{" "}
                                        {viewOrder.paidAmount}
                                    </p>

                                    <p>
                                        <strong>Pending Amount:</strong> ₹{" "}
                                        {viewOrder.pendingAmount}
                                    </p>

                                    <p>
                                        <strong>Status:</strong>{" "}
                                        {viewOrder.orderStatus}
                                    </p>

                                    <hr />

                                    <h5>Items</h5>

                                    <table className="table table-bordered">

                                        <thead>

                                            <tr>
                                                <th>Pickle</th>
                                                <th>Pack Size</th>
                                                <th>Quantity</th>
                                                <th>Unit Price</th>
                                                <th>Sub Total</th>
                                            </tr>

                                        </thead>

                                        <tbody>

                                            {
                                                viewOrder.items?.map(
                                                    (item, index) => (

                                                        <tr key={index}>

                                                            <td>
                                                                {item.pickleType}
                                                            </td>

                                                            <td>
                                                                {item.packSizeKg}
                                                            </td>

                                                            <td>
                                                                {item.quantity}
                                                            </td>

                                                            <td>
                                                                ₹ {item.unitPrice}
                                                            </td>

                                                            <td>
                                                                ₹ {item.subTotal}
                                                            </td>

                                                        </tr>
                                                    )
                                                )
                                            }

                                        </tbody>

                                    </table>

                                </div>

                            </div>

                        </div>

                    </div>
                )
            }

        </div>
    );
}

export default Orders;