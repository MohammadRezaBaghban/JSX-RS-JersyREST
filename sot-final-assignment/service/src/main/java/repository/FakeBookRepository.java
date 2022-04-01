package repository;

import model.Book;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FakeBookRepository implements IBookRepository {

    private static FakeBookRepository instance = new FakeBookRepository();
    private final List<Book> books = new ArrayList<>();

    private FakeBookRepository() {
        books.add(new Book("Calculus I","Mathematics",15.0));
        books.add(new Book("Probability and statistics","Mathematics",19.0));
        books.add(new Book("Linear Algebra & Application","Mathematics",34.0));
        books.add(new Book("Object Oriented Programming with C#","Computer Science",28.0));
        books.add(new Book("Database & Data Modelling","Computer Science",75.0));
        books.add(new Book("Chemistry","Natural Science",15.0));
        books.add(new Book("Physics","Natural Science",15.0));
    }

    public static FakeBookRepository getInstance() {
        return instance;
    }

    @Override
    public int count() {
        return books.size();
    }

    @Override
    public Collection<Book> getAll() {
        return books;
    }

    @Override
    public Book getById(int id) {
        for (Book book : books) {
            if (book.getId() == id) return book;
        }
        return null;
    }

    @Override
    public List<Book> filterBooksByName(String name) {
        List<Book> booksResult = new ArrayList<>();
        System.out.println(name);
        for (Book book : books) {
            if (book.getName().equals(name) ) {
                booksResult.add(book);
            }
        }
        return booksResult;
    }

    @Override
    public Book getBookByIndex(int index) {
        if (books.size() < 1 || index < 0) return null;
        else return books.get(index);
    }

    @Override
    public void deleteById(int bookId) {
        for (Book book : books) {
            if (book.getId() == bookId) {
                books.remove(book);
                break;
            }
        }
    }

    @Override
    public Book add(Book book) {
        var bookObj = new Book(book.getName(),book.getSubject(),book.getPrice());
        books.add(bookObj);
        return bookObj;
    }

    @Override
    public void update(Book book) {
        var bookObj = this.getById(book.getId());
        if (bookObj != null){
            bookObj.setName(book.getName());
            bookObj.setPrice(book.getPrice());
            bookObj.setSubject(book.getSubject());
        }
    }
}
