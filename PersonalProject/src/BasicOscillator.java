//import com.craig1.softsynth.consumer.SamplePlayer.SampleProviderIntfc;


public class BasicOscillator implements SampleProviderIntfc {
//Waveshape enumeration
	public enum WAVESHAPE {
		SIN, SQU, SAW
	}
	
	//Basic Oscillator Class Constructor
	//Default instance has SIN waveshape at 1000 Hz
	
	public BasicOscillator(){
		//Set defaults
		setOscWaveshape(WAVESHAPE.SIN);
		setFrequency(1000.0);
	}
	
	//Set waveshape of oscillator
	//waveshape - Determines the waveshape of this oscillator
	
	public void setOscWaveshape(WAVESHAPE waveshape){
		
		this.waveshape = waveshape;
	}
	
	//Set the frequency of the oscillator in Hz.
	//frequency - Frequency in Hz for this 

	public void setFrequency(double frequency){
		
		periodSamples = (long)(SamplePlayer.sample_rate / frequency);
	}
	
	//return the next sample of the oscillator's waveform
	//returns next oscillator sample
	
	protected double getSample() {
		
		double value;
		double x = sampleNumber / (double) periodSamples;
		
		switch (waveshape){
			default:
			case SIN:
				value = Math.sin(2.0 * Math.PI * x);
				break;
			
			case SQU:
				if (sampleNumber < (periodSamples / 2)){
					value = 1.0;
				} else {
					value = -1.0;
				}
				break;
				
			case SAW:
				
				value = 2.0 * (x - Math.floor(x + 0.5));
				break;
		}
		sampleNumber = (sampleNumber + 1) % periodSamples;
		return value;
	}
	//get a buffer of oscillator samples
	//buffer - Array to fill with samples
	//returns Count of bytes produced
	
	public int getSamples(byte [] buffer){
		int index = 0;
		for (int i = 0; i < SamplePlayer.samples_per_buffer; i++){
			double ds = getSample() * Short.MAX_VALUE;
			short ss = (short) Math.round(ds);
			buffer[index++] = (byte)(ss >> 8);
			buffer[index++] = (byte)(ss >> 8);
		}
		return SamplePlayer.buffer_size;
	}
	
	//Instance data
	private WAVESHAPE waveshape;
	private long periodSamples;
	private long sampleNumber;
	
}
