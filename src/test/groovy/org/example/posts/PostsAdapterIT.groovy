package org.example.posts

import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import org.example.posts.adapter.PostsJSONPlaceholderAdapter
import org.example.posts.domain.error.FailedToGetPostsException
import spock.lang.Specification

class PostsAdapterIT extends Specification {

    def 'should map IOException to domain exception'() {
        given:
        TestOkHttpClient client = new TestOkHttpClient()
        def call = Mock(Call)
        client.setCallToReturn(call)
        call.execute() >> { throw new IOException() }

        def postsAdapter = new PostsJSONPlaceholderAdapter(client, "http://dummy.com")

        when:
        postsAdapter.getAllPosts()

        then:
        thrown(FailedToGetPostsException)
    }
}

class TestOkHttpClient extends OkHttpClient {
    Call callToReturn

    @Override
    Call newCall(Request request) {
        if (callToReturn == null) throw new IllegalStateException("Set callToReturn")
        return callToReturn
    }
}