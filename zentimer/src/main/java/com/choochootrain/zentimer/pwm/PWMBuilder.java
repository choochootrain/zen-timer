package com.choochootrain.zentimer.pwm;

import java.util.ArrayList;

public class PWMBuilder {

    private ArrayList<Long> pulses;

    public PWMBuilder(boolean firstIsOff) {
        this.pulses = new ArrayList<Long>();
        //first number in generated array is for vibrate off
        if (firstIsOff)
            this.pulses.add(0l);
    }

    public PWMBuilder addPulse(long duration, double duty) {
        //TODO: add bounds checking

        //downsample duty resolution to 10%
        long duty_cycle_ms = (long)(duty * 100);

        if (duty_cycle_ms == 0)
            return this;
        //TODO: concatenate any adjacent 100% duty pulses
        else if (duty_cycle_ms == 100) {
            this.pulses.add(duration);
            this.pulses.add(0l);
            return this;
        }

        long cycle_length = (long)(Math.pow((int)(duty_cycle_ms/10), 2) - 20);
        for (int i = 0; i < duration / cycle_length; i++) {
            this.pulses.add(cycle_length / 10 * duty_cycle_ms);
            this.pulses.add(cycle_length / 10 * (10 - duty_cycle_ms));
        }

        return this;
    }

    public long[] generate() {
        long[] gen = new long[this.pulses.size()];

        for (int i = 0; i < this.pulses.size(); i++)
            gen[i] = this.pulses.get(i);

        return gen;
    }
}
