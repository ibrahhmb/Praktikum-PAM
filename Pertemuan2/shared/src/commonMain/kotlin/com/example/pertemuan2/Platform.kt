package com.example.pertemuan2

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform