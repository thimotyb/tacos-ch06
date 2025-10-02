# Taco Cloud React UI

Lightweight React interface for the upgraded Taco Cloud API. It provides:

- Ingredient management (list + create)
- Taco designer with ingredient selection
- Order board that assembles orders from recent tacos

## Prerequisites

- Node.js 18+
- Spring Boot backend running locally on port 8080

## Install & Run

```bash
npm install
npm run dev
```

The development server listens on `http://localhost:5173` and proxies `/api`
requests to the Spring backend. The proxy target can be changed via
`VITE_API_BASE_URL` (defaults to `/api`).

## Build

```bash
npm run build
```

Production assets are emitted to `dist/` and can be served by any static server.
