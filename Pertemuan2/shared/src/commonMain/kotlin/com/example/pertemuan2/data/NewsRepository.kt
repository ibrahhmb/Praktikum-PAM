package com.example.pertemuan2.data

import com.example.pertemuan2.model.NewsCategory
import com.example.pertemuan2.model.NewsDetail
import com.example.pertemuan2.model.RawNewsArticle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class NewsRepository {

    // Kumpulan template artikel simulasi berita
    private val sampleNews = listOf(
        RawNewsArticle(
            id = "tech_1",
            title = "Perkembangan Model AI Generatif Terbaru Mempercepat Pengembangan Aplikasi",
            category = NewsCategory.TEKNOLOGI,
            rawContent = "Teknologi kecerdasan buatan terus berevolusi pesat menghadirkan arsitektur transformer mutakhir yang lebih efisien dan akurat untuk pengembang piranti lunak global.",
            source = "TechInAsia",
            author = "Andi Pratama",
            timestampSeconds = 0L,
            readMinutes = 3
        ),
        RawNewsArticle(
            id = "biz_1",
            title = "Indeks Saham Gabungan Menguat Didorong Sektor Perbankan Digital",
            category = NewsCategory.BISNIS,
            rawContent = "Pergerakan pasar modal hari ini menunjukkan optimisme investor seiring kinerja positif laporan keuangan kuartal terbaru perbankan nasional.",
            source = "BisnisDaily",
            author = "Siti Rahma",
            timestampSeconds = 0L,
            readMinutes = 2
        ),
        RawNewsArticle(
            id = "sport_1",
            title = "Timnas Indonesia Mempersiapkan Strategi Kunci Jelang Laga Kualifikasi",
            category = NewsCategory.OLAHRAGA,
            rawContent = "Pelatih memfokuskan latihan pada transisi cepat dan koordinasi lini pertahanan untuk menghadapi pertandingan penentuan akhir pekan ini.",
            source = "SportCorner",
            author = "Budi Santoso",
            timestampSeconds = 0L,
            readMinutes = 4
        ),
        RawNewsArticle(
            id = "health_1",
            title = "Pola Tidur Berkualitas Terbukti Meningkatkan Daya Ingat dan Fokus Kerja",
            category = NewsCategory.KESEHATAN,
            rawContent = "Riset kesehatan terkini mengungkapkan tidur cukup 7 hingga 8 jam membantu regenerasi sel neuron dan menjaga stabilitas metabolisme tubuh.",
            source = "SehatKu Media",
            author = "dr. Amanda Putri",
            timestampSeconds = 0L,
            readMinutes = 3
        ),
        RawNewsArticle(
            id = "ent_1",
            title = "Festival Film Nasional Umumkan Deretan Karya Sinema Unggulan 2026",
            category = NewsCategory.HIBURAN,
            rawContent = "Beragam genre film dari sineas muda nusantara meramaikan ajang penghargaan perfilman tahunan dengan visual dan narasi memukau.",
            source = "SinemaPost",
            author = "Dimas Arya",
            timestampSeconds = 0L,
            readMinutes = 2
        ),
        RawNewsArticle(
            id = "tech_2",
            title = "Adopsi Kotlin Multiplatform Meningkat Pesat di Perusahaan Teknologi Global",
            category = NewsCategory.TEKNOLOGI,
            rawContent = "Penggunaan Kotlin Multiplatform dan Compose Multiplatform memudahkan tim engineering berbagi logika bisnis dan UI secara konsisten lintas platform Android, Desktop, dan iOS.",
            source = "KotlinWeekly",
            author = "Reza Fahlevi",
            timestampSeconds = 0L,
            readMinutes = 5
        ),
        RawNewsArticle(
            id = "biz_2",
            title = "Pertumbuhan Startup Ramah Lingkungan Menarik Minat Investor Hijau",
            category = NewsCategory.BISNIS,
            rawContent = "Inovasi energi terbarukan dan ekonomi sirkular kini menjadi pendorong utama pendanaan ventura berkelanjutan di kawasan Asia Tenggara.",
            source = "EconWatch",
            author = "Dewi Lestari",
            timestampSeconds = 0L,
            readMinutes = 3
        ),
        RawNewsArticle(
            id = "sport_2",
            title = "Juara Bertahan Bulu Tangkis Melaju Mulus ke Babak Semifinal",
            category = NewsCategory.OLAHRAGA,
            rawContent = "Permainan agresif dan smes akurat memastikan kemenangan dua gim langsung atas lawan tangguh di turnamen bulu tangkis bergengsi.",
            source = "RaketMania",
            author = "Hendrawan",
            timestampSeconds = 0L,
            readMinutes = 2
        ),
        RawNewsArticle(
            id = "health_2",
            title = "Khasiat Konsumsi Buah Segar dan Sayur Hijau Terhadap Sistem Imun",
            category = NewsCategory.KESEHATAN,
            rawContent = "Kandungan antioksidan alami dan serat pangan berperan krusial dalam melawan radikal bebas serta mendukung mikrobioma usus yang sehat.",
            source = "NutrisiAktif",
            author = "dr. Kevin Wijaya",
            timestampSeconds = 0L,
            readMinutes = 3
        ),
        RawNewsArticle(
            id = "ent_2",
            title = "Konser Musik Orkestra Harmoni Nusantara Pukau Ribuan Penonton",
            category = NewsCategory.HIBURAN,
            rawContent = "Kolaborasi aransemen tradisional dengan instrumen modern menciptakan pengalaman musikal epik yang diapresiasi standing ovation.",
            source = "ArtCulture ID",
            author = "Nadia Kirana",
            timestampSeconds = 0L,
            readMinutes = 3
        )
    )

    /**
     * [FITUR 1] Flow yang mensimulasikan data berita baru setiap 2 detik
     * Memancarkan objek RawNewsArticle berkala dengan delay(2000)
     */
    fun getNewsStream(): Flow<RawNewsArticle> = flow {
        var counter = 1
        while (true) {
            // Jeda 2 detik sebelum memancarkan berita berikutnya
            delay(2000L)

            val templateIndex = (counter - 1) % sampleNews.size
            val template = sampleNews[templateIndex]
            val currentTimeSec = (counter * 2).toLong()

            val simulatedArticle = template.copy(
                id = "${template.category.name.lowercase()}_$counter",
                title = if (counter <= sampleNews.size) template.title else "[Update #$counter] ${template.title}",
                timestampSeconds = currentTimeSec
            )

            emit(simulatedArticle)
            counter++
        }
    }

    /**
     * [FITUR 5] Coroutines untuk mengambil detail berita secara async
     * Menggunakan suspend function dengan delay untuk mensimulasikan network/IO latency
     */
    suspend fun fetchNewsDetailAsync(rawArticle: RawNewsArticle): NewsDetail = withContext(Dispatchers.Default) {
        // Simulasi network request delay secara Asynchronous via coroutine
        delay(750L)

        NewsDetail(
            id = rawArticle.id,
            title = rawArticle.title,
            category = rawArticle.category,
            author = rawArticle.author,
            source = rawArticle.source,
            fullContent = buildString {
                append(rawArticle.rawContent)
                append("\n\n")
                append("Laporan Mendalam:\n")
                append("Investigasi lapangan menunjukkan bahwa fenomena ini membawa pengaruh signifikan bagi perkembangan sektor ${rawArticle.category.displayName.lowercase()}. Para pakar dan pengamat industri menyoroti pentingnya adaptasi terhadap perubahan yang cepat ini.\n\n")
                append("Menurut ${rawArticle.author}, langkah strategis perlu segera diambil agar dampak positif dapat dioptimalkan secara berkelanjutan.")
            },
            publishedAt = "Dipublikasikan beberapa saat lalu oleh ${rawArticle.source}",
            readTimeMinutes = rawArticle.readMinutes,
            tags = listOf(
                rawArticle.category.displayName,
                "Trending",
                "Update 2026",
                rawArticle.source
            )
        )
    }
}
