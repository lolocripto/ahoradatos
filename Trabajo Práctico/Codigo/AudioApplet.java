package ar.com.datos.capturaaudio.gui;

import java.applet.Applet;
import java.awt.Button;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.sound.sampled.AudioFileFormat;

import ar.com.datos.capturaaudio.core.SimpleAudioRecorder;

public class AudioApplet extends Applet implements ActionListener{

	 private SimpleAudioRecorder sr;
	 private Button start;
	 private Button stop;

	 public void init(){
		 
		 start = new Button("Start");
		 stop = new Button("Stop");
		 
		 add(start);
		 add(stop);
		 
		 //Archivo para guardar el sonido
		 String	strFilename = "test.au";
		 File	outputFile = new File(strFilename);
		 
		OutputStream os = null;
		try {
			os = new FileOutputStream(outputFile);
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
		
		 //Formato para guardar el sonido
		 AudioFileFormat.Type	targetType = AudioFileFormat.Type.AU;
		
		 //Creo el grabador
		 sr = new SimpleAudioRecorder(targetType, os);
		 try {
			sr.init();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		 
		 start.addActionListener(this);
		 stop.addActionListener(this);
	 }
	 
	 public void actionPerformed(ActionEvent evt) {
		 
		if (evt.getSource() == start) {
			try {
				sr.startRecording();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		} else if (evt.getSource() == stop) {
			sr.stopRecording();
		}
	}
	
}
