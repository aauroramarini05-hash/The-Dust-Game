# Auryx Soda - Match-3 Puzzle Game

![Version](https://img.shields.io/badge/version-1.305.01-blue)
![Platform](https://img.shields.io/badge/platform-Android-green)
![MinSDK](https://img.shields.io/badge/minSdk-21-orange)
![TargetSDK](https://img.shields.io/badge/targetSdk-34-orange)

Un gioco puzzle match-3 professionale per Android, simile a Candy Crush Saga ma con identità originale.

## 🎮 Caratteristiche

- **Gameplay Match-3**: Griglia 8x8 con meccanica di swap touch-based
- **Sistema di Match**: Rilevamento automatico di combinazioni 3, 4 e 5 elementi
- **Caramelle Speciali**: 
  - Match-4: Caramella linea (distrugge riga o colonna)
  - Match-5: Caramella bomba (distrugge area 3x3)
- **Gravità e Cascata**: Sistema automatico di caduta e riempimento
- **50 Livelli**: Difficoltà progressiva (easy → expert)
- **Sistema di Progressione**: Salvataggio stelle e livelli completati
- **Combo System**: Moltiplicatore di punteggio per match consecutivi
- **Animazioni**: Swap, distruzione, caduta e particelle
- **Audio**: Effetti sonori per match, combo, vittoria e sconfitta
- **Vibrazione**: Feedback aptico per azioni importanti

## 📱 Specifiche Tecniche

- **Package**: `com.xdustatom.auryxsoda`
- **Versione**: 1.305.01
- **Linguaggio**: Kotlin 100%
- **Rendering**: Android Canvas nativo
- **Build System**: Gradle Kotlin DSL
- **Min SDK**: 21 (Android 5.0)
- **Target SDK**: 34 (Android 14)
- **Orientamento**: Portrait (fullscreen)

## 🏗️ Architettura

### Classi Core
- `BoardManager`: Gestione griglia e stato del gioco
- `Candy` / `Tile`: Entità di gioco
- `MatchDetector`: Rilevamento combinazioni
- `GravitySystem`: Sistema gravità e cascata
- `SpecialCandySystem`: Gestione caramelle speciali
- `AnimationSystem`: Sistema animazioni
- `ScoreSystem`: Punteggio e combo
- `LevelManager`: Gestione 50 livelli e progressione
- `ScreenManager`: Navigazione tra schermate

### Schermate
1. **Main Menu**: Menu principale
2. **Level Select**: Selezione livelli con stelle
3. **Game Screen**: Schermata di gioco
4. **Win Screen**: Vittoria con valutazione stelle
5. **Lose Screen**: Sconfitta con opzione retry

## 🚀 Build e Installazione

### Build Locale

```bash
# Clona il repository
git clone <repository-url>
cd AuryxSoda

# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# APK generato in: app/build/outputs/apk/
```

### GitHub Actions CI/CD

Il progetto include un workflow GitHub Actions (`.github/workflows/build.yml`) che:
- Si attiva automaticamente su push
- Installa JDK 17 e Android SDK
- Compila il progetto
- Genera APK release
- Carica l'APK come artifact: `AuryxSoda-v1.305.01.apk`

## 📦 Struttura Progetto

```
AuryxSoda/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/xdustatom/auryxsoda/
│   │   │   │   ├── MainActivity.kt
│   │   │   │   ├── GameView.kt
│   │   │   │   ├── BoardManager.kt
│   │   │   │   ├── Candy.kt
│   │   │   │   ├── Tile.kt
│   │   │   │   ├── Match.kt
│   │   │   │   ├── MatchDetector.kt
│   │   │   │   ├── GravitySystem.kt
│   │   │   │   ├── SpecialCandySystem.kt
│   │   │   │   ├── AnimationSystem.kt
│   │   │   │   ├── ScoreSystem.kt
│   │   │   │   ├── LevelManager.kt
│   │   │   │   └── ScreenManager.kt
│   │   │   ├── res/
│   │   │   │   ├── drawable/
│   │   │   │   │   ├── logo_auryx_soda.xml
│   │   │   │   │   ├── candy_red.xml
│   │   │   │   │   ├── candy_blue.xml
│   │   │   │   │   ├── candy_green.xml
│   │   │   │   │   ├── candy_yellow.xml
│   │   │   │   │   ├── candy_purple.xml
│   │   │   │   │   └── candy_orange.xml
│   │   │   │   ├── raw/
│   │   │   │   │   ├── match.wav
│   │   │   │   │   ├── swap.wav
│   │   │   │   │   ├── combo.wav
│   │   │   │   │   ├── win.wav
│   │   │   │   │   └── lose.wav
│   │   │   │   └── values/
│   │   │   ├── assets/
│   │   │   │   └── levels.json
│   │   │   └── AndroidManifest.xml
│   │   └── build.gradle.kts
│   └── proguard-rules.pro
├── gradle/
├── .github/
│   └── workflows/
│       └── build.yml
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── gradlew
└── README.md
```

## 🎯 Sistema di Livelli

50 livelli con difficoltà crescente configurati in `levels.json`:
- **Livelli 1-10**: Easy (30 mosse, target 1100-2000)
- **Livelli 11-25**: Medium (23-25 mosse, target 2150-4500)
- **Livelli 26-40**: Hard (18-20 mosse, target 4800-9000)
- **Livelli 41-50**: Expert (14-15 mosse, target 9500-14000)

## ⭐ Sistema di Valutazione

- **1 Stella**: 70-99% del target score
- **2 Stelle**: 100-149% del target score
- **3 Stelle**: 150%+ del target score

## 🎨 Asset

- **Logo**: Bottiglia di soda con bollicine colorate
- **Caramelle**: 6 colori (rosso, blu, verde, giallo, viola, arancione)
- **Audio**: 5 effetti sonori placeholder

## 📄 Licenza

Progetto creato per scopo educativo e dimostrativo.

## 🔧 Sviluppo Futuro

Possibili miglioramenti:
- Power-ups aggiuntivi
- Modalità multiplayer
- Integrazione Google Play Games
- Effetti particellari avanzati
- Musica di sottofondo
- Più tipi di livelli (tempo limitato, ostacoli, ecc.)

## 📧 Contatti

Package: com.xdustatom.auryxsoda
Version: 1.305.01

---

**Auryx Soda** - Un'esperienza match-3 professionale per Android! 🍭✨
