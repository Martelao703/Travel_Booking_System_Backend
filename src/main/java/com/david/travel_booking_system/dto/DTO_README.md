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

For create/update requests, the project uses a single DTO for each entity, which includes all fields needed to create or update the entity.