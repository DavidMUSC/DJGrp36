import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@NoArgsConstructor
@Builder
public class Loader {

    public static List<Occupant> loadFromCSV(String filePath) {
        Map<Integer, Occupant> occupants = new HashMap<>();
        Map<String, Artist> artists = new HashMap<>();
        Map<String, Album> albums = new HashMap<>();

        int songIdCounter = 1;
        AtomicInteger albumIdCounter = new AtomicInteger(1);
        AtomicInteger artistIdCounter = new AtomicInteger(1);

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // saltar cabecera

            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",", -1);

                int occupantId = Integer.parseInt(fields[0].trim());
                String fullName = fields[1].trim() + " " + fields[2].trim();
                String artistName = fields[3].trim();
                String albumTitle = fields[4].trim();
                String songTitle = fields[5].trim();
                String durationStr = fields[6].trim();

                int duration = parseDuration(durationStr);

                // Artist
                Artist artist = artists.computeIfAbsent(artistName, name ->
                        Artist.builder()
                                .id(artistIdCounter.getAndIncrement())
                                .name(name)
                                .build()
                );

                // Album
                String albumKey = albumTitle + "::" + artist.getName();
                Album album = albums.computeIfAbsent(albumKey, key ->
                        Album.builder()
                                .id(albumIdCounter.getAndIncrement())
                                .title(albumTitle)
                                .artist(artist)
                                .build()
                );

                // Song
                Song song = Song.builder()
                        .id(songIdCounter++)
                        .title(songTitle)
                        .duration(duration)
                        .album(album)
                        .build();

                // Occupant
                occupants.computeIfAbsent(occupantId, id ->
                        Occupant.builder()
                                .id(id)
                                .name(fullName)
                                .build()
                ).getSongs().add(song);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>(occupants.values());
    }

    private static int parseDuration(String mmss) {
        String[] parts = mmss.split(":");
        return Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
    }
}
