import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ro.mpp2024.domain.PersoanaOficiu;
import ro.mpp2024.domain.Proba;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestEntity {
    @Test
    @DisplayName("Test create PersoanaOficiu")
    public void testCreatePersoanaOficiu() {
        PersoanaOficiu a = new PersoanaOficiu("Cluj",  "a@b.com", "1234");
        assertEquals("a@b.com", a.getUsername());
        assertEquals("Cluj", a.getOrasOficiu());
        assertEquals("1234", a.getPassword());
    }

    @Test
    @DisplayName("Test create Proba")
    public void testCreateProba() {
        var proba = new Proba("200m", "spate", 12);
        assertEquals("200m", proba.getDistance());
        assertEquals("spate", proba.getStil());
        assertEquals(12, proba.getNrParticipants());

    }
}
