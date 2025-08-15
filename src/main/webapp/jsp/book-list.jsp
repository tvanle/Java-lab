<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Books - Bookstore</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/">Bookstore Online</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav me-auto">
                    <li class="nav-item">
                        <a class="nav-link active" href="${pageContext.request.contextPath}/books">Books</a>
                    </li>
                    <c:if test="${sessionScope.role == 'ADMIN'}">
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/admin/books">Manage Books</a>
                        </li>
                    </c:if>
                </ul>
                <ul class="navbar-nav">
                    <c:choose>
                        <c:when test="${not empty sessionScope.username}">
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/cart">Cart</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/orders">My Orders</a>
                            </li>
                            <li class="nav-item">
                                <span class="nav-link">Welcome, ${sessionScope.username}</span>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/logout">Logout</a>
                            </li>
                        </c:when>
                        <c:otherwise>
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/login">Login</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/register">Register</a>
                            </li>
                        </c:otherwise>
                    </c:choose>
                </ul>
            </div>
        </div>
    </nav>

    <div class="container mt-4">
        <h2>Book Catalog</h2>
        
        <!-- Search and Filter -->
        <div class="row mb-4">
            <div class="col-md-6">
                <form action="${pageContext.request.contextPath}/books" method="get" class="d-flex">
                    <input type="text" name="search" class="form-control me-2" 
                           placeholder="Search books..." value="${searchTerm}">
                    <button type="submit" class="btn btn-primary">Search</button>
                </form>
            </div>
            <div class="col-md-6">
                <form action="${pageContext.request.contextPath}/books" method="get">
                    <select name="category" class="form-select" onchange="this.form.submit()">
                        <option value="">All Categories</option>
                        <c:forEach var="category" items="${categories}">
                            <option value="${category}" ${selectedCategory == category ? 'selected' : ''}>
                                ${category}
                            </option>
                        </c:forEach>
                    </select>
                </form>
            </div>
        </div>
        
        <!-- Book Grid -->
        <div class="row">
            <c:forEach var="book" items="${books}">
                <div class="col-md-3 mb-4">
                    <div class="card h-100">
                        <img src="${book.imageUrl != null ? book.imageUrl : '/images/default-book.jpg'}" 
                             class="card-img-top" alt="${book.title}" style="height: 300px; object-fit: cover;">
                        <div class="card-body d-flex flex-column">
                            <h5 class="card-title">${book.title}</h5>
                            <p class="card-text">
                                <small class="text-muted">by ${book.author}</small><br>
                                <span class="badge bg-secondary">${book.category}</span>
                            </p>
                            <p class="card-text flex-grow-1">
                                ${book.description != null && book.description.length() > 100 ? 
                                  book.description.substring(0, 100).concat('...') : book.description}
                            </p>
                            <div class="mt-auto">
                                <p class="card-text">
                                    <strong>$<fmt:formatNumber value="${book.price}" pattern="#,##0.00"/></strong>
                                    <c:choose>
                                        <c:when test="${book.quantity > 0}">
                                            <span class="badge bg-success">In Stock</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-danger">Out of Stock</span>
                                        </c:otherwise>
                                    </c:choose>
                                </p>
                                <div class="d-grid gap-2">
                                    <a href="${pageContext.request.contextPath}/book?id=${book.id}" 
                                       class="btn btn-outline-primary">View Details</a>
                                    <c:if test="${book.quantity > 0}">
                                        <form action="${pageContext.request.contextPath}/cart" method="post">
                                            <input type="hidden" name="bookId" value="${book.id}">
                                            <input type="hidden" name="action" value="add">
                                            <button type="submit" class="btn btn-primary w-100">Add to Cart</button>
                                        </form>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
        
        <c:if test="${empty books}">
            <div class="alert alert-info" role="alert">
                No books found. Try adjusting your search criteria.
            </div>
        </c:if>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>