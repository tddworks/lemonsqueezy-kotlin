package com.tddworks.lemonsqueezy.api.user

import com.tddworks.lemonsqueezy.api.internal.network.ktor.HttpRequester
import com.tddworks.lemonsqueezy.api.user.data.UserAttributes
import com.tddworks.lemonsqueezy.api.user.response.UserResponse
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.util.reflect.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.kotlin.argThat
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class UsersApiTest {
    private var requester: HttpRequester = mock()

    @Test
    fun `should return me when success`() = runTest {
        val httpRequestCaptor = argumentCaptor<HttpRequestBuilder.() -> Unit>()
        val expectedResponse = UserResponse(
            type = "users",
            id = "some-user-id",
            attributes = UserAttributes(
                name = "some-user-name",
                email = "some-user-email",
                color = "#898FA9",
                avatarUrl = "some-avatar-url",
                hasCustomAvatar = false,
                createdAt = "2021-05-24T14:08:31.000000Z",
                updatedAt = "2021-08-26T13:24:54.000000Z"
            )
        )

        whenever(
            requester.performRequest<UserResponse>(
                info = argThat<TypeInfo> {
                    this.type == UserResponse::class
                },
                builder = httpRequestCaptor.capture()
            )
        ).thenReturn(expectedResponse)

        val result = LemonSqueezyUsersApi(requester).me()

        val httpRequestBuilder = HttpRequestBuilder()
        httpRequestCaptor.firstValue.invoke(httpRequestBuilder)

        assertEquals(expectedResponse, result)
        assertEquals("application/vnd.api+json", httpRequestBuilder.contentType().toString())
        assertEquals(HttpMethod.Get, httpRequestBuilder.method)
    }
}