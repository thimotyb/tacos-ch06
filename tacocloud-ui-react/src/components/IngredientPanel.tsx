import { FormEvent, useEffect, useState } from 'react';
import { createIngredient, fetchIngredients, Ingredient } from '../api';

const ingredientTypes: Ingredient['type'][] = ['WRAP', 'PROTEIN', 'VEGGIES', 'CHEESE', 'SAUCE'];

export function IngredientPanel() {
  const [ingredients, setIngredients] = useState<Ingredient[]>([]);
  const [form, setForm] = useState<Ingredient>({ id: '', name: '', type: 'WRAP' });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    load();
  }, []);

  async function load() {
    try {
      setLoading(true);
      setIngredients(await fetchIngredients());
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to load ingredients');
    } finally {
      setLoading(false);
    }
  }

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    if (!form.id || !form.name) {
      setError('Ingredient id and name are required');
      return;
    }
    try {
      setError(null);
      await createIngredient(form);
      setForm({ id: '', name: '', type: 'WRAP' });
      await load();
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to create ingredient');
    }
  }

  return (
    <section>
      <header>
        <h2>Ingredients</h2>
        <button type="button" onClick={load} disabled={loading}>
          Refresh
        </button>
      </header>
      {error ? <div className="error">{error}</div> : null}
      <form className="panel" onSubmit={handleSubmit}>
        <label>
          Id
          <input value={form.id} onChange={event => setForm({ ...form, id: event.target.value })} />
        </label>
        <label>
          Name
          <input value={form.name} onChange={event => setForm({ ...form, name: event.target.value })} />
        </label>
        <label>
          Type
          <select value={form.type} onChange={event => setForm({ ...form, type: event.target.value as Ingredient['type'] })}>
            {ingredientTypes.map(type => (
              <option key={type} value={type}>
                {type}
              </option>
            ))}
          </select>
        </label>
        <button type="submit">Add Ingredient</button>
      </form>
      {loading ? <p>Loading...</p> : null}
      <ul className="list">
        {ingredients.map(ingredient => (
          <li key={ingredient.id}>
            <span>{ingredient.name}</span>
            <small>{ingredient.type}</small>
          </li>
        ))}
      </ul>
    </section>
  );
}
