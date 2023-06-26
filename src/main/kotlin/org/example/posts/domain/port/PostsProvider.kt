package org.example.posts.domain.port

import org.example.posts.domain.Post

interface PostsProvider {
    fun getAllPosts(): List<Post>
}