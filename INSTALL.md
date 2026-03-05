# Auryx Soda - Istruzioni di Installazione e Build

## 🎯 Guida Rapida

Questo documento fornisce istruzioni dettagliate su come compilare e utilizzare il progetto Auryx Soda.

## 📋 Prerequisiti

### Necessari per Build Locale

1. **JDK 17** o superiore
   ```bash
   # Verifica versione Java
   java -version
   ```

2. **Android SDK** (se build locale)
   - Installabile tramite Android Studio
   - O tramite command-line tools
   - Imposta variabile `ANDROID_HOME`

3. **Git** (per clonare repository)
   ```bash
   git --version
   ```

### Necessari per GitHub Actions (Automatico)

Nessun prerequisito! GitHub Actions installa automaticamente:
- ✅ JDK 17
- ✅ Android SDK
- ✅ Gradle

## 🚀 Metodo 1: Build Automatico con GitHub Actions (CONSIGLIATO)

### Passo 1: Carica su GitHub

```bash
# 1. Crea repository su GitHub (tramite web)
#    Nome suggerito: auryx-soda

# 2. Nella cartella del progetto, inizializza Git
cd /percorso/auryx-soda
git init

# 3. Aggiungi tutti i file
git add .

# 4. Commit iniziale
git commit -m "Initial commit: Auryx Soda v1.305.01"

# 5. Collega repository remoto
git remote add origin https://github.com/TUO_USERNAME/auryx-soda.git

# 6. Push del codice
git branch -M main
git push -u origin main
```

### Passo 2: Attendi il Build

1. GitHub Actions si attiva automaticamente dopo il push
2. Vai su: `https://github.com/TUO_USERNAME/auryx-soda/actions`
3. Vedrai il workflow "Android CI Build" in esecuzione
4. Attendi completamento (~5-10 minuti)

### Passo 3: Scarica APK

1. Click sul workflow completato
2. Scorri in basso fino a "Artifacts"
3. Download: **AuryxSoda-v1.305.01.apk** (~5-8 MB)

✅ **FATTO! Hai il tuo APK pronto!**

## 🔨 Metodo 2: Build Locale

### Setup Ambiente

```bash
# 1. Installa Android SDK
# Opzione A: Tramite Android Studio
# Scarica da: https://developer.android.com/studio

# Opzione B: Command Line Tools
# Scarica da: https://developer.android.com/studio#command-tools

# 2. Imposta ANDROID_HOME
export ANDROID_HOME=$HOME/Android/Sdk
export PATH=$PATH:$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools

# 3. Verifica installazione
adb --version
```

### Build Debug APK

```bash
cd /percorso/auryx-soda

# Metodo 1: Script helper
./build.sh debug

# Metodo 2: Gradle diretto
./gradlew assembleDebug

# APK generato in:
# app/build/outputs/apk/debug/app-debug.apk
```

### Build Release APK

```bash
# Metodo 1: Script helper
./build.sh release

# Metodo 2: Gradle diretto
./gradlew assembleRelease

# APK generato in:
# app/build/outputs/apk/release/app-release-unsigned.apk
```

### Clean Project

```bash
# Pulisce file di build
./build.sh clean

# O
./gradlew clean
```

## 📲 Installazione APK su Dispositivo Android

### Metodo 1: ADB (USB)

```bash
# 1. Abilita "Debug USB" sul dispositivo
#    Impostazioni → Info telefono → Tocca 7 volte "Numero build"
#    Impostazioni → Opzioni sviluppatore → Debug USB

# 2. Collega dispositivo via USB

# 3. Verifica connessione
adb devices

# 4. Installa APK
adb install app/build/outputs/apk/debug/app-debug.apk

# O per release
adb install app/build/outputs/apk/release/AuryxSoda-v1.305.01.apk
```

### Metodo 2: Trasferimento File

```bash
# 1. Copia APK sul telefono
#    - Via USB
#    - Via email
#    - Via cloud (Google Drive, Dropbox, ecc.)

# 2. Sul telefono:
#    - Apri file manager
#    - Trova APK
#    - Tocca per installare
#    - Autorizza "Installa app sconosciute" se richiesto
```

### Metodo 3: Wireless ADB

```bash
# 1. Connetti telefono e PC alla stessa rete WiFi

# 2. Sul telefono abilita "Debug wireless"

# 3. Su PC:
adb tcpip 5555
adb connect INDIRIZZO_IP_TELEFONO:5555

# 4. Installa APK
adb install AuryxSoda-v1.305.01.apk
```

## 🧪 Testing e Sviluppo

### Esegui su Emulatore Android

```bash
# 1. Crea emulatore con Android Studio
#    Tools → AVD Manager → Create Virtual Device

# 2. Avvia emulatore
emulator -avd Nome_Emulatore

# 3. Installa e run
./gradlew installDebug
```

### Debug con Android Studio

```bash
# 1. Apri progetto in Android Studio
#    File → Open → Seleziona cartella progetto

# 2. Sync Gradle
#    Toolbar → Sync Project with Gradle Files

# 3. Run app
#    Toolbar → Run (Shift+F10)
```

### Logs e Debugging

```bash
# Visualizza logs in tempo reale
adb logcat | grep -i auryx

# Filtra per package
adb logcat | grep com.xdustatom.auryxsoda

# Salva logs su file
adb logcat -d > auryx_logs.txt
```

## 🔧 Troubleshooting

### Problema: Gradle Build Fallisce

**Errore**: `Unable to find Android SDK`

**Soluzione**:
```bash
# Imposta ANDROID_HOME
export ANDROID_HOME=/percorso/al/sdk
export PATH=$PATH:$ANDROID_HOME/tools
```

### Problema: Permission Denied su gradlew

**Errore**: `Permission denied: ./gradlew`

**Soluzione**:
```bash
chmod +x gradlew
./gradlew assembleDebug
```

### Problema: SDK Non Trovato

**Errore**: `SDK location not found`

**Soluzione**:
Crea/modifica `local.properties`:
```properties
sdk.dir=/percorso/al/Android/Sdk
```

### Problema: Build Tools Version

**Errore**: `Failed to find Build Tools revision X.X.X`

**Soluzione**:
```bash
# Installa build tools
sdkmanager "build-tools;34.0.0"
```

### Problema: Spazio Insufficiente

**Errore**: `No space left on device`

**Soluzione**:
```bash
# Pulisci build cache
./gradlew clean
rm -rf ~/.gradle/caches/
```

## 📊 Verifica Build Completato

Dopo il build, verifica:

```bash
# Controlla APK generato
ls -lh app/build/outputs/apk/release/

# Info APK
aapt dump badging app/build/outputs/apk/release/app-release-unsigned.apk | grep -E 'package|version|sdkVersion'

# Output atteso:
# package: name='com.xdustatom.auryxsoda' versionCode='130501' versionName='1.305.01'
# sdkVersion:'21'
# targetSdkVersion:'34'
```

## 🎮 Primo Avvio

Dopo installazione:

1. **Icona App**: Cerca "Auryx Soda" nel drawer
2. **Primo Tap**: Apre Main Menu
3. **Level Select**: Tap su "Level 1" per iniziare
4. **Gameplay**: 
   - Tap caramella → Tap adiacente → Swap
   - Match 3+ stesso colore
   - Obiettivo: Raggiungere target score

## 🚢 Distribuzione

### Google Play Store

Per pubblicare su Play Store:

1. **Genera Keystore**:
   ```bash
   keytool -genkey -v -keystore auryx-soda.keystore \
     -alias auryxsoda -keyalg RSA -keysize 2048 -validity 10000
   ```

2. **Firma APK**:
   ```bash
   jarsigner -verbose -sigalg SHA256withRSA -digestalg SHA-256 \
     -keystore auryx-soda.keystore \
     app/build/outputs/apk/release/app-release-unsigned.apk \
     auryxsoda
   ```

3. **Allinea APK**:
   ```bash
   zipalign -v 4 app-release-unsigned.apk AuryxSoda-v1.305.01-signed.apk
   ```

4. **Upload su Play Console**

### Distribuzione Diretta

1. Condividi APK via:
   - Link diretto
   - Email
   - Cloud storage
   - Sito web download

2. Utente installa abilitando "Sorgenti sconosciute"

## 📚 Documentazione Aggiuntiva

- **README.md**: Panoramica progetto
- **PROJECT_VERIFICATION.md**: Checklist completezza
- **GITHUB_ACTIONS.md**: Guida CI/CD

## ❓ Supporto

Per problemi:
1. Controlla logs: `adb logcat`
2. Verifica requisiti minimi: Android 5.0+ (API 21)
3. Test su emulatore prima di dispositivo fisico

---

**Auryx Soda v1.305.01** - Buon divertimento! 🎮✨
