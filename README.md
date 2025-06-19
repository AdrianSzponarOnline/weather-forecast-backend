# weather-forecast-backend
Weather forecast and solar energy estimation application

# Weather Forecast App

## Jak uruchomić

1. Zbuduj projekt:
   ```bash
   ./mvnw clean package
   ```
2. Uruchom aplikację:
   ```bash
   ./mvnw spring-boot:run
   ```

## Testowanie

Aby uruchomić testy:
```bash
./mvnw test
```

## Przykładowe zapytania do API

- Prognoza:
  ```
  GET /api/forecast?lat=50.0&lon=20.0
  ```
- Podsumowanie tygodnia:
  ```
  GET /api/forecast/summary?lat=50.0&lon=20.0
  ```
