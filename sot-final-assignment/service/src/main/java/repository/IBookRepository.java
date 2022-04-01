package repository;

import model.Book;

import java.util.Collection;
import java.util.List;

public interface IBookRepository {
    int count();

    Collection<Book> getAll();

    Book getById(int id);

    List<Book> filterBooksByName(String name);

    Book getBookByIndex(int index);

    void deleteById(int book);

    Book add(String name);

    void update(Book book);
}
