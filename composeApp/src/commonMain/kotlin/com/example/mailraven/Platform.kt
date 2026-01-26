package com.example.mailraven

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform