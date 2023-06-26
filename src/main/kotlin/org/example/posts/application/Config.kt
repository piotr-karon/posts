package org.example.posts.application

import okhttp3.OkHttpClient
import org.example.posts.adapter.PostsFileSaver
import org.example.posts.adapter.PostsJSONPlaceholderAdapter
import org.example.posts.domain.PostsService
import org.example.posts.domain.port.PostsProvider
import org.example.posts.domain.port.PostsSaver
import java.nio.file.Path
import java.time.Duration

object Config {

    val httpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .callTimeout(Duration.ofSeconds(15))
            .build()
    }

    fun postsFacade(
        postsProvider: PostsProvider = PostsJSONPlaceholderAdapter(httpClient),
        postsSaver: PostsSaver = PostsFileSaver(Path.of("./default"))
    ): PostsFacade = PostsFacade(PostsService(postsProvider, postsSaver))
}