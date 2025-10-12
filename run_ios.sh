#!/bin/bash

# Android Studio iOS Build Script
# Bu script Android Studio'dan iOS uygulamasını çalıştırmak için kullanılır

echo "🚀 Starting iOS build process..."

# Proje dizinine git
cd "$(dirname "$0")"

# Build klasörünü temizle
echo "🧹 Cleaning build artifacts..."
./gradlew clean

# iOS framework'ünü build et
echo "🔨 Building iOS framework..."
./gradlew :composeApp:linkDebugFrameworkIosSimulatorArm64

# Xcode için framework'ü hazırla
echo "📱 Preparing framework for Xcode..."
./gradlew :composeApp:embedAndSignAppleFrameworkForXcode

# Xcode'da build et
echo "🏗️ Building in Xcode..."
xcodebuild -project iosApp/iosApp.xcodeproj -scheme iosApp -destination 'platform=iOS Simulator,name=iPhone 17' build

# Simülatörde çalıştır
echo "▶️ Launching on simulator..."
xcrun simctl install "iPhone 17" "/Users/enes/Library/Developer/Xcode/DerivedData/iosApp-eiwazqyafxqssqbctjmwbetokoau/Build/Products/Debug-iphonesimulator/DiscDog.app"
xcrun simctl launch "iPhone 17" com.discdogs.app.DiscDog

echo "✅ iOS app launched successfully!"
