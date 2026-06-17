# 🌾 EcoFarm: Równowaga Plonów (Farm Simulator)

Projekt to obiektowa symulacja ekosystemu rolniczego oparta na siatce o strukturze torusa (krawędzie planszy zawijają się). Symulacja analizuje dynamiczne interakcje pomiędzy rolnikami, pszczołami, uprawami oraz szkodnikami, które walczą o przetrwanie i zasoby.

Aplikacja została zbudowana przy użyciu języka **Java 17+** oraz biblioteki **JavaFX** w architekturze inspirowanej wzorcem MVC (Model-View-Controller).

---

## 🛠️ Architektura Systemu i Agenci

Ekosystem składa się z niezależnych agentów działających w turach (Ticks):

* **Farmer (Rolnik):** Odpowiada za obsiewanie pustych pól oraz zbieranie dojrzałych plonów. Dodatkowo aktywnie poluje na szkodniki w swoim otoczeniu i aplikuje chemiczne opryski (pestycydy). Posiada ograniczoną energię – po jej wyczerpaniu musi odpocząć.
* **Bee (Pszczoła):** Porusza się losowo omijając chmury pestycydów. Jej głównym zadaniem jest zapylanie roślin, co znacząco przyspiesza ich czas wzrostu.
* **Pest (Szkodnik):** Skanuje otoczenie w poszukiwaniu żywności. Zjada uprawy rolnika, konwertując je na energię. Po osiągnięciu odpowiedniego poziomu energii posiada zdolność do reprodukcji. Umiera z głodu, w wyniku potrącenia przez Farmera lub po wejściu w chmurę pestycydów.
* **Crop (Uprawa):** Dynamiczny element planszy. Przechodzi przez fazy od zasiewu (`empty`), przez wzrost (`growing`), aż do pełnej dojrzałości (`mature`).

---

## 📊 Wykresy i Statystyki Real-Time

Aplikacja oferuje zaawansowany panel boczny interfejsu graficznego, który w czasie rzeczywistym monitoruje:
* Wskaźnik zebranych, posadzonych oraz zapylonych roślin.
* Historię populacji szkodników oraz bilans ich narodzin i zgonów.
* Dynamiczne wykresy liniowe (`LineChart`) obrazujące stosunek populacji szkodników do ilości żywych upraw na przestrzeni czasu.

---

## ⚙️ Zaawansowana Konfiguracja (Klasa Settings)

Wszystkie kluczowe zmienne środowiskowe, fizyka świata oraz zachowania agentów zostały wyciągnięte do jednej klasy narzędziowej `com.ditur.Settings`. Modyfikując te wartości, możesz całkowicie zmienić balans ekosystemu:

| Parametr | Wartość domyślna | Opis |
| :--- | :---: | :--- |
| `BOARD_WIDTH` / `BOARD_HEIGHT` | 20 / 22 | Wymiary siatki świata symulacji. |
| `START_FORESTATION` | 25% | Początkowe pokrycie planszy losowymi uprawami. |
| `BOOST_POLLINATE` | 3 | Liczba tur, o którą pszczoła skraca czas wzrostu rośliny przy zapyleniu. |
| `PEST_START_ENERGY` | 50 | Początkowa energia nowo zrodzonego szkodnika. |
| `ENERGY_TO_MULTIPLICATION` | 50 | Próg energii, powyżej którego szkodnik przystępuje do rozmnażania. |
| `FARMER_VIEW_RADIUS` | 10 | Promień wzroku rolnika podczas poszukiwania priorytetowych pól. |
| `FARMER_GO_OFF_TRACK` | 0.3 (30%) | Szansa, że farmer zboczy z optymalnej ścieżki (losowy krok). |
| `CARROT` / `POTATO` / `WHEAT` | 12 / 18 / 25 | Bazowy czas wzrostu (w taktach) dla poszczególnych gatunków roślin. |

---

## 🚀 Szybki Start (Quick Start)

Wybierz najwygodniejszy dla siebie sposób uruchomienia symulacji:

### Metoda 1: Uruchomienie jednym kliknięciem (Dla każdego)
Najszybsza opcja, która nie wymaga instalowania narzędzi programistycznych ani dotykania kodu.
1. Wejdź do katalogu `/out` w plikach projektu.
2. Znajdź plik **`farm-simulator.jar`** i kliknij na niego dwukrotnie lewym przyciskiem myszy.
3. *Uwaga (macOS):* Jeśli system zablokuje otwarcie, kliknij na plik z wciśniętym klawiszem `Control`, wybierz **Otwórz**, a następnie **Otwórz mimo to**.

> 💡 **Wymaganie:** Do uruchomienia pliku `.jar` wymagane jest posiadanie zainstalowanej na komputerze Javy (środowiska JRE/JDK w wersji 17 lub nowszej).

### Metoda 2: Uruchomienie z terminala (Dla deweloperów)
Jeśli masz zainstalowane środowisko JDK oraz narzędzie Maven i chcesz uruchomić projekt bezpośrednio z kodu źródłowego:

1. Sklonuj repozytorium i przejdź do folderu projektu:
```bash
git clone https://github.com/Ditur0/farm-simulation.git
```
2. Uruchom prgram przez polecenie:
```bash
mvn compile exec:java -Dexec.mainClass="com.ditur.Main"
```
### 📐 Diagram Klas
Poniższy diagram przedstawia strukturę obiektową projektu:

![Diagram Klas EcoFarm](src/main/resources/diagramy/diagram-klas.svg)

---

### 🌐 Diagram Instancji
Diagram obrazujący przykładowy stan świata i relacje przestrzenne:

![Diagram Instancji EcoFarm](src/main/resources/diagramy/diagram-obiektow.svg)

---

### 🔄 Diagram Stanów
Wizualizacja cyklu życia symulacji:

![Diagram Stanów Uprawy EcoFarm](src/main/resources/diagramy/diagram-stanow.svg)