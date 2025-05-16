 API Dökümantasyonu - Leave Management Sistemi
Genel Bilgiler
Base URL: http://localhost:8080/api
Auth: JWT Token / Basic Auth 
HR, sadece kendi kayıtlı olduğu ofis ile ilgili işlemleri yapabilir. Sadece kendi ofisinde employee oluşturabilir kendi çalışanlarının izin taleplerini onaylayabilir kendi çalışanlarını görüntüleyebilir gibi.
SUPER_HR, HR dan farklı olarak tüm ofislerde yetkisi vardır. belli bir ofise bağlı değildir.


ROLLER
ADMIN	Sistemde tam yetkili, tüm kullanıcı ve izin işlemlerine erişebilir
SUPER_HR	Tüm İK işlemlerini yapabilir, ayrıca diğer HR'ları yönetebilir
HR	İzin onayı, personel izleme gibi İK görevlerini yürütür
EMPLOYEE	İzin talebinde bulunabilir, kendi bilgilerini görüntüleyebilir


Authentication API
Base URL: /api/auth
1. POST /api/auth/login
Açıklama:
Kullanıcı giriş işlemi. Geçerli bilgilerle token döner.
Request Body (application/json):
{
  "email": "user@example.com",
  "password": "123456"
}
Response (200 OK):
{
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQURNSU4iLCJzdWIiOiJhZG1pbkBleGFtcGxlLmNvbSIsImlhdCI6MTc0NzMwODc5MSwiZXhwIjoxNzQ3Mzk1MTkxfQ.wEkvU48wGmpzJBFh1ZqnEEsaDGoCQyknYDcDAzs_yf0",
    "firstName": "Admin",
    "lastName": "User",
    "email": "admin@example.com"
}
Hatalar:

403 Unauthorized – Geçersiz e-posta veya şifre.

Roller: Herkes (kayıtlı kullanıcı)

2. POST /api/auth/forgot-password
Açıklama:
Kullanıcının TC kimlik no, e-posta ve telefon numarasına göre doğrulama yapılır.
Request Body (application/json):
{
  "email": "user@example.com",
  "tcNo": "12345678901",
  "phoneNumber": "05554443322"
}
Response (200 OK):
"Information is correct."
Hatalar:

400 Bad Request – Invalid email or TcNo



Roller: Herkes (şifre sıfırlamak isteyen kullanıcı)

3. POST /api/auth/reset-password
Açıklama:
Kullanıcı e-posta adresi ile sistemde varsa yeni şifre belirler.
Request Body (application/json):
{
  "email": "user@example.com",
  "newPassword": "password123"
}
Response (200 OK):
"Password reset successful."
Hatalar:

400 Bad Request – "e-mail is not registered in the system"
Roller: Herkes (şifre sıfırlamak isteyen kullanıcı)

User API
Base URL: /api/users
1. POST /api/users
Açıklama:
Yeni kullanıcı ekler. Erişim hakları role göre değişir:
ADMIN, SUPER_HR: Her rolde ve ofiste kullanıcı ekleyebilir.

HR: Sadece EMPLOYEE rolünde ve kendi ofisine kullanıcı ekleyebilir.
Request Body (application/json):
{
  "firstName": "ozcan",
  "lastName": "atis",
  "email": "ozcan@example.com",
  "password": "ozcan22",
  "tcNo": "12345678901",
  "phoneNumber": "05554443322",
  "role": "EMPLOYEE",
  "officeId": 3,
"jobEntryDate": "2023-08-15"
}
Response:
{
  "id": 12,
  "firstName": "ozcan",
  "lastName": "atis",
  "email": "ozcan@example.com",
  "role": "EMPLOYEE",
  "officeId": 3
}
403 Forbidden – HR başka ofise ya da EMPLOYEE dışında kullanıcı eklemeye çalışırsa.

400 Bad Request – Eksik ya da hatalı veri.
Roller:

 ADMIN, SUPER_HR

 HR (sınırlı yetki)

 2. GET /api/users/{id}
Açıklama:
Belirli bir kullanıcıyı ID ile getirir.

Response (200 OK):
{
  "id": 12,
  "firstName": "Ahmet",
  "lastName": "Yılmaz",
  "email": "ahmet@example.com",
  "role": "EMPLOYEE",
  "officeId": 3
}
Hatalar:

404 Not Found – Kullanıcı bulunamazsa.
Roller:

ADMIN, SUPER_HR, HR
HR sadece kendi ofisinde ki kullanıcıları görebilir.
3. PUT /api/users/{id}
Açıklama:
Bir kullanıcıyı günceller. HR sadece kendi ofisindeki kullanıcıları güncelleyebilir.
Request Body (application/json):

{
  "firstName": "Ahmet",
  "lastName": "Yılmaz",
  "email": "ahmet@example.com",
  "phoneNumber": "05551234567"
}
Response (200 OK):
{
  "id": 12,
  "firstName": "Ahmet",
  "lastName": "Yılmaz",
  "email": "ahmet@example.com",
  "role": "EMPLOYEE",
  "officeId": 3
}
Hatalar:

403 Forbidden – HR başka ofisteki kullanıcıyı güncellemeye çalışırsa.

404 Not Found – Kullanıcı bulunamazsa
Roller:
 ADMIN, SUPER_HR
HR (sınırlı yetki)
Not: Tüm endpoint’lerde JWT token doğrulaması gereklidir. Authorization: Bearer <token> header’ı kullanılmalıdır.

Leave Answer API
Base URL: /api/leave-answers
1. POST /api/leave-answers/approve/{id}
Açıklama:
Belirtilen izin talebini onaylar. Sadece HR, ADMIN veya SUPER_HR rolündeki kullanıcılar bu işlemi yapabilir.
Örnek Request:
POST /api/leave-answers/approve/5
Response (200 OK):
Authorization: Bearer <token>
{
  "leaveRequestId": 5,
  "status": "APPROVED",
  "approverEmail": "superhr@example.com",
  "approvedAt": "2025-05-15T12:44:00"
}
Hata Kodları:

404 Not Found: İzin talebi bulunamadı.

403 Forbidden: Kullanıcının bu işlemi yapmaya yetkisi yok.
 2. POST /reject/{id}
Açıklama:
Belirtilen izin talebini reddeder. Sadece HR, ADMIN veya SUPER_HR kullanıcıları tarafından yapılabilir.
Yetki Gereksinimi:
HR 
ADMIN 
SUPER_HR 
Response (200 OK):
{
  "leaveRequestId": 5,
  "status": "REJECTED",
  "approverEmail": "hr@example.com",
  "approvedAt": "2025-05-15T12:46:00"
}
Hata Kodları:
404 Not Found: İzin talebi bulunamadı.
403 Forbidden: Yetkisiz erişim.
Office API
Base URL: /api/offices
Amaç: Ofis (office) verilerini oluşturma ve listeleme.
 1. POST /api/offices/create
Açıklama:
Yeni bir ofis oluşturur.
Yetkili Roller: ADMIN
Request Body (application/json):
{
  "officeName": "İstanbul Ofisi"
}
Response:
{
  "id": 3,
  "officeName": "İstanbul Ofisi"
}
Hatalı Durum:
Eğer aynı isimde bir ofis zaten varsa:
409 Conflict yanıtı döner.
  2. GET /api/offices
Açıklama:
Sistemdeki tüm ofisleri listeler.
Yetkili Roller: ADMIN, SUPER_HR
Response örneği:
[
  {
    "id": 1,
    "officeName": "Ankara Ofisi"
  },
  {
    "id": 2,
    "officeName": "Edirne Ofisi
  }
]
|
Leave Balance API
1. GET /api/leave-balance/{userId}
Açıklama:
Belirtilen kullanıcıya (userId) ait izin bakiyesini getirir.
Yetkili Roller: ADMIN, HR, SUPER_HR
Request Örneği:
GET /api/leave-balance/42
Authorization: Bearer <JWT>
Response (200 OK):
{
  "userId": 42,
  "annualLeave": 14,
  "usedLeave": 5,
  "remainingLeave": 9
}
2. GET /api/leave-balance/my-balance
Açıklama:
Giriş yapmış olan (token üzerinden kimliği alınan) kullanıcının izin bakiyesini getirir.
Yetkili Roller: EMPLOYEE, HR, SUPER_HR
Request Örneği:
GET /api/leave-balance/my-balance
Authorization: Bearer <JWT>
Response (200 OK):
{
  "userId": 42,
  "annualLeave": 14,
  "usedLeave": 5,
  "remainingLeave": 9
}
Leave Request API
Base URL: /api/leave-requests
Amaç: Kullanıcıların izin talebi oluşturmasını ve yöneticilerin talepleri yönetmesini sağlar.
1. POST /api/leave-requests
Açıklama:
Giriş yapan EMPLOYEE rolündeki kullanıcı izin talebi oluşturur.
Yetkili Roller: EMPLOYEE,HR,SUPER_HR
Request Body (application/json):
{
  "leaveType": "PAID"
  "startDate": "2025-06-01",
  "endDate": "2025-06-05"
}
VEYA:
{
  "leaveType": "UNPAID",
  "startDate": "2025-06-01",
  "endDate": "2025-06-05"
}
Response:
{
  "id": 12,
  "leaveType": "PAID
  "startDate": "2025-06-01",
  "endDate": "2025-06-05",
  "status": "PENDING",
  "createdAt": "2025-05-15T10:23:45"
}
PUT /api/leave-requests/status
Açıklama:
Yönetici izin talebinin durumunu (APPROVED, REJECTED, vb.) günceller.
Yetkili Roller: ADMIN, HR,SUPER_HR
Request Body:
{
  "leaveRequestId": 12,
  "newStatus": "APPROVED"
}
Response (200 OK):
{
  "id": 12,
  "leaveType": "PAID
  "startDate": "2025-06-01",
  "endDate": "2025-06-05",
  "status": "APPROVED",
  "createdAt": "2025-05-15T10:23:45"
}
Hata Durumu (400 Bad Request):
"Invalid status update: request not found or already finalized."
GET /api/leave-requests/user/{userId}
Açıklama:
Belirtilen kullanıcıya ait izin taleplerini listeler.
Yetkili Roller: ADMIN, HR,SUPER_HR
Response (200 OK):
[
  {
    "id": 11,
    "leaveType": "ANNUAL",
    "startDate": "2025-04-15",
    "endDate": "2025-04-20",
    "status": "REJECTED",
    "createdAt": "2025-03-25T14:10:00"
  },
  {
    "id": 12,
    "leaveType": "ANNUAL",
    "startDate": "2025-06-01",
    "endDate": "2025-06-05",
    "status": "PENDING",
    "createdAt": "2025-05-15T10:23:45"
  }
]
GET /api/leave-requests/user/{userId}
Yetki: HR, ADMIN,SUPER_HR

GET /api/leave-requests/my-requests
Açıklama: Giriş yapmış kullanıcının (EMPLOYEE) tüm izin taleplerini getirir.
Yetki: EMPLOYEE
DELETE /api/leave-requests/my
Açıklama: Giriş yapan çalışanın en son oluşturduğu PENDING durumundaki talebini siler.
Yetki: EMPLOYEE
Response:
200 OK: "Your pending leave request has been deleted."
404 Not Found: "No pending leave request found to delete."
GET /api/leave-requests
Açıklama: Sistemdeki tüm izin taleplerini getirir.
Yetki: ADMIN, HR, SUPER_HR
GET /api/leave-requests/filter-by-date?startDate=2025-05-01&endDate=2025-05-31
Açıklama: Tüm izin taleplerini belirtilen tarih aralığında filtreler.
Yetki: ADMIN, HR,SUPER_HR
GET /api/leave-requests/my/filter-by-date?startDate=2025-05-01&endDate=2025-05-31
Açıklama: Giriş yapan kullanıcının kendi izin taleplerini belirtilen tarih aralığında filtreler.
Yetki: EMPLOYEE
Statistics API
GET /api/stats/dashboard
Açıklama: Genel istatistik verilerini döner















