package ar.com.datos.reproduccionaudio.gui;
import java.applet.Applet;
import java.awt.Button;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import ar.com.datos.reproduccionaudio.core.SimpleAudioPlayer;


public class AudioApplet extends Applet implements ActionListener{
	private SimpleAudioPlayer sp;
	private Button start;

	public void init(){
		start = new Button("Start");
		add(start);
		start.addActionListener(this);
		
		
		String	strFilename = "myfile.au";
		
		//Archivo de sonido a reproducir
		File	soundFile = new File(strFilename);
		FileInputStream inf = null;
		
		//Necesito una clase que redefina los métodos mark() y reset
		BufferedInputStream b = null;
		try{
			inf = new FileInputStream(soundFile);
			 b = new BufferedInputStream(inf);
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}
		 
		//Creo el reproductor con el buffer a reproducir
		sp = new SimpleAudioPlayer(b);
		try{
			 sp.init();
		 }catch(Exception e){
			 System.out.println(e.getMessage());
			 e.printStackTrace();
		 }

	}
	
	 public void actionPerformed(ActionEvent evt){
		 if (evt.getSource() == start)
			 try{
				 sp.startPlaying();
			 }catch(Exception e){
				 e.printStackTrace();
			 }
	 }
	 
}
