package com.la.port;

import java.util.HashMap;
import java.util.Map;

/**
 * The Advantech IO Card API exposes an array of ports, where each port represents 8 dry contact sensors (pin values).
 */
public class Port {
    private static final int SENSORS_PER_PORT = 8;
    private final int        number;
    private byte             state;

    /**
     * Constructs a new instance.
     *
     * @param number The port number, used to identify the sensor (dry contact) locations. Port 0 is sensors 0-7, Port
     *               1, is sensors 8-15, etc.
     */
    Port(final int number) {
        this.number = number;
    }

    /**
     * Returns a map of pin values, based on that byte position/value.;
     *
     * @return Returns a map of sensor values based on that byte values;
     */
    public Map<Integer, Integer> getPinValues() {
        final Map<Integer, Integer> rc     = new HashMap<>();
        final var                   offset = number * SENSORS_PER_PORT;
        for (var i = 0; i < 4; i++) {
            final var high      = 7 - i;
            final var low       = 3 - i;
            final var highValue = state >> high & 0x1;
            final var lowValue  = state >> low & 0x1;

            rc.put(high + offset, highValue);
            rc.put(low + offset, lowValue);
        }
        return rc;
    }

    /**
     * Resets the byte value using the provided value.
     *
     * @param state The new state of the byte value.
     */
    public void set(final byte state) {
        this.state = state;
    }

    /**
     * Turn on the bit in the specified position.
     *
     * @param bit The position.
     */
    public void on(final int bit) {
        state |= 1 << bit;
    }

    /**
     * Turn off the bit in the specified position.
     *
     * @param bit The position.
     */

    public void off(final int bit) {
        state &= ~(1 << bit);
    }

    @Override
    public String toString() {
        return "Port [number=" + number + ", decode()=" + getPinValues() + "]";
    }
}
