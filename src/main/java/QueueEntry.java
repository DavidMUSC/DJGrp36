import java.util.Date;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class QueueEntry {
    private Date queuedAt;
    private Date playedAt;
    private Occupant occupant;
    private Song song;

    //funcion para reproducir la siguiente cancion

}
