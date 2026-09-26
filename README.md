# java-study

## Cấu trúc

```text
backend/
├── pom.xml
└── src/
    ├── main/
    │   ├── java/project_os/project/
    │   │   └── modules/elearning/
    │   │       ├── common/
    │   │       ├── config/
    │   │       ├── dao/
    │   │       ├── model/
    │   │       ├── rest/
    │   │       ├── service/
    │   │       └── wrapper/
    │   └── resources/
    │       ├── application.properties
    │       └── application-dev.properties
    └── test/
        ├── java/
        └── resources/application-test.properties
└── frontend/
```

Backend sử dụng DAO với `EntityManager`. Các Spring Data repository không dùng đã được loại bỏ để tránh hai lớp persistence trùng nhau.

## Chạy backend với MySQL

Đặt thông tin kết nối trong shell, không ghi mật khẩu vào source:

```bash
export DB_USERNAME=your_mysql_user
export DB_PASSWORD=your_mysql_password
cd backend
./mvnw spring-boot:run
```

Có thể thay đổi `DB_HOST`, `DB_PORT` và `DB_NAME` nếu cần. Khi không chỉ định profile, Spring Boot dùng profile `dev`.

## Chạy test

```bash
cd backend
./mvnw test
```

Test tự động dùng H2 in-memory với profile `test`, nên không cần cài hoặc khởi động MySQL local.

## OOP trong module `elearning`

- `Course` và `Student` tự kiểm tra dữ liệu đầu vào trong constructor và các method nghiệp vụ.
- `BaseEntity` là lớp cha `@MappedSuperclass` dùng chung `id`, `createdAt` và `updatedAt` cho các entity.
- `CourseServiceImpl` và `StudentServiceImpl` điều phối use case, không sửa field entity trực tiếp.
- `CourseDAO` và `StudentDAO` là abstraction của persistence; implementation dùng `EntityManager`.
- Các test trong `src/test/java/.../model` minh họa cách kiểm tra behavior của domain object mà không cần Spring hoặc database.

Khi đọc code, nên bắt đầu từ `Course.updateInformation`, `Course.changeStatus`,
`Student.updateContactInformation`, sau đó xem cách service gọi các method này.
