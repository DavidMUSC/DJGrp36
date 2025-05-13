import lombok.*;
import net.bytebuddy.asm.Advice;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Song {
    private int id;
    private String title;
    private int duration;
    private Album album;
}
