package org.example.posts.domain.error

class FailedToGetPostsException(msg: String, cause: Throwable? = null): RuntimeException(msg, cause)