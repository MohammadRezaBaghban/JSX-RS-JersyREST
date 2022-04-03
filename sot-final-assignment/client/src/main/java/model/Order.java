package model;

import java.util.Objects;

public class Order {

    private static int orderId = 0;
    private int id;
    private String bookName;
    private String subjectName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id && Objects.equals(bookName, order.bookName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, bookName);
    }

    public int getId() {
        return id;
    }

    public void setId() {
        this.id = orderId++;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public Order(){}

    public Order(String name, String subjectName) {
        this.id = ++orderId;
        this.bookName = name;
        this.subjectName = subjectName;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", bookName='" + bookName + '\'' +
                ", subjectName='" + subjectName + '\'' +
                '}';
    }
}
