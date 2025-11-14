# Real Data Guide - IDs from CSV Files

## Available Data in Your System

### Users (from users.csv)
- **STU001** - Maria Gonzalez (Student - Systems Engineering, Semester 6)
- **STU002** - Carlos Rodriguez (Student - Medicine, Semester 3)
- **TCH001** - Ana Martinez (Teacher - Sciences, Physics)
- **ADM001** - Luis Sanchez (Administrator)

### Authors (from authors.csv)
- **47f9454a-0665-464c-9d03-9f05ccb489c9** - Gabriel Garcia Marquez (Colombian, 1927-03-06)
- **856de6b2-a449-4edd-bbd5-d737e1b99808** - Robert Martin (American, 1952-12-05)
- **04ff06ef-6181-4c29-8085-252fea413737** - Stephen King (American, 1947-09-21)
- **927ed3fb-e638-4fce-b2af-b14a5d1c4914** - Robert Martin (American, 1952-12-05)
- **41bdcf7d-89ba-4744-bf9c-1df2d29f51c5** - Isabel Allende (Chilean, 1942-08-02)

### Publishers (from publishers.csv)
- **Addison-Wesley** - United States
- **Bantam Books** - United States
- **PUB001** - Penguin Random House (USA)
- **PUB002** - diegos House (USA)
- **Gallimard** - France
- **Sudamericana Publishing** - Argentina
- **Secker & Warburg** - United Kingdom
- **Prentice Hall** - United States

### Books (from books.csv)
- **da2fde6f-ac0a-4ccf-9d03-1bdfb9d01fdb** - Sample Book (ISBN: 978-0-123-45678-9, FICTION, 2023)
- **f1112352-9cd8-4f60-89db-5e4c885c660d** - a kiss under the rain (ISBN: 978-0-123-73678-8, FICTION, 2020)

### Libraries (from libraries.csv)
- **LIB001** - Central Library (New York, United States)
- **LIB002** - Downtown Library (Los Angeles, United States)
- **LIB003** - University Library (Boston, United States)
- **LIB004** - Public Library (Chicago, United States)

### Loans (from loans.csv)
- **d88ce785-d1ca-4d42-9ccd-eb575e8be33d** - ADM001 → BK003 (ACTIVE)
- **f9db725a-7b5d-47f2-bfcd-31178cbeaf56** - TCH001 → BK002 (ACTIVE)
- **ae1b2e62-c4a9-475e-9c60-a7b911dd6a84** - STU001 → BK001 (ACTIVE)

### Reservations (from reservations.csv)
- **c46f9b39-be1d-4c9a-994f-e9f93d6a2aea** - STU002 → BK003 (inactive)
- **3b2e2399-bced-49f0-a524-31895b76d8c3** - USER001 → BOOK001 (inactive)

### Reviews (from reviews.csv)
- **54e39bb3-8c18-4e7d-9a11-9f1f86fc2798** - STU001 → BK001 (5 stars, not approved)
- **REV001** - STU001 → BK002 (4 stars, approved)
- **REV002** - STU002 → BK003 (5 stars, approved)
- **REV003** - TCH001 → BK001 (5 stars, approved)

### Notifications (from notification.csv)
- **17bdf1bc-3ba3-42ff-a4a9-b13947a66060** - STU001 (GENERAL, not read)

## Quick Test Examples

### Test 1: Get Existing User
```
GET {{baseUrl}}/api/users/STU001
```

### Test 2: Get Existing Book
```
GET {{baseUrl}}/api/books/da2fde6f-ac0a-4ccf-9d03-1bdfb9d01fdb
```

### Test 3: Get Loans by User
```
GET {{baseUrl}}/api/loans/user/STU001
```

### Test 4: Get Reviews by Book
```
GET {{baseUrl}}/api/reviews/book/BK001
```

### Test 5: Create Notification for Existing User
```json
POST {{baseUrl}}/api/notifications
{
  "userId": "STU001",
  "type": "GENERAL",
  "message": "Your book is ready for pickup",
  "sendDate": "2025-11-14T14:00:00",
  "read": false
}
```

### Test 6: Perform Loan with Existing Data
```json
POST {{baseUrl}}/api/loans/perform
{
  "userId": "STU001",
  "bookId": "da2fde6f-ac0a-4ccf-9d03-1bdfb9d01fdb"
}
```

### Test 7: Create Book with Existing Author and Publisher
```json
POST {{baseUrl}}/api/books
{
  "isbn": "978-0-14-303943-3",
  "title": "One Hundred Years of Solitude",
  "genre": "FICTION",
  "publicationYear": 1967,
  "availableCopies": 10,
  "totalCopies": 15,
  "author": {
    "id": "47f9454a-0665-464c-9d03-9f05ccb489c9"
  },
  "publisher": {
    "id": "PUB001"
  }
}
```

## Important Notes

1. **User IDs**: Use STU001, STU002, TCH001, or ADM001 for testing
2. **Book IDs**: Use the UUIDs from the books.csv file
3. **Author IDs**: Use the UUIDs from the authors.csv file
4. **Publisher IDs**: Use PUB001, PUB002, or the full names
5. **Notifications**: Support both English (userId, type, message, sendDate, read) and Spanish (usuarioId, tipo, mensaje, fechaEnvio, leida)

## Valid Enum Values

### UserType
- STUDENT
- TEACHER
- ADMINISTRATOR

### BookGenre
- FICTION
- NON_FICTION
- SCIENCE
- TECHNOLOGY
- HISTORY
- BIOGRAPHY
- MYSTERY
- ROMANCE

### NotificationType
- LOAN
- RETURN
- OVERDUE
- GENERAL

### LoanStatus
- ACTIVE
- RETURNED
- OVERDUE

### ReservationStatus
- ACTIVE
- COMPLETED
- CANCELLED
