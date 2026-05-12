// src/services/stockService.jsx

import axios from "axios";

const BASE_URL = "https://svacham-admin-management-2.onrender.com";

const getToken = () => {
    return localStorage.getItem("token");
};

export const getAllStock = async () => {
    return await axios.get(`${BASE_URL}/all`, {
        headers: {
            Authorization: `Bearer ${getToken()}`
        }
    });
};

export const addStock = async (stockData) => {
    return await axios.post(
        `${BASE_URL}/add`,
        stockData,
        {
            headers: {
                Authorization: `Bearer ${getToken()}`
            }
        }
    );
};

export const updateStock = async (id, stockData) => {
    return await axios.put(
        `${BASE_URL}/update/${id}`,
        stockData,
        {
            headers: {
                Authorization: `Bearer ${getToken()}`
            }
        }
    );
};

export const deleteStock = async (id) => {
    return await axios.delete(
        `${BASE_URL}/delete/${id}`,
        {
            headers: {
                Authorization: `Bearer ${getToken()}`
            }
        }
    );
};

export const getStockSummary = async () => {
    return await axios.get(
        `${BASE_URL}/summary`,
        {
            headers: {
                Authorization: `Bearer ${getToken()}`
            }
        }
    );
};