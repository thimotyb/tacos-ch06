import { useState } from 'react';
import { IngredientPanel } from './components/IngredientPanel';
import { TacoDesigner } from './components/TacoDesigner';
import { OrderBoard } from './components/OrderBoard';
import { Taco } from './api';

export function App() {
  const [lastTaco, setLastTaco] = useState<Taco | null>(null);

  return (
    <div className="app">
      <header>
        <h1>Taco Cloud React Console</h1>
        <p>Design tacos, manage ingredients, and submit orders via the Spring Boot 3 API.</p>
        {lastTaco ? <p className="info">Last taco created: {lastTaco.name}</p> : null}
      </header>
      <main className="grid-layout">
        <IngredientPanel />
        <TacoDesigner onTacoCreated={setLastTaco} />
        <OrderBoard />
      </main>
    </div>
  );
}
