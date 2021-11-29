package com.backend.backend.Controller;

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
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import com.narcano.jni.CaffParser;
import com.narcano.jni.CIFF;
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
    public ResponseEntity<Void> addCaff(@Valid @RequestParam @NotNull MultipartFile caffFile, @RequestParam @NotNull String title) throws IOException {
        Optional<User> user = ur.findByUsername( ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
        if(!caffFile.isEmpty()) {
            String root  = System.getProperty("user.dir");
            String path = root + "/src/main/java/com/backend/backend/CaffFileDirectory/" + caffFile.getOriginalFilename() + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss-")); ;
            File dest = new File(path);
            caffFile.transferTo(dest);
            cr.save(new Caff(title,path, new ArrayList<>(), user.get()));
            lr.save(new Log("Added Caff by user: " + user.get().getUsername() + " with title: " + title,java.time.LocalDateTime.now().toString()));
            return ResponseEntity.ok().build();
        }
        lr.save(new Log("Failed to add Caff by user: " + user.get().getUsername() + " with title: " + title + ",due to no file in params",java.time.LocalDateTime.now().toString()));
        return  ResponseEntity.badRequest().build();
    }


    @GetMapping("/searchCaff")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_USER')")
    public ResponseEntity<List<GetAllCaffResponse>> getCaffLike(@Valid @RequestBody @NotNull SearchRequest title) {
        List<Caff> caffs = cr.findByTitleContaining(title.getTitle());
        List<GetAllCaffResponse> responses = parseCaffRequest(caffs);
        lr.save(new Log("User: " + ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername() + " searched for caff with title " + title.getTitle(),java.time.LocalDateTime.now().toString()));
        return  ResponseEntity.ok(responses);
    }

    @GetMapping("/downloadCaff/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_USER')")
    public ResponseEntity<File> downloadCaff(@PathVariable UUID id) {
        Optional<Caff> caff = cr.findById(id);
        if(caff.isEmpty()) {
            lr.save(new Log("User: " + ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername() + " gave wrong id to download" + id,java.time.LocalDateTime.now().toString()));
            return ResponseEntity.badRequest().build();
        }
        else {
            lr.save(new Log("User: " + ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername() + " downloaded Caff wit id" + id,java.time.LocalDateTime.now().toString()));
            String path = caff.get().getCaffFile();
            File file = new File(path);

            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + file.getName() + "\"").body(file);
        }
    }

    @GetMapping("/getAllCaff")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_USER')")
    public ResponseEntity<List<GetAllCaffResponse>> getAllCaff() {
        List<Caff> caffs = cr.findAll();
        List<GetAllCaffResponse> responses = parseCaffRequest(caffs);
        lr.save(new Log("User: " + ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername() + " getAll caffs",java.time.LocalDateTime.now().toString()));
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> updateCaff(@Valid @RequestBody @NotNull UpdateCaffRequest updateCaffRequest, @PathVariable UUID id) {
        Optional<Caff> caff = cr.findById(id);
        if(caff.isEmpty()) {
            lr.save(new Log("Caff with id: " + id + " failed to update by admin: " +  ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername() + ", due to wrong id",java.time.LocalDateTime.now().toString()));
            return ResponseEntity.badRequest().build();
        }
        caff.get().setTitle(updateCaffRequest.getTitle());
        cr.save(caff.get());
        lr.save(new Log("Caff with id: " + id + " updated by admin: " +  ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername(),java.time.LocalDateTime.now().toString()));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteCaff(@PathVariable UUID id) {
        Optional<Caff> caff = cr.findById(id);
        if(caff.isEmpty()) {
            lr.save(new Log("Failed to delete Caff with id: " + id + " by admin: " + ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername() + ", due to wrong id", java.time.LocalDateTime.now().toString()));
            return ResponseEntity.badRequest().build();
        }
        cr.delete(caff.get());
        File file = new File(caff.get().getCaffFile());
        file.delete();
        lr.save(new Log("Deleted Caff with id: " + id + " by admin: " + ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername(), java.time.LocalDateTime.now().toString()));
        return ResponseEntity.ok().build();
    }

    private List<GetAllCaffResponse> parseCaffRequest(List<Caff> caffs) {
        List<GetAllCaffResponse> responses = new ArrayList<>();
        for(int i=0; i<caffs.size(); i++) {
            CIFF[] ciffs = new CaffParser().CallParser(caffs.get(i).getCaffFile());
            if(ciffs[0].duration == -1) {
                lr.save(new Log("Caff file does not exists with id: " + caffs.get(i), java.time.LocalDateTime.now().toString()));
                cr.delete(caffs.get(i));
            }
            else if(ciffs[0].duration == -2) {
                lr.save(new Log("Caff is not well formatted with id: " + caffs.get(i) + " uploaded by: " + caffs.get(i).getUser().getUsername(), java.time.LocalDateTime.now().toString()));
                File file = new File(caffs.get(i).getCaffFile());
                file.delete();
                cr.delete(caffs.get(i));
            } else {
                GetAllCaffResponse caffResponse = new GetAllCaffResponse(caffs.get(i).getId(), ciffs, caffs.get(i).getUser().getUsername(), new ArrayList<>(), caffs.get(i).getTitle());
                for (int j = 0; j < caffs.get(i).getComments().size(); j++) {
                    caffResponse.getComments().add(new GetCommentResponse(caffs.get(i).getComments().get(j).getId(), caffs.get(i).getComments().get(j).getText(), caffs.get(i).getComments().get(j).getUser().getUsername()));
                }
                responses.add(caffResponse);
            }
        }
        return responses;
    }
}
