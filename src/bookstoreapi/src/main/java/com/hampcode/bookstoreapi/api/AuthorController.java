package com.hampcode.bookstoreapi.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hampcode.bookstoreapi.model.Author;
import com.hampcode.bookstoreapi.repository.AuthorRepository;

@RestController // verbos HTTP: GET-POST-PUT-DELETE
@RequestMapping("/api/v1/authors")
public class AuthorController {
    // TODO: Dependencia El AuthorController recibe la dependencia del
    // AuthorRepository
    private final AuthorRepository authorRepository;

    public AuthorController(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @GetMapping
    public ResponseEntity<List<Author>> getAllAuthor() {
        return new ResponseEntity<>(authorRepository.findAll(),
                HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Author> registerAuthor(@RequestBody Author author) {
        return new ResponseEntity<>(authorRepository.save(author),
                HttpStatus.CREATED);
    }

}
