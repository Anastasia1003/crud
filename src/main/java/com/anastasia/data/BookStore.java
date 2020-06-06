package com.anastasia.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created on 22.10.2019.
 * @author Kodoma.
 */
@XmlRootElement(name = "bookStore")
@XmlAccessorType(XmlAccessType.FIELD)
public class BookStore {

    @XmlElementWrapper(name = "books")
    @XmlElement(name = "book")
    private Set<Book> books;

    public BookStore() {
        books = new TreeSet<>();
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    public void addBook(Book book) {
        if (this.books == null) {
            this.books = new TreeSet<>();
        }
        this.books.add(book);
    }
}
