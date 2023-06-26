package org.example.posts.domain.port

import org.example.posts.domain.Post
import java.nio.file.Path

interface PostsSaver {
    fun upsertAll(posts: Collection<Post>, outputDirectory: Path? = null)
}