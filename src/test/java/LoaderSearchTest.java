import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LoaderSearchTest {

    private Loader loader;

    @BeforeEach
    void setUp() {
        loader = Loader.getInstance();


        QueueEntry entry1 = QueueEntry.builder()
                .song(Song.builder().id(1).title("Love Story").duration(240).build())
                .build();

        QueueEntry entry2 = QueueEntry.builder()
                .song(Song.builder().id(2).title("Lovers in Japan").duration(300).build())
                .build();

        QueueEntry entry3 = QueueEntry.builder()
                .song(Song.builder().id(3).title("Hate That I Love You").duration(310).build())
                .build();

        QueueEntry entry4 = QueueEntry.builder()
                .song(Song.builder().id(4).title("Break Free").duration(170).build())
                .build();

        loader.setQueue(List.of(entry1, entry2, entry3, entry4));
    }

    @Test
    void searchSongsByTitleReturnsCorrectMatches() {
        List<QueueEntry> results = loader.searchSongsByTitle("love");
        assertEquals(3, results.size());
        assertTrue(results.stream().anyMatch(e -> e.getSong().getTitle().equals("Love Story")));
        assertTrue(results.stream().anyMatch(e -> e.getSong().getTitle().equals("Hate That I Love You")));
        assertTrue(results.stream().anyMatch(e -> e.getSong().getTitle().equals("Lovers in Japan")));
    }

    @Test
    void searchSongsByTitleIsCaseInsensitive() {
        List<QueueEntry> results = loader.searchSongsByTitle("LOVERS");
        assertEquals(1, results.size());
        assertEquals("Lovers in Japan", results.get(0).getSong().getTitle());
    }

    @Test
    void filterSongsByMinDurationReturnsCorrectEntries() {
        List<QueueEntry> results = loader.filterSongsByMinDuration(250);
        assertEquals(2, results.size());
        assertTrue(results.stream().anyMatch(e -> e.getSong().getTitle().equals("Love Story")));
        assertTrue(results.stream().anyMatch(e -> e.getSong().getTitle().equals("Break Free")));
    }

    @Test
    void filterSongsByMinDurationReturnsEmptyListWhenNoneMatch() {
        List<QueueEntry> results = loader.filterSongsByMinDuration(100);
        assertTrue(results.isEmpty());
    }

    @Test
    void searchSongsByTitleReturnsEmptyListWhenNoMatches() {
        List<QueueEntry> results = loader.searchSongsByTitle("xyz");
        assertTrue(results.isEmpty());
    }

    @Test
    void filterSongsByMinDurationReturnsEmptyListWhenNoQueue(){
        loader.setQueue(null);
        List<QueueEntry> results = loader.filterSongsByMinDuration(100);
        assertTrue(results.isEmpty());
    }

    @Test
    void searchSongsByTitleReturnsEmptyListWhenNoQueue(){
        loader.setQueue(null);
        List<QueueEntry> results = loader.searchSongsByTitle("xyz");
        assertTrue(results.isEmpty());
    }
}
