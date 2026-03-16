package com.bookstore.jpa.services;

import com.bookstore.jpa.dtos.BookRequestDto;
import com.bookstore.jpa.dtos.BookResponseDto;

import java.util.List;
import java.util.UUID;

public interface IBookService {
    List<BookResponseDto> getAllBooks();
    BookResponseDto saveBook(BookRequestDto dto);
    void deleteBook(UUID id);
}
