# Tugas PAM - Pertemuan 2

Nama : Ibrahim Budi Satria  
NIM : 123140097  
Kelas : RA  

---

## Fitur

| No | Kriteria Tugas | File Implementasi | Deskripsi Singkat |
| :---: | :--- | :--- | :--- |
| **1** | **Flow Simulasi Berita (2 Detik)** | [`NewsRepository.kt`](./shared/src/commonMain/kotlin/com/example/pertemuan2/data/NewsRepository.kt) | Menggunakan Kotlin Flow builder `flow { ... }` dengan `delay(2000L)` secara berkala untuk memancarkan berita simulasi baru setiap 2 detik. Terdapat juga tombol kontrol **Jeda / Lanjut Flow** pada UI. |
| **2** | **Filter Kategori Berita** | [`NewsFeedViewModel.kt`](./shared/src/commonMain/kotlin/com/example/pertemuan2/viewmodel/NewsFeedViewModel.kt) & [`NewsFeedScreen.kt`](./shared/src/commonMain/kotlin/com/example/pertemuan2/ui/NewsFeedScreen.kt) | Filter berita reaktif berbasis operator Flow `combine` dan `.filter { ... }`. Kategori yang didukung: `🌐 Semua`, `💻 Teknologi`, `📈 Bisnis`, `⚽ Olahraga`, `🏥 Kesehatan`, dan `🎬 Hiburan`. |
| **3** | **Transform Data ke Format Tampilan** | [`NewsFeedViewModel.kt`](./shared/src/commonMain/kotlin/com/example/pertemuan2/viewmodel/NewsFeedViewModel.kt) | Mengubah (*transform*) objek mentah `RawNewsArticle` menjadi `NewsUiModel` dengan format judul menarik, badge emoji kategori, format waktu relatif (`Terkirim +Xs lalu`), cuplikan ringkasan (*preview snippet*), serta badge status baca. |
| **4** | **StateFlow Jumlah Berita Dibaca** | [`NewsFeedViewModel.kt`](./shared/src/commonMain/kotlin/com/example/pertemuan2/viewmodel/NewsFeedViewModel.kt) | Menggunakan `MutableStateFlow(0)` yang diekspos sebagai `StateFlow<Int>`. Saat artikel dibuka, ID artikel disimpan dan jumlah berita dibaca otomatis diperbarui serta ditampilkan pada dashboard statistik UI. |
| **5** | **Coroutines Fetch Detail Berita (Async)** | [`NewsRepository.kt`](./shared/src/commonMain/kotlin/com/example/pertemuan2/data/NewsRepository.kt) & [`NewsFeedViewModel.kt`](./shared/src/commonMain/kotlin/com/example/pertemuan2/viewmodel/NewsFeedViewModel.kt) | Memanggil suspend function `fetchNewsDetailAsync` di dalam `viewModelScope.launch` dengan latensi asinkron simulasi (`delay(750L)`). UI menampilkan indikator loading (*spinner*) sebelum detail lengkap ditampilkan. |

---

## Struktur Kode 

```text
Pertemuan2/
├── androidApp/                               # Modul aplikasi Android
│   └── src/main/kotlin/.../MainActivity.kt  # Entry point Android
├── desktopApp/                               # Modul aplikasi Desktop (JVM)
│   └── src/main/kotlin/.../main.kt          # Entry point Desktop
├── shared/                                   # Modul Shared (KMP & Compose)
│   ├── src/commonMain/kotlin/com/example/pertemuan2/
│   │   ├── model/
│   │   │   └── NewsModels.kt                 # Model data: RawNewsArticle, NewsUiModel, NewsDetail, dll.
│   │   ├── data/
│   │   │   └── NewsRepository.kt             # Flow stream 2s & Coroutine async fetch detail
│   │   ├── viewmodel/
│   │   │   └── NewsFeedViewModel.kt          # Logika StateFlow, Filter, dan Transformasi
│   │   ├── ui/
│   │   │   └── NewsFeedScreen.kt             # Tampilan UI Compose Multiplatform
│   │   └── App.kt                            # Root Composable
│   └── src/commonTest/kotlin/com/example/pertemuan2/
│       └── NewsFeedSimulatorTest.kt          # Unit test untuk 5 fitur utama
└── Referensi/
    └── Screenshot 2026-09-25 193518.png      # Gambar acuan deskripsi tugas
```

---

## 

1. **JDK 17 atau JDK 21**
   - Disarankan menggunakan Java/JBR bawaan Android Studio (`C:\Program Files\Android\Android Studio\jbr`).
   - Telah dikonfigurasikan otomatis pada file `gradle.properties`:
     ```properties
     org.gradle.java.home=C:\\Program Files\\Android\\Android Studio\\jbr
     ```
2. **Android Studio** (Koala, Ladybug, Meerkat, atau versi yang lebih baru).
3. **Android SDK** (API level 34+ untuk target Android).

---

## Cara Menjalankan 

#### Menjalankan di Android (Emulator / Perangkat Asli):
1. Buka proyek ini di **Android Studio**.
2. Tunggu proses *Gradle Sync* selesai.
3. Di dropdown *Run Configuration* di bagian atas, pilih **`androidApp`**.
4. Pilih Emulator Android atau perangkat fisik yang tersambung.
5. Klik tombol **Run**.

#### Menjalankan di Desktop (Windows/JVM):
1. Di dropdown *Run Configuration*, pilih **`desktopApp`**.
2. Klik tombol **Run**.
*(Aplikasi akan terbuka langsung sebagai jendela native desktop Windows).*

---