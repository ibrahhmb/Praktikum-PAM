package com.example.pertemuan2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pertemuan2.data.NewsRepository
import com.example.pertemuan2.model.DetailUiState
import com.example.pertemuan2.model.NewsCategory
import com.example.pertemuan2.model.NewsUiModel
import com.example.pertemuan2.model.RawNewsArticle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewsFeedViewModel(
    private val repository: NewsRepository = NewsRepository()
) : ViewModel() {

    // Status simulasi running/pause
    private val _isSimulationRunning = MutableStateFlow(true)
    val isSimulationRunning: StateFlow<Boolean> = _isSimulationRunning.asStateFlow()

    // Kumpulan artikel mentah yang diterima dari Flow stream
    private val _rawArticles = MutableStateFlow<List<RawNewsArticle>>(emptyList())

    // [FITUR 2] Filter berita berdasarkan kategori tertentu
    private val _selectedCategory = MutableStateFlow(NewsCategory.ALL)
    val selectedCategory: StateFlow<NewsCategory> = _selectedCategory.asStateFlow()

    // [FITUR 4] StateFlow untuk menyimpan jumlah berita yang sudah dibaca
    private val _readCount = MutableStateFlow(0)
    val readCount: StateFlow<Int> = _readCount.asStateFlow()

    // Set ID berita yang telah dibaca
    private val _readArticleIds = MutableStateFlow<Set<String>>(emptySet())
    val readArticleIds: StateFlow<Set<String>> = _readArticleIds.asStateFlow()

    // [FITUR 5] State Coroutines untuk mengambil detail berita secara async
    private val _detailUiState = MutableStateFlow<DetailUiState>(DetailUiState.Idle)
    val detailUiState: StateFlow<DetailUiState> = _detailUiState.asStateFlow()

    /**
     * [FITUR 2 & FITUR 3]
     * Menggabungkan data mentah, kategori filter, dan status baca menggunakan Flow operator (combine & map)
     * Mengubah (Transform) RawNewsArticle menjadi NewsUiModel yang siap ditampilkan pada UI
     */
    val displayedNews: StateFlow<List<NewsUiModel>> = combine(
        _rawArticles,
        _selectedCategory,
        _readArticleIds
    ) { articles, category, readIds ->
        // [FITUR 2] Filter berita berdasarkan kategori yang dipilih
        val filteredList = if (category == NewsCategory.ALL) {
            articles
        } else {
            articles.filter { it.category == category }
        }

        // [FITUR 3] Transform data mentah menjadi format UI model yang ditampilkan
        filteredList.map { raw ->
            val isRead = readIds.contains(raw.id)
            transformToUiModel(raw, isRead)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        // [FITUR 1] Mulai mengumpulkan Flow simulasi berita baru setiap 2 detik
        startSimulatedNewsFlow()
    }

    /**
     * [FITUR 1] Menjalankan coroutine untuk menerima Flow berita baru setiap 2 detik
     */
    private fun startSimulatedNewsFlow() {
        viewModelScope.launch {
            repository.getNewsStream().collect { newArticle ->
                if (_isSimulationRunning.value) {
                    // Masukkan berita terbaru di urutan teratas
                    _rawArticles.update { current ->
                        listOf(newArticle) + current
                    }
                }
            }
        }
    }

    /**
     * [FITUR 2] Mengganti kategori filter berita
     */
    fun selectCategory(category: NewsCategory) {
        _selectedCategory.value = category
    }

    /**
     * [FITUR 3] Fungsi transformasi data menjadi format tampilan yang informatif
     */
    private fun transformToUiModel(raw: RawNewsArticle, isRead: Boolean): NewsUiModel {
        val snippet = if (raw.rawContent.length > 75) {
            raw.rawContent.take(72) + "..."
        } else {
            raw.rawContent
        }

        return NewsUiModel(
            id = raw.id,
            displayTitle = raw.title,
            category = raw.category,
            categoryBadge = "${raw.category.emoji} ${raw.category.displayName}",
            formattedTime = "Terkirim +${raw.timestampSeconds} detik lalu",
            summaryPreview = snippet,
            sourceInfo = "Sumber: ${raw.source} • Estimasi ${raw.readMinutes} mnt baca",
            isRead = isRead,
            readStatusBadge = if (isRead) "✓ Sudah Dibaca" else "Baru"
        )
    }

    /**
     * [FITUR 5] Coroutines untuk mengambil detail berita secara async
     * Menjalankan viewModelScope.launch dengan suspend function
     */
    fun loadArticleDetail(articleId: String) {
        val raw = _rawArticles.value.find { it.id == articleId } ?: return

        viewModelScope.launch {
            // Pasang state Loading
            _detailUiState.value = DetailUiState.Loading(articleId)

            try {
                // Async network simulation
                val detail = repository.fetchNewsDetailAsync(raw)
                _detailUiState.value = DetailUiState.Success(detail)

                // [FITUR 4] Tandai berita sudah dibaca dan update StateFlow readCount
                markAsRead(articleId)
            } catch (e: Exception) {
                _detailUiState.value = DetailUiState.Error(e.message ?: "Gagal mengambil detail berita")
            }
        }
    }

    /**
     * Menutup modal dialog detail berita
     */
    fun dismissDetailDialog() {
        _detailUiState.value = DetailUiState.Idle
    }

    /**
     * [FITUR 4] Menambahkan berita ke daftar baca dan mengupdate StateFlow jumlah berita dibaca
     */
    fun markAsRead(articleId: String) {
        if (!_readArticleIds.value.contains(articleId)) {
            _readArticleIds.update { it + articleId }
            // StateFlow menyimpan jumlah berita yang sudah dibaca
            _readCount.value = _readArticleIds.value.size
        }
    }

    /**
     * Mengontrol jeda (pause) / lanjut (resume) simulasi berita
     */
    fun toggleSimulation() {
        _isSimulationRunning.update { !it }
    }

    /**
     * Reset semua berita yang telah terkumpul
     */
    fun resetNews() {
        _rawArticles.value = emptyList()
        _readArticleIds.value = emptySet()
        _readCount.value = 0
    }
}
