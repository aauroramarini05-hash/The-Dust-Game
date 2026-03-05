# GitHub Actions - Setup Instructions

## 📋 Come Configurare GitHub Actions per Build Automatico

Questo progetto include un workflow GitHub Actions (`.github/workflows/build.yml`) che compila automaticamente l'APK di Auryx Soda quando effettui push sul repository.

## 🚀 Setup Rapido

### 1. Crea Repository GitHub

```bash
# Inizializza repository locale (se non già fatto)
git init

# Aggiungi tutti i file
git add .

# Commit iniziale
git commit -m "Initial commit: Auryx Soda v1.305.01"

# Crea repository su GitHub (tramite web o CLI)
# Poi collega il repository remoto:
git remote add origin https://github.com/TUO_USERNAME/auryx-soda.git

# Push del codice
git branch -M main
git push -u origin main
```

### 2. GitHub Actions si Attiva Automaticamente

Dopo il push, GitHub Actions:
1. ✅ Rileva il workflow in `.github/workflows/build.yml`
2. ✅ Installa JDK 17
3. ✅ Installa Android SDK
4. ✅ Esegue `./gradlew assembleRelease`
5. ✅ Genera APK
6. ✅ Carica l'APK come artifact

### 3. Scarica l'APK

1. Vai su GitHub repository
2. Click su "Actions" tab
3. Seleziona l'ultimo workflow run
4. Scarica l'artifact: **AuryxSoda-v1.305.01.apk**

## 🔐 Firma APK (Opzionale)

Per firmare l'APK con il tuo keystore:

### Crea Keystore

```bash
keytool -genkey -v -keystore auryx-soda.keystore \
  -alias auryxsoda -keyalg RSA -keysize 2048 -validity 10000
```

### Aggiungi Secrets su GitHub

1. Repository Settings → Secrets → Actions
2. Aggiungi questi secrets:
   - `KEYSTORE_PATH`: Path al file keystore (es: `/path/to/auryx-soda.keystore`)
   - `KEYSTORE_PASSWORD`: Password del keystore
   - `KEY_ALIAS`: Alias della chiave (es: `auryxsoda`)
   - `KEY_PASSWORD`: Password della chiave

### Modifica build.gradle.kts (opzionale)

Aggiungi configurazione signing in `app/build.gradle.kts`:

```kotlin
android {
    signingConfigs {
        create("release") {
            storeFile = file(System.getenv("KEYSTORE_PATH") ?: "keystore.jks")
            storePassword = System.getenv("KEYSTORE_PASSWORD")
            keyAlias = System.getenv("KEY_ALIAS")
            keyPassword = System.getenv("KEY_PASSWORD")
        }
    }
    
    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
            // ... resto configurazione
        }
    }
}
```

## 🔄 Trigger del Workflow

Il workflow si attiva su:
- ✅ Push su branch `main`, `master`, `develop`
- ✅ Pull request su `main`, `master`
- ✅ Manualmente tramite "workflow_dispatch"

### Esecuzione Manuale

1. GitHub repository → Actions
2. Seleziona "Android CI Build"
3. Click "Run workflow"
4. Seleziona branch
5. Click "Run workflow"

## 📦 Output del Build

### Artifact Generati

- **Nome**: `AuryxSoda-v1.305.01`
- **File**: `AuryxSoda-v1.305.01.apk`
- **Retention**: 30 giorni
- **Dimensione stimata**: ~5-8 MB

### Logs

Per vedere i log del build:
1. Actions → Workflow run
2. Click su job "build"
3. Espandi gli step per vedere output dettagliato

## 🐛 Troubleshooting

### Build Fallito

**Problema**: Gradle build fallisce

**Soluzioni**:
1. Controlla logs in GitHub Actions
2. Testa build locale:
   ```bash
   ./gradlew assembleRelease --stacktrace
   ```
3. Verifica dipendenze in `app/build.gradle.kts`

### SDK Non Trovato

**Problema**: Android SDK non configurato

**Soluzione**: Il workflow usa `android-actions/setup-android@v3` che installa automaticamente l'SDK. Se fallisce:
- Verifica versioni SDK in `app/build.gradle.kts`:
  - `compileSdk = 34`
  - `targetSdk = 34`
  - `minSdk = 21`

### Gradlew Permission Denied

**Problema**: `./gradlew` non eseguibile

**Soluzione**: Il workflow include:
```yaml
- name: Grant execute permission for gradlew
  run: chmod +x gradlew
```

Se persiste localmente:
```bash
chmod +x gradlew
git add gradlew
git commit -m "Fix gradlew permissions"
```

### Artifact Non Trovato

**Problema**: APK non generato

**Soluzione**: Il workflow rinomina l'APK:
- Debug: `app-debug.apk` → `AuryxSoda-v1.305.01.apk`
- Release: `app-release-unsigned.apk` → `AuryxSoda-v1.305.01.apk`

Controlla path in workflow se modificato.

## 📊 Status Badge

Aggiungi badge GitHub Actions al README:

```markdown
![Build Status](https://github.com/TUO_USERNAME/auryx-soda/workflows/Android%20CI%20Build/badge.svg)
```

## 🎯 Best Practices

1. ✅ **Version Tagging**: Crea tag per release
   ```bash
   git tag -a v1.305.01 -m "Release v1.305.01"
   git push origin v1.305.01
   ```

2. ✅ **Branch Protection**: Proteggi branch `main` per PR obbligatorie

3. ✅ **Caching**: Il workflow usa cache per velocizzare build:
   - Gradle packages
   - Gradle wrapper

4. ✅ **Parallel Jobs**: Per progetti grandi, splitta in più job

## 🚢 Deployment su Play Store

Per pubblicare su Google Play Store:

1. Genera keystore release
2. Configura signing in Gradle
3. Aggiungi secrets su GitHub
4. Build APK/AAB firmato
5. Upload su Play Console

### AAB vs APK

Per Play Store usa **AAB** (Android App Bundle):

```bash
./gradlew bundleRelease
```

Modifica workflow per caricare AAB:
```yaml
path: app/build/outputs/bundle/release/app-release.aab
```

## 📞 Supporto

Per problemi con GitHub Actions:
- Controlla logs dettagliati
- Verifica configurazioni Gradle
- Testa build locale prima di push

## ✅ Checklist Pre-Push

Prima di fare push:
- [ ] `./gradlew assembleDebug` funziona localmente
- [ ] Tutti i file necessari sono presenti
- [ ] `.gitignore` configurato correttamente
- [ ] `gradlew` ha permessi di esecuzione
- [ ] Workflow YAML sintatticamente corretto

---

**Auryx Soda** - Build automatico configurato! 🚀
