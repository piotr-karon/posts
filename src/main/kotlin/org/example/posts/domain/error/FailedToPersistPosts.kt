package org.example.posts.domain.error

class FailedToPersistPosts(msg: String, cause: Throwable? = null): RuntimeException(msg, cause)