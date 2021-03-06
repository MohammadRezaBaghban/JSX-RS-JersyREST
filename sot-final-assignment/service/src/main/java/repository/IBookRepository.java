package repository;

import model.Book;

import java.util.Collection;
import java.util.List;

public interface IBookRepository {
    int count();

    Collection<Book> getAll();

    Book getById(int id);

    List<Book> filterBooksBySubject(String subjectName);

    Book getBookByIndex(int index);

    void deleteById(int bookId);

    Book add(Book book);

    void update(int bookId, Book book);
}
