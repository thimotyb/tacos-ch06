import { FormEvent, useEffect, useMemo, useState } from 'react';
import { createTaco, fetchIngredients, fetchRecentTacos, Ingredient, Taco } from '../api';

interface TacoDraft {
  name: string;
  ingredientIds: Set<string>;
}

export function TacoDesigner({ onTacoCreated }: { onTacoCreated?: (taco: Taco) => void }) {
  const [ingredients, setIngredients] = useState<Ingredient[]>([]);
  const [tacos, setTacos] = useState<Taco[]>([]);
  const [draft, setDraft] = useState<TacoDraft>({ name: '', ingredientIds: new Set() });
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    load();
  }, []);

  const grouped = useMemo(() => {
    const map: Record<Ingredient['type'], Ingredient[]> = {
      WRAP: [],
      PROTEIN: [],
      VEGGIES: [],
      CHEESE: [],
      SAUCE: []
    };
    ingredients.forEach(ingredient => map[ingredient.type].push(ingredient));
    return map;
  }, [ingredients]);

  async function load() {
    try {
      setLoading(true);
      const [ing, recent] = await Promise.all([fetchIngredients(), fetchRecentTacos()]);
      setIngredients(ing);
      setTacos(recent);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to load tacos');
    } finally {
      setLoading(false);
    }
  }

  function toggleIngredient(id: string) {
    setDraft(current => {
      const next = new Set(current.ingredientIds);
      if (next.has(id)) {
        next.delete(id);
      } else {
        next.add(id);
      }
      return { ...current, ingredientIds: next };
    });
  }

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    if (!draft.name.trim() || draft.ingredientIds.size === 0) {
      setError('Provide a name and at least one ingredient');
      return;
    }
    try {
      setError(null);
      const payload: Taco = {
        name: draft.name.trim(),
        ingredients: ingredients.filter(ingredient => draft.ingredientIds.has(ingredient.id))
      };
      const taco = await createTaco(payload);
      setDraft({ name: '', ingredientIds: new Set() });
      setTacos(current => [taco, ...current].slice(0, 12));
      onTacoCreated?.(taco);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to create taco');
    }
  }

  return (
    <section>
      <header>
        <h2>Design Taco</h2>
        <button type="button" onClick={load} disabled={loading}>
          Refresh
        </button>
      </header>
      {error ? <div className="error">{error}</div> : null}
      <form className="panel" onSubmit={handleSubmit}>
        <label>
          Name
          <input
            value={draft.name}
            onChange={event => setDraft({ ...draft, name: event.target.value })}
            placeholder="Carnivore"
          />
        </label>
        <fieldset>
          <legend>Ingredients</legend>
          {Object.entries(grouped).map(([type, list]) => (
            <div key={type} className="ingredient-group">
              <strong>{type}</strong>
              {list.length === 0 ? <em>—</em> : null}
              {list.map(ingredient => (
                <label key={ingredient.id} className="checkbox">
                  <input
                    type="checkbox"
                    checked={draft.ingredientIds.has(ingredient.id)}
                    onChange={() => toggleIngredient(ingredient.id)}
                  />
                  {ingredient.name}
                </label>
              ))}
            </div>
          ))}
        </fieldset>
        <button type="submit">Save Taco</button>
      </form>
      {loading ? <p>Loading tacos...</p> : null}
      <ul className="list">
        {tacos.map(taco => (
          <li key={taco.id ?? `${taco.name}-${taco.createdAt}`}>
            <span>{taco.name}</span>
            <small>{taco.ingredients.map(ingredient => ingredient.name).join(', ')}</small>
          </li>
        ))}
      </ul>
    </section>
  );
}
