import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Calendar;

@Getter
@Setter
@NoArgsConstructor
public class Occupant {
    private int id;
    private String name;
    private ArrayList<Song> songs;

    public ArrayList<Song> getSongs() {
        if (songs == null) {
            songs = new ArrayList<>();
        }
        return songs;
    }
}
