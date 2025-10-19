# Weather Forecast App

Aplikacja do prognozowania pogody i szacowania energii słonecznej z paneli fotowoltaicznych.

## Funkcjonalności

- **7-dniowa prognoza pogody** z danymi z Open-Meteo API
- **Szacowanie energii słonecznej** z paneli PV (2.5kW, sprawność 20%)
- **Podsumowanie tygodnia** z temperaturami, ciśnieniem i nasłonecznieniem
- **Walidacja współrzędnych geograficznych** (lat: -90 do 90, lon: -180 do 180)
- **Cache'owanie** odpowiedzi API (Caffeine)
- **Dokumentacja API** (Swagger/OpenAPI)
- **Obsługa błędów** z przyjaznymi komunikatami

## Technologie

- **Java 17**
- **Spring Boot 3.5.0**
- **Maven**
- **Open-Meteo API** (zewnętrzne API pogodowe)
- **Caffeine Cache**
- **Swagger/OpenAPI 3**
- **Docker**

## Wymagania

- Java 17+
- Maven 3.6+
- Docker (opcjonalnie)

## Jak uruchomić

### Lokalnie

1. **Sklonuj repozytorium:**
   ```bash
   git clone <repository-url>
   cd weather-forecast-app
   ```

2. **Zbuduj projekt:**
   ```bash
   ./mvnw clean package
   ```

3. **Uruchom aplikację:**
   ```bash
   ./mvnw spring-boot:run
   ```

4. **Aplikacja będzie dostępna pod adresem:**
   - API: `http://localhost:8080/api`
   - Swagger UI: `http://localhost:8080/swagger-ui/index.html`
   - Health check: `http://localhost:8080/actuator/health`

### Docker

1. **Zbuduj obraz:**
   ```bash
   docker build -t weather-forecast-app .
   ```

2. **Uruchom kontener:**
   ```bash
   docker run -p 8080:8080 weather-forecast-app
   ```

## Testowanie

### Uruchomienie testów
```bash
./mvnw test
```

### Testy integracyjne
Aplikacja zawiera testy integracyjne dla kontrolerów (`@WebMvcTest`) oraz testy jednostkowe dla serwisów.

## API Endpoints

### 1. Prognoza 7-dniowa
```http
GET /api/forecast?lat={latitude}&lon={longitude}
```

**Parametry:**
- `lat` (wymagane): Szerokość geograficzna (-90 do 90)
- `lon` (wymagane): Długość geograficzna (-180 do 180)

**Przykład:**
```bash
curl "http://localhost:8080/api/forecast?lat=50.0&lon=20.0"
```

**Odpowiedź:**
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

### 2. Podsumowanie tygodnia
```http
GET /api/forecast/summary?lat={latitude}&lon={longitude}
```

**Przykład:**
```bash
curl "http://localhost:8080/api/forecast/summary?lat=50.0&lon=20.0"
```

**Odpowiedź:**
```json
{
  "minTemperature": -0.0,
  "maxTemperature": 19.0,
  "avgPressure": 980.56,
  "avgSunshineHours": 5.9,
  "weekComment": "z opadami"
}
```

## Konfiguracja

Główne ustawienia w `src/main/resources/application.properties`:

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

API returns user-friendly error messages in English:

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

## Dane pogodowe

Aplikacja pobiera dane z [Open-Meteo API](https://open-meteo.com/):
- **Kod pogody** (0-99) - określa warunki atmosferyczne
- **Temperatura** - min/max w °C
- **Nasłonecznienie** - w sekundach (konwertowane na godziny)
- **Ciśnienie** - średnie ciśnienie atmosferyczne w hPa

## Energia słoneczna

Obliczenia energii z paneli PV:
- **Moc paneli**: 2.5 kW
- **Sprawność**: 20%
- **Wzór**: `Energia = Moc × Godziny_słońca × Sprawność`

## Deployment

### Render.com
1. Połącz repozytorium z Render
2. Wybierz "Web Service"
3. Ustaw Build Command: `mvn clean package`
4. Ustaw Start Command: `java -jar target/*.jar`
5. Deploy!

## Przykłady użycia

### JavaScript (Frontend)
```javascript
// Pobierz prognozę dla Warszawy
fetch('http://localhost:8080/api/forecast?lat=52.2297&lon=21.0122')
  .then(response => response.json())
  .then(data => console.log(data));

// Pobierz podsumowanie dla Krakowa
fetch('http://localhost:8080/api/forecast/summary?lat=50.0647&lon=19.9450')
  .then(response => response.json())
  .then(data => console.log(data));
```

### cURL
```bash
# Prognoza dla Gdańska
curl "http://localhost:8080/api/forecast?lat=54.3520&lon=18.6466"

# Podsumowanie dla Wrocławia
curl "http://localhost:8080/api/forecast/summary?lat=51.1079&lon=17.0385"
```

## Wkład w projekt

1. Fork repozytorium
2. Stwórz branch (`git checkout -b feature/nowa-funkcjonalnosc`)
3. Commit zmiany (`git commit -m 'Dodaj nową funkcjonalność'`)
4. Push do branch (`git push origin feature/nowa-funkcjonalnosc`)
5. Stwórz Pull Request

