package com.example.reactive.dto;

import java.util.UUID;

public record ExtendedReturnedItem(UUID id, UUID calledWithId, String info) {
}
