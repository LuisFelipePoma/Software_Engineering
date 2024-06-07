package com.hampcode.bookstoreapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hampcode.bookstoreapi.model.Author;

//CREATE - READ - UPDATE - DELETE ( CRUD)
//@Query
public interface AuthorRepository extends JpaRepository<Author, Long> {

}
