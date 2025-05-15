import java.util.HashMap;
import java.util.Map;

public class VotingService {

    // Map<songId, Map<occupantId, Vote>>
    private final Map<Integer, Map<Integer, Vote>> votesBySong = new HashMap<>();

    /**
     * Registra el voto de un ocupante para una canción.
     */
    public void castVote(Occupant occupant, Song song, Vote vote) {
        votesBySong
                .computeIfAbsent(song.getId(), id -> new HashMap<>())
                .put(occupant.getId(), vote);
    }

    /**
     * Devuelve true si esa canción tiene más votos SKIP que LIKE.
     */
    public boolean shouldSkip(Song song) {
        Map<Integer, Vote> votes = votesBySong.get(song.getId());
        if (votes == null) {
            return false;
        }
        long likes = votes.values().stream().filter(v -> v == Vote.LIKE).count();
        long skips = votes.values().stream().filter(v -> v == Vote.SKIP).count();
        return skips > likes;
    }

    /**
     * Limpia todos los votos registrados para esa canción.
     */
    public void clearVotes(Song song) {
        votesBySong.remove(song.getId());
    }


}
