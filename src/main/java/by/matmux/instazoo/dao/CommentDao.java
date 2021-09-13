package by.matmux.instazoo.dao;

import by.matmux.instazoo.entity.Comment;
import by.matmux.instazoo.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentDao extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPost(Post post);

    Comment findByIdAndAndUserId(Long commentId, Long userId);
    }
