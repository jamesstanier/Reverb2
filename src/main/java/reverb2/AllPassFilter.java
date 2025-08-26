package reverb2;

public class AllPassFilter {

	private int channels;
	private int sampleRate;
	private int bufferSize;
	private int writePosition = 0;
	private int readPosition = 0;
	private int delayBufferSize;
	private double[][] delayBuffer;
	private double[][] midBuffer;
	private double[][] mainBuffer;

	public AllPassFilter(int channelsIn, int sampleRateIn, int bufferSizeIn, float delayBufferSizeInMs) {

		channels = channelsIn;
		sampleRate = sampleRateIn;
		bufferSize = bufferSizeIn;
		delayBufferSize = (int)(delayBufferSizeInMs * sampleRate / 1000.0f);

		delayBuffer = new double[channels][delayBufferSize];
		midBuffer = new double[channels][bufferSize];
		mainBuffer = new double[channels][bufferSize];
	}

	public double[][] allPassFilter(double[][] inputBuffer, float delayInMs, float gain) {

		//Attenuate input
		mainBuffer = attenauteAndCopy(inputBuffer, 0.7f);

		int delayInSamples = (int)(delayInMs * sampleRate / 1000.0f);
		double gainSquared = Math.pow(gain, 2);

		for (int j = 0; j < inputBuffer[0].length; j++) { //data
			//Calculate read position
			readPosition = (writePosition - delayInSamples + delayBufferSize) % delayBufferSize;
			for (int i = 0; i < mainBuffer.length; i++) { //channels
				//Copy input buffer to delay buffer
				delayBuffer[i][writePosition] = mainBuffer[i][j];
				//Apply gain squared to delay and add to -gain of input
				midBuffer[i][j] = (delayBuffer[i][readPosition] * (1 - gainSquared)) + (mainBuffer[i][j] * -gain);
				//Add delay buffer to input buffer at delayed position
				mainBuffer[i][j] += delayBuffer[i][readPosition] * gain;
				//Copy input buffer back to delay buffer
				delayBuffer[i][writePosition] = mainBuffer[i][j];
			}
			writePosition++;
			writePosition %= delayBufferSize;
		}
		return midBuffer;
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
