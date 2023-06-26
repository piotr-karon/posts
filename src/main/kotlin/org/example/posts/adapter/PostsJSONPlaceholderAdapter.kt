package org.example.posts.adapter

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.IOException
import org.example.posts.domain.error.FailedToGetPostsException
import org.example.posts.domain.Post
import org.example.posts.domain.port.PostsProvider

class PostsJSONPlaceholderAdapter(
    private val httpClient: OkHttpClient,
    private val baseUrl: String = "https://jsonplaceholder.typicode.com"
): PostsProvider {

    private val postsUrl = "$baseUrl/posts"

    override fun getAllPosts(): List<Post> {
        val request = Request.Builder()
            .get().url(postsUrl)
            .addHeader("Accept", "application/json")
            .build()

        return try {
            httpClient.newCall(request).execute().use { response ->
                if(!response.isSuccessful) throw FailedToGetPostsException("HTTP request failed. HTTP ${response.code}. $response")

                val body = response.body
                    ?: throw FailedToGetPostsException("No body in HTTP response")

                Json.decodeFromString<List<PostDto>>(body.string())
            }.map { it.toDomain() }
        } catch (e: IOException) {
            throw FailedToGetPostsException("Connection error", e)
        }
    }

    @Serializable
    private data class PostDto(val id: Long, val title: String, val body: String, val userId: Long)
    private fun PostDto.toDomain() = Post(id, title, body, userId)
}
