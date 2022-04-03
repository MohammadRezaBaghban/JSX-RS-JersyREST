package repository;

import model.Book;
import model.Order;
import model.Subject;

import java.util.Collection;
import java.util.List;

public interface IOrderRepository {

    List<Order> getAllOrders();
    Subject getSubjectByName(String name);
    Book getBookByName(String name);
    Order add(Order order);
}
