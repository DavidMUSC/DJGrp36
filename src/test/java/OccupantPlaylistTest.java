import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OccupantPlaylistTest {

    @Test
    void playlistFitsExactlyOneHour() {
        Song s1 = Song.builder().id(1).title("A").duration(1800).build();
        Song s2 = Song.builder().id(2).title("B").duration(900).build();
        Song s3 = Song.builder().id(3).title("C").duration(1000).build();

        Occupant o = Occupant.builder()
                .id(42)
                .name("Test User")
                .songs(new java.util.ArrayList<>(List.of(s1, s2, s3)))
                .build();

        List<Song> result = o.playlistForTrip(3600);

        assertEquals(List.of(s1, s2), result);
        assertEquals(2700, result.stream().mapToInt(Song::getDuration).sum());
    }

    @Test
    void playlistForNinetyMinutesAcceptsLongerSongs() {
        Song longSong = Song.builder().id(99).title("Epic").duration(5400).build();
        Occupant o = Occupant.builder().id(1).name("User").songs(new java.util.ArrayList<>(List.of(longSong))).build();

        List<Song> result = o.playlistForTrip(5400);
        assertEquals(List.of(longSong), result);
    }

    @Test
    void playlistFitsExactlyOneAndHalfHours() {

        Song s1 = Song.builder().id(10).title("Track1").duration(2000).build();
        Song s2 = Song.builder().id(11).title("Track2").duration(1500).build();
        Song s3 = Song.builder().id(12).title("Track3").duration(1900).build();
        Song s4 = Song.builder().id(13).title("Track4").duration(800).build();


        Occupant occupant = Occupant.builder()
                .id(88)
                .name("Playlist Tester")
                .songs(new java.util.ArrayList<>(List.of(s1, s2, s3, s4)))
                .build();

        List<Song> playlist = occupant.playlistForTrip(5400);

        assertEquals(List.of(s1, s2, s3), playlist);
        assertEquals(5400, playlist.stream().mapToInt(Song::getDuration).sum());
    }
}