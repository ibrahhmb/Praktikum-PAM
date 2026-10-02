package com.example.pertemuan2.model

/**
 * Kategori berita yang didukung aplikasi
 */
enum class NewsCategory(val displayName: String, val emoji: String) {
    ALL("Semua", "🌐"),
    TEKNOLOGI("Teknologi", "💻"),
    BISNIS("Bisnis", "📈"),
    OLAHRAGA("Olahraga", "⚽"),
    KESEHATAN("Kesehatan", "🏥"),
    HIBURAN("Hiburan", "🎬")
}

/**
 * Data mentah berita yang dihasilkan oleh data stream
 */
data class RawNewsArticle(
    val id: String,
    val title: String,
    val category: NewsCategory,
    val rawContent: String,
    val source: String,
    val author: String,
    val timestampSeconds: Long,
    val readMinutes: Int
)

/**
 * [FITUR 3] Data hasil transformasi Flow untuk format tampilan UI
 */
data class NewsUiModel(
    val id: String,
    val displayTitle: String,
    val category: NewsCategory,
    val categoryBadge: String,
    val formattedTime: String,
    val summaryPreview: String,
    val sourceInfo: String,
    val isRead: Boolean,
    val readStatusBadge: String
)

/**
 * [FITUR 5] Data detail berita lengkap yang diambil secara Asynchronous via Coroutines
 */
data class NewsDetail(
    val id: String,
    val title: String,
    val category: NewsCategory,
    val author: String,
    val source: String,
    val fullContent: String,
    val publishedAt: String,
    val readTimeMinutes: Int,
    val tags: List<String>
)

/**
 * State penanganan UI untuk pemuatan detail berita async
 */
sealed interface DetailUiState {
    data object Idle : DetailUiState
    data class Loading(val articleId: String) : DetailUiState
    data class Success(val detail: NewsDetail) : DetailUiState
    data class Error(val message: String) : DetailUiState
}
