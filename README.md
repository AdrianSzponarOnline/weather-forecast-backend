# Weather Forecast App

A weather forecasting application with solar energy estimation from photovoltaic panels.

## Features

- **7-day weather forecast** with data from Open-Meteo API
- **Solar energy estimation** from PV panels (2.5kW, 20% efficiency)
- **Weekly summary** with temperatures, pressure and sunshine hours
- **Geographic coordinates validation** (lat: -90 to 90, lon: -180 to 180)
- **API response caching** (Caffeine)
- **API documentation** (Swagger/OpenAPI)
- **Error handling** with user-friendly messages

## Technologies

- **Java 17**
- **Spring Boot 3.5.0**
- **Maven**
- **Open-Meteo API** (external weather API)
- **Caffeine Cache**
- **Swagger/OpenAPI 3**
- **Docker**

## Requirements

- Java 17+
- Maven 3.6+
- Docker (optional)

## How to run

### Locally

1. **Clone the repository:**
   ```bash
   git clone <repository-url>
   cd weather-forecast-app
   ```

2. **Build the project:**
   ```bash
   ./mvnw clean package
   ```

3. **Run the application:**
   ```bash
   ./mvnw spring-boot:run
   ```

4. **Application will be available at:**
   - API: `http://localhost:8080/api`
   - Swagger UI: `http://localhost:8080/swagger-ui/index.html`
   - Health check: `http://localhost:8080/actuator/health`

### Docker

1. **Build the image:**
   ```bash
   docker build -t weather-forecast-app .
   ```

2. **Run the container:**
   ```bash
   docker run -p 8080:8080 weather-forecast-app
   ```

## Testing

### Running tests
```bash
./mvnw test
```

### Integration tests
The application contains integration tests for controllers (`@WebMvcTest`) and unit tests for services.

## API Endpoints

### 1. 7-day forecast
```http
GET /api/forecast?lat={latitude}&lon={longitude}
```

**Parameters:**
- `lat` (required): Latitude (-90 to 90)
- `lon` (required): Longitude (-180 to 180)

**Example:**
```bash
curl "http://localhost:8080/api/forecast?lat=50.0&lon=20.0"
```

**Response:**
```json
[
  {
    "date": "2025-10-19",
    "weatherCode": 51,
    "tempMin": 3.4,
    "tempMax": 9.2,
    "energyKwh": 0.0
  },
  {
    "date": "2025-10-20",
    "weatherCode": 0,
    "tempMin": -0.0,
    "tempMax": 11.6,
    "energyKwh": 4.26
  }
]
```

### 2. Weekly summary
```http
GET /api/forecast/summary?lat={latitude}&lon={longitude}
```

**Example:**
```bash
curl "http://localhost:8080/api/forecast/summary?lat=50.0&lon=20.0"
```

**Response:**
```json
{
  "minTemperature": -0.0,
  "maxTemperature": 19.0,
  "avgPressure": 980.56,
  "avgSunshineHours": 5.9,
  "weekComment": "with precipitation"
}
```

## Configuration

Main settings in `src/main/resources/application.properties`:

```properties
# Open-Meteo API Configuration
weather.api.base-url=https://api.open-meteo.com/v1/forecast
weather.api.daily-params=weathercode,temperature_2m_max,temperature_2m_min,sunshine_duration,surface_pressure_mean
weather.api.forecast-days=7
weather.api.timezone=auto
weather.api.connect-timeout=10
weather.api.read-timeout=10
```

## Error Handling

API returns error messages in English:

- **400 Bad Request**: Invalid parameters (missing lat/lon, out of range)
- **502 Bad Gateway**: External weather API issues
- **500 Internal Server Error**: Unexpected server errors

**Error example:**
```json
{
  "timestamp": "2025-10-19T11:25:15.674Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid input parameters",
  "details": "Latitude must be >= -90"
}
```

## Weather Data

The application fetches data from [Open-Meteo API](https://open-meteo.com/):
- **Weather code** (0-99) - determines atmospheric conditions
- **Temperature** - min/max in °C
- **Sunshine duration** - in seconds (converted to hours)
- **Pressure** - average atmospheric pressure in hPa

## Solar Energy

PV panel energy calculations:
- **Panel power**: 2.5 kW
- **Efficiency**: 20%
- **Formula**: `Energy = Power × Sunshine_hours × Efficiency`

## Deployment

### Render.com
1. Connect repository to Render
2. Select "Web Service"
3. Set Build Command: `mvn clean package`
4. Set Start Command: `java -jar target/*.jar`
5. Deploy!

## Usage Examples

### JavaScript (Frontend)
```javascript
// Get forecast for Warsaw
fetch('http://localhost:8080/api/forecast?lat=52.2297&lon=21.0122')
  .then(response => response.json())
  .then(data => console.log(data));

// Get summary for Krakow
fetch('http://localhost:8080/api/forecast/summary?lat=50.0647&lon=19.9450')
  .then(response => response.json())
  .then(data => console.log(data));
```

### cURL
```bash
# Forecast for Gdansk
curl "http://localhost:8080/api/forecast?lat=54.3520&lon=18.6466"

# Summary for Wroclaw
curl "http://localhost:8080/api/forecast/summary?lat=51.1079&lon=17.0385"
```
