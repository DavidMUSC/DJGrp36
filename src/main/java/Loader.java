import lombok.Getter;

import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import com.opencsv.CSVReader;
import lombok.Setter;

@Getter
@Setter
public class Loader {
    private static Loader instance;

    private Loader() {
    }

    public static Loader getInstance() {
        if (instance == null) {
            instance = new Loader();
        }
        return instance;
    }

    private List<QueueEntry> queue;
    private int position;

    public QueueEntry getCurrentSong(){
        return queue.get(position);
    }

    public QueueEntry nextSong(){
        position++;
        queue.get(position).setPlayedAt(new Date());
        return queue.get(position);
    }

    public QueueEntry previousSong(){
        position--;
        queue.get(position).setPlayedAt(new Date());
        return queue.get(position);
    }

    public List<QueueEntry> searchSongsByTitle(String keyword) {
        if (queue == null) return Collections.emptyList();
        String lowerKeyword = keyword.toLowerCase(Locale.ROOT);
        return queue.stream()
                .filter(entry -> entry.getSong().getTitle().toLowerCase(Locale.ROOT).contains(lowerKeyword))
                .collect(Collectors.toList());
    }

    public List<QueueEntry> filterSongsByMinDuration(int minSeconds) {
        if (queue == null) return Collections.emptyList();
        return queue.stream()
                .filter(entry -> entry.getSong().getDuration() <= minSeconds)
                .collect(Collectors.toList());
    }

    public void loadFromCSV(String filePath) {
        Map<Integer, Occupant> occupantMap = new HashMap<>();
        Map<String, Artist> artists = new HashMap<>();
        Map<String, Album> albums = new HashMap<>();
        List<QueueEntry> queueList = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] fields;
            reader.readNext(); // skip header

            while ((fields = reader.readNext()) != null) {
                // Ignore blank lines
                if (fields.length < 7) {
                    continue;
                }
                try {
                    int queueEntryId = Integer.parseInt(fields[0].trim());
                    String fullName = fields[1].trim() + " " + fields[2].trim();
                    String artistName = fields[3].trim();
                    String albumTitle = fields[4].trim();
                    String songTitle = fields[5].trim();
                    String durationStr = fields[6].trim();

                    int duration = parseDuration(durationStr);

                    // ---- Artist -------------------------------------------------
                    Artist artist = artists.computeIfAbsent(artistName, name ->
                            Artist.builder()
                                    .id(name.hashCode())
                                    .name(name)
                                    .build()
                    );

                    // ---- Album ---------------------------------------------------
                    String albumKey = albumTitle + "::" + artist.getName();
                    Album album = albums.computeIfAbsent(albumKey, key ->
                            Album.builder()
                                    .id((albumTitle + artist.getName()).hashCode())
                                    .title(albumTitle)
                                    .artist(artist)
                                    .build()
                    );

                    // ---- Song --------------------------------------------------
                    Song song = Song.builder()
                            .id(songTitle.hashCode()) // Assuming song ID generation changed or kept as before
                            .title(songTitle)
                            .duration(duration)
                            .album(album)
                            .build();

                    // ---- Occupant -------------------------------------------------
                    Occupant occupant = occupantMap.computeIfAbsent(fullName.hashCode(), id ->
                            Occupant.builder()
                                    .id(fullName.hashCode())
                                    .name(fullName)
                                    .songs(new ArrayList<>())
                                    .build()
                    );
                    occupant.getSongs().add(song);

                    // ---- Queue Entry -----------------------------------------
                    QueueEntry entry = QueueEntry.builder()
                            .id(queueEntryId)
                            .occupant(occupant)
                            .song(song)
                            .queuedAt(new Date())
                            .build();

                    queueList.add(entry);
                } catch (Exception e) {
                    // skip invalid line
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Error loading CSV", e);
        }

        // ---- Save on static loaders ------------------
        queue = queueList;
        position = 0;
    }

    private static int parseDuration(String mmss) {
        String[] parts = mmss.split(":");
        return Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
    }
}
