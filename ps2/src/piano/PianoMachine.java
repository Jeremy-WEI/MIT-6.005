package piano;

import music.Pitch;

import javax.sound.midi.MidiUnavailableException;

import music.Pitch;
import midi.*;

public class PianoMachine {
	
	private Midi midi;
	private int pitchTranspose = 0;
    private Instrument instrument = midi.DEFAULT_INSTRUMENT;
    private boolean isRecording;
	/**
	 * constructor for PianoMachine.
	 * 
	 * initialize midi device and any other state that we're storing.
	 */
    public PianoMachine() {
    	try {
            midi = Midi.getInstance();
        } catch (MidiUnavailableException e1) {
            System.err.println("Could not initialize midi device");
            e1.printStackTrace();
            return;
        }
    }
    
    //TODO write method spec
    /**
     * this method plays a specific note according to the Pitch. 
     * a note would begin if it isn't already sounding.
     * @param rawPitch
     */
    public void beginNote(Pitch rawPitch) {
//    	midi.beginNote(new Pitch(0).toMidiFrequency());
    	//TODO implement for question 1
        midi.beginNote(rawPitch.transpose(pitchTranspose).toMidiFrequency(), instrument);
    }
    
    //TODO write method spec
    /**
     * this method stops playing a specific note according to the Pitch. 
     * a note would end if it is currently sounding.
     * @param rawPitch
     */
    public void endNote(Pitch rawPitch) {
//    	midi.endNote(new Pitch(0).toMidiFrequency());
    	//TODO implement for question 1
    	midi.endNote(rawPitch.transpose(pitchTranspose).toMidiFrequency(), instrument);
    }
    
    //TODO write method spec
    public void changeInstrument() {
       	//TODO: implement for question 2
        instrument = instrument.next();
    }
    
    //TODO write method spec
    public void shiftUp() {
    	//TODO: implement for question 3
        pitchTranspose += Pitch.OCTAVE;
    }
    
    //TODO write method spec
    public void shiftDown() {
    	//TODO: implement for question 3
        pitchTranspose -= Pitch.OCTAVE;
    }
    
    //TODO write method spec
    public boolean toggleRecording() {
//    	return false;
    	//TODO: implement for question 4
        isRecording = !isRecording;
        if (isRecording) midi.clearHistory();
    	return isRecording;
    }
//    private Instrument switchIntrument(String instru) {
//        switch (instru) {
//        
//        }
//        return null;
//    }
    //TODO write method spec
    protected void playback() {    	
        //TODO: implement for question 4
        String history =midi.history();
        if (history.length() == 0) return;
        String[] token = history.split("\\s+");
        for (String str : token) {
            String[] split = str.split("[(),]");
            if (split[0].equals("on")) midi.beginNote(Integer.valueOf(split[1])); 
            else if (split[0].equals("wait")) midi.wait(Integer.valueOf(split[1]));
            else if (split[0].equals("off")) midi.endNote(Integer.valueOf(split[1])); 
        }
            
    }
    public static void main (String[] args) throws InterruptedException {
        PianoMachine piano = new PianoMachine();
        piano.beginNote(new Pitch('C'));
        Thread.sleep(10000);
    }

}
