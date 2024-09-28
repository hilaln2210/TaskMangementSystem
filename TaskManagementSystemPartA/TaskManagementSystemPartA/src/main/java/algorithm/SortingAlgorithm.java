package algorithm;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class SortingAlgorithm<T> implements IAlgoTaskManager<T> {
    @Override
    public List<T> sort(List<T> tasks, Comparator<T> comparator) {
        List<T> sortedList = new ArrayList<>(tasks);
        sortedList.sort(comparator);
        return sortedList;
    }

    @Override
    public List<T> search(List<T> tasks, Predicate<T> searchCriteria) {
        throw new UnsupportedOperationException("Search operation is not supported in SortingAlgorithm");
    }
}