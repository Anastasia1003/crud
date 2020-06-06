package com.anastasia.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

/**
 * Created on 22.10.2019.
 * @author Kodoma.
 */
@XmlRootElement(name = "book")
@XmlAccessorType(XmlAccessType.FIELD)
public class Book implements Comparable {

    private int id;
    private String name;
    private String author;

    public Book() {
    }

    public Book(int id, String name, String author) {
        this.id = id;
        this.name = name;
        this.author = author;
    }

    @Override
    public int compareTo(Object o) {
        return Long.compare(this.id, ((Book)o).getId());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Book book = (Book)o;
        return Objects.equals(id, book.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
