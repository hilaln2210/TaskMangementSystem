package algorithm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import java.util.Comparator;
import static org.junit.jupiter.api.Assertions.*;

class AlgoTaskManagerTest {
    private IAlgoTaskManager<String> sortingAlgorithm;
    private IAlgoTaskManager<String> searchAlgorithm;
    private List<String> tasks;

    @BeforeEach
    void setUp() {
        sortingAlgorithm = new SortingAlgorithm<>();
        searchAlgorithm = new SearchAlgorithm<>();
        tasks = Arrays.asList("Task C", "Task A", "Task B");
    }

    @Test
    void testSort() {
        List<String> sortedTasks = sortingAlgorithm.sort(tasks, Comparator.naturalOrder());
        assertEquals(Arrays.asList("Task A", "Task B", "Task C"), sortedTasks);
    }

    @Test
    void testSearch() {
        List<String> searchResults = searchAlgorithm.search(tasks, task -> task.endsWith("A"));
        assertEquals(List.of("Task A"), searchResults);
    }
}