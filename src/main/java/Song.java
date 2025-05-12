import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class Song {
    private int id;
    private String title;
    private int duration;
    private Album album;
}
