/*
 * Copyright (c) 2025 James G. Stanier
 *
 * This file is part of Reverb2.
 *
 * This software is dual-licensed under:
 *   1. The GNU General Public License v3.0 (GPLv3)
 *   2. A commercial license (contact j.stanier766(at)gmail.com for details)
 *
 * You may use this file under the terms of the GPLv3 as published by
 * the Free Software Foundation. For proprietary/commercial use,
 * please see the LICENSE-COMMERCIAL file or contact the copyright holder.
 */

package reverb2;

public class ProcessAudio {

	private CombFilter combFilterR1, combFilterR2, combFilterR3, combFilterR4;
	private CombFilter combFilterV1, combFilterV2, combFilterV3, combFilterV4;
	private AllPassFilter allPassFilter1, allPassFilter2;
	private int channels;
	private int sampleRate;
	private int bufferSize;
	public float delayRD1, delayRD2, delayRD3, delayRD4;
	public float delayVD1, delayVD2, delayVD3, delayVD4;
	public float gainR, gainV;
	private double[][] combFiltersOut1, combFiltersOut2, combFiltersOut3, combFiltersOut4, combFilterOutputBuffer;

	public ProcessAudio(int channelsIn, int sampleRateIn, int frameSizeIn, int bufferSizeIn) {

		channels = channelsIn;
		sampleRate = sampleRateIn;
		bufferSize = bufferSizeIn;

		combFilterR1 = new CombFilter(channels, sampleRate, bufferSize, 2000.0f);
		combFilterR2 = new CombFilter(channels, sampleRate, bufferSize, 2000.0f);
		combFilterR3 = new CombFilter(channels, sampleRate, bufferSize, 2000.0f);
		combFilterR4 = new CombFilter(channels, sampleRate, bufferSize, 2000.0f);

		combFilterV1 = new CombFilter(channels, sampleRate, bufferSize, 2.0f);
		combFilterV2 = new CombFilter(channels, sampleRate, bufferSize, 2.0f);
		combFilterV3 = new CombFilter(channels, sampleRate, bufferSize, 2.0f);
		combFilterV4 = new CombFilter(channels, sampleRate, bufferSize, 2.0f);

		allPassFilter1 = new AllPassFilter(channels, sampleRate, bufferSize, 2000.0f);
		allPassFilter2 = new AllPassFilter(channels, sampleRate, bufferSize, 2000.0f);

		combFiltersOut1 = new double [channels][bufferSize];
		combFiltersOut2 = new double [channels][bufferSize];
		combFiltersOut3 = new double [channels][bufferSize];
		combFiltersOut4 = new double [channels][bufferSize];
		combFilterOutputBuffer = new double [channels][bufferSize];
	}

	public double[][] processData(double[][] inputBuffer) {

		combFiltersOut1 = combFilterV1.combFilter(combFilterR1.combFilter(inputBuffer, delayRD1, gainR), delayVD1, gainV);
		combFiltersOut2 = combFilterV2.combFilter(combFilterR2.combFilter(inputBuffer, delayRD2, gainR), delayVD2, gainV);
		combFiltersOut3 = combFilterV3.combFilter(combFilterR3.combFilter(inputBuffer, delayRD3, gainR), delayVD3, gainV);
		combFiltersOut4 = combFilterV4.combFilter(combFilterR4.combFilter(inputBuffer, delayRD4, gainR), delayVD4, gainV);

		for (int i = 0; i < channels; i++) { //channels
			for (int j = 0; j < bufferSize; j++) { //data
				combFilterOutputBuffer[i][j] = (combFiltersOut1[i][j] + combFiltersOut2[i][j] + combFiltersOut3[i][j] + combFiltersOut4[i][j]) * 0.3f;
			}
		}

		//return combFilterOutputBuffer;
		return allPassFilter1.allPassFilter(allPassFilter2.allPassFilter(combFilterOutputBuffer, 1.0f, gainR), 5.0f, gainR);
	}
}
