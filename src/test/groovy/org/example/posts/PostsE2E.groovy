package org.example.posts

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.example.posts.adapter.PostsFileSaver
import org.example.posts.adapter.PostsJSONPlaceholderAdapter
import org.example.posts.application.Config
import org.example.posts.application.PostsFacade
import spock.lang.Specification
import spock.lang.Subject

import java.nio.file.Files
import java.nio.file.Path

class PostsE2E extends Specification {

    private MockWebServer server

    private final def postsUrl = "/posts"
    private final def testOutputPath = Path.of("./test")

    @Subject
    private PostsFacade postsFacade

    def setup() {
        server = new MockWebServer()
        def url = server.url(postsUrl)

        postsFacade = Config.INSTANCE.postsFacade(
                new PostsJSONPlaceholderAdapter(Config.INSTANCE.httpClient, url.toString()),
                new PostsFileSaver(testOutputPath)
        )
    }

    def cleanup() {
        server.shutdown()
        testOutputPath.deleteDir()
    }

    def 'should pull posts and save them'() {
        given:
        def responseBody = """
        [
          {
            "userId": 1,
            "id": 1,
            "title": "sunt aut facere repellat provident occaecati excepturi optio reprehenderit",
            "body": "quia et suscipit\\nsuscipit recusandae consequuntur expedita et cum\\nreprehenderit molestiae ut ut quas totam\\nnostrum rerum est autem sunt rem eveniet architecto"
          },
          {
            "userId": 2,
            "id": 2,
            "title": "sunt aut facere repellat provident occaecati excepturi optio reprehenderit",
            "body": "quia et suscipit\\nsuscipit recusandae consequuntur expedita et cum\\nreprehenderit molestiae ut ut quas totam\\nnostrum rerum est autem sunt rem eveniet architecto"
          }
        ]
        """
        server.enqueue(new MockResponse().setBody(responseBody))

        when:
        postsFacade.pullAndSavePosts()

        then:
        def files = Files.list(testOutputPath).toList()
        files.size() == 2
        files.any { it.fileName.toString() == "1.json" }
        files.any { it.fileName.toString() == "2.json" }
    }
}
