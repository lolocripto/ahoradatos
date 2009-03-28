package ar.com.datos.reproduccionaudio.core;

import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import ar.com.datos.reproduccionaudio.exception.ForgotInitSimpleAudioPlayerException;
import ar.com.datos.reproduccionaudio.exception.SimpleAudioPlayerException;

/**
 * .
 * @author cristian
 *
 */
public class SimpleAudioPlayer extends Thread {
	
	/**
	 * 
	 */
	private static final int EXTERNAL_BUFFER_SIZE = 128000;
	
	/**
	 * .
	 */
	private InputStream m_input;
	
	/**
	 * .
	 */
	private AudioInputStream m_audioInputStream;
	
	/**
	 * .
	 */
	private SourceDataLine m_line;
	
	/**
	 * 
	 * @param input .
	 */
	public SimpleAudioPlayer(final InputStream input) {
		m_input = input;
	}
	
	/**
	 * .
	 * @throws SimpleAudioPlayerException .
	 */
	public final void init() throws SimpleAudioPlayerException {
		
		try {
			m_audioInputStream = AudioSystem.getAudioInputStream(m_input);

			AudioFormat audioFormat = m_audioInputStream.getFormat();

			DataLine.Info info = new DataLine.Info(SourceDataLine.class,
					audioFormat);
			m_line = (SourceDataLine) AudioSystem.getLine(info);
			m_line.open(audioFormat);
		} catch (LineUnavailableException e) {
			throw new SimpleAudioPlayerException(e);
		} catch (IOException e) {
			throw new SimpleAudioPlayerException(e);
		} catch (UnsupportedAudioFileException e) {
			throw new SimpleAudioPlayerException(e);
		}
	}

	/**
	 * .
	 * @throws SimpleAudioPlayerException .
	 */
	public final void startPlaying() throws SimpleAudioPlayerException {
		try {
			start();
		} catch (NullPointerException e) {
			throw new ForgotInitSimpleAudioPlayerException();
		}
	}

	/**
	 * .
	 */
	public final void start() {
		m_line.start();
		super.start();
	}

	/**
	 * .
	 */
	public final void stopPlaying() {
		m_line.stop();
		m_line.close();
	}

	/**
	 * .
	 */
	public final void run() {
		int nBytesRead = 0;
		byte[] abData = new byte[EXTERNAL_BUFFER_SIZE];
		while (nBytesRead != -1) {
			try {
				nBytesRead = m_audioInputStream.read(abData, 0, abData.length);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (nBytesRead >= 0) {
				m_line.write(abData, 0, nBytesRead);
			}
		}
		m_line.drain();
		m_line.close();
	}

}


