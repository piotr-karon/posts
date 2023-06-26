package org.example.posts.adapter

import kotlinx.serialization.Serializable
import java.nio.file.Path
import java.nio.file.StandardOpenOption.CREATE
import java.nio.file.StandardOpenOption.WRITE
import kotlin.io.path.createDirectories
import kotlin.io.path.deleteIfExists
import kotlin.io.path.writeText
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.example.posts.domain.error.FailedToPersistPosts
import org.example.posts.domain.Post
import org.example.posts.domain.port.PostsSaver
import kotlin.io.path.createFile

class PostsFileSaver(
    private val rootOutputDirectory: Path
) : PostsSaver {

    init {
        rootOutputDirectory.createDirectories()
    }

    override fun upsertAll(posts: Collection<Post>, outputDirectory: Path?) {
        val targetOutputDir = outputDirectory?.let { rootOutputDirectory.resolve(it) }
            ?: rootOutputDirectory

        targetOutputDir.createDirectories()

        try {
            posts
                .asSequence()
                .forEach { post ->
                    targetOutputDir
                        .resolve(post.toFileName())
                        .writeText(Json.encodeToString(post.toEntity()), options = arrayOf(CREATE, WRITE))
                }
        } catch (e: Exception) {
            primitiveRollback(posts, targetOutputDir)
            throw FailedToPersistPosts("Persisting posts failed", e)
        }
    }

    private fun primitiveRollback(posts: Collection<Post>, targetOutputDir: Path) {
        posts
            .asSequence()
            .forEach { post ->
                targetOutputDir.resolve(post.toFileName())
                    .deleteIfExists()
            }
    }

    @Serializable
    private data class PostEntity(val id: Long, val title: String, val body: String, val userId: Long)
    private fun Post.toFileName() = "${this.id}.json"
    private fun Post.toEntity() = PostEntity(id, title, body, userId)
}
