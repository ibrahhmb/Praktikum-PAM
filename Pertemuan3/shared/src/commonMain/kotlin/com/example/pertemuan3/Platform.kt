package com.example.pertemuan3

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform