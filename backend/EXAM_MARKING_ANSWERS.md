# Practical Marking Answers and Evidence (30/30)

This file answers each rubric point with implementation logic and concrete API examples.

## 1) ERD with at least 5 tables (3 marks)

Implemented tables:
1. `location`
2. `users`
3. `farmer`
4. `harvest`
5. `payment`
6. `user_farmer` (many-to-many join table)

Relationship summary:
- `Location` self-reference: `parent_id` supports Rwanda hierarchy (Province -> District -> Sector -> Cell -> Village).
- `users.location_id` links user to a province.
- `farmer.location_id` links farmer to a location.
- `harvest.farmer_id` links harvest to farmer.
- `payment.harvest_id UNIQUE` enforces one payment per harvest.
- `user_farmer(user_id, farmer_id)` maps many-to-many.

## 2) Save Location + relationship handling (2 marks)

Code:
- `LocationService.saveLocation(Location location, String parentId)`

Logic:
- `PROVINCE` must not have a parent.
- `DISTRICT` must have a parent of type `PROVINCE`.
- `SECTOR` must have parent `DISTRICT`.
- `CELL` must have parent `SECTOR`.
- `VILLAGE` must have parent `CELL`.
- Rejects duplicate codes via `existsByCodeIgnoreCase`.

Postman examples:
- Create province:
  - `POST /api/locations/save`
  - Body:
```json
{
  "name": "Kigali City",
  "code": "KGL",
  "type": "PROVINCE"
}
```
- Create district under province:
  - `POST /api/locations/save?parentId={provinceId}`
  - Body:
```json
{
  "name": "Gasabo",
  "code": "GAS",
  "type": "DISTRICT"
}
```

## 3) Sorting + Pagination (5 marks)

Implemented with `PageRequest` + `Sort`:
- `GET /api/users/paged?page=0&size=5&sortBy=username&direction=asc`
- `GET /api/farmers/paged?page=0&size=5&sortBy=name&direction=desc`

Why it improves performance:
- Only requested page is fetched.
- Sorting happens in SQL (`ORDER BY`), not in memory.

## 4) Many-to-Many (3 marks)

Entities:
- `User.farmers` owns relationship with:
  - `@ManyToMany`
  - `@JoinTable(name = "user_farmer", ...)`
- `Farmer.assignedUsers` inverse side: `@ManyToMany(mappedBy = "farmers")`

Endpoint:
- `POST /api/users/assign-farmer?userId={id}&farmerId={id}`

## 5) One-to-Many (2 marks)

Examples:
- `Location (1) -> Farmer (many)` using `farmer.location_id`.
- `Farmer (1) -> Harvest (many)` using `harvest.farmer_id`.

This is modeled with `@ManyToOne` on child entities and DB foreign keys.

## 6) One-to-One (2 marks)

Entity:
- `Payment` has `@OneToOne` with `Harvest`
- `@JoinColumn(name = "harvest_id", unique = true)`

Meaning:
- A harvest can have only one payment record.

## 7) existsBy() (2 marks)

Implemented:
- `UserRepository.existsByUsername(...)`
- `FarmerRepository.existsByNationalId(...)`
- `LocationRepository.existsByCodeIgnoreCase(...)`

Use:
- Prevent duplicate usernames, national IDs, and location codes.

## 8) Retrieve users by province code OR province name (4 marks)

Repository:
- `findByLocationCodeIgnoreCaseOrLocationNameIgnoreCase(String code, String name)`

Service:
- `getUsersByProvince(provinceValue)` sends same value to both parameters.

Endpoint:
- `GET /api/users/by-province?province=KGL`
- `GET /api/users/by-province?province=Kigali City`

Note:
- User save validates that user location must be of type `PROVINCE`, so this query is accurate for province filtering.

## 9) Viva-Voce preparation points (7 marks)

1. Why self-referencing `Location` is needed: it models real Rwanda administrative hierarchy.
2. Why many-to-many uses a join table: both sides can have multiple links.
3. Why one-to-one uses unique FK: prevents multiple payments for one harvest.
4. Why `existsBy` is used: fast integrity checks before save.
5. How pagination works: `page`, `size`, `sortBy`, `direction` produce `Page<T>`.
6. How province query works: Spring Data derived method traverses `User -> Location`.
7. How hierarchy endpoints support UI cascading dropdowns.

## Rwanda Cascading Endpoints (for Postman demo)

1. `GET /api/locations/provinces`
2. `GET /api/locations/districts?provinceId={provinceId}`
3. `GET /api/locations/sectors?districtId={districtId}`
4. `GET /api/locations/cells?sectorId={sectorId}`
5. `GET /api/locations/villages?cellId={cellId}`

Generic endpoint alternative:
- `GET /api/locations/children?parentId={id}&type=DISTRICT|SECTOR|CELL|VILLAGE`

