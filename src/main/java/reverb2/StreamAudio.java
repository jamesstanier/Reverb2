package reverb2;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

public class StreamAudio implements Runnable {

	private Thread t;
	public String fileName;
	private File f, fOut;
	private AudioInputStream stream;
	private AudioFormat format;
	private FileOutputStream fos;
	private BufferedOutputStream bos = null;
	private DataOutputStream out;
	private static int BUFFER_SIZE = 4096;
	private int frameSize;
	private int sampleRate;
	private int channels;
	public ProcessAudio processAudio;
	public boolean bLoop = false;
	public boolean bStop = false;


	public StreamAudio(String fileName) throws UnsupportedAudioFileException, IOException {
		setup(fileName);
	}

	private void setup(String fileName) throws UnsupportedAudioFileException, IOException {

		f = new File(fileName);
		fOut = new File("output.wav");
		fos = new FileOutputStream(fOut);
		bos = new BufferedOutputStream(fos);
		out = new DataOutputStream(bos);

	    stream = AudioSystem.getAudioInputStream(f);
	    format = stream.getFormat();

	    frameSize = format.getFrameSize();
	    sampleRate = (int)format.getSampleRate();
	    format.getSampleSizeInBits();
	    channels = format.getChannels();

        out.writeBytes("RIFF");// 0-4 ChunkId always RIFF
        out.writeInt(Integer.reverseBytes((int)(stream.getFrameLength() * format.getFrameSize()) + 44));// 5-8 ChunkSize always audio-length +header-length(44)
        out.writeBytes("WAVE");// 9-12 Format always WAVE
        out.writeBytes("fmt ");// 13-16 Subchunk1 ID always "fmt " with trailing whitespace
        out.writeInt(Integer.reverseBytes(16)); // 17-20 Subchunk1 Size always 16
        out.writeShort(Short.reverseBytes((short) 1));// 21-22 Audio-Format 1 for PCM PulseAudio
        out.writeShort(Short.reverseBytes((short)format.getChannels()));// 23-24 Num-Channels 1 for mono, 2 for stereo
        out.writeInt(Integer.reverseBytes((int)format.getSampleRate()));// 25-28 Sample-Rate
        out.writeInt(Integer.reverseBytes((int)(format.getSampleRate() * format.getFrameSize())));// 29-32 Byte Rate
        out.writeShort(Short.reverseBytes((short)format.getFrameSize()));// 33-34 Block Align
        out.writeShort(Short.reverseBytes((short)format.getSampleSizeInBits()));// 35-36 Bits-Per-Sample
        out.writeBytes("data");// 37-40 Subchunk2 ID always data
        out.writeInt(Integer.reverseBytes((int)(5 * stream.getFrameLength() * format.getFrameSize())));// 41-44 Subchunk 2 Size audio-length

		processAudio = new ProcessAudio(channels, sampleRate, frameSize, BUFFER_SIZE / frameSize);

	}

	public void playAudio() throws LineUnavailableException, IOException {

        SourceDataLine.Info info = new DataLine.Info(SourceDataLine.class, stream
	            .getFormat(), ((int) stream.getFrameLength() * format.getFrameSize()));
        SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
        line.open(stream.getFormat(), BUFFER_SIZE);
        line.start();
        stream.mark((int)(stream.getFrameLength() * format.getFrameSize()));

        int numRead = 0;
        byte[] buf = new byte[line.getBufferSize()];
        double[][] doubleArray = new double[channels][buf.length / frameSize];
        while (true) {
	        while ((numRead = stream.read(buf, 0, buf.length)) >= 0) {
	        	//Convert from byte to double array
	        	doubleArray = toDoubleArray(buf, frameSize, channels);

	        	//Process Audio
	        	doubleArray = processAudio.processData(doubleArray);

	        	//Convert back from double to byte array
	        	buf = toByteArray(doubleArray, frameSize, channels);

	        	int offset = 0;
	        	while (offset < numRead) {
	        		offset += line.write(buf, offset, numRead - offset);
	        	}
	        	out.write(buf);

	        	if (bStop) {
					break;
				}
	        }
	    	if (bLoop) {
				stream.reset();
			} else {
				break;
			}
	    	if (bStop) {
				break;
			}
        }

        line.drain();
        line.stop();
        out.flush();
        out.close();

	}

	public void start () {
		System.out.println("Starting audio thread...");
		if (t == null) {
			t = new Thread (this);
			t.start ();
		}
	}

	@Override
	public void run() {
		try {
			playAudio();
		} catch (LineUnavailableException | IOException e) {
			e.printStackTrace();
		}
	}

	private byte[] toByte(double value) {
	    byte[] bytes = new byte[2];
	    ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).putShort((short)value);
	    return bytes;

	}

	private double toDouble(byte[] value) {
		return ByteBuffer.wrap(value).order(ByteOrder.LITTLE_ENDIAN).getShort();
	}

	private byte[] toByteArray(double[][] array, int frameSize, int channel) {
		byte[] byteArray = new byte[array[0].length * frameSize];
		byte[] bytes = new byte[frameSize / channel];
		int index = 0;

		for (int j = 0; j < array[0].length; j++) {
			for (double[] element : array) {
				bytes = toByte(element[j]);
				for (int k = 0; k < frameSize / channel; k++) {
					byteArray[index++] = bytes[k];
				}
			}
		}
		return byteArray;
	}

	private double[][] toDoubleArray(byte[] array, int frameSize, int channel) {
		double[][] doubleArray = new double[channel][array.length / frameSize];
		byte[] bytes = new byte[frameSize / channel];
		int index = 0;

		for (int j = 0; j < doubleArray[0].length; j++) { //data
			for (int i = 0; i < doubleArray.length; i++) { //channel
				for (int k = 0; k < frameSize / channel; k++) {
					bytes[k] = array[index++];
				}
				doubleArray[i][j] = toDouble(bytes);
			}
		}
		return doubleArray;
	}
}
