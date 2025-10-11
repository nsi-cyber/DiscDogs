# API Key Configuration Guide

Bu proje güvenli API key yönetimi kullanır. API key'lerinizi aşağıdaki adımları takip ederek
yapılandırın:

## 🔑 API Key'leri Ayarlama

### 1. local.properties Dosyasını Güncelleyin

Proje kök dizinindeki `local.properties` dosyasını açın ve API key'lerinizi ekleyin:

```properties
# API Keys - Replace with your actual keys
GEMINI_API_KEY=your_actual_gemini_api_key_here
DISCOGS_API_KEY=your_actual_discogs_api_key_here
```

### 2. iOS için Info.plist Güncellemesi

`iosApp/iosApp/Info.plist` dosyasında API key'leri güncelleyin:

```xml

<key>GEMINI_API_KEY</key><string>your_actual_gemini_api_key_here</string><key>DISCOGS_API_KEY
</key><string>your_actual_discogs_api_key_here</string>
```

## 🛡️ Güvenlik Notları

- `local.properties` dosyası `.gitignore`'da olduğu için Git'e commit edilmez
- API key'lerinizi asla kod repository'sine commit etmeyin
- Production build'lerde farklı API key'leri kullanın
- API key'lerinizi düzenli olarak rotate edin

## 📱 Platform Desteği

- **Android**: BuildConfig üzerinden API key'ler alınır
- **iOS**: Info.plist üzerinden API key'ler alınır
- **Common**: Platform-specific implementasyonlar expect/actual pattern ile yönetilir

## 🔧 API Key'leri Nasıl Alınır

### Gemini API Key

1. [Google AI Studio](https://makersuite.google.com/app/apikey) adresine gidin
2. Google hesabınızla giriş yapın
3. "Create API Key" butonuna tıklayın
4. Oluşturulan key'i kopyalayın

### Discogs API Key

1. [Discogs API](https://www.discogs.com/settings/developers) adresine gidin
2. Discogs hesabınızla giriş yapın
3. "Generate new token" butonuna tıklayın
4. Oluşturulan token'ı kopyalayın

## ⚠️ Önemli Uyarılar

- API key'lerinizi kimseyle paylaşmayın
- Key'leri public repository'lerde paylaşmayın
- Eğer key'leriniz compromise olduysa hemen değiştirin
- Rate limiting ayarlarını kontrol edin
