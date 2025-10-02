import { FormEvent, useEffect, useState } from 'react';
import { createOrder, fetchOrders, fetchRecentTacos, Order, Taco } from '../api';

const initialOrder: Order = {
  deliveryName: '',
  deliveryStreet: '',
  deliveryCity: '',
  deliveryState: '',
  deliveryZip: '',
  ccNumber: '',
  ccExpiration: '',
  ccCVV: '',
  tacos: []
};

export function OrderBoard() {
  const [orders, setOrders] = useState<Order[]>([]);
  const [tacos, setTacos] = useState<Taco[]>([]);
  const [order, setOrder] = useState<Order>(initialOrder);
  const [selectedTacoId, setSelectedTacoId] = useState<number | undefined>();
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    load();
  }, []);

  async function load() {
    try {
      setLoading(true);
      const [existingOrders, recentTacos] = await Promise.all([fetchOrders(), fetchRecentTacos()]);
      setOrders(existingOrders);
      setTacos(recentTacos);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to load orders');
    } finally {
      setLoading(false);
    }
  }

  function update(field: keyof Order, value: string) {
    setOrder(current => ({ ...current, [field]: value }));
  }

  function addSelectedTaco() {
    if (!selectedTacoId) {
      return;
    }
    const taco = tacos.find(t => t.id === selectedTacoId);
    if (!taco) {
      return;
    }
    setOrder(current => ({ ...current, tacos: [...current.tacos, taco] }));
    setSelectedTacoId(undefined);
  }

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    if (order.tacos.length === 0) {
      setError('Add at least one taco');
      return;
    }
    try {
      setError(null);
      const saved = await createOrder(order);
      setOrders(current => [saved, ...current]);
      setOrder(initialOrder);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to create order');
    }
  }

  return (
    <section>
      <header>
        <h2>Orders</h2>
        <button type="button" onClick={load} disabled={loading}>
          Refresh
        </button>
      </header>
      {error ? <div className="error">{error}</div> : null}
      <form className="panel order-form" onSubmit={handleSubmit}>
        <div className="grid">
          <label>
            Name
            <input value={order.deliveryName} onChange={e => update('deliveryName', e.target.value)} />
          </label>
          <label>
            Street
            <input value={order.deliveryStreet} onChange={e => update('deliveryStreet', e.target.value)} />
          </label>
          <label>
            City
            <input value={order.deliveryCity} onChange={e => update('deliveryCity', e.target.value)} />
          </label>
          <label>
            State
            <input value={order.deliveryState} onChange={e => update('deliveryState', e.target.value)} />
          </label>
          <label>
            Zip
            <input value={order.deliveryZip} onChange={e => update('deliveryZip', e.target.value)} />
          </label>
        </div>
        <div className="grid">
          <label>
            Card Number
            <input value={order.ccNumber} onChange={e => update('ccNumber', e.target.value)} />
          </label>
          <label>
            Expiration (MM/YY)
            <input value={order.ccExpiration} onChange={e => update('ccExpiration', e.target.value)} />
          </label>
          <label>
            CVV
            <input value={order.ccCVV} onChange={e => update('ccCVV', e.target.value)} />
          </label>
        </div>
        <div className="taco-selector">
          <select
            value={selectedTacoId ?? ''}
            onChange={e => setSelectedTacoId(e.target.value ? Number(e.target.value) : undefined)}
          >
            <option value="">Select taco</option>
            {tacos.map(taco => (
              <option key={taco.id ?? taco.name} value={taco.id}>
                {taco.name}
              </option>
            ))}
          </select>
          <button type="button" onClick={addSelectedTaco} disabled={!selectedTacoId}>
            Add Taco
          </button>
        </div>
        <div className="selected-tacos">
          {order.tacos.map((taco, index) => (
            <span key={`${taco.id ?? taco.name}-${index}`} className="chip">
              {taco.name}
            </span>
          ))}
        </div>
        <button type="submit">Place Order</button>
      </form>
      {loading ? <p>Loading orders...</p> : null}
      <ul className="list">
        {orders.map(orderItem => (
          <li key={orderItem.id ?? orderItem.placedAt}>
            <div>
              <strong>{orderItem.deliveryName}</strong>
              <small>{orderItem.deliveryCity}</small>
            </div>
            <ol>
              {orderItem.tacos.map(taco => (
                <li key={taco.id ?? taco.name}>{taco.name}</li>
              ))}
            </ol>
          </li>
        ))}
      </ul>
    </section>
  );
}
