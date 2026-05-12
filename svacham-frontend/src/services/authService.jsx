import axios from "axios";

const BASE_URL = "https://svacham-admin-management-2.onrender.com";

export const loginUser = async (loginData) => {
    return await axios.post(
        `${BASE_URL}/auth/login`,
        loginData
    );
};

export const registerUser = async (registerData) => {
    return await axios.post(
        `${BASE_URL}/auth/register`,
        registerData
    );
};