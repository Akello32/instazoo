package by.matmux.instazoo.service;

import by.matmux.instazoo.dao.ImageModelDao;
import by.matmux.instazoo.dao.PostDao;
import by.matmux.instazoo.dao.UserDao;
import by.matmux.instazoo.dto.PostDTO;
import by.matmux.instazoo.entity.ImageModel;
import by.matmux.instazoo.entity.Post;
import by.matmux.instazoo.entity.User;
import by.matmux.instazoo.exceptions.PostNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    private static final Logger LOG = LoggerFactory.getLogger(PostService.class);

    private final PostDao postDao;
    private final UserDao userDao;
    private ImageModelDao imageModelDao;

    @Autowired
    public PostService(PostDao postDao, UserDao userDao, ImageModelDao imageModelDao) {
        this.postDao = postDao;
        this.userDao = userDao;
        this.imageModelDao = imageModelDao;
    }

    public Post createPost(PostDTO postDTO, Principal principal) {
        User user = getUserByPrincipal(principal);
        Post post = new Post();
        post.setUser(user);
        post.setCaption(postDTO.getCaption());
        post.setLocation(postDTO.getLocation());
        post.setTitle(postDTO.getTitle());
        post.setLikes(0);

        LOG.info("Saving Post for User " + user.getEmail());

        return postDao.save(post);
    }

    public List<Post> getAllPosts() {
        return postDao.findAllByOrderByCreatedDateDesc();
    }

    public Post getPostById(Long postId, Principal principal) {
        User user = getUserByPrincipal(principal);
        return postDao.findPostByIdAndUser(postId, user)
                .orElseThrow(() -> new PostNotFoundException("Post cannot be found for username " + user.getEmail()));
    }

    public List<Post> getAllPostsForUser(Principal principal) {
        User user = getUserByPrincipal(principal);
        return postDao.findAllByUserOrderByCreatedDateDesc(user);
    }

    public Post likePost(Long postId, String username) {
        Post post = postDao.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post cannot be found"));

        Optional<String> userLiked = post.getLikedUsers()
                .stream()
                .filter(u -> u.equals(username))
                .findAny();

        if (userLiked.isPresent()) {
            post.setLikes(post.getLikes() - 1);
            post.getLikedUsers().remove(username);
        } else {
            post.setLikes(post.getLikes() + 1);
            post.getLikedUsers().add(username);
        }

        return postDao.save(post);
    }

    public void deletePost(Long postId, Principal principal) {
        Post post = getPostById(postId, principal);
        Optional<ImageModel> imageModel = imageModelDao.findByPostId(post.getId());
        postDao.delete(post);
        imageModel.ifPresent(imageModelDao::delete);
    }

    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userDao.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found with username " + username));
    }
}
