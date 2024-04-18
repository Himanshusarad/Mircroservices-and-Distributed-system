package com.mds.customer;

public record CustomerRegistrationRequest(
        String firstName,
        String lastName,
        String email
) {
}
