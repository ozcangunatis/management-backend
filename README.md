# İzin Yönetimi Projesi

Leave Management API Dokümantasyonu
LeaveStatus Enumları: 
PENDING, // Beklemede
APPROVED, // Onaylandı
REJECTED // Reddedildi
LeaveType Enumları: 
ANNUAL, // Yıllık izin
SICK, // Hastalık izni
UNPAID, // Ücretsiz izin
MATERNITY // Doğum izni

JWT Yetkileri:
.requestMatchers("/api/auth/**").permitAll() // Tüm roller
.requestMatchers("/api/leave-requests/**").hasAnyRole("ADMIN", "USER", "EMPLOYEE") // ADMIN-USER-EMPLOYEE
.requestMatchers("/api/leave-balance/**").hasAnyRole("ADMIN", "EMPLOYEE", "USER") // ADMIN - USER- EMPLOYEE

AUTHENTICATION KİMLİK DOĞRULAMA
POST /api/auth/register
Yeni kullanıcı kaydı. Request body içinde firstName, lastName, email, password, role alanları olmalıdır
BODY:
{
  "firstName": "Yeni",
  "lastName": "Kullanici",
  "email": "kullanici@gmail.com",
  "password": "1234",
  "role": "ADMIN"
}
Dönen response: {
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
POST /api/auth/login
Kullanıcı giriş yapar ve JWT token döner.
{
  "email": "kullanici@gmail.com",
  "password": "1234"
}
Dönen response:
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
DELETE http://localhost:8080/api/users/{userId}
Kullanıcıyı siler.
Token lazım. Authorization: Bearer {token}


 Kullanıcı İşlemleri
GET /api/users
Tüm kullanıcıları getirir. Yalnızca Admin rolündeki kullanıcılar için erişilebilir.
Token lazım. Authorization: Bearer {token}
Dönen response:
{
  "users": [
    {
      "id": 1,
      "firstName": "Yeni",
      "lastName": "Kullanici",
      "email": "kullanici@gmail.com",
      "role": "ADMIN"
    }
  ]
}
Yetki: ADMIN,HR
 Leave Request (İzin Talepleri)
POST /api/leave-requests/{userId}
Kullanıcı için yeni izin talebi oluşturur.
Token lazım: Authorization: Bearer {token}
BODY:
{
  "leaveType": "ANNUAL",
  "startDate": "2025-04-15",
  "endDate": "2025-05-5"
}
Dönen Response:
{
  "id": 1,
  "user": {
    "id": 4,
    "firstName": "Yeni",
    "lastName": "Kullanici",
    "email": "kullanici@gmail.com",
    "role": "EMPLOYEE"
  },
  "leaveType": "ANNUAL",
  "startDate": "2025-04-15",
  "endDate": "2025-04-17",
  "status": "PENDING",
  "createdAt": "2025-04-13T05:47:59.526039"
}
GET /api/leave-requests/user/{userId}
Kullanıcıya ait izin taleplerini getirir.
Token lazım: Authorization: Bearer {token}
Dönecek Response:
[
  {
    "id": 1,
    "user": {
      "id": 4,
      "firstName": "Yeni",
      "lastName": "Kullanici",
      "email": "kullanici@gmail.com",
      "password": "$2a$10$BpN02mNSkp1nlrzKphmmluozmvcOTCT7KHDVqMlg6nAtsyuSmK1kC",
      "role": "EMPLOYEE",
      "createdAt": "2025-04-13T05:44:45.645329",
      "enabled": true,
      "authorities": [
        {
          "authority": "ROLE_EMPLOYEE"
        }
      ],
      "username": "kullanici@gmail.com",
      "accountNonExpired": true,
      "credentialsNonExpired": true,
      "accountNonLocked": true
    },
    "leaveType": "ANNUAL",
    "startDate": "2025-04-15",
    "endDate": "2025-04-17",
    "status": "APPROVED",
    "createdAt": "2025-04-13T05:47:59.526039"
  }
]
PUT /api/leave-requests/status
İzin talebinin statusunu günceller. Request body içinde requestId ve yeni status olmalıdır.
Token lazım: Authorization: Bearer {token}
BODY:
{
  "requestId": 1,
  "newStatus": "APPROVED"
}
Dönen Response:
{
  "id": 1,
  "userId": 4,
  "leaveType": "ANNUAL",
  "startDate": "2025-04-15",
  "endDate": "2025-04-17",
  "status": "APPROVED"
}
Leave Request bulunamaz ise;
Response Body (Fail - 400 Bad Request):
"Leave request not found"
Burada PENDING durumunda olan status'u APPROVED olarak onaylıyor. 
Red edilmesi istendiğinde REJECTED olarakta döndürebilir.
 Leave Balance (İzin Bakiyesi) 
POST /api/leave-balance/{userId}/{totalDays}
Belirtilen kullanıcı için belirtilen gün sayısında izin bakiyesi oluşturur.
Token lazım. Authorization: Bearer {token}
Dönecek Response:
{
  "id": 1,
  "user": {
    "id": 4,
    "firstName": "Yeni",
    "lastName": "Kullanici",
    "email": "kullanici@gmail.com",
    "role": "EMPLOYEE",
    "createdAt": "2025-04-13T05:44:45.645329"
  },
  "totalDays": 20,
  "usedDays": 0,
  "remainingDays": 20
}
USER yok ise;
Hata Durumu (HTTP 400 Bad Request)
"User not found!"
veya:
"Leave balance already exists for userId: 4"
GET /api/leave-balance/{userId}
Belirtilen kullanıcıya ait izin bakiyesini getirir.
Token lazım: Authorization: Bearer {token}
Dönecek Response:
{
  "id": 1,
  "totalDays": 20,
  "usedDays": 3,
  "remainingDays": 17,
  "user": {
    "id": 4,
    "firstName": "Yeni",
    "lastName": "Kullanici",
    "email": "kullanici@gmail.com",
    "role": "EMPLOYEE"
  }
}
Rol tanımları:
ADMIN:	Tüm işlemleri yapabilir
EMPLOYEE:	Sadece kendi işlemlerini yapabilir
HR:	İzin taleplerini görebilir/güncelleyebilir














