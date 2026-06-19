import { useEffect, useState } from 'react';
import api from '../api/api';
import { Purchase } from '../types';

export default function PurchasesPage() {
  const [purchases, setPurchases] = useState<Purchase[]>([]);

  useEffect(() => {
    fetchPurchases();
  }, []);

  const fetchPurchases = async () => {
    const response = await api.get<{ content: Purchase[] }>('/purchases/my-purchases?size=20');
    setPurchases(response.data.content);
  };

  return (
    <div>
      <h2>My Purchases</h2>
      <div className="list-group">
        {purchases.map((purchase) => (
          <div key={purchase.id} className="list-group-item">
            <h5>{purchase.projectName}</h5>
            <p>Amount: ${purchase.amount.toFixed(2)}</p>
            <p>Purchased on: {new Date(purchase.purchaseDate).toLocaleString()}</p>
          </div>
        ))}
      </div>
    </div>
  );
}
