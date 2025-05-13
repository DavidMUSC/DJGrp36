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
}