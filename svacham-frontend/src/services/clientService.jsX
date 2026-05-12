import axios from "axios";

const BASE_URL = "https://svacham-admin-management-2.onrender.com";

const getToken = () => {
    return localStorage.getItem("token");
};

const getConfig = () => {

    return {
        headers: {
            Authorization: `Bearer ${getToken()}`
        }
    };
};

export const addClient = async (clientData) => {

    return axios.post(
        `${BASE_URL}/add`,
        clientData,
        getConfig()
    );
};

export const getAllClients = async () => {

    return axios.get(
        `${BASE_URL}/all`,
        getConfig()
    );
};

export const deleteClient = async (id) => {

    return axios.delete(
        `${BASE_URL}/delete/${id}`,
        getConfig()
    );
};

export const getClientSummary = async () => {

    return axios.get(
        `${BASE_URL}/summary`,
        getConfig()
    );
};