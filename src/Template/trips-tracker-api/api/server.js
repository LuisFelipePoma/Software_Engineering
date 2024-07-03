const express = require('express');
const path = require('path');
const cors = require('cors');
const bodyParser = require('body-parser');
const jsonServer = require('json-server');
const swaggerUi = require('swagger-ui-express');
const yaml = require('yaml');
const fs = require('fs');

const app = express();
const jsonServerRouter = jsonServer.router(path.join(__dirname, 'db.json'));
const middlewares = jsonServer.defaults();

app.use(cors());
app.use(bodyParser.json());
app.use(middlewares);

// Cargar archivo Swagger YAML
const swaggerFile = fs.readFileSync(path.join(__dirname, 'swagger.yaml'), 'utf8');
const swaggerDocument = yaml.parse(swaggerFile);

// Ruta para la documentación de Swagger
app.use('/api-docs', swaggerUi.serve, swaggerUi.setup(swaggerDocument));

// Endpoints personalizados

// Obtener todos los viajes
app.get('/api/v1/trips', (req, res) => {
  const db = jsonServerRouter.db;
  const trips = db.get('trips').value();
  res.json(trips);
});

// Registrar un nuevo viaje
app.post('/api/v1/trips', (req, res) => {
  const db = jsonServerRouter.db;
  const { date, time, origin, destination, route } = req.body;

  if (!date || !time || !origin || !destination || !route) {
    return res.status(400).json({ error: 'Todos los campos son requeridos: date, time, origin, destination, route' });
  }

  const newTrip = {
    id: db.get('trips').value().length + 1,
    date,
    time,
    origin,
    destination,
    route
  };

  db.get('trips').push(newTrip).write();

  res.json(newTrip);
});

// Filtrar viajes por fecha
app.get('/api/v1/trips/filter-by-date', (req, res) => {
  const db = jsonServerRouter.db;
  const { date } = req.query;
  const filteredTrips = db.get('trips').filter({ date }).value();
  res.json(filteredTrips);
});

// Filtrar viajes por ruta
app.get('/api/v1/trips/filter-by-route', (req, res) => {
  const db = jsonServerRouter.db;
  const { route } = req.query;
  const filteredTrips = db.get('trips').filter({ route }).value();
  res.json(filteredTrips);
});

// Estadísticas por uso de rutas
app.get('/api/v1/trips/stats-by-route', (req, res) => {
  const db = jsonServerRouter.db;
  const trips = db.get('trips').value();
  const statsByRoute = [];

  const statsMap = new Map();

  trips.forEach(trip => {
    const { route } = trip;

    if (!statsMap.has(route)) {
      statsMap.set(route, {
        route: route,
        tripCount: 0
      });
    }
    const routeStats = statsMap.get(route);
    routeStats.tripCount++;
  });

  statsMap.forEach(value => {
    statsByRoute.push(value);
  });

  res.json(statsByRoute);
});

app.use('/api/v1', jsonServerRouter);

const port = process.env.PORT || 3000;
app.listen(port, () => {
  console.log(`Servidor Express con JSON Server y Swagger está corriendo en el puerto ${port}`);
});
