package com.la.port;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Stream;

/**
 * Represents the Advantech IO Card with A configurable number of ports, where each port maps to 8 specific dry contact
 * "sensors".
 */
public class Card {
    private static final int    BITS_PER_BYTE = 8;
    private static final String SPACE         = " ";
    private static final String PLUS_SIGN     = "+";
    private static final String DASH          = "-";
    private static final String VERTICAL_BAR  = "|";

    private final Port[]        ports;
    private final String        deviceName;

    /**
     * Constructs a new instance of the card
     *
     * @param deviceName    The name of the device.
     * @param numberOfPorts The number of ports on the device.
     */
    public Card(final String deviceName,
                final int numberOfPorts) {
        this.deviceName = deviceName;
        this.ports      = new Port[numberOfPorts];
        for (var i = 0; i < numberOfPorts; i++) {
            ports[i] = new Port(i);
        }
    }

    public void clearAll() {
        Stream.of(ports).forEach(p -> p.set((byte) 0));
    }

    public void setPort(final int port, final byte state) {
        ports[port].set(state);
    }

    public void clearPin(final int pin) {
        set(pin, false);
    }

    public void setPin(final int pin) {
        set(pin, true);
    }

    private void set(final int pin, final boolean flag) {
        final var port = pin / BITS_PER_BYTE;
        final var bit  = pin % BITS_PER_BYTE;

        if (port <= ports.length) {
            if (flag) {
                ports[port].on(bit);
            }
            else {
                ports[port].off(bit);
            }
        }
    }

    @Override
    public String toString() {
        final var seperator = new StringBuilder();
        final var header    = new StringBuilder();
        final var values    = new StringBuilder();
        final var val       = extractPinValues();

        for (final var e : val.entrySet()) {
            seperator.append(PLUS_SIGN + DASH);
            header.append(VERTICAL_BAR + e.getKey());
            values.append(VERTICAL_BAR);
            if (e.getKey() >= 10) {
                seperator.append(DASH);
                values.append(SPACE);
            }
            values.append(val.getOrDefault(e.getKey(), 0));
        }
        seperator.append(PLUS_SIGN);
        header.append(VERTICAL_BAR);
        values.append(VERTICAL_BAR);

        final var name         = VERTICAL_BAR + SPACE + "Device:" + SPACE + deviceName;
        final var deviceHeader = name + SPACE.repeat(seperator.length() - name.length() - 1) + VERTICAL_BAR;
        final var separator    = seperator.toString();
        return System.lineSeparator() + separator + System.lineSeparator() + deviceHeader + System.lineSeparator()
                + separator + System.lineSeparator() + header.toString() + System.lineSeparator() + separator
                + System.lineSeparator() + values.toString() + System.lineSeparator() + separator;
    }

    private HashMap<Integer, Integer> extractPinValues() {
        final var val = new HashMap<Integer, Integer>();
        Stream.of(ports).forEach(p -> val.putAll(p.getPinValues()));
        return val;
    }

    public Collection<Integer> getPinsSetOn() {
        final var rc = new ArrayList<Integer>();

        for (final var e : extractPinValues().entrySet()) {
            if (e.getValue() == null || e.getValue() == 0) {
                continue;
            }
            rc.add(e.getKey());
        }

        return rc;

    }
}
