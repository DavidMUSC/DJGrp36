import lombok.*;

import java.util.ArrayList;
import java.util.Calendar;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Occupant {
    private int id;
    private String name;
    @Builder.Default
    private ArrayList<Song> songs = new ArrayList<>();
}
