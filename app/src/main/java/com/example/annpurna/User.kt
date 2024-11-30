package com.example.annpurna

// Data class for storing user details
data class User(
    val name: String,
    val email: String,
    val contactNumber: String,
    val password: String,
    val address: String? = null, // Optional address field
    val pincode: String? = null,
    val state: String? = null,
    val city: String? = null
)
