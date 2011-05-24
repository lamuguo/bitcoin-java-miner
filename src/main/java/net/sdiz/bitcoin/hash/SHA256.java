package net.sdiz.bitcoin.hash;

//some code sharmlessly ripped from bouncy castle

/*-
 * Copyright (c) 2000 - 2009 The Legion Of The Bouncy Castle 
 * (http://www.bouncycastle.org)
 * Permission is hereby granted, free of charge, to any person 
 * obtaining a copy of this software and associated documentation 
 * files (the "Software"), to deal in the Software without 
 * restriction, including without limitation the rights to use, 
 * copy, modify, merge, publish, distribute, sublicense, and/or 
 * sell copies of the Software, and to permit persons to whom the 
 * Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be 
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, 
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES 
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND 
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS 
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN 
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN 
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE 
 * SOFTWARE.
 */
public class SHA256 {
	public static int[] initState() {
		/*
		 * SHA-256 initial hash value The first 32 bits of the fractional parts
		 * of the square roots of the first eight prime numbers
		 */
		int[] state = new int[8];

		state[0] = 0x6a09e667;
		state[1] = 0xbb67ae85;
		state[2] = 0x3c6ef372;
		state[3] = 0xa54ff53a;
		state[4] = 0x510e527f;
		state[5] = 0x9b05688c;
		state[6] = 0x1f83d9ab;
		state[7] = 0x5be0cd19;

		return state;
	}

	private final int[] X = new int[64];

	public synchronized void processBlock(int[] state, int[] input) {
		System.arraycopy(input, 0, X, 0, 16);

		//
		// expand 16 word block into 64 word blocks.
		//
		for (int t = 16; t <= 63; t++) {
			int x = X[t - 15];
			int x1 = X[t - 2];
			X[t] = (((x1 >>> 17) | (x1 << 15)) ^ ((x1 >>> 19) | (x1 << 13)) ^ (x1 >>> 10))
					+ X[t - 7]
					+ (((x >>> 7) | (x << 25)) ^ ((x >>> 18) | (x << 14)) ^ (x >>> 3))
					+ X[t - 16];
		}

		//
		// set up working variables.
		//
		int a = state[0];
		int b = state[1];
		int c = state[2];
		int d = state[3];
		int e = state[4];
		int f = state[5];
		int g = state[6];
		int h = state[7];

		int t = 0;
		for (int i = 0; i < 8; i++) {
			// t = 8 * i
			h += (((e >>> 6) | (e << 26)) ^ ((e >>> 11) | (e << 21)) ^ ((e >>> 25) | (e << 7)))
					+ ((e & f) ^ ((~e) & g)) + K[t] + X[t];
			d += h;
			h += (((a >>> 2) | (a << 30)) ^ ((a >>> 13) | (a << 19)) ^ ((a >>> 22) | (a << 10)))
					+ ((a & b) ^ (a & c) ^ (b & c));
			++t;

			// t = 8 * i + 1
			g += (((d >>> 6) | (d << 26)) ^ ((d >>> 11) | (d << 21)) ^ ((d >>> 25) | (d << 7)))
					+ ((d & e) ^ ((~d) & f)) + K[t] + X[t];
			c += g;
			g += (((h >>> 2) | (h << 30)) ^ ((h >>> 13) | (h << 19)) ^ ((h >>> 22) | (h << 10)))
					+ ((h & a) ^ (h & b) ^ (a & b));
			++t;

			// t = 8 * i + 2
			f += (((c >>> 6) | (c << 26)) ^ ((c >>> 11) | (c << 21)) ^ ((c >>> 25) | (c << 7)))
					+ ((c & d) ^ ((~c) & e)) + K[t] + X[t];
			b += f;
			f += (((g >>> 2) | (g << 30)) ^ ((g >>> 13) | (g << 19)) ^ ((g >>> 22) | (g << 10)))
					+ ((g & h) ^ (g & a) ^ (h & a));
			++t;

			// t = 8 * i + 3
			e += (((b >>> 6) | (b << 26)) ^ ((b >>> 11) | (b << 21)) ^ ((b >>> 25) | (b << 7)))
					+ ((b & c) ^ ((~b) & d)) + K[t] + X[t];
			a += e;
			e += (((f >>> 2) | (f << 30)) ^ ((f >>> 13) | (f << 19)) ^ ((f >>> 22) | (f << 10)))
					+ ((f & g) ^ (f & h) ^ (g & h));
			++t;

			// t = 8 * i + 4
			d += (((a >>> 6) | (a << 26)) ^ ((a >>> 11) | (a << 21)) ^ ((a >>> 25) | (a << 7)))
					+ ((a & b) ^ ((~a) & c)) + K[t] + X[t];
			h += d;
			d += (((e >>> 2) | (e << 30)) ^ ((e >>> 13) | (e << 19)) ^ ((e >>> 22) | (e << 10)))
					+ ((e & f) ^ (e & g) ^ (f & g));
			++t;

			// t = 8 * i + 5
			c += (((h >>> 6) | (h << 26)) ^ ((h >>> 11) | (h << 21)) ^ ((h >>> 25) | (h << 7)))
					+ ((h & a) ^ ((~h) & b)) + K[t] + X[t];
			g += c;
			c += (((d >>> 2) | (d << 30)) ^ ((d >>> 13) | (d << 19)) ^ ((d >>> 22) | (d << 10)))
					+ ((d & e) ^ (d & f) ^ (e & f));
			++t;

			// t = 8 * i + 6
			b += (((g >>> 6) | (g << 26)) ^ ((g >>> 11) | (g << 21)) ^ ((g >>> 25) | (g << 7)))
					+ ((g & h) ^ ((~g) & a)) + K[t] + X[t];
			f += b;
			b += (((c >>> 2) | (c << 30)) ^ ((c >>> 13) | (c << 19)) ^ ((c >>> 22) | (c << 10)))
					+ ((c & d) ^ (c & e) ^ (d & e));
			++t;

			// t = 8 * i + 7
			a += (((f >>> 6) | (f << 26)) ^ ((f >>> 11) | (f << 21)) ^ ((f >>> 25) | (f << 7)))
					+ ((f & g) ^ ((~f) & h)) + K[t] + X[t];
			e += a;
			a += (((b >>> 2) | (b << 30)) ^ ((b >>> 13) | (b << 19)) ^ ((b >>> 22) | (b << 10)))
					+ ((b & c) ^ (b & d) ^ (c & d));
			++t;
		}

		state[0] += a;
		state[1] += b;
		state[2] += c;
		state[3] += d;
		state[4] += e;
		state[5] += f;
		state[6] += g;
		state[7] += h;
	}

	/*
	 * SHA-256 Constants (represent the first 32 bits of the fractional parts of
	 * the cube roots of the first sixty-four prime numbers)
	 */
	private static final int K[] = { 0x428a2f98, 0x71374491, 0xb5c0fbcf,
			0xe9b5dba5, 0x3956c25b, 0x59f111f1, 0x923f82a4, 0xab1c5ed5,
			0xd807aa98, 0x12835b01, 0x243185be, 0x550c7dc3, 0x72be5d74,
			0x80deb1fe, 0x9bdc06a7, 0xc19bf174, 0xe49b69c1, 0xefbe4786,
			0x0fc19dc6, 0x240ca1cc, 0x2de92c6f, 0x4a7484aa, 0x5cb0a9dc,
			0x76f988da, 0x983e5152, 0xa831c66d, 0xb00327c8, 0xbf597fc7,
			0xc6e00bf3, 0xd5a79147, 0x06ca6351, 0x14292967, 0x27b70a85,
			0x2e1b2138, 0x4d2c6dfc, 0x53380d13, 0x650a7354, 0x766a0abb,
			0x81c2c92e, 0x92722c85, 0xa2bfe8a1, 0xa81a664b, 0xc24b8b70,
			0xc76c51a3, 0xd192e819, 0xd6990624, 0xf40e3585, 0x106aa070,
			0x19a4c116, 0x1e376c08, 0x2748774c, 0x34b0bcb5, 0x391c0cb3,
			0x4ed8aa4a, 0x5b9cca4f, 0x682e6ff3, 0x748f82ee, 0x78a5636f,
			0x84c87814, 0x8cc70208, 0x90befffa, 0xa4506ceb, 0xbef9a3f7,
			0xc67178f2 };
}
