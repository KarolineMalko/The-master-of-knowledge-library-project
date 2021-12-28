/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package beans;

/**
 *
 * @author garom
 */
public class VisitorCurrentRental {
    private int isbn;
    private String title;
    private String author;
    private String language;
    private String rental_date;
    private String return_date;

    public VisitorCurrentRental() {
    }

    public int getIsbn() {
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

    public String getRental_date() {
        return rental_date;
    }

    public String getReturn_date() {
        return return_date;
    }

    public void setIsbn(int isbn) {
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

    public void setRental_date(String rental_date) {
        this.rental_date = rental_date;
    }

    public void setReturn_date(String return_date) {
        this.return_date = return_date;
    }
    
    
}
