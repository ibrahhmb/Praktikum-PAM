package com.example.pertemuan2

import com.example.pertemuan2.data.NewsRepository
import com.example.pertemuan2.model.DetailUiState
import com.example.pertemuan2.model.NewsCategory
import com.example.pertemuan2.model.RawNewsArticle
import com.example.pertemuan2.viewmodel.NewsFeedViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class NewsFeedSimulatorTest {

    private val repository = NewsRepository()

    /**
     * [FITUR 1] Pengujian Flow yang mensimulasikan data berita baru
     */
    @Test
    fun testNewsStreamFlowEmitsArticles() = runTest {
        val articles = repository.getNewsStream().take(2).toList()
        assertEquals(2, articles.size)
        assertNotNull(articles[0].id)
        assertNotNull(articles[1].id)
    }

    /**
     * [FITUR 2 & FITUR 3] Pengujian Filter Kategori & Transformasi Tampilan UI
     */
    @Test
    fun testFilteringAndTransformation() = runTest {
        val viewModel = NewsFeedViewModel(repository)

        // Ubah filter ke kategori TEKNOLOGI
        viewModel.selectCategory(NewsCategory.TEKNOLOGI)
        assertEquals(NewsCategory.TEKNOLOGI, viewModel.selectedCategory.value)

        // Filter kembali ke ALL
        viewModel.selectCategory(NewsCategory.ALL)
        assertEquals(NewsCategory.ALL, viewModel.selectedCategory.value)
    }

    /**
     * [FITUR 4] Pengujian StateFlow untuk menyimpan jumlah berita yang sudah dibaca
     */
    @Test
    fun testReadCountStateFlow() = runTest {
        val viewModel = NewsFeedViewModel(repository)

        // Awalnya jumlah dibaca harus 0
        assertEquals(0, viewModel.readCount.value)

        // Tandai berita 1 dibaca
        viewModel.markAsRead("tech_1")
        assertEquals(1, viewModel.readCount.value)

        // Tandai berita 2 dibaca
        viewModel.markAsRead("biz_1")
        assertEquals(2, viewModel.readCount.value)

        // Menandai berita yang sama tidak boleh menambah count (idempotent)
        viewModel.markAsRead("tech_1")
        assertEquals(2, viewModel.readCount.value)

        // Reset berita mengembalikan count ke 0
        viewModel.resetNews()
        assertEquals(0, viewModel.readCount.value)
    }

    /**
     * [FITUR 5] Pengujian Coroutines untuk mengambil detail berita secara async
     */
    @Test
    fun testFetchDetailAsyncWithCoroutine() = runTest {
        val rawArticle = RawNewsArticle(
            id = "test_1",
            title = "Pengujian Async Coroutines",
            category = NewsCategory.TEKNOLOGI,
            rawContent = "Konten singkat pengujian coroutine.",
            source = "TestNews",
            author = "Penguji",
            timestampSeconds = 10L,
            readMinutes = 2
        )

        // Panggil suspend function
        val detail = repository.fetchNewsDetailAsync(rawArticle)

        assertEquals("test_1", detail.id)
        assertEquals("Pengujian Async Coroutines", detail.title)
        assertEquals("Penguji", detail.author)
        assertTrue(detail.fullContent.contains("Konten singkat pengujian"))
        assertTrue(detail.tags.contains("Teknologi"))
    }
}
