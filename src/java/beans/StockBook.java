/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package beans;

import java.math.BigInteger;

/**
 *
 * @author Malko
 */
public class StockBook {
    private BigInteger isbn;
    private String title;
    private String author;
    private String language;
    private String publishDate;
    private int availableCopies;
    
    public StockBook() {
        
    }

    public BigInteger getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getLanguage() {
        return language;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    public void setIsbn(BigInteger isbn) {
        this.isbn = isbn;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }
   
    
}
