# DTO Architecture Overview

## Response Objects

This project uses a **three-tier DTO architecture** for API response objects:

1. **BasicDTO**: Contains all simple fields that are not collections or relationships.
2. **DetailDTO**: Includes fields from `BasicDTO` and any collection fields that are non-nested (e.g., lists of strings, integers, etc.).
3. **FullDTO**: Includes everything from `DetailDTO` plus any deeply nested relationships or collections of other DTOs.

**NOTE**: Some entities may not have all three tiers due to their simplicity, so the same controller method of different
entities may return different DTOs based on the complexity of the entity.

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

The project organizes request DTOs into one of the following two categories:

1. **General `RequestDTO`**:
    - Used when the same fields are required for both `create` and `update` operations of an entity.
    - Contain all fields needed to create or update an entity.
    - Entities that use this DTO do not have specific `CreateRequestDTO` or `UpdateRequestDTO`.

2. **Specific RequestDTOs**:
    - **`CreateRequestDTO`**:
        - Used for endpoints that handle the creation of new entities.
        - Contain all fields needed to create a new entity.
    - **`UpdateRequestDTO`**:
        - Used for endpoints that handle updates to existing entities.
        - Contain only the fields that can be updated.

### Entities and Corresponding Request DTOs

| Entity       | Request DTOs   |
|--------------|----------------|
| **User**     | -              |
| **Booking**  | -              |
| **Property** | Create, Update |
| **RoomType** | General        |
| **Room**     | Create, Update |
| **Bed**      | -              |