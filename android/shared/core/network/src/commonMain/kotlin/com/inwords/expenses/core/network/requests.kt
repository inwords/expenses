package com.inwords.expenses.core.network

import com.inwords.expenses.core.utils.Result
import com.inwords.expenses.core.utils.SuspendLazy
import io.ktor.client.HttpClient
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.http.URLBuilder
import io.ktor.serialization.ContentConvertException
import kotlinx.io.IOException

suspend inline fun <T : Any> SuspendLazy<HttpClient>.requestWithExceptionHandling(
    block: HttpClient.() -> T
): NetworkResult<T> {
    return try {
        NetworkResult.Ok(value().block())
    } catch (e: ClientRequestException) {
        NetworkResult.Error.Http.Client(e)
    } catch (e: ServerResponseException) {
        NetworkResult.Error.Http.Server(e)
    } catch (e: RedirectResponseException) {
        NetworkResult.Error.Http.Redirect(e)
    } catch (e: IOException) {
        NetworkResult.Error.Transport(e)
    } catch (e: ContentConvertException) {
        NetworkResult.Error.Parse(e)
    }
}

fun <T : Any> NetworkResult<T>.toBasicResult(): Result<T> {
    return when (this) {
        is NetworkResult.Ok -> Result.Success(data)
        is NetworkResult.Error -> Result.Error
    }
}

inline fun HttpRequestBuilder.url(config: HostConfig, crossinline block: URLBuilder.(URLBuilder) -> Unit) {
    url {
        protocol = config.protocol
        host = config.host
        block(this)
    }
}