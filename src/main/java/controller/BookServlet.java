package controller;

import dao.BookDAO;
import model.Book;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

@WebServlet({"/books", "/book", "/admin/books/*"})
public class BookServlet extends HttpServlet {
    private BookDAO bookDAO;
    
    @Override
    public void init() throws ServletException {
        bookDAO = new BookDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        String servletPath = request.getServletPath();
        
        try {
            if ("/book".equals(servletPath)) {
                showBookDetail(request, response);
            } else if (servletPath.startsWith("/admin")) {
                handleAdminRequest(request, response, pathInfo);
            } else {
                listBooks(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Database error: " + e.getMessage());
            request.getRequestDispatcher("/jsp/error.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        try {
            if ("create".equals(action)) {
                createBook(request, response);
            } else if ("update".equals(action)) {
                updateBook(request, response);
            } else if ("delete".equals(action)) {
                deleteBook(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Database error: " + e.getMessage());
            request.getRequestDispatcher("/jsp/error.jsp").forward(request, response);
        }
    }
    
    private void listBooks(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        String category = request.getParameter("category");
        String search = request.getParameter("search");
        
        List<Book> books;
        if (search != null && !search.trim().isEmpty()) {
            books = bookDAO.search(search);
            request.setAttribute("searchTerm", search);
        } else if (category != null && !category.trim().isEmpty()) {
            books = bookDAO.findByCategory(category);
            request.setAttribute("selectedCategory", category);
        } else {
            books = bookDAO.findAll();
        }
        
        List<String> categories = bookDAO.getAllCategories();
        
        request.setAttribute("books", books);
        request.setAttribute("categories", categories);
        request.getRequestDispatcher("/jsp/book-list.jsp").forward(request, response);
    }
    
    private void showBookDetail(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        String bookId = request.getParameter("id");
        if (bookId != null) {
            Book book = bookDAO.findById(Integer.parseInt(bookId));
            if (book != null) {
                request.setAttribute("book", book);
                request.getRequestDispatcher("/jsp/book-detail.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/books");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/books");
        }
    }
    
    private void handleAdminRequest(HttpServletRequest request, HttpServletResponse response, String pathInfo) 
            throws SQLException, ServletException, IOException {
        if (pathInfo == null || "/".equals(pathInfo)) {
            List<Book> books = bookDAO.findAll();
            request.setAttribute("books", books);
            request.getRequestDispatcher("/jsp/admin/book-list.jsp").forward(request, response);
        } else if ("/new".equals(pathInfo)) {
            List<String> categories = bookDAO.getAllCategories();
            request.setAttribute("categories", categories);
            request.getRequestDispatcher("/jsp/admin/book-form.jsp").forward(request, response);
        } else if (pathInfo.startsWith("/edit/")) {
            String bookId = pathInfo.substring(6);
            Book book = bookDAO.findById(Integer.parseInt(bookId));
            List<String> categories = bookDAO.getAllCategories();
            request.setAttribute("book", book);
            request.setAttribute("categories", categories);
            request.getRequestDispatcher("/jsp/admin/book-form.jsp").forward(request, response);
        }
    }
    
    private void createBook(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        Book book = extractBookFromRequest(request);
        bookDAO.insert(book);
        response.sendRedirect(request.getContextPath() + "/admin/books");
    }
    
    private void updateBook(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        Book book = extractBookFromRequest(request);
        book.setId(Integer.parseInt(request.getParameter("id")));
        bookDAO.update(book);
        response.sendRedirect(request.getContextPath() + "/admin/books");
    }
    
    private void deleteBook(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        int bookId = Integer.parseInt(request.getParameter("id"));
        bookDAO.delete(bookId);
        response.sendRedirect(request.getContextPath() + "/admin/books");
    }
    
    private Book extractBookFromRequest(HttpServletRequest request) {
        Book book = new Book();
        book.setTitle(request.getParameter("title"));
        book.setAuthor(request.getParameter("author"));
        book.setIsbn(request.getParameter("isbn"));
        book.setPrice(new BigDecimal(request.getParameter("price")));
        book.setQuantity(Integer.parseInt(request.getParameter("quantity")));
        book.setCategory(request.getParameter("category"));
        book.setDescription(request.getParameter("description"));
        book.setImageUrl(request.getParameter("imageUrl"));
        return book;
    }
    
    @Override
    public void destroy() {
        if (bookDAO != null) {
            bookDAO.closeConnection();
        }
    }
}