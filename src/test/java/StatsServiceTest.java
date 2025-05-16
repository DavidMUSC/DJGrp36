import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StatsServiceTest {

    private StatsService statsService;
    private Song song1, song2, song3;
    private Album album1, album2;
    private Artist artist1, artist2;
    private QueueEntry entry1, entry2, entry3, entry4, entry5;

    @BeforeEach
    public void setup() {
        artist1 = mock(Artist.class);
        artist2 = mock(Artist.class);

        when(artist1.getName()).thenReturn("Artist A");
        when(artist2.getName()).thenReturn("Artist B");

        album1 = mock(Album.class);
        album2 = mock(Album.class);

        when(album1.getArtist()).thenReturn(artist1);
        when(album2.getArtist()).thenReturn(artist2);

        song1 = mock(Song.class);
        song2 = mock(Song.class);
        song3 = mock(Song.class);

        when(song1.getAlbum()).thenReturn(album1);
        when(song2.getAlbum()).thenReturn(album1);
        when(song3.getAlbum()).thenReturn(album2);

        when(song1.getTitle()).thenReturn("Song One");
        when(song2.getTitle()).thenReturn("Song Two");
        when(song3.getTitle()).thenReturn("Song Three");

        entry1 = mock(QueueEntry.class);
        entry2 = mock(QueueEntry.class);
        entry3 = mock(QueueEntry.class);
        entry4 = mock(QueueEntry.class);
        entry5 = mock(QueueEntry.class);

        when(entry1.getSong()).thenReturn(song1);
        when(entry2.getSong()).thenReturn(song1);
        when(entry3.getSong()).thenReturn(song2);
        when(entry4.getSong()).thenReturn(song3);
        when(entry5.getSong()).thenReturn(song3);

        List<QueueEntry> history = List.of(entry1, entry2, entry3, entry4, entry5);
        statsService = new StatsService(history);
    }

    @Test
    public void testGetMostPlayedSongs() {
        List<Song> topSongs = statsService.getMostPlayedSongs(2);
        assertEquals(2, topSongs.size());
        assertTrue(topSongs.contains(song1));
        assertTrue(topSongs.contains(song3));
    }

    @Test
    public void testGetMostPopularArtist() {
        Artist mostPopular = statsService.getMostPopularArtist();
        assertEquals("Artist A", mostPopular.getName());
    }

    @Test
    public void testGetMostPopularAlbum() {
        Album mostPopular = statsService.getMostPopularAlbum();
        assertEquals(album1, mostPopular);
    }

    @Test
    public void testGetMostPlayedSongs_EmptyHistory() {
        statsService = new StatsService(List.of());
        List<Song> topSongs = statsService.getMostPlayedSongs(3);
        assertNotNull(topSongs);
        assertTrue(topSongs.isEmpty());
    }

    @Test
    public void testGetMostPopularArtist_EmptyHistory() {
        statsService = new StatsService(List.of());
        Artist mostPopular = statsService.getMostPopularArtist();
        assertNull(mostPopular);
    }

    @Test
    public void testGetMostPopularAlbum_EmptyHistory() {
        statsService = new StatsService(List.of());
        Album mostPopular = statsService.getMostPopularAlbum();
        assertNull(mostPopular);
    }

    @Test
    public void testGetMostPlayedSongs_TopNGreaterThanDistinctSongs() {
        List<QueueEntry> history = List.of(entry1, entry2);
        statsService = new StatsService(history);

        List<Song> topSongs = statsService.getMostPlayedSongs(3);
        assertEquals(1, topSongs.size());
        assertTrue(topSongs.contains(song1));
    }
}
