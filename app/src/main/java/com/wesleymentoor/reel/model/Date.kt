package com.wesleymentoor.reel.model

data class Date(
    val id: String,
    val time: String,
    val title: String,
    val pending: Boolean,
    val description: String,
    val cancelled: Boolean
)
