package repository;

import model.Book;
import model.Order;
import model.Subject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FakeOrderRepository implements IOrderRepository {

    private static FakeOrderRepository instance = new FakeOrderRepository();
    private final List<Order> orders = new ArrayList<>();
    private final FakeBookRepository books = FakeBookRepository.getInstance();
    private final FakeSubjectRepository subjects = FakeSubjectRepository.getInstance();

    public Collection<Order> getAll() {
        return orders;
    }

    public static FakeOrderRepository getInstance() {
        return instance;
    }


    @Override
    public List<Order> getAllOrders() {
        return orders;
    }

    @Override
    public Subject getSubjectByName(String name) {
        for (Subject subject : subjects.getAll()) {
            if (subject.getName().equals(name)) return subject;
        }
        return null;
    }

    @Override
    public Book getBookByName(String name) {
        for (Book book : books.getAll()) {
            if (book.getName().equals(name)) return book;
        }
        return null;
    }

    @Override
    public Order add(Order order) {
        order.setId();
        orders.add(order);
        return order;
    }
}
