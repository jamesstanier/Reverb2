/*
 * Copyright (c) 2025 James G. Stanier
 *
 * This file is part of Reverb2.
 *
 * This software is dual-licensed under:
 *   1. The GNU General Public License v3.0 (GPLv3)
 *   2. A commercial license (contact j.stanier766@gmail.com for details)
 *
 * You may use this file under the terms of the GPLv3 as published by
 * the Free Software Foundation. For proprietary/commercial use,
 * please see the LICENSE-COMMERCIAL file or contact the copyright holder.
 */

package reverb2;

public class CombFilter {

	private int channels;
	private int sampleRate;
	private int writePosition = 0;
	private int readPosition = 0;
	private int bufferSize;
	private int delayBufferSize;
	private double[][] delayBuffer;
	private double[][] mainBuffer;

	public CombFilter(int channelsIn, int sampleRateIn, int bufferSizeIn, float delayBufferSizeInMs) {

		channels = channelsIn;
		sampleRate = sampleRateIn;
		bufferSize = bufferSizeIn;
		delayBufferSize = (int)(delayBufferSizeInMs * sampleRate / 1000.0f);

		delayBuffer = new double[channels][delayBufferSize];
		mainBuffer = new double[channels][bufferSize];
	}

	public double[][] combFilter(double[][] inputBuffer, float delayInMs, float gain) {

		//Attenuate input
		mainBuffer = attenauteAndCopy(inputBuffer, 0.7f);

		int delayInSamples = (int)(delayInMs * sampleRate / 1000.0f);

		for (int j = 0; j < inputBuffer[0].length; j++) { //data
			//Calculate read position
			readPosition = (writePosition - delayInSamples + delayBufferSize) % delayBufferSize;
			for (int i = 0; i < inputBuffer.length; i++) { //channels
				//Copy input buffer to delay buffer
				delayBuffer[i][writePosition] = mainBuffer[i][j];
				//Add delay buffer to input buffer at delayed position
				mainBuffer[i][j] += delayBuffer[i][readPosition] * gain;
				//Copy input buffer back to delay buffer
				delayBuffer[i][writePosition] = mainBuffer[i][j];
			}
			writePosition++;
			writePosition %= delayBufferSize;
		}
		return mainBuffer;
	}

	private double[][] attenauteAndCopy(double[][] buffer, float gain) {

		for (int i = 0; i < buffer.length; i++) { //channels
			for (int j = 0; j < buffer[0].length; j++) { //data
				mainBuffer[i][j] = buffer[i][j] * gain;
			}
		}
		return mainBuffer;
	}
}
