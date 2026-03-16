package com.bookstore.jpa.dtos;

import java.util.Set;
import java.util.UUID;

public record BookDto(
        String title,
        UUID publisherId,
        Set<UUID> authorsId,
        String reviewComment) {

}
