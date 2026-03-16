package com.bookstore.jpa.dtos;

import java.util.Set;
import java.util.UUID;

public record BookResponseDto(
        UUID id,
        String title,
        String publisherName,
        Set<String> authorNames,
        String reviewComment) {
}