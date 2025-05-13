import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Album {
    private int id;
    private String title;
    private Artist artist;
}
