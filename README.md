Aplikacja została zbudowana przy użyciu:

- **Android Studio Koala** | 2024.1.1

## Pobieranie Android Studio

Aby pobrać i zainstalować Android Studio, skorzystaj z poniższego linku:\
[Android Studio - Instrukcja pobierania i instalacji](https://developer.android.com/studio/install)

## Otwieranie projektu w Android Studio

1. Po zainstalowaniu Android Studio otwórz program.
2. Wybierz **File -> Open** i wskaż folder zawierający projekt.
3. Po otwarciu projektu poczekaj, aż aplikacja zostanie zbudowana i zsynchronizowana z zależnościami.
4. Gdy proces synchronizacji zakończy się pomyślnie, aplikację można uruchomić w symulatorze.\
   Kliknij przycisk **Run** oznaczony zielonym trójkątem w prawym górnym rogu interfejsu.

---

## Budowanie projektu za pomocą wiersza poleceń (Command Line)

### Wymagania wstępne

1. **Java Development Kit (JDK)**:

   - Upewnij się, że JDK jest zainstalowany i skonfigurowany.
   - Upewnij się, że wersja JDK jest 11 lub wyżej.
   - Dodaj zmienną środowiskową `JAVA_HOME` wskazującą na katalog instalacji JDK.

2. **Android SDK**:

   - Zainstaluj Android Studio lub Command Line Tools.
   - Dodaj zmienne środowiskowe:
     - `ANDROID_HOME`: Ścieżka do katalogu Android SDK.
     - Dodaj `$ANDROID_HOME/tools` i `$ANDROID_HOME/platform-tools` do zmiennej `PATH`.

3. **Gradle Wrapper**:

   - Plik `gradlew` powinien znajdować się w katalogu głównym projektu.

### Kroki budowania projektu

1. **Przejdź do katalogu projektu**:
   Otwórz terminal i przejdź do katalogu głównego projektu, gdzie znajduje się plik `gradlew`.

   ```bash
   cd $PROJECT_PATH

2. **Wyczyść projekt**:
   Wykonaj polecenie, aby usunąć stare artefakty budowy:

   ```bash
   ./gradlew clean

3. **Zbuduj APK**:
   Aby zbudować wersję debugową aplikacji:

   ```bash
   ./gradlew assembleDebug

## Zmiana adresu serwera

Jeśli konieczne jest zmienienie adresu serwera, należy to zrobić przed uruchomieniem aplikacji:

1. Otwórz następujące pliki w projekcie:
   - `$PROJECT_PATH\app\src\main\res\values\strings.xml`
   - `$PROJECT_PATH\app\src\main\res\xml\network_security_config.xml`

2. Zaktualizuj odpowiednie wartości adresu serwera w obu plikach.

3. Zapisz zmiany i zbuduj aplikację ponownie (zgodnie z instrukcją powyżej).