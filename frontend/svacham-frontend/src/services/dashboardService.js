import API from "../api/axiosConfig";

export const getDashboard = async () => {
    return await API.get("/api/dashboard/admin-summary");
};