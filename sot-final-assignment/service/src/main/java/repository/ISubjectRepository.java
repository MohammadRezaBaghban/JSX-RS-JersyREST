package repository;

import model.Subject;

import java.util.Collection;
import java.util.List;
public interface ISubjectRepository {
    int count();

    Collection<Subject> getAll();

    Subject getById(int id);

    List<Subject> filterSubjectByName(String subjectName);

    Subject getSubjectByIndex(int index);

    void deleteById(int bookId);

    Subject add(Subject book);

    void update(int bookId, Subject book);
}
