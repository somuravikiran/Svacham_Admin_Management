import axios from "axios";

const BASE_URL = "https://svacham-admin-management-2.onrender.com";

const getToken = () => {
    return localStorage.getItem("token");
};

export const addSalary = async (salaryData) => {

    return await axios.post(
        `${BASE_URL}/add`,
        salaryData,
        {
            headers: {
                Authorization: `Bearer ${getToken()}`
            }
        }
    );
};

export const getAllSalaries = async () => {

    return await axios.get(
        `${BASE_URL}/all`,
        {
            headers: {
                Authorization: `Bearer ${getToken()}`
            }
        }
    );
};

export const deleteSalary = async (id) => {

    return await axios.delete(
        `${BASE_URL}/delete/${id}`,
        {
            headers: {
                Authorization: `Bearer ${getToken()}`
            }
        }
    );
};

export const getSalarySummary = async () => {

    return await axios.get(
        `${BASE_URL}/summary`,
        {
            headers: {
                Authorization: `Bearer ${getToken()}`
            }
        }
    );
};