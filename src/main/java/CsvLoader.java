import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class CsvLoader {

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
                String[] fields = line.split(",", -1); // permitir campos vacíos

                int occupantId = Integer.parseInt(fields[0].trim());
                String fullName = fields[1].trim() + " " + fields[2].trim();
                String artistName = fields[3].trim();
                String albumTitle = fields[4].trim();
                String songTitle = fields[5].trim();
                String durationStr = fields[6].trim();

                int duration = parseDuration(durationStr);

                // Artist (reutilizar si ya existe)
                Artist artist = artists.computeIfAbsent(artistName, name -> {
                    Artist a = new Artist();
                    a.setId(artistIdCounter.getAndIncrement());
                    a.setName(name);
                    return a;
                });

                // Album (único por nombre + artista)
                String albumKey = albumTitle + "::" + artist.getName();
                Album album = albums.computeIfAbsent(albumKey, key -> {
                    Album al = new Album();
                    al.setId(albumIdCounter.getAndIncrement());
                    al.setTitle(albumTitle);
                    al.setArtist(artist);
                    return al;
                });

                // Song
                Song song = new Song();
                song.setId(songIdCounter++);
                song.setTitle(songTitle);
                song.setDuration(duration);
                song.setAlbum(album);

                // Occupant
                Occupant occupant = occupants.computeIfAbsent(occupantId, id -> {
                    Occupant o = new Occupant();
                    o.setId(id);
                    o.setName(fullName);
                    return o;
                });

                occupant.getSongs().add(song);
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
