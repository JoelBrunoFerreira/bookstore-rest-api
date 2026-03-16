package com.bookstore.jpa.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;
import java.util.UUID;

public record BookRequestDto(
        @NotBlank String title,
        @NotNull UUID publisherId,
        @NotEmpty Set<UUID> authorsId,
        @NotBlank String reviewComment) {
}