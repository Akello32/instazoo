package by.matmux.instazoo.service;

import by.matmux.instazoo.dao.CommentDao;
import by.matmux.instazoo.dao.PostDao;
import by.matmux.instazoo.dao.UserDao;
import by.matmux.instazoo.dto.CommentDTO;
import by.matmux.instazoo.entity.Comment;
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
public class CommentService {
    private static final Logger LOG = LoggerFactory.getLogger(CommentService.class);

    private final CommentDao commentDao;
    private final PostDao postDao;
    private final UserDao userDao;

    @Autowired
    public CommentService(CommentDao commentDao, PostDao postDao, UserDao userDao) {
        this.commentDao = commentDao;
        this.postDao = postDao;
        this.userDao = userDao;
    }

    public Comment saveComment(Long postId, CommentDTO commentDTO, Principal principal) {
        User user = getUserByPrincipal(principal);
        Post post = postDao.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post cannot be found for username " + user.getEmail()));
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUserId(user.getId());
        comment.setUsername(user.getUsername());
        comment.setMessage(commentDTO.getMessage());

        LOG.info("Saving comment for Post: {}", post.getId());
        return commentDao.save(comment);
    }

    public List<Comment> getAllCommentsForPost(Long postId) {
        Post post = postDao.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post cannot be found"));
        return commentDao.findAllByPost(post);
    }

    public void deleteComment(Long commentId) {
        Optional<Comment> comment = commentDao.findById(commentId);
        comment.ifPresent(commentDao::delete);
    }

    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userDao.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found with username " + username));
    }
}
