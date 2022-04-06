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

    void deleteById(int subjectId);

    Subject add(Subject subject);

    void update(int subjectId, Subject subject);
}
