# Dự án Bookstore Online - Java Web Application

## 📚 Ý tưởng dự án

Xây dựng một hệ thống cửa hàng sách trực tuyến với các chức năng quản lý sách và đơn hàng cho khách hàng. Hệ thống cho phép:
- Khách hàng đăng ký, đăng nhập và quản lý tài khoản
- Tìm kiếm và xem thông tin sách
- Tạo đơn hàng và theo dõi lịch sử mua hàng
- Admin quản lý sách (CRUD operations)

## 🎯 Kịch bản sử dụng (Use Cases)

### 1. Khách hàng mới
1. Truy cập trang web → Đăng ký tài khoản
2. Điền thông tin: họ tên, email, password, địa chỉ
3. Xác nhận đăng ký → Hệ thống lưu vào database

### 2. Khách hàng mua sách
1. Đăng nhập với email/password
2. Tìm kiếm sách theo: tên, tác giả, thể loại
3. Xem chi tiết sách: giá, mô tả, số lượng còn
4. Thêm sách vào giỏ hàng
5. Xem giỏ hàng và điều chỉnh số lượng
6. Tạo đơn hàng → Hệ thống lưu order

### 3. Admin quản lý sách
1. Đăng nhập với quyền admin
2. Xem danh sách tất cả sách
3. Thêm sách mới với thông tin đầy đủ
4. Cập nhật thông tin sách
5. Xóa sách khỏi hệ thống

## 🏗️ Cấu trúc dự án

```
JavaLab/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── controller/     # Servlets
│   │   │   │   ├── LoginServlet.java
│   │   │   │   ├── RegisterServlet.java
│   │   │   │   ├── LogoutServlet.java
│   │   │   │   ├── BookServlet.java
│   │   │   │   ├── SearchServlet.java
│   │   │   │   └── OrderServlet.java
│   │   │   ├── model/          # Entity classes
│   │   │   │   ├── Customer.java
│   │   │   │   ├── Address.java
│   │   │   │   ├── Account.java
│   │   │   │   ├── Book.java
│   │   │   │   ├── Order.java
│   │   │   │   └── OrderList.java
│   │   │   ├── dao/            # Data Access Objects
│   │   │   │   ├── CustomerDAO.java
│   │   │   │   ├── BookDAO.java
│   │   │   │   ├── OrderDAO.java
│   │   │   │   └── BaseDAO.java
│   │   │   └── util/           # Utilities
│   │   │       └── DBConnection.java
│   │   └── webapp/
│   │       ├── WEB-INF/
│   │       │   └── web.xml
│   │       ├── jsp/            # JSP views
│   │       │   ├── login.jsp
│   │       │   ├── register.jsp
│   │       │   ├── book-list.jsp
│   │       │   ├── book-form.jsp
│   │       │   ├── search.jsp
│   │       │   ├── cart.jsp
│   │       │   └── order.jsp
│   │       ├── css/
│   │       │   └── style.css
│   │       └── js/
│   │           └── script.js
├── database/
│   └── bookstore.sql          # Database schema
├── lib/                       # External libraries
│   ├── mysql-connector-java-8.0.33.jar
│   └── jstl-1.2.jar
└── pom.xml                    # Maven configuration

```

## 💾 Database Schema

### Tables:
1. **customers**: id, name, email, phone
2. **addresses**: id, customer_id, street, city, country
3. **accounts**: id, customer_id, username, password, created_date
4. **books**: id, title, author, isbn, price, quantity, category
5. **orders**: id, customer_id, order_date, total_amount, status
6. **order_items**: id, order_id, book_id, quantity, price

## 🚀 Hướng dẫn chạy dự án

### Yêu cầu hệ thống:
- JDK 8 hoặc cao hơn
- Apache Tomcat 9.0
- MySQL 8.0
- Maven 3.6+
- IDE: Eclipse/IntelliJ IDEA

### Bước 1: Cấu hình Database
```bash
# Tạo database
mysql -u root -p
CREATE DATABASE bookstore;
USE bookstore;

# Import schema
source database/bookstore.sql
```

### Bước 2: Cấu hình kết nối Database
Sửa file `src/main/java/util/DBConnection.java`:
```java
private static final String URL = "jdbc:mysql://localhost:3306/bookstore";
private static final String USER = "root";
private static final String PASSWORD = "your_password";
```

### Bước 3: Build project với Maven
```bash
mvn clean install
```

### Bước 4: Deploy lên Tomcat
1. Copy file `target/bookstore.war` vào folder `tomcat/webapps/`
2. Start Tomcat:
```bash
cd tomcat/bin
./startup.sh  # Linux/Mac
startup.bat   # Windows
```

### Bước 5: Truy cập ứng dụng
```
http://localhost:8080/bookstore
```

## 📝 API Endpoints

### Authentication
- `POST /register` - Đăng ký tài khoản mới
- `POST /login` - Đăng nhập
- `GET /logout` - Đăng xuất

### Books
- `GET /books` - Lấy danh sách sách
- `GET /books/{id}` - Xem chi tiết sách
- `POST /books` - Thêm sách mới (Admin)
- `PUT /books/{id}` - Cập nhật sách (Admin)
- `DELETE /books/{id}` - Xóa sách (Admin)
- `GET /search?q={keyword}` - Tìm kiếm sách

### Orders
- `POST /orders` - Tạo đơn hàng mới
- `GET /orders` - Xem lịch sử đơn hàng
- `GET /orders/{id}` - Xem chi tiết đơn hàng

## 🧪 Testing

### Unit Tests
```bash
mvn test
```

### Integration Tests
1. Test đăng ký/đăng nhập
2. Test CRUD operations cho sách
3. Test tạo đơn hàng
4. Test tìm kiếm

## 📚 Tài liệu tham khảo

1. [JSP Servlet JDBC MySQL CRUD Tutorial](https://www.javaguides.net/2019/03/jsp-servlet-jdbc-mysql-crud-example-tutorial.html)
2. [Registration Form Example](https://www.javaguides.net/2019/03/registration-form-using-jsp-servlet-jdbc-mysql-example.html)
3. [Login Form Example](https://www.javaguides.net/2019/03/login-form-using-jsp-servlet-jdbc-mysql-example.html)
4. [CodeJava CRUD Example](https://www.codejava.net/coding/jsp-servlet-jdbc-mysql-create-read-update-delete-crud-example)

## 🛠️ Công nghệ sử dụng

- **Backend**: Java Servlet, JSP
- **Database**: MySQL
- **Build Tool**: Maven
- **Server**: Apache Tomcat
- **Pattern**: MVC, DAO Pattern
- **Frontend**: HTML, CSS, JavaScript, Bootstrap

## 📌 Lưu ý cho developers

1. Luôn sử dụng PreparedStatement để tránh SQL Injection
2. Hash password trước khi lưu vào database
3. Validate dữ liệu ở cả client và server side
4. Sử dụng connection pooling cho performance
5. Log các exception để debug dễ dàng
6. Tách biệt logic business và presentation layer
7. Follow Java naming conventions
8. Comment code cho các phần logic phức tạp