package org.example.posts.domain

import org.example.posts.domain.port.PostsProvider
import org.example.posts.domain.port.PostsSaver

class PostsService(
    private val postsProvider: PostsProvider,
    private val postsSaver: PostsSaver
) {
    fun pullAndPersistPosts() {
        val allPosts = postsProvider.getAllPosts()
        postsSaver.upsertAll(allPosts)
    }
}