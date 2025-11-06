# Config

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:port/dbname
    username: root
    password: root
```

config db port, db name, username and password

rename application.example.yml to application.yml

run the app and auto-ddl will create the db

do NOT commit to main, make your own branch

---

copy file application.example.yml, tạo file application.yml, copy nội dung trong file example qua, điển thông tin db các thứ riêng của mình vào application.yml

điền thông tin url db: port, tên db, username password

KHÔNG commit file application.yml

chạy app để tạo các bảng trong db

không commit main, tự tạo branch của mình chờ merge