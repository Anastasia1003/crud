package com.anastasia.dao;

import com.anastasia.data.Book;
import com.anastasia.exceptions.DataAccessException;

import java.sql.SQLException;
import java.util.List;

/**
 * Created on 22.10.2019.
 * @author Kodoma.
 */
public interface BookDao {

    Book saveBook(Book book) throws SQLException;

    void updateBook(Book book) throws SQLException;

    void removeBook(Book book) throws SQLException;

    void removeAllBooks() throws SQLException;

    List<Book> getAllBooks() throws SQLException;

    List<Book> searchBooksByName(String name) throws SQLException;
}
