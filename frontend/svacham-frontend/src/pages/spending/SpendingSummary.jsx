function StockSummary({ data }) {

    if (!data) return <div>No Stock Data</div>;

    return (
        <div className="card">
            <h3>Stocks</h3>

            {data.stocks && data.stocks.length > 0 ? (
                data.stocks.map((stock, index) => (
                    <p key={index}>
                        {stock.name} - ₹{stock.value}
                    </p>
                ))
            ) : (
                <p>No stocks available</p>
            )}

        </div>
    );
}

export default StockSummary;