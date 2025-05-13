import lombok.Getter;

import java.io.FileReader;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import com.opencsv.CSVReader;

@Getter
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

    private static List<Occupant> occupants;

    private static List<QueueEntry> queue;
    private static int position;

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

    public void loadFromCSV(String filePath) {
        Map<Integer, Occupant> occupantMap = new HashMap<>();
        Map<String, Artist> artists = new HashMap<>();
        Map<String, Album> albums = new HashMap<>();
        List<QueueEntry> queueList = new ArrayList<>();

        int songIdCounter = 1;
        AtomicInteger albumIdCounter = new AtomicInteger(1);
        AtomicInteger artistIdCounter = new AtomicInteger(1);

        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] fields;
            reader.readNext(); // skip header

            while ((fields = reader.readNext()) != null) {
                // Ignore blank lines
                if (fields.length < 7 || fields[0].trim().isEmpty() || fields[6].trim().isEmpty()) {
                    continue;
                }

                int occupantId = Integer.parseInt(fields[0].trim());
                String fullName = fields[1].trim() + " " + fields[2].trim();
                String artistName = fields[3].trim();
                String albumTitle = fields[4].trim();
                String songTitle = fields[5].trim();
                String durationStr = fields[6].trim();

                int duration = parseDuration(durationStr);

                // ---- Artist -------------------------------------------------
                Artist artist = artists.computeIfAbsent(artistName, name ->
                        Artist.builder()
                                .id(artistIdCounter.getAndIncrement())
                                .name(name)
                                .build()
                );

                // ---- Album ---------------------------------------------------
                String albumKey = albumTitle + "::" + artist.getName();
                Album album = albums.computeIfAbsent(albumKey, key ->
                        Album.builder()
                                .id(albumIdCounter.getAndIncrement())
                                .title(albumTitle)
                                .artist(artist)
                                .build()
                );

                // ---- Song --------------------------------------------------
                Song song = Song.builder()
                        .id(songIdCounter++)
                        .title(songTitle)
                        .duration(duration)
                        .album(album)
                        .build();

                // ---- Occupant -------------------------------------------------
                Occupant occupant = occupantMap.computeIfAbsent(occupantId, id ->
                        Occupant.builder()
                                .id(id)
                                .name(fullName)
                                .songs(new ArrayList<>())
                                .build()
                );
                occupant.getSongs().add(song);

                // ---- Queue Entry -----------------------------------------
                QueueEntry entry = QueueEntry.builder()
                        .occupant(occupant)
                        .song(song)
                        .queuedAt(new Date())
                        .build();

                queueList.add(entry);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // ---- Save on static loaders ------------------
        Loader.occupants = new ArrayList<>(occupantMap.values());
        Loader.queue = queueList;
        Loader.position = 0;
    }

    private static int parseDuration(String mmss) {
        String[] parts = mmss.split(":");
        return Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
    }
}
