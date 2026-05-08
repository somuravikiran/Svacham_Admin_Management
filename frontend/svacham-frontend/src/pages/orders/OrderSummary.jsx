import { useEffect, useState } from "react";
import { getOrderSummary } from "../../services/orderService";

export default function OrderSummary() {
  const [summary, setSummary] = useState({});

  useEffect(() => {
    const load = async () => {
      const res = await getOrderSummary();
      setSummary(res.data);
    };

    load();
  }, []);

  return (
    <div>
      <h2>Order Summary</h2>

      <p>Total Orders: {summary.totalOrders}</p>
      <p>Total Revenue: {summary.totalRevenue}</p>
      <p>Total Pending: {summary.totalPending}</p>
    </div>
  );
}