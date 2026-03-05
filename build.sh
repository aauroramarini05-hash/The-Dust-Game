#!/bin/bash

# Auryx Soda - Build Script
# This script helps to build and test the Android project

echo "======================================"
echo "   Auryx Soda - Build Script"
echo "======================================"
echo ""

# Check if ANDROID_HOME is set
if [ -z "$ANDROID_HOME" ]; then
    echo "⚠️  Warning: ANDROID_HOME is not set"
    echo "   Set it to your Android SDK location"
    echo ""
fi

# Make gradlew executable
chmod +x gradlew

# Function to build debug APK
build_debug() {
    echo "🔨 Building Debug APK..."
    ./gradlew assembleDebug --stacktrace
    if [ $? -eq 0 ]; then
        echo "✅ Debug APK built successfully!"
        echo "📦 Location: app/build/outputs/apk/debug/app-debug.apk"
    else
        echo "❌ Build failed!"
        exit 1
    fi
}

# Function to build release APK
build_release() {
    echo "🔨 Building Release APK..."
    ./gradlew assembleRelease --stacktrace
    if [ $? -eq 0 ]; then
        echo "✅ Release APK built successfully!"
        echo "📦 Location: app/build/outputs/apk/release/"
    else
        echo "❌ Build failed!"
        exit 1
    fi
}

# Function to clean project
clean_project() {
    echo "🧹 Cleaning project..."
    ./gradlew clean
    echo "✅ Project cleaned!"
}

# Main menu
case "$1" in
    debug)
        build_debug
        ;;
    release)
        build_release
        ;;
    clean)
        clean_project
        ;;
    *)
        echo "Usage: $0 {debug|release|clean}"
        echo ""
        echo "  debug   - Build debug APK"
        echo "  release - Build release APK"
        echo "  clean   - Clean project"
        exit 1
        ;;
esac

echo ""
echo "======================================"
echo "   Build Complete!"
echo "======================================"
