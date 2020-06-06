package com.anastasia.dao;

import com.anastasia.data.Book;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 13.05.2020.
 * @author Kodoma.
 */
public class SQLBookDao implements BookDao {

    private final Connection connection;

    public SQLBookDao() throws IOException, SQLException {
        final String url = "jdbc:mysql://localhost:3306/?serverTimezone=UTC";
        final String userName = "root";
        final String userPassword = "password";

        connection = DriverManager.getConnection(url, userName, userPassword);

        connection.prepareStatement("drop database if exists book_store").execute();
        connection.prepareStatement("create database book_store").execute();
        connection.prepareStatement("use book_store").execute();

        executeFileScript(getClass().getResource("/sql/create_tb.sql").getFile());
        executeFileScript(getClass().getResource("/sql/delete_query.sql").getFile());
        executeFileScript(getClass().getResource("/sql/insert_tb.sql").getFile());
        executeFileScript(getClass().getResource("/sql/search.sql").getFile());
        executeFileScript(getClass().getResource("/sql/select_all_tb.sql").getFile());
        executeFileScript(getClass().getResource("/sql/truncate_tb.sql").getFile());
        executeFileScript(getClass().getResource("/sql/update_query.sql").getFile());

        connection.prepareStatement("call create_tb('Book')").execute();
    }

    private void executeFileScript(String file) throws IOException, SQLException {
        final BufferedReader in = new BufferedReader(new FileReader(file));
        final StringBuilder sb = new StringBuilder();

        String str;
        while ((str = in.readLine()) != null) {
            sb.append(str).append("\n ");
        }
        in.close();
        connection.createStatement().executeUpdate(sb.toString());
    }

    @Override
    public Book saveBook(Book book) throws SQLException {
        final PreparedStatement ps = connection.prepareStatement("call insert_tb(?, ?)");

        ps.setString(1, book.getName());
        ps.setString(2, book.getAuthor());

        final ResultSet resultSet = ps.executeQuery();

        while (resultSet.next()) {
            final Book result = new Book();

            result.setId(resultSet.getInt(1));
            result.setName(book.getName());
            result.setAuthor(book.getAuthor());

            return result;
        }
        return null;
    }

    @Override
    public void updateBook(Book book) throws SQLException {
        final PreparedStatement ps = connection.prepareStatement("call update_query(?, ?, ?, ?, ?)");

        ps.setString(1, "book");
        ps.setString(2, "name");
        ps.setString(3, book.getName());
        ps.setString(4, "id");
        ps.setInt(5, book.getId());

        ps.executeQuery();
    }

    @Override
    public void removeBook(Book book) throws SQLException {
        final PreparedStatement ps = connection.prepareStatement("call delete_query(?, ?, ?)");

        ps.setString(1, "book");
        ps.setString(2, "name");
        ps.setString(3, book.getName());

        ps.executeQuery();
    }

    @Override
    public void removeAllBooks() throws SQLException {
        final PreparedStatement ps = connection.prepareStatement("call truncate_tb(?)");

        ps.setString(1, "book");
        ps.executeQuery();
    }

    @Override
    public List<Book> getAllBooks() throws SQLException {
        final PreparedStatement ps = connection.prepareStatement("call select_all_tb('Book')");
        final ResultSet resultSet = ps.executeQuery();
        final List<Book> result = new ArrayList<>();

        while (resultSet.next()) {
            final Book book = new Book();

            book.setId(resultSet.getInt(1));
            book.setName(resultSet.getString(2));
            book.setAuthor(resultSet.getString(3));

            result.add(book);
        }
        return result;
    }

    @Override
    public List<Book> searchBooksByName(String name) throws SQLException {
        final PreparedStatement ps = connection.prepareStatement("call search('Book', 'name', ?)");

        ps.setString(1, name);

        final ResultSet resultSet = ps.executeQuery();
        final List<Book> result = new ArrayList<>();

        while (resultSet.next()) {
            final Book book = new Book();

            book.setId(resultSet.getInt(1));
            book.setName(resultSet.getString(2));
            book.setAuthor(resultSet.getString(3));

            result.add(book);
        }
        return result;
    }
}
