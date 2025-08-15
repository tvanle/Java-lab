# Bookstore Online - Java Web Application

## 📖 Project Overview
A comprehensive online bookstore management system built with Java Servlet, JSP, and MySQL. This application provides a complete e-commerce solution for book sales with user authentication, product management, and order processing capabilities.

## 🚀 Features

### Customer Features
- User registration and authentication
- Browse and search books by title, author, or category
- View detailed book information
- Shopping cart management
- Order placement and tracking
- Order history

### Admin Features
- Book inventory management (CRUD operations)
- Order management
- User management
- Sales reports

## 🛠️ Technology Stack
- **Backend**: Java 8, Servlet 4.0, JSP 2.3
- **Database**: MySQL 8.0
- **Frontend**: HTML5, CSS3, JavaScript, Bootstrap 5
- **Build Tool**: Maven 3.6+
- **Server**: Apache Tomcat 9.0

## 📋 Prerequisites
- JDK 8 or higher
- Apache Tomcat 9.0
- MySQL 8.0
- Maven 3.6+
- Git

## 🔧 Installation & Setup

### 1. Clone the Repository
```bash
git clone https://github.com/yourusername/bookstore-online.git
cd bookstore-online
```

### 2. Database Setup
```bash
# Login to MySQL
mysql -u root -p

# Create database
CREATE DATABASE bookstore;
USE bookstore;

# Import database schema
source database/bookstore.sql
```

### 3. Configure Database Connection
Edit `src/main/java/util/DBConnection.java`:
```java
private static final String URL = "jdbc:mysql://localhost:3306/bookstore";
private static final String USERNAME = "root";
private static final String PASSWORD = "your_password";
```

### 4. Build the Project
```bash
mvn clean install
```

### 5. Deploy to Tomcat

#### Option A: Using Maven
```bash
mvn tomcat7:deploy
```

#### Option B: Manual Deployment
1. Copy `target/bookstore.war` to `tomcat/webapps/`
2. Start Tomcat:
```bash
# Windows
tomcat/bin/startup.bat

# Linux/Mac
tomcat/bin/startup.sh
```

### 6. Access the Application
Open your browser and navigate to:
```
http://localhost:8080/bookstore
```

## 📱 Default Credentials

### Admin Account
- Username: `admin`
- Password: `admin123`

### Sample Customer Account
- Username: `johndoe`
- Password: `password123`

## 📂 Project Structure
```
bookstore-online/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── controller/    # Servlet controllers
│   │   │   ├── model/         # Entity classes
│   │   │   ├── dao/           # Data Access Objects
│   │   │   └── util/          # Utility classes
│   │   └── webapp/
│   │       ├── WEB-INF/       # Configuration files
│   │       ├── jsp/           # JSP views
│   │       ├── css/           # Stylesheets
│   │       └── js/            # JavaScript files
├── database/                   # SQL scripts
├── lib/                       # External libraries
├── pom.xml                    # Maven configuration
└── README.md
```

## 🧪 Testing

### Run Unit Tests
```bash
mvn test
```

### Manual Testing Checklist
1. ✅ User registration with validation
2. ✅ User login/logout
3. ✅ Book search and filtering
4. ✅ Add to cart functionality
5. ✅ Order placement
6. ✅ Admin CRUD operations
7. ✅ Order status updates

## 🚦 API Endpoints

### Authentication
- `POST /register` - New user registration
- `POST /login` - User authentication
- `GET /logout` - User logout

### Books
- `GET /books` - List all books
- `GET /book?id={id}` - Get book details
- `POST /admin/books` - Add new book (Admin)
- `PUT /admin/books/{id}` - Update book (Admin)
- `DELETE /admin/books/{id}` - Delete book (Admin)

### Orders
- `POST /orders` - Create new order
- `GET /orders` - Get user orders
- `GET /orders/{id}` - Get order details

## 🐛 Troubleshooting

### Common Issues

1. **Database Connection Error**
   - Verify MySQL is running
   - Check database credentials in `DBConnection.java`
   - Ensure database exists

2. **404 Error on Deployment**
   - Verify WAR file is in `tomcat/webapps`
   - Check Tomcat logs for errors
   - Ensure context path is correct

3. **Login Issues**
   - Verify password hashing is consistent
   - Check session configuration
   - Clear browser cookies

## 📝 Development Notes

### Adding New Features
1. Create model class in `src/main/java/model/`
2. Create DAO class in `src/main/java/dao/`
3. Create Servlet controller in `src/main/java/controller/`
4. Create JSP views in `src/main/webapp/jsp/`
5. Update `web.xml` if needed

### Database Migrations
Place SQL scripts in `database/migrations/` with naming convention:
- `V1__initial_schema.sql`
- `V2__add_reviews_table.sql`

## 🤝 Contributing
1. Fork the repository
2. Create feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Open Pull Request

## 📄 License
This project is licensed under the MIT License - see the LICENSE file for details.

## 👥 Authors
- Your Name - Initial work

## 🙏 Acknowledgments
- Bootstrap for UI components
- MySQL for database
- Apache Tomcat for server
- Maven for build automation