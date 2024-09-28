package algorithm;

import java.util.List;
import java.util.function.Predicate;
import java.util.Comparator;

public interface IAlgoTaskManager<T> {
    List<T> sort(List<T> tasks, Comparator<T> comparator);
    List<T> search(List<T> tasks, Predicate<T> searchCriteria);
}