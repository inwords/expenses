package com.inwords.expenses.feature.events.data.network

import com.inwords.expenses.feature.events.domain.model.Person
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class Test(
    private val client: HttpClient
) {
    private val host = "https://dev-api.commonex.ru"

    suspend fun getEvent() {
        client.get("$host/event/1")
    }

    suspend fun createEvent(
        name: String,
        primaryCurrencyId: Int,
        users: List<Person>,
        pinCode: String,
    ) {
        val response = client.post("$host/event") {
            this.contentType(ContentType.Application.Json)
            this.setBody(
                CreateEventRequest(
                    name = name,
                    currencyId = primaryCurrencyId,
                    users = users.map { CreateUserDto(it.name) },
                    pinCode = pinCode,
                )
            )
        }.body<CreateEventResponse>()
        println(response.toString())
    }

}

@Serializable
data class CreateEventRequest(
    @SerialName("name")
    val name: String,

    @SerialName("currencyId")
    val currencyId: Int,

    @SerialName("users")
    val users: List<CreateUserDto>,

    @SerialName("pinCode")
    val pinCode: String
)

@Serializable
data class CreateEventResponse(
    @SerialName("name")
    val name: String,

    @SerialName("currencyId")
    val currencyId: Int,

    @SerialName("users")
    val users: List<UserDto>,

    @SerialName("pinCode")
    val pinCode: String,

    @SerialName("id")
    val id: Int
)

@Serializable
data class CreateUserDto(
    @SerialName("name")
    val name: String,
)

@Serializable
data class UserDto(
    @SerialName("name")
    val name: String,

    @SerialName("eventId")
    val eventId: Int,

    @SerialName("id")
    val id: Int
)