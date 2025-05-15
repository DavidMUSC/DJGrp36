import org.junit.jupiter.api.*;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LoaderNavigationTest {

    private static Loader loader;
    private static List<QueueEntry> queue;

    @BeforeAll
    static void setUp() throws Exception {
        URL resource = LoaderNavigationTest.class
                .getClassLoader()
                .getResource("discos_usuarios.csv");
        assertNotNull(resource);

        loader = Loader.getInstance();
        loader.loadFromCSV(resource.getFile());

        queue = loader.getQueue();
    }

    @Order(1)
    @Test
    void currentSongStartsAtZero() {
        assertEquals(queue.getFirst(), loader.getCurrentSong());
    }

    @Test
    void nextSongAdvancesPositionAndSetsPlayedAt() {
        QueueEntry before = loader.getCurrentSong();
        QueueEntry next = loader.nextSong();

        assertEquals(queue.get(1), next);
        assertNotNull(next.getPlayedAt());
        assertNull(before.getPlayedAt());
    }

    @Test
    void previousSongGoesBackAndSetsPlayedAt() {
        // at this point position == 1
        QueueEntry previous = loader.previousSong();
        assertEquals(queue.getFirst(), previous);
        assertNotNull(previous.getPlayedAt());
    }
}