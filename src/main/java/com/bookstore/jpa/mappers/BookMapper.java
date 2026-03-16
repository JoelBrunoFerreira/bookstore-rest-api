package com.bookstore.jpa.mappers;

import com.bookstore.jpa.dtos.BookRequestDto;
import com.bookstore.jpa.dtos.BookResponseDto;
import com.bookstore.jpa.entities.Author;
import com.bookstore.jpa.entities.Book;
import com.bookstore.jpa.entities.Publisher;
import com.bookstore.jpa.entities.Review;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class BookMapper {

    public Book toEntity(BookRequestDto dto, Publisher publisher, Set<Author> authors) {
        Book book = new Book();
        book.setTitle(dto.title());
        book.setPublisher(publisher);
        book.setAuthors(authors);

        Review review = new Review();
        review.setComment(dto.reviewComment());
        review.setBook(book);
        book.setReview(review);

        return book;
    }

    public BookResponseDto toResponseDto(Book book) {
        return new BookResponseDto(
                book.getId(),
                book.getTitle(),
                book.getPublisher().getName(),
                book.getAuthors().stream()
                        .map(Author::getName)
                        .collect(Collectors.toSet()),
                book.getReview().getComment()
        );
    }
}
