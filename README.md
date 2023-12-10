# Akademia Tarnowska

# Kurs
### Testowanie i Jakość Oprogramowania / Projekt

# Autor
### Piotr Duda

# Temat projektu
### Testowanie koszyka


# Opis projektu
### Aplikacja do obsługi restauracji została zaprojektowana, aby dostarczyć użytkownikom wygodne i intuicyjne doświadczenia podczas składania zamówień. Klienci mogą swobodnie przeglądać menu, dodawać dania do koszyka, a ponowne kliknięcie tego samego dania zwiększa jego ilość. W koszyku istnieje możliwość precyzyjnego zarządzania ilościami, poprzez przyciski "+" i "-". Klient ma także opcję usunięcia dania z koszyka.
### Po wybraniu dań, użytkownik może dokonać wyboru preferowanej metody płatności, mając do dyspozycji gotówkę lub kartę. Dodatkowo, klient ma możliwość wybrania numeru stolika poprzez skanowanie kodu QR. W przypadku błędnego kodu, aplikacja informuje o tym użytkownika i udostępnia opcję powtórzenia skanowania. Jeśli kod jest poprawny, klientowi prezentowana jest zawartość stolika, a on może zaakceptować lub ponownie zeskanować.
### Po zakończeniu procesu wyboru dań i stolika, klient klika przycisk "Zamów". W następnym oknie, użytkownik ma możliwość potwierdzenia lub anulowania złożonego zamówienia. Aplikacja do obsługi restauracji zapewnia kompleksowe narzędzie, umożliwiające łatwe, szybkie i przyjemne korzystanie z usług restauracji, z pełnym dostosowaniem do indywidualnych preferencji klienta.

### Prezentacja aplikacji 
[![Alt text](https://img.youtube.com/vi/gV_oXgNHykQ/hqdefault.jpg)](https://www.youtube.com/watch?v=gV_oXgNHykQ)

# Uruchomienie projektu
### mvn spring-boot:run

## Uruchomienie aplikacji mobilnej
### npx react-native run-android

# Uruchomienie testów jednostkowych i integracyjnych
### mvn test

# Dokumentacja Api

### Dokładny opis api został wygenerowany w swagger.
### Po uruchomieniu aplikacji należy wejść na adres http://localhost:8080/swagger-ui/index.html#/
 
- Adres usługi: /order/addToOrder/?dishId={id}
- Typ: POST
- Przyjmuje: 
```json
{
  "tableNoId": "string",
  "cost": 0.0,
  "order": [
    {
      "dish": {
        "id": "string",
        "dishType": "string",
        "name": "string",
        "price": 0.0,
        "ingredients": ["string"]
      },
      "quantity": 0,
      "cost": 0.0
    }
  ],
  "paymentMethod": "string"
}
```
- Zwraca
```json
{
  "tableNoId": "string",
  "cost": 0.0,
  "order": [
    {
      "dish": {
        "id": "string",
        "dishType": "string",
        "name": "string",
        "price": 0.0,
        "ingredients": ["string"]
      },
      "quantity": 0,
      "cost": 0.0
    }
  ],
  "paymentMethod": "string"
}  
```

- Adres usługi: /order/removeOrder/?dishId={id}
- Typ: POST
- Przyjmuje:
```json
{
  "tableNoId": "string",
  "cost": 0.0,
  "order": [
    {
      "dish": {
        "id": "string",
        "dishType": "string",
        "name": "string",
        "price": 0.0,
        "ingredients": ["string"]
      },
      "quantity": 0,
      "cost": 0.0
    }
  ],
  "paymentMethod": "string"
}
```
- Zwraca
```json
{
  "tableNoId": "string",
  "cost": 0.0,
  "order": [
    {
      "dish": {
        "id": "string",
        "dishType": "string",
        "name": "string",
        "price": 0.0,
        "ingredients": ["string"]
      },
      "quantity": 0,
      "cost": 0.0
    }
  ],
  "paymentMethod": "string"
}  
```

- Adres usługi: /order/acceptOrder
- Typ: POST
- Przyjmuje:
```json
{
  "tableNoId": "string",
  "cost": 0.0,
  "order": [
    {
      "dish": {
        "id": "string",
        "dishType": "string",
        "name": "string",
        "price": 0.0,
        "ingredients": ["string"]
      },
      "quantity": 0,
      "cost": 0.0
    }
  ],
  "paymentMethod": "string"
}
```
- Zwraca
```json
{
  "id": "string",
  "tableNoId": "string",
  "cost": 0.0,
  "order": [
    {
      "dish": {
        "id": "string",
        "dishType": "string",
        "name": "string",
        "price": 0.0,
        "ingredients": ["string"]
      },
      "quantity": 0,
      "cost": 0.0
    }
  ],
  "paymentMethod": "string",
  "date": "string"
}  
```

- Adres usługi: /dishes/getAllDishes
- Typ: GET
- Zwraca
```json
[
  {
    "id": "string",
    "dishType": "string",
    "name": "string",
    "price": 0.0,
    "ingredients": ["string"]
  },
  {
    "id": "string",
    "dishType": "string",
    "name": "string",
    "price": 0.0,
    "ingredients": ["string"]
  }
]  
```

- Adres usługi: /qrCode/getValue/{id}
- Typ: GET
- Zwraca
```json 
{
  "id": "string",
  "qrCode": "string"
}
```

# Scenariusze testowe dla testera manualnego

| Test<br/>Case Id | Scenariusz                          | Kroki Testowe                                                                                                                                              | Oczekiwany Wynik                                                                                         |
|------------------|-------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------|
| TC_01            | Dodawanie dania do koszyka z menu   | 1. Przejdź do menu restauracji.  <br> 2. Wybierz konkretne danie.  <br> 3. Wybież znak "+".                                                                 | Danie zostaje dodane do koszyka lub ilość w koszyku zwiększa się o 1.                                    |
| TC_02            | Zwiększanie ilości dania w koszyku  | 1. Przejdź do koszyka.  <br> 2. Znajdź danie, które już jest w koszyku.  <br> 3. Kliknij przycisk "+" przy danym daniu.                                    | Ilość dania w koszyku zostaje zwiększona o 1.                                                            |
| TC_03            | Zmniejszanie ilości dania w koszyku | 1. Przejdź do koszyka.  <br> 2. Znajdź danie, które już jest w koszyku.  <br> 3. Kliknij przycisk "-" przy danym daniu.                                    | Ilość dania w koszyku zostaje zmniejszona o 1.                                                           |
| TC_04            | Usuwanie dania z koszyka            | 1. Przejdź do koszyka.  <br> 2. Znajdź danie, które już jest w koszyku.  <br> 3. Zmniejszaj ilość dania do usunięcia.                                      | Dane zostaje usunięte z koszyka.                                                                         |
| TC_05            | Wybór metody płatności - gotówka    | 1. Przejdź do koszyka.  <br> 2. Kliknij przycisk "Gotówka".                                                                                                | Klient wybrał metodę płatności gotówką                                                                   |
| TC_06            | Wybór metody płatności - karta      | 1. Przejdź do koszyka.  <br> 2. Kliknij przycisk "Karta".                                                                                                  | Klient wybrał metodę płatności kartą                                                                     |
| TC_07            | Skanowanie kodu QR - błędny kod     | 1. Przejdź do skanowania.  <br> 2. Skanuj niepoprawny kod QR. <br> 3. Wybierz opcję "Powtórz skanowanie"                                                   | Wyświetla się komunikat informujący o błędnym kodzie, a klientowi proponowane jest ponowne zeskanowanie. |
| TC_08            | Skanowanie kodu QR - poprawny kod   | 1. Przejdź do skanowania.  <br> 2. Skanuj poprawny kod QR. <br> 3. Wybierz opcję "Powtórz skanowanie"                                                      | Zawartość kodu zostaje wyświetlona, klient skanuje ponownie.                                             |
| TC_09            | Akceptacja wybranego stolika        | 1. Przejdź do skanowania.  <br> 2. Skanuj poprawny kod QR.  <br> 3. Wybierz opcję "Zaakceptuj"                                                             | Zawartość kodu zostaje wyświetlona a klient potwierdzona kod, zostaje przeniesiony do koszyka.           |
| TC_10            | Wysłanie zamówienia                 | 1. Wybież dania.  <br> 2. Dodaj numer stolika. <br> 3. Przejdź do Koszyka <br> 4. Wybierz metodę płatności <br> 5. Wybierz zamów <br> 6. Wybierz potwierdź | Klient złożył zamówienie                                                                                 |

# Technologie użyte w projekcie

- Java 17
- Maven 3.9.5
- JavaScript
- Spring Boot
- React Native
