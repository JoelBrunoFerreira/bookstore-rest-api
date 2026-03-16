package com.bookstore.jpa.repositories;

import com.bookstore.jpa.entities.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PublisherRepository extends JpaRepository<Publisher, UUID> {

}
