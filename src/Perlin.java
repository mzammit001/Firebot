// ######################################
// ### DO NOT MODIFY THIS SOURCE FILE ###
// ######################################

import java.util.Random;

public class Perlin {

	private int seed;
	private Random random;

	public Perlin(int seed) {

		this.seed = seed;
		this.random = new Random();
	}

	private double rand(double x, double y, double freq) {

		random.setSeed(seed);
		random.setSeed(random.nextLong() + (long) ((x * 40303 + y * 325679) * freq));
		return random.nextDouble();
	}

	private double interpolate(double x, double y, double frac) {

		frac = (1 - Math.cos(frac * Math.PI)) / 2;
		return x + (y - x) * frac;
	}

	public double noise(double x, double y, double persistence) {

		double sum = 0;
		double amp = 1;
		double freq = 1;
		double value = 0;

		for (int stage = 0; stage < 8; stage++) {

			double xx = (int) (x * freq) / freq;
			double yy = (int) (y * freq) / freq;

			double step = 1.0 / freq;
			double r00 = rand(xx, yy, freq);
			double r10 = rand(xx + step, yy, freq);
			double r01 = rand(xx, yy + step, freq);
			double r11 = rand(xx + step, yy + step, freq);

			double xxfrac = (x - xx) / step;
			double yyfrac = (y - yy) / step;

			double v1 = interpolate(r00, r10, xxfrac);
			double v2 = interpolate(r01, r11, xxfrac);
			double v3 = interpolate(v1, v2, yyfrac);

			value += v3 * amp;
			sum += amp;
			freq *= 2;
			amp *= persistence;
		}

		return value / sum;
	}
}
