# Akademia Tarnowska

# Kurs
### Testowanie i Jakość Oprogramowania / Projekt

# Autor
### Piotr Duda

# Opis projektu
### Aplikacja do obsługi restauracji została zaprojektowana, aby dostarczyć użytkownikom wygodne i intuicyjne doświadczenia podczas składania zamówień. Klienci mogą swobodnie przeglądać menu, dodawać dania do koszyka, a ponowne kliknięcie tego samego dania zwiększa jego ilość. W koszyku istnieje możliwość precyzyjnego zarządzania ilościami, poprzez przyciski "+" i "-". Klient ma także opcję usunięcia dania z koszyka.
### Po wybraniu dań, użytkownik może dokonać wyboru preferowanej metody płatności, mając do dyspozycji gotówkę lub kartę. Dodatkowo, klient ma możliwość wybrania numeru stolika poprzez skanowanie kodu QR. W przypadku błędnego kodu, aplikacja informuje o tym użytkownika i udostępnia opcję powtórzenia skanowania. Jeśli kod jest poprawny, klientowi prezentowana jest zawartość stolika, a on może zaakceptować lub ponownie zeskanować.
### Po zakończeniu procesu wyboru dań i stolika, klient klika przycisk "Zamów". W następnym oknie, użytkownik ma możliwość potwierdzenia lub anulowania złożonego zamówienia. Aplikacja do obsługi restauracji zapewnia kompleksowe narzędzie, umożliwiające łatwe, szybkie i przyjemne korzystanie z usług restauracji, z pełnym dostosowaniem do indywidualnych preferencji klienta.

# Uruchomienie aplikacji
### mvn spring-boot:run

# Uruchomienie testów jednostkowych i integracyjnych
### mvn test

# Scenariusze testowe dla testera manualnego

| Test<br/>Case Id | Scenariusz                                     | Kroki Testowe                                                                                                     | Oczekiwany Wynik                                                                                                           |
|------------------|------------------------------------------------|--------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------|
| TC_01            | Dodawanie dania do koszyka z menu              | 1. Zaloguj się do konta klienta.  <br> 2. Przejdź do menu restauracji.  <br> 3. Wybierz konkretne danie.  <br> 4. Dodaj to danie do koszyka.  | Dania zostaje dodane do koszyka, a ilość w koszyku zwiększa się o 1.                                                           |
| TC_02            | Zwiększanie ilości dania w koszyku             | 1. Zaloguj się do konta klienta.  <br> 2. Przejdź do koszyka.  <br> 3. Znajdź danie, które już jest w koszyku.  <br> 4. Kliknij przycisk "+" przy danym daniu.  | Ilość dania w koszyku zostaje zwiększona o 1.                                                                               |
| TC_03            | Zmniejszanie ilości dania w koszyku            | 1. Zaloguj się do konta klienta.  <br> 2. Przejdź do koszyka.  <br> 3. Znajdź danie, które już jest w koszyku.  <br> 4. Kliknij przycisk "-" przy danym daniu.  | Ilość dania w koszyku zostaje zmniejszona o 1.                                                                             |
| TC_04            | Usuwanie dania z koszyka                       | 1. Zaloguj się do konta klienta.  <br> 2. Przejdź do koszyka.  <br> 3. Znajdź danie, które już jest w koszyku.  <br> 4. Kliknij przycisk usuń dane.  | Dane zostaje usunięte z koszyka.                                                                                           |
| TC_05            | Wybór metody płatności - gotówka               | 1. Zaloguj się do konta klienta.  <br> 2. Przejdź do koszyka.  <br> 3. Kliknij przycisk "Zapłać".  <br> 4. Wybierz metodę płatności "Gotówka".  | Klient wybrał metodę płatności gotówką, koszyk zostaje zatwierdzony, a zamówienie jest przygotowane do dostawy lub odbioru. |
| TC_06            | Wybór metody płatności - karta                 | 1. Zaloguj się do konta klienta.  <br> 2. Przejdź do koszyka.  <br> 3. Kliknij przycisk "Zapłać".  <br> 4. Wybierz metodę płatności "Karta".  | Klient wybrał metodę płatności kartą, koszyk zostaje zatwierdzony, a zamówienie jest przygotowane do dostawy lub odbioru.   |
| TC_07            | Skanowanie kodu QR - błędny kod                | 1. Przejdź do opcji wyboru stolika.  <br> 2. Skanuj niepoprawny kod QR.  | Wyświetla się komunikat informujący o błędnym kodzie, a klientowi proponowane jest ponowne zeskanowanie.                 |
| TC_08            | Skanowanie kodu QR - poprawny kod              | 1. Przejdź do opcji wyboru stolika.  <br> 2. Skanuj poprawny kod QR.  | Zawartość kodu zostaje wyświetlona, klient może zaakceptować lub ponownie zeskanować.                                    |
| TC_09            | Akceptacja wybranego stolika                   | 1. Przejdź do opcji wyboru stolika.  <br> 2. Wybierz stolik i zaakceptuj wybór.  | Stolik zostaje wybrany i potwierdzony, a klient jest przeniesiony do menu lub koszyka.                                     |
| TC_10            | Powrót do skanowania                           | 1. Przejdź do opcji wyboru stolika.  <br> 2. Skanuj poprawny kod, a następnie kliknij przycisk "Powtórz skanowanie".  | Klient zostaje przeniesiony do ekranu skanowania, gotowy do zeskanowania kolejnego kodu QR.                              |
