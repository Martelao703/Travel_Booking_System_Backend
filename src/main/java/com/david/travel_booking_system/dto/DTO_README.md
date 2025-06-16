# DTO Architecture Overview

## Response Objects

---

This project uses a **three-tier DTO architecture** for API response objects:

1. **BasicDTO**: Contains all simple fields that are not collections or relationships.
2. **DetailDTO**: Includes fields from `BasicDTO` and any collection fields that are non-nested (e.g., lists of strings,
   integers, etc.).
3. **FullDTO**: Includes everything from `DetailDTO` plus any deeply nested relationships or collections of other DTOs.

**NOTE**: Some entities may not have all three tiers due to their simplicity, so the same controller method of different
entities may return different DTOs based on the complexity of the entity.

### Authentication Process:

- **`JwtResponseDTO`**:
    - Used for the `POST /auth/register`, `POST /auth/login` and `POST /auth/refresh-token` endpoints.
    - Contains the JWT access and refresh tokens.

### Entities and Corresponding Response DTOs

| Entity       | Response DTOs       |
|--------------|---------------------|
| **User**     | Basic, Full         |
| **Booking**  | Basic               |
| **Property** | Basic, Detail, Full |
| **RoomType** | Basic, Detail, Full |
| **Room**     | Basic, Full         |
| **Bed**      | Basic, Full         |

## Request Objects

---

The project organizes request DTOs into one of the following two categories:

1. **CRUD RequestDTOs**:
    - **`CreateRequestDTO`**:
        - Used for endpoints that handle the creation of new entities.
        - Contain all fields needed to create a new entity.
    - **`UpdateRequestDTO`**:
        - Used for endpoints that handle updates to existing entities.
        - Contain only the fields that can be updated.
    - **`PatchRequestDTO`**:
        - Used for endpoints that handle partial updates to existing entities.
        - Contain only the fields that can be updated, wrapped in a custom `OptionalFieldWrapper` object, to distinguish
          between explicitly set and unset fields in the incoming payload.

2. **Specialized RequestDTOs**:
    - Used for custom endpoints that require a specific set of fields.
    - Contain only the fields needed for the specific endpoint.
    - **`BookingDateChangeRequestDTO`**:
        - Used for the `PATCH /booking/{id}/dates` endpoint.
        - Contains the new `checkInDate` and `checkOutDate` fields, wrapped in a custom `OptionalFieldWrapper` object,
          to distinguish between explicitly set and unset fields in the incoming payload.
    - **`UserRoleChangeRequestDTO`**:
        - Used for the `PATCH /user/{id}/roles` endpoint.
        - Contains the new `roles` field.
    - **`AdminResetPasswordRequestDTO`**:
        - Used for the `POST /users/{id}/reset-password` endpoint.
        - Contains the `oldPassword` field.

### Authentication Process:

- **`RegisterRequestDTO`**:
    - Used for the `POST /auth/register` endpoint.
    - Contains the fields required to register a new user.
- **`LoginRequestDTO`**:
    - Used for the `POST /auth/login` endpoint.
    - Contains the `username` and `password` fields for authentication.
- **`ResetPasswordRequestDTO`**:
    - Used for the `POST /auth/reset-password` endpoint.
    - Contains the `oldPassword` and `newPassword` fields.

### Entities and Corresponding General and CRUD Request DTOs

| Entity       | Request DTOs          |
|--------------|-----------------------|
| **User**     | Create, Update, Patch |
| **Booking**  | Create, Update, Patch |
| **Property** | Create, Update, Patch |
| **RoomType** | Create, Update, Patch |
| **Room**     | Create, Update, Patch |
| **Bed**      | Create, Update, Patch |

### Entities and Corresponding Specialized Request DTOs

| Entity       | Request DTOs                   |
|--------------|--------------------------------|
| **User**     | RoleChange, AdminResetPassword |
| **Booking**  | DateChange                     |
| **Property** | -----------------------        |
| **RoomType** | -----------------------        |
| **Room**     | -----------------------        |
| **Bed**      | -----------------------        |

