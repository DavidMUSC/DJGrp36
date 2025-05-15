import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class QueueEntryTest {

    @Test
    void builderCreatesExpectedFields() {
        Song song = Song.builder().id(1).title("X").duration(60).build();
        Occupant occ = Occupant.builder().id(1).name("Y").build();

        Date queued = new Date();
        QueueEntry entry = QueueEntry.builder()
                .occupant(occ)
                .song(song)
                .queuedAt(queued)
                .build();

        assertSame(song, entry.getSong());
        assertSame(occ, entry.getOccupant());
        assertEquals(queued, entry.getQueuedAt());
        assertNull(entry.getPlayedAt());
    }

    @Test
    void getSecondsSincePlayedReturnsCorrectValue() throws InterruptedException {
        QueueEntry entry = QueueEntry.builder()
                .song(Song.builder().id(1).title("X").duration(60).build())
                .occupant(Occupant.builder().id(1).name("Y").build())
                .queuedAt(new Date())
                .playedAt(new Date())
                .build();

        Thread.sleep(1500); // espera 1.5 segundos
        long seconds = entry.getSecondsSincePlayed();

        assertTrue(seconds >= 1 && seconds <= 2); // tolerancia de tiempo
    }

    @Test
    void getSecondsSincePlayedReturnsMinusOneWhenNotPlayed() {
        QueueEntry entry = QueueEntry.builder()
                .song(Song.builder().id(2).title("Y").duration(90).build())
                .occupant(Occupant.builder().id(2).name("Z").build())
                .queuedAt(new Date())
                .build();

        assertEquals(-1, entry.getSecondsSincePlayed());
    }
}