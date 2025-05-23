import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.List;

public class LoaderTest {

    Loader loader  = Loader.getInstance();
    List<QueueEntry> queue;

    @Test
    public void testLoadFromLittleCSV() throws Exception {
        // CSV mocked as a String
        String fakeCsv =
                "ID,Nombre,Apellido,Artista,Album,Canción,Duración\n" +
                        "1,Juan,Pérez,BTS,Map of the Soul: 7,Intro: Persona,2:51\n" +
                        "1,Juan,Pérez,BTS,Map of the Soul: 7,Boy With Luv,3:49\n";

        // Create a temporary file with the CSV content
        File tempFile = File.createTempFile("test", ".csv");
        try (FileWriter fw = new FileWriter(tempFile)) {
            fw.write(fakeCsv);
        }

        // Execute the loader
        loader.loadFromCSV(tempFile.getAbsolutePath());
        queue = Loader.getInstance().getQueue();

        assertEquals(2, queue.size(), "Queue should contain two entries");

        QueueEntry first = queue.getFirst();
        assertEquals("Juan Pérez", first.getOccupant().getName());
        assertEquals("Intro: Persona", first.getSong().getTitle());
        assertEquals(171, first.getSong().getDuration());
        assertEquals("Map of the Soul: 7", first.getSong().getAlbum().getTitle());
        assertEquals("BTS", first.getSong().getAlbum().getArtist().getName());

        // Clean up
        tempFile.delete();
    }

    @Test
    public void testLoadFromRealCSV() throws Exception {
        URL resource = getClass().getClassLoader().getResource("discos_usuarios.csv");
        assertNotNull(resource, "CSV file not found");

        File file = new File(resource.getFile());
        assertTrue(file.exists());

        loader.loadFromCSV(file.getAbsolutePath());
        queue = loader.getQueue();

        // 1. Check the number of distinct occupants
        long distinctOccupants = queue.stream()
                                      .map(q -> q.getOccupant().getId())
                                      .distinct()
                                      .count();
        assertEquals(4, distinctOccupants);

        // 2. Verify data of the first occupant
        QueueEntry first = queue.getFirst();
        assertEquals("Juan Pérez", first.getOccupant().getName());
        List<QueueEntry> songsOfJuan = queue.stream()
                                            .filter(q -> q.getOccupant().getName().equals("Juan Pérez"))
                                            .toList();
        assertEquals(5, songsOfJuan.size());

        QueueEntry s1 = songsOfJuan.getFirst();
        assertEquals("Intro: Persona", s1.getSong().getTitle());
        assertEquals("Map of the Soul: 7", s1.getSong().getAlbum().getTitle());
        assertEquals("BTS", s1.getSong().getAlbum().getArtist().getName());
        assertEquals(171, s1.getSong().getDuration());
    }

    @Test
    public void testLoadFromMissingCSV() {
        assertThrows(RuntimeException.class, () -> loader.loadFromCSV("nonexistent_file.csv"));
    }

    @Test
    void loadFromCSVWithInvalidDataDoesNotCrash() throws Exception {
        URL resource = LoaderNavigationTest.class
                .getClassLoader()
                .getResource("discos_invalidos.csv");
        assertNotNull(resource);

        assertDoesNotThrow(() -> loader.loadFromCSV(resource.getFile()));
    }

    @Test
    void loadFromEmptyCSVDoesNotCrash() throws Exception {

        URL resource = LoaderNavigationTest.class
                .getClassLoader()
                .getResource("discos_vacios.csv");
        assertNotNull(resource);

        assertDoesNotThrow(() -> loader.loadFromCSV(resource.getFile()));
    }
}
