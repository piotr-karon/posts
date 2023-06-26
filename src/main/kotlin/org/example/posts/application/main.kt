package org.example.posts.application

fun main() {
    val facade = Config.postsFacade()
    facade.pullAndSavePosts()
}