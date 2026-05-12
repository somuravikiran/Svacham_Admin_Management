import { useEffect, useState } from "react";
import axios from "axios";

const BASE_URL = "https://svacham-admin-management-2.onrender.com";

function Clients() {

    const token = localStorage.getItem("token");

    const [clients, setClients] = useState([]);

    const [showForm, setShowForm] = useState(false);

    const [editingId, setEditingId] = useState(null);

    const [viewClient, setViewClient] = useState(null);

    const [formData, setFormData] = useState({
        clientName: "",
        phoneNumber: "",
        address: "",
        city: "",
        state: "",
        totalAmount: "",
        amountPaid: ""
    });

    // ================= LOAD CLIENTS =================

    const loadClients = async () => {

        try {

            const response = await axios.get(
                `${BASE_URL}/api/clients/all`,
                {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                }
            );

            if (Array.isArray(response.data)) {
                setClients(response.data);
            } else {
                setClients([]);
            }

        } catch (error) {

            console.log(error);

            alert("Failed to load clients");
        }
    };

    useEffect(() => {
        loadClients();
    }, []);

    // ================= HANDLE CHANGE =================

    const handleChange = (e) => {

        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    // ================= ADD / UPDATE =================

    const handleSubmit = async (e) => {

        e.preventDefault();

        try {

            const payload = {
                ...formData,
                totalAmount: Number(formData.totalAmount),
                amountPaid: Number(formData.amountPaid)
            };

            if (editingId) {

                await axios.put(
                    `${BASE_URL}/api/clients/update/${editingId}`,
                    payload,
                    {
                        headers: {
                            Authorization: `Bearer ${token}`
                        }
                    }
                );

                alert("Client Updated Successfully");

            } else {

                await axios.post(
                    `${BASE_URL}/api/clients/add`,
                    payload,
                    {
                        headers: {
                            Authorization: `Bearer ${token}`
                        }
                    }
                );

                alert("Client Added Successfully");
            }

            resetForm();

            loadClients();

        } catch (error) {

            console.log(error);

            alert("Save Failed");
        }
    };

    // ================= DELETE =================

    const handleDelete = async (id) => {

        try {

            await axios.delete(
                `${BASE_URL}/api/clients/delete/${id}`,
                {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                }
            );

            alert("Client Deleted Successfully");

            loadClients();

        } catch (error) {

            console.log(error);

            alert("Delete Failed");
        }
    };

    // ================= EDIT =================

    const handleEdit = (client) => {

        setEditingId(client.id);

        setFormData({
            clientName: client.clientName || "",
            phoneNumber: client.phoneNumber || "",
            address: client.address || "",
            city: client.city || "",
            state: client.state || "",
            totalAmount: client.totalAmount || "",
            amountPaid: client.amountPaid || ""
        });

        setShowForm(true);

        window.scrollTo(0, 0);
    };

    // ================= VIEW =================

    const handleView = (client) => {

        setViewClient(client);
    };

    // ================= RESET =================

    const resetForm = () => {

        setEditingId(null);

        setShowForm(false);

        setFormData({
            clientName: "",
            phoneNumber: "",
            address: "",
            city: "",
            state: "",
            totalAmount: "",
            amountPaid: ""
        });
    };

    // ================= SUMMARY =================

    const totalBusiness = clients.reduce(
        (sum, client) => sum + (client.totalAmount || 0),
        0
    );

    const totalPaid = clients.reduce(
        (sum, client) => sum + (client.amountPaid || 0),
        0
    );

    const totalBalance = clients.reduce(
        (sum, client) => sum + (client.balanceAmount || 0),
        0
    );

    return (

        <div className="container mt-4">

            {/* ================= HEADER ================= */}

            <div className="d-flex justify-content-between align-items-center mb-4">

                <h2>Clients Management</h2>

                <button
                    className="btn btn-primary"
                    onClick={() => {
                        resetForm();
                        setShowForm(!showForm);
                    }}
                >
                    {showForm ? "Close Form" : "Add Client"}
                </button>

            </div>

            {/* ================= SUMMARY ================= */}

            <div className="row mb-4">

                <div className="col-md-3">
                    <div className="card p-3 shadow-sm">
                        <h5>Total Clients</h5>
                        <h3>{clients.length}</h3>
                    </div>
                </div>

                <div className="col-md-3">
                    <div className="card p-3 shadow-sm">
                        <h5>Total Business</h5>
                        <h3>₹ {totalBusiness}</h3>
                    </div>
                </div>

                <div className="col-md-3">
                    <div className="card p-3 shadow-sm">
                        <h5>Total Paid</h5>
                        <h3>₹ {totalPaid}</h3>
                    </div>
                </div>

                <div className="col-md-3">
                    <div className="card p-3 shadow-sm">
                        <h5>Total Balance</h5>
                        <h3>₹ {totalBalance}</h3>
                    </div>
                </div>

            </div>

            {/* ================= FORM ================= */}

            {
                showForm && (

                    <div className="card shadow p-4 mb-4">

                        <h4 className="mb-4">
                            {editingId ? "Update Client" : "Add Client"}
                        </h4>

                        <form onSubmit={handleSubmit}>

                            <div className="row">

                                <div className="col-md-6 mb-3">
                                    <input
                                        type="text"
                                        name="clientName"
                                        placeholder="Client Name"
                                        className="form-control"
                                        value={formData.clientName}
                                        onChange={handleChange}
                                        required
                                    />
                                </div>

                                <div className="col-md-6 mb-3">
                                    <input
                                        type="text"
                                        name="phoneNumber"
                                        placeholder="Phone Number"
                                        className="form-control"
                                        value={formData.phoneNumber}
                                        onChange={handleChange}
                                        required
                                    />
                                </div>

                                <div className="col-md-12 mb-3">
                                    <input
                                        type="text"
                                        name="address"
                                        placeholder="Address"
                                        className="form-control"
                                        value={formData.address}
                                        onChange={handleChange}
                                    />
                                </div>

                                <div className="col-md-6 mb-3">
                                    <input
                                        type="text"
                                        name="city"
                                        placeholder="City"
                                        className="form-control"
                                        value={formData.city}
                                        onChange={handleChange}
                                    />
                                </div>

                                <div className="col-md-6 mb-3">
                                    <input
                                        type="text"
                                        name="state"
                                        placeholder="State"
                                        className="form-control"
                                        value={formData.state}
                                        onChange={handleChange}
                                    />
                                </div>

                                <div className="col-md-6 mb-3">
                                    <input
                                        type="number"
                                        name="totalAmount"
                                        placeholder="Total Amount"
                                        className="form-control"
                                        value={formData.totalAmount}
                                        onChange={handleChange}
                                    />
                                </div>

                                <div className="col-md-6 mb-3">
                                    <input
                                        type="number"
                                        name="amountPaid"
                                        placeholder="Amount Paid"
                                        className="form-control"
                                        value={formData.amountPaid}
                                        onChange={handleChange}
                                    />
                                </div>

                            </div>

                            <button className="btn btn-success">
                                {editingId ? "Update Client" : "Save Client"}
                            </button>

                        </form>

                    </div>
                )
            }

            {/* ================= TABLE ================= */}

            <div className="card shadow p-4">

                <h4 className="mb-4">All Clients</h4>

                <table className="table table-bordered table-hover">

                    <thead className="table-dark">

                        <tr>

                            <th>ID</th>
                            <th>Name</th>
                            <th>Phone</th>
                            <th>City</th>
                            <th>Total</th>
                            <th>Paid</th>
                            <th>Balance</th>
                            <th>Actions</th>

                        </tr>

                    </thead>

                    <tbody>

                        {
                            clients.length > 0 ? (

                                clients.map((client) => (

                                    
                                    <tr key={client.id}>

                                        <td>{client.id}</td>

                                        <td>{client.clientName}</td>

                                        <td>{client.phoneNumber}</td>

                                        <td>{client.city}</td>

                                        <td>₹ {client.totalAmount}</td>

                                        <td>₹ {client.amountPaid}</td>

                                        <td>₹ {client.balanceAmount}</td>

                                        <td>

                                            <button
                                                className="btn btn-info btn-sm me-2"
                                                onClick={() => handleView(client)}
                                            >
                                                View
                                            </button>

                                            <button
                                                className="btn btn-warning btn-sm me-2"
                                                onClick={() => handleEdit(client)}
                                            >
                                                Edit
                                            </button>

                                            <button
                                                className="btn btn-danger btn-sm"
                                                onClick={() =>
                                                    handleDelete(client.id)
                                                }
                                            >
                                                Delete
                                            </button>

                                        </td>

                                    </tr>
                                ))

                            ) : (

                                <tr>

                                    <td
                                        colSpan="8"
                                        className="text-center"
                                    >
                                        No Clients Found
                                    </td>

                                </tr>
                            )
                        }

                    </tbody>

                </table>

            </div>

            {/* ================= VIEW MODAL ================= */}

            {
                viewClient && (

                    <div
                        className="modal d-block"
                        style={{
                            backgroundColor: "rgba(0,0,0,0.5)"
                        }}
                    >

                        <div className="modal-dialog modal-lg">

                            <div className="modal-content">

                                <div className="modal-header">

                                    <h4>Client Details</h4>

                                    <button
                                        className="btn-close"
                                        onClick={() => setViewClient(null)}
                                    ></button>

                                </div>

                                <div className="modal-body">

                                    <div className="row">

                                        <div className="col-md-6 mb-3">
                                            <strong>Client Name:</strong>
                                            <p>{viewClient.clientName}</p>
                                        </div>

                                        <div className="col-md-6 mb-3">
                                            <strong>Phone Number:</strong>
                                            <p>{viewClient.phoneNumber}</p>
                                        </div>

                                        <div className="col-md-12 mb-3">
                                            <strong>Address:</strong>
                                            <p>{viewClient.address}</p>
                                        </div>

                                        <div className="col-md-6 mb-3">
                                            <strong>City:</strong>
                                            <p>{viewClient.city}</p>
                                        </div>

                                        <div className="col-md-6 mb-3">
                                            <strong>State:</strong>
                                            <p>{viewClient.state}</p>
                                        </div>

                                        <div className="col-md-4 mb-3">
                                            <strong>Total Amount:</strong>
                                            <p>₹ {viewClient.totalAmount}</p>
                                        </div>

                                        <div className="col-md-4 mb-3">
                                            <strong>Amount Paid:</strong>
                                            <p>₹ {viewClient.amountPaid}</p>
                                        </div>

                                        <div className="col-md-4 mb-3">
                                            <strong>Balance Amount:</strong>
                                            <p>₹ {viewClient.balanceAmount}</p>
                                        </div>

                                        <div className="col-md-12 mb-3">
                                            <strong>Created Date:</strong>
                                            <p>{viewClient.createdDate}</p>
                                        </div>

                                    </div>

                                </div>

                            </div>

                        </div>

                    </div>
                )
            }

        </div>
    );
}

export default Clients;