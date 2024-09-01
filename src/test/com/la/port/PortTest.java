package test.com.la.port;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.la.port.Card;

class PortTest {

    @Test
    void test() {
        final var card = new Card("xxxxxx", 8);
        System.out.println("->" + card.toString());
        assertTrue(card.getPinsSetOn().isEmpty());

        card.setPin(4);
        card.setPin(7);
        card.setPin(1);
        card.setPin(49);
        card.setPin(63);
        System.out.println("->" + card.toString());
        assertEquals("[1, 4, 7, 49, 63]", card.getPinsSetOn().toString());

        card.clearPin(4);
        card.clearPin(7);
        card.clearPin(1);
        card.clearPin(63);
        System.out.println("->" + card.toString());
        assertEquals("[49]", card.getPinsSetOn().toString());

        card.clearPin(49);
        System.out.println("->" + card.toString());
        assertTrue(card.getPinsSetOn().isEmpty());
        byte state = 0;
        for (var i = 0; i < 8; i++) {
            state |= 1 << i;
        }
        card.setPort(7, state);
        System.out.println("->" + card.toString());
        assertEquals("[56, 57, 58, 59, 60, 61, 62, 63]", card.getPinsSetOn().toString());

        card.clearAll();
        System.out.println("->" + card.toString());
        assertTrue(card.getPinsSetOn().isEmpty());
    }

}
