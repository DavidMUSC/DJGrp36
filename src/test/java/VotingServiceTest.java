
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VotingServiceTest {

    private VotingService service;
    private Occupant o1, o2, o3;
    private Song s;

    @BeforeEach
    void setUp() {
        service = new VotingService();
        o1 = Occupant.builder()
                .id(1)
                .name("Alice")
                .build();
        o2 = Occupant.builder()
                .id(2)
                .name("Bob")
                .build();
        o3 = Occupant.builder()
                .id(3)
                .name("Charlie")
                .build();
        s = Song.builder()
                .id(1)
                .title("Test Song")
                .duration(180)
                .build();
    }

    @Test
    void withoutVotes_shouldNotSkip() {
        assertFalse(service.shouldSkip(s));
    }

    @Test
    void moreLikesThanSkips_shouldNotSkip() {
        service.castVote(o1, s, Vote.LIKE);
        service.castVote(o2, s, Vote.LIKE);
        service.castVote(o3, s, Vote.SKIP);
        assertFalse(service.shouldSkip(s));
    }

    @Test
    void moreSkipsThanLikes_shouldSkip() {
        service.castVote(o1, s, Vote.SKIP);
        service.castVote(o2, s, Vote.SKIP);
        service.castVote(o3, s, Vote.LIKE);
        assertTrue(service.shouldSkip(s));
    }

    @Test
    void equalLikesAndSkips_shouldNotSkip() {
        service.castVote(o1, s, Vote.SKIP);
        service.castVote(o2, s, Vote.LIKE);
        assertFalse(service.shouldSkip(s));
    }

    @Test
    void clearVotes_resetsDecision() {
        service.castVote(o1, s, Vote.SKIP);
        assertTrue(service.shouldSkip(s));
        service.clearVotes(s);
        assertFalse(service.shouldSkip(s));
    }

}
