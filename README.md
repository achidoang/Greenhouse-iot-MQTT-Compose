# 🌱 Greenhouse Monitoring & Control System

## 📌 Deskripsi Proyek
Aplikasi **monitoring dan kontrol berbasis IoT** untuk **greenhouse hidroponik NFT selada**. Dengan aplikasi ini, pengguna dapat **memantau kondisi lingkungan secara real-time**, **mengontrol aktuator secara manual maupun otomatis**, serta mendapatkan notifikasi saat parameter lingkungan berada di luar batas optimal. Aplikasi ini menggunakan **protokol MQTT** untuk komunikasi antara perangkat IoT dan aplikasi Android.

## ✨ Fitur Utama
- ✅ **Monitoring Data Sensor** (pH, TDS, suhu, kelembaban) secara **real-time**
- ✅ **Kontrol Aktuator** (Manual & Otomatis)
- ✅ **Notifikasi & Peringatan** saat kondisi kritis
- ✅ **Manajemen Perangkat IoT** (Tambah/Edit/Hapus Profil Perangkat)
- ✅ **Prediksi Cuaca** menggunakan API
- ✅ **Tutorial Hidroponik** untuk pemula
- ✅ **Role-Based Access Control (RBAC)** (Viewer, Administrator)
- ✅ **Penyimpanan Data Offline** dan **Sinkronisasi Otomatis** saat kembali online

## 🛠 Teknologi yang Digunakan
### **Backend & IoT**
- Protokol **MQTT** untuk komunikasi perangkat IoT
- **Express.js** untuk backend API
- **Database Cloud (Firebase/Custom API)** untuk penyimpanan data

### **Frontend (Aplikasi Android)**
- **Jetpack Compose** untuk UI modern dan responsif
- **MVVM + Clean Architecture** untuk struktur kode modular
- **Hilt** untuk Dependency Injection
- **LiveData & ViewModel** untuk real-time sync
- **Room Database** untuk penyimpanan offline

### **Pengujian & Validasi**
- **Metode Scrum** untuk pengembangan perangkat lunak secara iteratif
- **Aiken Index** untuk validasi dengan tingkat validitas tinggi (>0.80)
- **Black Box Testing** untuk memastikan setiap fitur berfungsi dengan baik

## Screenshots

<div style="display: flex; flex-wrap: wrap; justify-content: space-between;">

  <img src="/Screenshoot/ss-1.jpg" alt="Screenshot 1" height="450px">
  <img src="/Screenshoot/ss-2.jpg" alt="Screenshot 2" height="450px">
  <img src="/Screenshoot/ss-3.jpg" alt="Screenshot 3" height="450px">
  <img src="/Screenshoot/ss-4.jpg" alt="Screenshot 4" height="450px">
  
  <img src="/Screenshoot/ss-5.jpg" alt="Screenshot 5" height="450px">
  <img src="/Screenshoot/ss-6.jpg" alt="Screenshot 6" height="450px">
  <img src="/Screenshoot/ss-7.jpg" alt="Screenshot 7" height="450px">
  <img src="/Screenshoot/ss-8.jpg" alt="Screenshot 8" height="450px">

</div>

## 📌 Instalasi & Penggunaan
```bash
# Clone repository
$ git clone https://github.com/achidoang/Greenhouse-iot-MQTT-Compose.git
$ cd Greenhouse-iot-MQTT-Compose


Untuk aplikasi Android, buka folder proyek di **Android Studio**, lalu jalankan dengan emulator atau perangkat fisik.

## 🚀 Kontribusi
Kami sangat terbuka terhadap kontribusi dari komunitas. Jika Anda ingin berkontribusi, silakan fork repository ini, buat branch baru, dan ajukan pull request.


🔥 **Dibuat dengan ❤️ oleh Muhammad Aji Saputra**
