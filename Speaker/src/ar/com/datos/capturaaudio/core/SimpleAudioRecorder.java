package ar.com.datos.capturaaudio.core;
import java.io.IOException;
import java.io.OutputStream;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import ar.com.datos.capturaaudio.exception.ForgotInitSimpleAudioRecorderException;
import ar.com.datos.capturaaudio.exception.SimpleAudioRecorderException;


public class SimpleAudioRecorder extends Thread{
	
	private TargetDataLine		m_line;
	private AudioFileFormat.Type	m_targetType;
	private AudioInputStream	m_audioInputStream;
	private OutputStream	m_output;


	public SimpleAudioRecorder(AudioFileFormat.Type targetType, OutputStream output){
		super();
		m_targetType = targetType;
		m_output = output;
	}
	
	
	public void init()throws SimpleAudioRecorderException{
		AudioFormat	audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,44100.0F, 16, 2, 4, 44100.0F, false);
		init(audioFormat);
	}
	
	public void init(AudioFormat audioFormat)throws SimpleAudioRecorderException {
		DataLine.Info info = new DataLine.Info(TargetDataLine.class,audioFormat);
		try{
			m_line = (TargetDataLine) AudioSystem.getLine(info);
			m_line.open(audioFormat);
			
		}catch(LineUnavailableException e){
			throw new SimpleAudioRecorderException(e);
		}
		
		m_audioInputStream = new AudioInputStream(m_line);
	}
	
	/*Forgetting to call init()*/
	public void startRecording() throws SimpleAudioRecorderException{
		try{
			start();
		}catch(NullPointerException e){
			throw new ForgotInitSimpleAudioRecorderException();
		}
	}
	
	/* Starts the recording.The line is started and the thread is started.*/
	public void start()
	{
		m_line.start();
		super.start();
	}


	/* Stops the recording.*/
	public void stopRecording()
	{
		m_line.stop();
		m_line.close();
	}

	public void run()
	{
			try
			{
					AudioSystem.write(m_audioInputStream, m_targetType,	m_output);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
	}

	public OutputStream getOutput(){
		if(!isAlive())
			return m_output;
		else
			return null;
	}
}


