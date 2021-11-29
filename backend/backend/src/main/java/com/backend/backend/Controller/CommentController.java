package com.backend.backend.Controller;

import com.backend.backend.Communication.Request.AddCommentRequest;
import com.backend.backend.Communication.Request.UpdateCommentRequest;
import com.backend.backend.Communication.Response.GetAllCaffResponse;
import com.backend.backend.Communication.Response.GetCommentResponse;
import com.backend.backend.Data.Caff;
import com.backend.backend.Data.Comment;
import com.backend.backend.Data.Log;
import com.backend.backend.Data.User;
import com.backend.backend.Repository.CaffRepository;
import com.backend.backend.Repository.CommentRepository;
import com.backend.backend.Repository.LogRepository;
import com.backend.backend.Repository.UserRepository;
import com.backend.backend.Security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/api/comment")
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CaffRepository caffRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LogRepository logRepository;


    @PostMapping("/addComment/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_USER')")
    public ResponseEntity<Void> addComment(@Valid @RequestBody @NotNull AddCommentRequest addCommentRequest, @PathVariable UUID id) {
        Optional<User> user = userRepository.findByUsername( ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
        Optional<Caff> caff = caffRepository.findById(id);
        if(caff.isEmpty() || user.isEmpty()) {
            logRepository.save(new Log("User with name: " + user.get().getUsername() + " failed to comment on Caff with id: " + id + " with text: " + addCommentRequest.getText() + " due to wrong id", java.time.LocalDateTime.now().toString()));
            return ResponseEntity.badRequest().build();
        }
        commentRepository.save(new Comment(addCommentRequest.getText(), caff.get(), user.get()));
        logRepository.save(new Log("User with name: " + user.get().getUsername() + " commented on Caff with id: " + id + " with text: " + addCommentRequest.getText(), java.time.LocalDateTime.now().toString()));
        return  ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/getComments")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_USER')")
    public ResponseEntity<List<GetCommentResponse>> getCaffComments(@PathVariable UUID id) {
        Optional<Caff> caff = caffRepository.findById(id);
        List<GetCommentResponse> result = new ArrayList<>();
        if(caff.isEmpty() || !caff.isPresent()) {
            logRepository.save(new Log("User: " + ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername() + " gave wrong caff id" + id,java.time.LocalDateTime.now().toString()));
            return ResponseEntity.badRequest().build();
        } else {
            for (Comment comment: caff.get().getComments()) {
                result.add(new GetCommentResponse(comment.getId(),comment.getText(),comment.getUser().getUsername()));
            }
        }
       return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> updateComment(@Valid @RequestBody @NotNull UpdateCommentRequest updateCommentRequest, @PathVariable UUID id) {
        Optional<Comment> comment = commentRepository.findById(id);
        if(comment.isEmpty())  {
            logRepository.save(new Log("Failed to update comment with id: " + id + " by admin: " + ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername() + " with text " + updateCommentRequest.getText() + ", due to wrong id ", java.time.LocalDateTime.now().toString()));
            return ResponseEntity.badRequest().build();
        }
        comment.get().setText(updateCommentRequest.getText());
        commentRepository.save(comment.get());
        logRepository.save(new Log("Updated comment with id: " + id + " by admin: " + ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername() + " with text " + updateCommentRequest.getText(), java.time.LocalDateTime.now().toString()));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteComment(@PathVariable UUID id) {
        commentRepository.delete(commentRepository.getById(id));
        logRepository.save(new Log("Deleted comment with id: " + id + " by admin: " + ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername(), java.time.LocalDateTime.now().toString()));
        return ResponseEntity.ok().build();
    }
}
