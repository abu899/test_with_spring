package com.example.demo.mock;

import com.example.demo.post.domain.Post;
import com.example.demo.post.service.port.PostRepository;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FakePostRepository implements PostRepository {
    private Long id = 0L;
    private final List<Post> posts = new ArrayList<>();

    @Override
    public Optional<Post> findById(long id) {
        return posts.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    @Override
    public Post save(Post post) {
        if (post.getId() == null || post.getId() == 0) {
            Post newPost = Post.builder()
                    .id(id++)
                    .content(post.getContent())
                    .createdAt(post.getCreatedAt())
                    .modifiedAt(post.getModifiedAt())
                    .writer(post.getWriter())
                    .build();
            posts.add(newPost);
            return newPost;
        } else {
            posts.removeIf(u -> u.getId().equals(post.getId()));
            posts.add(post);
            return post;
        }
    }
}
