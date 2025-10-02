export const apiBase = (import.meta.env.VITE_API_BASE_URL ?? '/api').replace(/\/$/, '');

async function handleResponse<T>(response: Response): Promise<T> {
  if (!response.ok) {
    const text = await response.text();
    throw new Error(text || response.statusText);
  }
  return (await response.json()) as T;
}

export interface Ingredient {
  id: string;
  name: string;
  type: 'WRAP' | 'PROTEIN' | 'VEGGIES' | 'CHEESE' | 'SAUCE';
}

export interface Taco {
  id?: number;
  name: string;
  createdAt?: string;
  ingredients: Ingredient[];
}

export interface Order {
  id?: number;
  deliveryName: string;
  deliveryStreet: string;
  deliveryCity: string;
  deliveryState: string;
  deliveryZip: string;
  ccNumber: string;
  ccExpiration: string;
  ccCVV: string;
  tacos: Taco[];
  placedAt?: string;
}

export async function fetchIngredients(): Promise<Ingredient[]> {
  return await handleResponse<Ingredient[]>(await fetch(`${apiBase}/ingredients`));
}

export async function createIngredient(ingredient: Ingredient): Promise<Ingredient> {
  const response = await fetch(`${apiBase}/ingredients`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(ingredient)
  });
  return await handleResponse<Ingredient>(response);
}

export async function fetchRecentTacos(): Promise<Taco[]> {
  return await handleResponse<Taco[]>(await fetch(`${apiBase}/design/recent`));
}

export async function createTaco(taco: Taco): Promise<Taco> {
  const payload = {
    ...taco,
    ingredients: taco.ingredients.map(ingredient => ({ id: ingredient.id }))
  };
  const response = await fetch(`${apiBase}/design`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload)
  });
  return await handleResponse<Taco>(response);
}

export async function fetchOrders(): Promise<Order[]> {
  return await handleResponse<Order[]>(await fetch(`${apiBase}/orders`));
}

export async function createOrder(order: Order): Promise<Order> {
  const payload = {
    ...order,
    tacos: order.tacos.map(taco => ({ id: taco.id }))
  };
  const response = await fetch(`${apiBase}/orders`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload)
  });
  return await handleResponse<Order>(response);
}
