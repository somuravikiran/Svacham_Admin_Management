// src/services/spendingService.jsx

import axios from "axios";

const BASE_URL = "https://svacham-admin-management-2.onrender.com";

const getToken = () => {
    return localStorage.getItem("token");
};

export const getAllSpendings = async () => {
    return await axios.get(`${BASE_URL}/all`, {
        headers: {
            Authorization: `Bearer ${getToken()}`
        }
    });
};

export const addSpending = async (spendingData) => {
    return await axios.post(
        `${BASE_URL}/add`,
        spendingData,
        {
            headers: {
                Authorization: `Bearer ${getToken()}`
            }
        }
    );
};

export const deleteSpending = async (id) => {
    return await axios.delete(`${BASE_URL}/delete/${id}`, {
        headers: {
            Authorization: `Bearer ${getToken()}`
        }
    });
};

export const updateSpending = async (id, spendingData) => {
    return await axios.put(
        `${BASE_URL}/update/${id}`,
        spendingData,
        {
            headers: {
                Authorization: `Bearer ${getToken()}`
            }
        }
    );
};

export const getSpendingSummary = async () => {
    return await axios.get(`${BASE_URL}/summary`, {
        headers: {
            Authorization: `Bearer ${getToken()}`
        }
    });
};