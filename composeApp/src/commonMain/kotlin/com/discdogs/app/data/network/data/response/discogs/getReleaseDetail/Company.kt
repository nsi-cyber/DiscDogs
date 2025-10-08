package com.discdogs.app.data.network.data.response.discogs.getReleaseDetail


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Company(
    @SerialName("catno")
    val catNo: String? = null,
    @SerialName("entity_type")
    val entityType: String? = null,
    @SerialName("entity_type_name")
    val entityTypeName: String? = null,
    @SerialName("id")
    val id: Int? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("resource_url")
    val resourceUrl: String? = null
)