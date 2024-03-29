package com.tddworks.lemonsqueezy.api.license

import com.tddworks.lemonsqueezy.api.internal.network.ktor.HttpRequester
import com.tddworks.lemonsqueezy.api.license.data.Instance
import com.tddworks.lemonsqueezy.api.license.data.LicenseKey
import com.tddworks.lemonsqueezy.api.license.data.Meta
import com.tddworks.lemonsqueezy.api.license.request.LicenseActivationRequest
import com.tddworks.lemonsqueezy.api.license.request.LicenseDeactivationRequest
import com.tddworks.lemonsqueezy.api.license.response.LicenseActivationErrorResponse
import com.tddworks.lemonsqueezy.api.license.response.LicenseActivationSuccessResponse
import com.tddworks.lemonsqueezy.api.license.response.LicenseDeactivationResponse
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.util.reflect.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.argThat
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.whenever

class LicenseKeysApiTest {
    private var requester: HttpRequester = mock()

    @Test
    fun `should return deactivation success when deactivated`() = runTest {
        val expectedResponse = LicenseDeactivationResponse(
            deactivated = true,
            error = null,
            licenseKey = LicenseKey(
                id = 1,
                status = "active",
                key = "some-license-key",
                activationLimit = 1,
                activationUsage = 5,
                createdAt = "2021-03-25 11:10:18",
                expiresAt = null
            ),
            meta = Meta(
                storeId = 1,
                orderId = 2,
                orderItemId = 3,
                productId = 4,
                productName = "Example Product",
                variantId = 5,
                variantName = "Default",
                customerId = 6,
                customerName = "some-customer-name",
                customerEmail = "some-customer-email"
            )
        )

        val api = LemonSqueezyLicenseKeysApi(requester)
        val licenseKey = "your_license_key"
        val instanceId = "your_instance_id"

        val httpRequestCaptor = argumentCaptor<HttpRequestBuilder.() -> Unit>()

        whenever(
            requester.performRequest<LicenseDeactivationResponse>(
                info = argThat<TypeInfo> {
                    this.type == LicenseDeactivationResponse::class
                },
                builder = httpRequestCaptor.capture()
            )
        ).thenReturn(expectedResponse)

        val result = api.deactivateLicense(licenseKey, instanceId)

        val httpRequestBuilder = HttpRequestBuilder()
        httpRequestCaptor.firstValue.invoke(httpRequestBuilder)

        assertEquals(httpRequestBuilder.method, HttpMethod.Post)
        assertEquals(httpRequestBuilder.url.encodedPath, "/v1/licenses/deactivate")
        assertEquals(httpRequestBuilder.body, LicenseDeactivationRequest(licenseKey, instanceId))

        assertEquals(expectedResponse, result)
    }

    @Test
    fun `should return activation error when not activated`() = runTest {
        val responseJson = """
            {
                "activated": false,
                "error": "some-error",
                "license_key": {
                    "id": 1,
                    "status": "active",
                    "key": "some-license-key",
                    "activation_limit": 1,
                    "activation_usage": 5,
                    "created_at": "2021-03-25 11:10:18",
                    "expires_at": null
                },
                "meta": {
                    "store_id": 1,
                    "order_id": 2,
                    "order_item_id": 3,
                    "product_id": 4,
                    "product_name": "Example Product",
                    "variant_id": 5,
                    "variant_name": "Default",
                    "customer_id": 6,
                    "customer_name": "some-customer-name",
                    "customer_email": "some-customer-email"
                }
            }
        """.trimIndent()


        val expectedResponse = LicenseActivationErrorResponse(
            activated = false,
            error = "some-error",
            licenseKey = LicenseKey(
                id = 1,
                status = "active",
                key = "some-license-key",
                activationLimit = 1,
                activationUsage = 5,
                createdAt = "2021-03-25 11:10:18",
                expiresAt = null
            ),
            meta = Meta(
                storeId = 1,
                orderId = 2,
                orderItemId = 3,
                productId = 4,
                productName = "Example Product",
                variantId = 5,
                variantName = "Default",
                customerId = 6,
                customerName = "some-customer-name",
                customerEmail = "some-customer-email"
            )
        )

        val api = LemonSqueezyLicenseKeysApi(requester)
        val licenseKey = "your_license_key"
        val instanceName = "your_instance_name"

        whenever(
            requester.performRequest<String>(
                info = argThat<TypeInfo> {
                    this.type == String::class
                },
                builder = any<HttpRequestBuilder.() -> Unit>()
            )
        ).thenReturn(responseJson)

        val result = api.activeLicense(licenseKey, instanceName)

        assertEquals(false, result.isSuccess())
        assertEquals(expectedResponse, result)
    }

    @Test
    fun `should return activation success when activated`() = runTest {
        val responseJson = """
            {
                "activated": true,
                "error": null,
                "license_key": {
                    "id": 1,
                    "status": "active",
                    "key": "some_license_key",
                    "activation_limit": 1,
                    "activation_usage": 5,
                    "created_at": "2021-03-25 11:10:18",
                    "expires_at": null
                },
                "instance": {
                    "id": "some-instance-id",
                    "name": "Test",
                    "created_at": "2021-04-06 14:08:46"
                },
                "meta": {
                    "store_id": 1,
                    "order_id": 2,
                    "order_item_id": 3,
                    "product_id": 4,
                    "product_name": "Example Product",
                    "variant_id": 5,
                    "variant_name": "Default",
                    "customer_id": 6,
                    "customer_name": "some-customer-name",
                    "customer_email": "some-customer-email"
                }
            }
        """.trimIndent()

        val expectedResponse = LicenseActivationSuccessResponse(
            activated = true,
            licenseKey = LicenseKey(
                id = 1,
                status = "active",
                key = "some_license_key",
                activationLimit = 1,
                activationUsage = 5,
                createdAt = "2021-03-25 11:10:18",
                expiresAt = null
            ),
            instance = Instance(
                id = "some-instance-id",
                name = "Test",
                createdAt = "2021-04-06 14:08:46"
            ),
            meta = Meta(
                storeId = 1,
                orderId = 2,
                orderItemId = 3,
                productId = 4,
                productName = "Example Product",
                variantId = 5,
                variantName = "Default",
                customerId = 6,
                customerName = "some-customer-name",
                customerEmail = "some-customer-email"
            )
        )

        val api = LemonSqueezyLicenseKeysApi(requester)
        val licenseKey = "your_license_key"
        val instanceName = "your_instance_name"

        val httpRequestCaptor = argumentCaptor<HttpRequestBuilder.() -> Unit>()

        whenever(
            requester.performRequest<String>(
                info = argThat<TypeInfo> {
                    this.type == String::class
                },
                builder = httpRequestCaptor.capture()
            )
        ).thenReturn(responseJson)

        val result = api.activeLicense(licenseKey, instanceName)

        val httpRequestBuilder = HttpRequestBuilder()
        httpRequestCaptor.firstValue.invoke(httpRequestBuilder)

        assertEquals(httpRequestBuilder.method, HttpMethod.Post)
        assertEquals(httpRequestBuilder.url.encodedPath, "/v1/licenses/activate")
        assertEquals(httpRequestBuilder.body, LicenseActivationRequest(licenseKey, instanceName))

        assertEquals(true, result.isSuccess())
        assertEquals(expectedResponse, result)
    }

}
