package com.example.database.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Jackson requires a default (no-argument) constructor to deserialise JSON objects to create Java objects. Recall that
// not all classes automatically have a default constructor. A default constructor is only provided if there are no
// other constructors explicitly defined in the class. Hence, always ensure that the @NoArgsConstructor annotation is
// used for all classes that utilises the Jackson library.

// The benefits of Data Transfer Objects (DTOs) are:

// (1) Separation of Concerns - DTOs allow for a clear separation between the data representation used in the frontend
// (i.e. DTOs in the presentation layer exposed by the controllers) and the domain entity used in the backend. By using
// DTOs you can tailor the data sent and received by the frontend to specific needs, without exposing all the internal
// details of the backend domain model.

// (2) Reduced Overhead - DTOs can help reduce network overhead by transmitting only the necessary data between the
// client and the server. For example, you might not need to send all fields of an entity to the client in some cases.

// (3) Versioning and Compatibility - DTOs provide flexibility for versioning and backward compatibility. You can modify
// the structure of DTOs independently of the backend domain entities, ensuring that changes in the backend do not
// directly impact client applications.

// (4) Security - DTOs allow you to sanitise and control the data sent from the client to the server, helping to prevent
// issues like over-posting (i.e. mass assignment vulnerabilities) and exposing sensitive information.

// Example of over-posting:

// (a) In a typical web form, users input data into fields (e.g. name, email, password) and submit it to the server.

// (b) An attacker modifies the form or API request to include extra fields that were not intended by developer. For
// example, they might add a hidden field like "isAdmin=true" or "role=admin" to elevate their privileges.

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthorDto {

    private Long id;

    private String name;

    private Integer age;
}
