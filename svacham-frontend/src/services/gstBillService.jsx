import axios from "axios";

const BASE_URL = "https://svacham-admin-management-2.onrender.com";

const getToken = () => {
    return localStorage.getItem("token");
};

const authHeader = () => ({
    headers: {
        Authorization: `Bearer ${getToken()}`
    }
});

export const getAllBills = async () => {
    return await axios.get(
        `${BASE_URL}/getAll`,
        authHeader()
    );
};

export const createBill = async (bill) => {
    return await axios.post(
        `${BASE_URL}/create`,
        bill,
        authHeader()
    );
};

export const updateBill = async (id, bill) => {
    return await axios.put(
        `${BASE_URL}/update/${id}`,
        bill,
        authHeader()
    );
};

export const deleteBill = async (id) => {
    return await axios.delete(
        `${BASE_URL}/delete/${id}`,
        authHeader()
    );
};

export const getBillById = async (id) => {
    return await axios.get(
        `${BASE_URL}/get/${id}`,
        authHeader()
    );
};

export const getBillSummary = async () => {
    return await axios.get(
        `${BASE_URL}/summary`,
        authHeader()
    );
};