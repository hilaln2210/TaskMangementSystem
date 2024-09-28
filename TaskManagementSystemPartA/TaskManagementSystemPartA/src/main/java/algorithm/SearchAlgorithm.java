package algorithm;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class SearchAlgorithm<T> implements IAlgoTaskManager<T> {
    @Override
    public List<T> sort(List<T> tasks, Comparator<T> comparator) {
        throw new UnsupportedOperationException("Sort operation is not supported in SearchAlgorithm");
    }

    @Override
    public List<T> search(List<T> tasks, Predicate<T> searchCriteria) {
        List<T> result = new ArrayList<>();
        for (T task : tasks) {
            if (searchCriteria.test(task)) {
                result.add(task);
            }
        }
        return result;
    }
}