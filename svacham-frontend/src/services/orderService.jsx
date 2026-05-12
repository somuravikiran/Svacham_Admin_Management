import axios from "axios";

const BASE_URL = "https://svacham-admin-management-2.onrender.com";

const getToken = () => {
    return localStorage.getItem("token");
};

export const getAllOrders = async () => {

    return await axios.get(
        `${BASE_URL}/api/orders/all`,
        {
            headers: {
                Authorization: `Bearer ${getToken()}`
            }
        }
    );
};

export const createOrder = async (orderData) => {

    return await axios.post(
        `${BASE_URL}/api/orders/add`,
        orderData,
        {
            headers: {
                Authorization: `Bearer ${getToken()}`
            }
        }
    );
};

export const deleteOrder = async (id) => {

    return await axios.delete(
        `${BASE_URL}/api/orders/delete/${id}`,
        {
            headers: {
                Authorization: `Bearer ${getToken()}`
            }
        }
    );
};

export const getOrderSummary = async () => {

    return await axios.get(
        `${BASE_URL}/api/orders/summary`,
        {
            headers: {
                Authorization: `Bearer ${getToken()}`
            }
        }
    );
};