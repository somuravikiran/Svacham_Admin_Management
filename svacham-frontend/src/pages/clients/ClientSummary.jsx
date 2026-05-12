import { useEffect, useState } from "react";
import { getClientSummary } from "../../services/clientService";

const ClientSummary = () => {

    const [summary, setSummary] = useState({});

    const fetchSummary = async () => {

        try {

            const response = await getClientSummary();

            setSummary(response.data);

        } catch (error) {

            console.log(error);
        }
    };

    useEffect(() => {

        fetchSummary();

    }, []);

    return (

        <div className="card shadow p-4 mb-4">

            <h2 className="mb-4">
                Client Summary
            </h2>

            <h5>
                Total Clients :
                {summary.totalClients}
            </h5>

            <h5>
                Total Business Amount :
                {summary.totalBusinessAmount}
            </h5>

            <h5>
                Total Amount Paid :
                {summary.totalAmountPaid}
            </h5>

            <h5>
                Total Pending Balance :
                {summary.totalPendingBalance}
            </h5>

        </div>
    );
};

export default ClientSummary;