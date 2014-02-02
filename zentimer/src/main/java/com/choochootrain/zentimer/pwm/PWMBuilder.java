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
        int cycle_length = 20;
        //downsample duration
        duration = duration/cycle_length*cycle_length;
        //downsample duty resolution to 10%
        long duty_cycle_ms = (long)(duty * cycle_length);
        System.out.println("duration " + duration + " -> " + duration);
        System.out.println("duty " + duty + " -> " + duty_cycle_ms);

        if (duty_cycle_ms == 0)
            return this;
        else if (duty_cycle_ms == 10) {
            this.pulses.add(duration);
            this.pulses.add(0l);
            return this;
        }

        for (int t = 0; t < duration / cycle_length; t++) {
            this.pulses.add(duty_cycle_ms);
            this.pulses.add(cycle_length - duty_cycle_ms);
        }

        return this;
    }

    public long[] generate() {
        long[] gen = new long[this.pulses.size()];

        for (int i = 0; i < this.pulses.size(); i++)
            gen[i] = this.pulses.get(i);

        return gen;
    }

    public static void main(String[] args) {
        long[] pwm = new PWMBuilder(true)
                .addPulse(104l, 0.143819)
                .addPulse(168l, 0.548239)
                .addPulse(432l, 1.0)
                .generate();
        for (int i = 0; i < pwm.length; i++)
            System.out.print(pwm[i] + " ");
    }
}
