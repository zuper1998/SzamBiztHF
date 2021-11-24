package com.backend.backend.Controller;

import com.backend.backend.Communication.Request.AddCaffRequest;
import com.backend.backend.Communication.Request.SearchRequest;
import com.backend.backend.Communication.Request.UpdateCaffRequest;
import com.backend.backend.Communication.Response.GetAllCaffResponse;
import com.backend.backend.Communication.Response.GetCommentResponse;
import com.backend.backend.Data.Caff;
import com.backend.backend.Data.Log;
import com.backend.backend.Data.User;
import com.backend.backend.Repository.CaffRepository;
import com.backend.backend.Repository.LogRepository;
import com.backend.backend.Repository.UserRepository;
import com.backend.backend.Security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/caff")
public class CaffController {

    @Autowired
    private UserRepository ur;
    @Autowired
    private CaffRepository cr;
    @Autowired
    private LogRepository lr;

    @PostMapping("/addCaff")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_USER')")
    public ResponseEntity<Void> addCaff(@Valid @RequestBody @NotNull AddCaffRequest addCaffRequest) {
        Optional<User> user = ur.findByUsername( ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
        cr.save(new Caff(addCaffRequest.getTitle(), addCaffRequest.getCaffFile(), new ArrayList<>(), user.get()));
        lr.save(new Log("Added Caff by user: " + user.get().getUsername() + " with title: " + addCaffRequest.getTitle(),java.time.LocalDateTime.now().toString()));
        return  ResponseEntity.ok().build();
    }

    @GetMapping("/searchCaff")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_USER')")
    public ResponseEntity<List<Caff>> getCaffLike(@Valid @RequestBody @NotNull SearchRequest title) {
        lr.save(new Log("User: " + ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername() + " searched for caff with title " + title.getTitle(),java.time.LocalDateTime.now().toString()));
        return  ResponseEntity.ok(cr.findByTitleContaining(title.getTitle()));
    }

    //TODO: change to file and parsing
    @GetMapping("/downloadCaff/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_USER')")
    public ResponseEntity<String> downloadCaff(@PathVariable UUID id) {
        Optional<Caff> caff = cr.findById(id);
        lr.save(new Log("User: " + ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername() + " downloaded Caff wit id" + id,java.time.LocalDateTime.now().toString()));
        return caff.map(value -> ResponseEntity.ok(value.getCaffFile())).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/getAllCaff")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_USER')")
    public ResponseEntity<List<GetAllCaffResponse>> getAllCaff() {
        List<Caff> caffs = cr.findAll();
        List<GetAllCaffResponse> responses = new ArrayList<>();
        for(int i=0; i<caffs.size(); i++) {
            GetAllCaffResponse caffResponse = new GetAllCaffResponse(caffs.get(i), caffs.get(i).getUser().getUsername(), new ArrayList<>());
            for(int j=0; j<caffs.get(i).getComments().size(); j++) {
                caffResponse.getComments().add(new GetCommentResponse(caffs.get(i).getComments().get(j).getId(), caffs.get(i).getComments().get(j).getText(), caffs.get(i).getComments().get(j).getUser().getUsername()));
            }
            responses.add(caffResponse);
        }
        lr.save(new Log("User: " + ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername() + " getAll caffs",java.time.LocalDateTime.now().toString()));
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> updateCaff(@Valid @RequestBody @NotNull UpdateCaffRequest updateCaffRequest, @PathVariable UUID id) {
        Optional<Caff> caff = cr.findById(id);
        if(caff.isEmpty()) return ResponseEntity.badRequest().build();
        caff.get().setTitle(updateCaffRequest.getTitle());
        cr.save(caff.get());
        lr.save(new Log("Caff with id: " + id + " updated by admin: " +  ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername(),java.time.LocalDateTime.now().toString()));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteCaff(@PathVariable UUID id) {
        Optional<Caff> caff = cr.findById(id);
        if(caff.isEmpty()) return ResponseEntity.badRequest().build();
        cr.delete(caff.get());
        lr.save(new Log("Deleted Caff with id: " + id + " by admin: " + ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername(), java.time.LocalDateTime.now().toString()));
        return ResponseEntity.ok().build();
    }

}
