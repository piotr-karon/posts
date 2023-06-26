package org.example.posts.application

import org.example.posts.domain.PostsService

class PostsFacade(
    private val postsService: PostsService
) {

    fun pullAndSavePosts() {
        postsService.pullAndPersistPosts()
    }
}