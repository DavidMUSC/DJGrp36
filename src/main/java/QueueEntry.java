import java.util.Date;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class QueueEntry {
    private int id;
    private Date queuedAt;
    private Date playedAt;
    private Occupant occupant;
    private Song song;

    public long getSecondsSincePlayed() {
        if (playedAt == null) {
            return -1;
        }
        long diffMillis = new Date().getTime() - playedAt.getTime();
        return diffMillis / 1000;
    }
}
