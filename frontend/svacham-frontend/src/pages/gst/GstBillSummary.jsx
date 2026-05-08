import { useEffect, useState } from "react";
import { getBillSummary } from "../../services/gstBillService";

const GstBillSummary = () => {

    const [summary, setSummary] = useState({});

    const fetchSummary = async () => {

        try {

            const response = await getBillSummary();

            setSummary(response.data.data);

        } catch (error) {

            console.log(error);

        }
    };

    useEffect(() => {

        fetchSummary();

    }, []);

    return (

        <div className="container mt-4">

            <div className="card shadow p-4">

                <h2 className="mb-4">
                    GST Bill Summary
                </h2>

                <h5>
                    Total Bills :
                    {summary.totalBills}
                </h5>

                <h5>
                    Total Bill Amount :
                    {summary.totalBillAmount}
                </h5>

                <h5>
                    Total GST Amount :
                    {summary.totalGstAmount}
                </h5>

                <h5>
                    Total Final Amount :
                    {summary.totalFinalAmount}
                </h5>

                <h5>
                    Paid Bills :
                    {summary.paidBills}
                </h5>

                <h5>
                    Pending Bills :
                    {summary.pendingBills}
                </h5>

                <h5>
                    Partial Bills :
                    {summary.partialBills}
                </h5>

            </div>

        </div>
    );
};

export default GstBillSummary;