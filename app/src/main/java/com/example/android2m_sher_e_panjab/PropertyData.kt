package com.example.android2m_sher_e_panjab

data class Property(
    val id: String? = null,
    val userid: String?= null,
    val name: String = "",
    val location: String = "",
    val propertyType: String = "",
    val soilType: String = "",
    val area: String = "",
    val areaUnit: String = "",
    val contact: String = "",
    val price: String = "",
    val description: String = "",
    val imageUrls: List<String> = emptyList()
): java.io.Serializable