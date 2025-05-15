import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StatsService {
    private List<QueueEntry> history;

    public StatsService(List<QueueEntry> history) {
        this.history = history;
    }

    public List<Song> getMostPlayedSongs(int topN) {
        Map<Song, Long> count = history.stream()
                .collect(Collectors.groupingBy(QueueEntry::getSong, Collectors.counting()));
        return count.entrySet().stream()
                .sorted(Map.Entry.<Song, Long>comparingByValue().reversed())
                .limit(topN)
                .map(Map.Entry::getKey)
                .toList();
    }

    public Artist getMostPopularArtist() {
        return history.stream()
                .map(QueueEntry::getSong)
                .map(song -> song.getAlbum().getArtist())
                .collect(Collectors.groupingBy(a -> a, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    public Album getMostPopularAlbum() {
        return history.stream()
                .map(QueueEntry::getSong)
                .map(Song::getAlbum)
                .collect(Collectors.groupingBy(a -> a, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }
}