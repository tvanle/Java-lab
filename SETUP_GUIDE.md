# 📚 Bookstore Online - Hướng Dẫn Setup và Cấu Trúc Code

## 📋 Mục Lục
1. [Giới thiệu Project](#giới-thiệu-project)
2. [Yêu cầu hệ thống](#yêu-cầu-hệ-thống)
3. [Cài đặt và Setup](#cài-đặt-và-setup)
4. [Cấu trúc Project](#cấu-trúc-project)
5. [Cấu hình Database](#cấu-hình-database)
6. [Chạy ứng dụng](#chạy-ứng-dụng)
7. [Tính năng chính](#tính-năng-chính)
8. [Troubleshooting](#troubleshooting)

---

## 🚀 Giới thiệu Project

**Bookstore Online** là một ứng dụng web quản lý cửa hàng sách trực tuyến được phát triển bằng Java với các công nghệ:
- **Backend**: Java Servlet + JSP
- **Database**: MySQL
- **Build Tool**: Maven
- **Server**: Apache Tomcat
- **Frontend**: HTML, CSS, JavaScript + JSP

---

## 💻 Yêu cầu Hệ Thống

### Phần mềm cần thiết:
- **Java Development Kit (JDK)**: 8 hoặc cao hơn
- **Apache Maven**: 3.6.0 trở lên - Build tool và dependency manager
- **Docker**: Để chạy MySQL container (khuyên dùng)
- **Apache Tomcat**: 9.0 trở lên - Web server để chạy ứng dụng
- **IDE**: Eclipse, IntelliJ IDEA, hoặc Visual Studio Code

### Giải thích các công cụ:
- **Maven**: Quản lý dependencies và build project. Tự động download các thư viện như MySQL connector, Servlet API, etc.
- **Docker**: Chạy MySQL trong container, không cần cài đặt MySQL trực tiếp
- **Tomcat**: Web server để host ứng dụng Java web (Servlet + JSP)

### Kiểm tra phiên bản:
```bash
java -version          # Kiểm tra Java đã cài chưa
mvn -version           # Kiểm tra Maven đã cài chưa  
docker --version       # Kiểm tra Docker đã cài chưa
docker-compose --version  # Kiểm tra Docker Compose
```

---

## ⚙️ Cài Đặt và Setup

### Bước 1: Clone hoặc Download Project
```bash
git clone <repository-url>
cd JavaLab
```

### Bước 2: Setup MySQL với Docker (Khuyên dùng)

#### Option A: Sử dụng Docker Compose (Dễ nhất)
Tạo file `docker-compose.yml` trong thư mục root:
```yaml
version: '3.8'
services:
  mysql:
    image: mysql:8.0
    container_name: bookstore_mysql
    environment:
      MYSQL_ROOT_PASSWORD: rootpassword
      MYSQL_DATABASE: bookstore
      MYSQL_USER: bookstore_user
      MYSQL_PASSWORD: bookstore_pass
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql
      - ./database/bookstore.sql:/docker-entrypoint-initdb.d/bookstore.sql
    restart: unless-stopped

volumes:
  mysql_data:
```

Chạy container:
```bash
# Start MySQL container
docker-compose up -d

# Kiểm tra container đang chạy
docker-compose ps

# Xem logs nếu cần
docker-compose logs mysql

# Stop container
docker-compose down
```

#### Option B: Chạy MySQL Container trực tiếp
```bash
# Pull MySQL image
docker pull mysql:8.0

# Run MySQL container
docker run -d \
  --name bookstore_mysql \
  -e MYSQL_ROOT_PASSWORD=rootpassword \
  -e MYSQL_DATABASE=bookstore \
  -e MYSQL_USER=bookstore_user \
  -e MYSQL_PASSWORD=bookstore_pass \
  -p 3306:3306 \
  -v mysql_data:/var/lib/mysql \
  mysql:8.0

# Kiểm tra container
docker ps

# Import database (sau khi container đã khởi động)
docker exec -i bookstore_mysql mysql -u root -prootpassword bookstore < database/bookstore.sql
```

#### Kết nối vào MySQL Container:
```bash
# Connect to MySQL
docker exec -it bookstore_mysql mysql -u root -p

# Hoặc với user bookstore_user
docker exec -it bookstore_mysql mysql -u bookstore_user -p
```

### Bước 2 (Alternative): Cài đặt MySQL Server trực tiếp
*Nếu không muốn dùng Docker:*
1. Tải và cài đặt MySQL Server từ [mysql.com](https://dev.mysql.com/downloads/)
2. Khởi động MySQL Service
3. Tạo user và database (xem phần Cấu hình Database)

### Bước 3: Cài đặt Apache Tomcat
1. Tải Apache Tomcat 9.0 từ [tomcat.apache.org](https://tomcat.apache.org/)
2. Giải nén và thiết lập `CATALINA_HOME`
3. Cấu hình port (mặc định 8080)

### Bước 4: Cấu hình Database Connection
Chỉnh sửa file `src/main/java/util/DBConnection.java` tương ứng với cách setup:

#### Nếu dùng Docker Compose:
```java
private static final String URL = "jdbc:mysql://localhost:3306/bookstore?useSSL=false&serverTimezone=UTC";
private static final String USERNAME = "bookstore_user";
private static final String PASSWORD = "bookstore_pass";
```

#### Nếu dùng MySQL trực tiếp:
```java
private static final String URL = "jdbc:mysql://localhost:3306/bookstore?useSSL=false&serverTimezone=UTC";
private static final String USERNAME = "your_mysql_username";  // Thay đổi
private static final String PASSWORD = "your_mysql_password";  // Thay đổi
```

### Bước 5: Build Project với Maven
```bash
# Clean project (xóa target folder)
mvn clean

# Compile source code
mvn compile

# Package thành WAR file
mvn package

# Clean + Compile + Package
mvn clean package

# Chạy test (nếu có)
mvn test

# Install dependencies và build
mvn clean install
```

### 📚 Giải thích Maven Commands:
- `mvn clean` - Xóa folder `target/` (compiled files)
- `mvn compile` - Compile Java source code thành .class files
- `mvn package` - Tạo file WAR để deploy
- `mvn test` - Chạy unit tests 
- `mvn install` - Build và install vào local repository

**Output**: File `target/bookstore.war` sẽ được tạo để deploy lên Tomcat

---

## 📁 Cấu Trúc Project

```
JavaLab/
├── 📄 pom.xml                     # Maven configuration
├── 📄 README.md                   # Tài liệu project
├── 📄 SETUP_GUIDE.md             # File hướng dẫn này
├── 📄 docker-compose.yml         # Docker Compose cho MySQL
│
├── 📂 database/                   # Database scripts
│   └── bookstore.sql             # SQL script tạo database
│
├── 📂 src/
│   └── 📂 main/
│       ├── 📂 java/              # Java source code
│       │   ├── 📂 controller/     # Servlet Controllers
│       │   │   ├── BookServlet.java      # Xử lý books
│       │   │   ├── LoginServlet.java     # Xử lý login
│       │   │   └── RegisterServlet.java  # Xử lý register
│       │   │
│       │   ├── 📂 dao/           # Data Access Objects
│       │   │   ├── BaseDAO.java          # Base DAO class
│       │   │   ├── AccountDAO.java       # Account operations
│       │   │   ├── BookDAO.java          # Book operations
│       │   │   ├── CustomerDAO.java      # Customer operations
│       │   │   └── OrderDAO.java         # Order operations
│       │   │
│       │   ├── 📂 model/         # Entity Models
│       │   │   ├── Account.java          # Account entity
│       │   │   ├── Address.java          # Address entity
│       │   │   ├── Book.java             # Book entity
│       │   │   ├── CartItem.java         # Cart item entity
│       │   │   ├── Customer.java         # Customer entity
│       │   │   ├── Order.java            # Order entity
│       │   │   └── OrderItem.java        # Order item entity
│       │   │
│       │   └── 📂 util/          # Utility classes
│       │       ├── DBConnection.java     # Database connection
│       │       └── PasswordUtil.java     # Password utilities
│       │
│       └── 📂 webapp/            # Web resources
│           ├── 📄 index.jsp               # Trang chủ
│           ├── 📂 css/                    # CSS files
│           │   └── style.css
│           ├── 📂 js/                     # JavaScript files
│           ├── 📂 jsp/                    # JSP pages
│           │   ├── book-list.jsp          # Danh sách sách
│           │   ├── login.jsp              # Trang login
│           │   └── register.jsp           # Trang đăng ký
│           └── 📂 WEB-INF/
│               └── web.xml                # Web configuration
│
├── 📂 target/                    # Compiled files (auto-generated)
│   ├── 📂 classes/              # Compiled Java classes
│   └── bookstore.war            # WAR file for deployment
│
└── 📂 lib/                      # External libraries (if any)
```

---

## 🗄️ Cấu Hình Database

### Option A: Với Docker (Khuyên dùng)

#### Nếu dùng Docker Compose:
Database sẽ tự động được tạo và import khi container khởi động lần đầu.

```bash
# Start container (nếu chưa start)
docker-compose up -d

# Kiểm tra database đã được tạo
docker exec -it bookstore_mysql mysql -u bookstore_user -pbookstore_pass -e "SHOW DATABASES;"

# Kiểm tra tables
docker exec -it bookstore_mysql mysql -u bookstore_user -pbookstore_pass bookstore -e "SHOW TABLES;"
```

#### Nếu cần import manual:
```bash
# Copy SQL file vào container
docker cp database/bookstore.sql bookstore_mysql:/tmp/

# Import vào database
docker exec -it bookstore_mysql mysql -u root -prootpassword bookstore -e "source /tmp/bookstore.sql"
```

### Option B: Với MySQL Server trực tiếp

### Bước 1: Tạo Database
```sql
mysql -u root -p
```

### Bước 2: Chạy SQL Script
```sql
source /path/to/database/bookstore.sql;
-- Hoặc copy-paste nội dung file bookstore.sql
```

### Bước 3: Kiểm tra Database
```sql
USE bookstore;
SHOW TABLES;
DESCRIBE customers;
DESCRIBE books;
DESCRIBE accounts;
```

### Schema Database:
- **customers**: Thông tin khách hàng
- **addresses**: Địa chỉ khách hàng
- **accounts**: Tài khoản đăng nhập
- **books**: Thông tin sách
- **orders**: Đơn hàng
- **order_items**: Chi tiết đơn hàng

---

## 🔧 Hiểu về Maven Dependencies

Project này sử dụng Maven để quản lý các thư viện trong `pom.xml`:

### Core Dependencies:
```xml
<!-- MySQL Database Driver -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.33</version>
</dependency>

<!-- Servlet API cho web development -->
<dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>javax.servlet-api</artifactId>
    <version>4.0.1</version>
</dependency>

<!-- JSP API cho dynamic web pages -->
<dependency>
    <groupId>javax.servlet.jsp</groupId>
    <artifactId>javax.servlet.jsp-api</artifactId>
    <version>2.3.3</version>
</dependency>
```

### Tại sao cần Maven?
- ✅ **Tự động download**: Không cần manual download JAR files
- ✅ **Version management**: Quản lý phiên bản tự động
- ✅ **Dependency resolution**: Tự động resolve transitive dependencies
- ✅ **Build automation**: Standardized build process
- ✅ **IDE integration**: Tích hợp tốt với Eclipse, IntelliJ

### Không có Maven thì sao?
- ❌ Phải download 10+ JAR files manually
- ❌ Phải manage classpath manually  
- ❌ Compatibility issues giữa các versions
- ❌ Không có standardized project structure

---

## 🚀 Chạy Ứng Dụng

### Method 1: Deploy trên Tomcat
```bash
# 1. Build WAR file
mvn clean package

# 2. Copy WAR file vào Tomcat webapps
cp target/bookstore.war $CATALINA_HOME/webapps/

# 3. Start Tomcat
$CATALINA_HOME/bin/startup.sh  # Linux/Mac
$CATALINA_HOME/bin/startup.bat # Windows

# 4. Truy cập ứng dụng
# http://localhost:8080/bookstore
```

### Method 2: Sử dụng Maven Tomcat Plugin
```bash
# Configure Tomcat manager (trong settings.xml hoặc pom.xml)
mvn tomcat7:deploy

# Hoặc redeploy nếu đã deploy trước đó
mvn tomcat7:redeploy
```

### Method 3: Chạy trong IDE
1. Import project vào IDE (Eclipse/IntelliJ)
2. Cấu hình Tomcat server trong IDE
3. Deploy project lên server
4. Start server và truy cập ứng dụng

---

## 🎯 Tính Năng Chính

### User Features:
- ✅ **Đăng ký tài khoản** - RegisterServlet
- ✅ **Đăng nhập/Đăng xuất** - LoginServlet
- ✅ **Xem danh sách sách** - BookServlet
- ✅ **Tìm kiếm sách** - BookServlet
- ✅ **Thêm sách vào giỏ hàng** - CartItem model
- ✅ **Quản lý địa chỉ** - Address model
- ✅ **Đặt hàng** - OrderServlet

### Admin Features:
- ✅ **Quản lý sách** (CRUD) - BookDAO
- ✅ **Quản lý khách hàng** - CustomerDAO
- ✅ **Quản lý đơn hàng** - OrderDAO

### Technical Features:
- ✅ **Database Connection Pooling** - DBConnection
- ✅ **Password Hashing** - PasswordUtil
- ✅ **MVC Architecture** - Controller/DAO/Model
- ✅ **Session Management** - web.xml
- ✅ **Error Handling** - web.xml error pages

---

## 🛠️ Troubleshooting

### Lỗi thường gặp:

#### 1. **Database Connection Error**
```
Error: java.sql.SQLException: Access denied for user 'root'@'localhost'
```
**Giải pháp:**
- **Với Docker**: Kiểm tra container có chạy: `docker ps`
- **Với Docker**: Kiểm tra credentials trong docker-compose.yml
- Cập nhật thông tin trong `DBConnection.java`
- Đảm bảo MySQL server/container đang chạy

#### 1b. **MySQL Connection Refused**
```
Error: java.sql.SQLException: Connection refused
```
**Giải pháp với Docker:**
```bash
# Kiểm tra container status
docker-compose ps

# Restart container nếu cần
docker-compose restart mysql

# Kiểm tra logs
docker-compose logs mysql

# Kiểm tra port mapping
docker port bookstore_mysql
```

#### 2. **ClassNotFoundException: com.mysql.cj.jdbc.Driver**
```
Error: ClassNotFoundException: com.mysql.cj.jdbc.Driver
```
**Giải pháp:**
- Kiểm tra MySQL connector trong `pom.xml`
- Chạy `mvn clean install` để download dependencies
- Đảm bảo MySQL connector có trong classpath

#### 3. **Port 8080 đã được sử dụng**
```
Error: Port 8080 required by Tomcat Server is already in use
```
**Giải pháp:**
- Thay đổi port trong `server.xml` của Tomcat
- Hoặc kill process đang sử dụng port 8080:
```bash
# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Linux/Mac
lsof -ti:8080 | xargs kill -9
```

#### 4. **Maven Build Failed**
```
Error: Maven build failed
```
**Giải pháp:**
```bash
# Clear Maven cache
mvn dependency:purge-local-repository

# Reinstall dependencies
mvn clean install -U

# Skip tests nếu cần
mvn clean package -DskipTests
```

#### 5. **JSP Compilation Error**
**Giải pháp:**
- Kiểm tra syntax JSP
- Đảm bảo các taglib được import đúng
- Kiểm tra web.xml configuration

---

## 📝 Development Notes

### Code Standards:
- Sử dụng **MVC Pattern**
- **DAO Pattern** cho database operations
- **Singleton Pattern** cho database connection
- Proper **exception handling**
- **Input validation** và **SQL injection prevention**

### Database Best Practices:
- Sử dụng **prepared statements**
- **Connection pooling** với HikariCP
- **Transaction management**
- Proper **indexing** trên các foreign keys

### Security Features:
- **Password hashing** với BCrypt
- **Session management**
- **XSS prevention**
- **CSRF protection** (cần implement thêm)

---

## 📞 Hỗ Trợ

Nếu gặp vấn đề trong quá trình setup, hãy:
1. Kiểm tra log files trong `$CATALINA_HOME/logs/`
2. Xem Maven console output
3. Kiểm tra database connectivity
4. Đọc error messages cẩn thận

## 📚 Tài Liệu Tham Khảo

- [Java Servlet Documentation](https://docs.oracle.com/javaee/7/tutorial/servlets.htm)
- [JSP Documentation](https://docs.oracle.com/javaee/7/tutorial/jsf-page.htm)
- [Maven Documentation](https://maven.apache.org/guides/)
- [Tomcat Documentation](https://tomcat.apache.org/tomcat-9.0-doc/)
- [MySQL Documentation](https://dev.mysql.com/doc/)
- [Docker Documentation](https://docs.docker.com/)
- [Docker Compose Documentation](https://docs.docker.com/compose/)

---

## 🐳 Quick Start với Docker

### Chỉ với 4 lệnh để chạy project:

```bash
# 1. Start MySQL container
docker-compose up -d

# 2. Đợi 30 giây để MySQL khởi động hoàn toàn
timeout /t 30

# 3. Build project
mvn clean package

# 4. Deploy lên Tomcat
# Copy target/bookstore.war vào Tomcat webapps hoặc chạy trong IDE
```

### Useful Docker Commands:

```bash
# Xem status containers
docker-compose ps

# Xem logs MySQL
docker-compose logs -f mysql

# Connect vào MySQL
docker exec -it bookstore_mysql mysql -u bookstore_user -pbookstore_pass bookstore

# Stop containers
docker-compose down

# Stop và xóa data (cẩn thận!)
docker-compose down -v

# Restart MySQL container
docker-compose restart mysql
```

### Database Test Commands:
```bash
# Test connection
docker exec -it bookstore_mysql mysql -u bookstore_user -pbookstore_pass -e "SELECT 1"

# Show tables
docker exec -it bookstore_mysql mysql -u bookstore_user -pbookstore_pass bookstore -e "SHOW TABLES;"

# Count records
docker exec -it bookstore_mysql mysql -u bookstore_user -pbookstore_pass bookstore -e "SELECT COUNT(*) FROM books;"
```

---

## ✨ Features to Implement (Future)

- [ ] **Shopping Cart Session Management**
- [ ] **Payment Integration**
- [ ] **Email Notifications**
- [ ] **File Upload for Book Images**
- [ ] **Admin Dashboard**
- [ ] **REST API Endpoints**
- [ ] **Unit Testing**
- [ ] **Docker Configuration**
- [ ] **CI/CD Pipeline**

---

**Happy Coding! 🎉**
