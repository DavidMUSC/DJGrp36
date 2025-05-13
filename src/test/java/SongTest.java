import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SongTest {

    @Test
    void testFormattedDuration() {
        Song song = Song.builder()
                .id(1)
                .title("Test Song")
                .duration(191) // 3 min 11 sec
                .album(null)
                .build();

        assertEquals("03:11", song.getFormattedDuration());
    }
}