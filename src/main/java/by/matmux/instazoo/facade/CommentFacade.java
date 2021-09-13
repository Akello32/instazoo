package by.matmux.instazoo.facade;

import by.matmux.instazoo.dto.CommentDTO;
import by.matmux.instazoo.entity.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentFacade {
    public CommentDTO commentToCommentDTO(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();

        commentDTO.setId(comment.getId());
        commentDTO.setMessage(comment.getMessage());
        commentDTO.setUsername(comment.getUsername());

        return commentDTO;
    }
}
