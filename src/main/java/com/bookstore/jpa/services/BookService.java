package com.bookstore.jpa.services;

import com.bookstore.jpa.dtos.BookRequestDto;
import com.bookstore.jpa.dtos.BookResponseDto;
import com.bookstore.jpa.entities.Author;
import com.bookstore.jpa.entities.Book;
import com.bookstore.jpa.entities.Publisher;
import com.bookstore.jpa.mappers.BookMapper;
import com.bookstore.jpa.repositories.AuthorRepository;
import com.bookstore.jpa.repositories.BookRepository;
import com.bookstore.jpa.repositories.PublisherRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookService implements IBookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final PublisherRepository publisherRepository;
    private final BookMapper bookMapper;

    public BookService(BookRepository bookRepository,
                       AuthorRepository authorRepository,
                       PublisherRepository publisherRepository,
                       BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.publisherRepository = publisherRepository;
        this.bookMapper = bookMapper;
    }

    public List<BookResponseDto> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(bookMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public BookResponseDto saveBook(BookRequestDto dto) {
        Publisher publisher = publisherRepository.findById(dto.publisherId())
                .orElseThrow(() -> new RuntimeException("Publisher not found"));

        Set<Author> authors = new HashSet<>(authorRepository.findAllById(dto.authorsId()));

        Book book = bookMapper.toEntity(dto, publisher, authors);

        return bookMapper.toResponseDto(bookRepository.save(book));
    }

    @Transactional
    public void deleteBook(UUID id) {
        bookRepository.deleteById(id);
    }
}
