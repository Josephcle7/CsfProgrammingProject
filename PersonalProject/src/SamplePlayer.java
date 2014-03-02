package com.craig1.softsynth.utils;
package com.craig1.softsynth.consumer;

import com.craig1.softsynth.utils.SampleProviderIntfc;
import javax.sound.sampled*;
import javax.sound.sampled.AudioSystem;

public class SamplePlayer extends Thread{
	
	public interface SampleProviderIntfc{
		
		int getSamples(byte [] samples);
		
	}
	
	//AudioFormat parameters
	public static final int sample_rate = 22050;
	private static final int sample_size = 16;
	private static final int channels = 1;
	private static final boolean signed = true;
	private static final boolean big_endian = true;
	
	//Chunk of audio processed at one time
	public static final int buffer_size = 1000;
	public static final int samples_per_buffer = buffer_size / 2;
	
	public SamplePlayer(){
		
		//Create the audio format we wish to use
		format = new AudioFormat(sample_rate, sample_size, channels, signed, big_endian);
		
		//Create new dataline info object describing line format
		info = new DataLine.Info(sourceDataLine.class, format);
	}
	
	public void run() {
		
		done = false;
		int nBytesRead = 0;
		
		try {
			//Get line to write data to
			auline = (SourceDataLine) AudioSystem.getLine(info);
			audline.open(format);
			auline.start();
			
			while ((nBytesRead != -1) && (! done)){
				nBytesRead = provider.getSamples(sampleData);
				if (nBytesRead > 0) {
					auline.write(sampleData, 0, nBytesRead);
				}
			}
		} catch(Exception e){
			e.printStackTrace();
		} finally {
			auline.drain();
			auline.close();
		}
	}
	
	public void startPlayer(){
		if (provider != null){
			start();
		}
	}
	
	public void stopPlayer(){
		done = true;
	}
	
	public void setSampleProvider(SampleProviderIntfc provider){
		this.provider = provider;
	}
	
	//Instance data
	private AudioFormat format;
	private DataLine.Info info;
	private SourceDataLine auline;
	private boolean done;
	private byte [] sampleData = new byte[buffer_size];
	private SampleProviderIntfc provider;
				
			
		
	}


