import lombok.*;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Occupant {
    private int id;
    private String name;
    @Builder.Default
    private ArrayList<Song> songs = new ArrayList<>();

    List<Song> playlistForTrip(int seconds){
        List<Song> playlist = new ArrayList<>();
        int duration = 0;

        for (Song song : songs) {
            duration += song.getDuration();
            if (duration > seconds) {
                return playlist;
            }
            playlist.add(song);
        }
        return playlist;
    }
}
