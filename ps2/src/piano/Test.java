package piano;
import java.util.ArrayList;

import javax.sound.midi.*;


public class Test {
    public static void main(String[] args) throws InvalidMidiDataException, MidiUnavailableException, InterruptedException {
        Synthesizer synth = MidiSystem.getSynthesizer();
        synth.open();
        MidiChannel chan[] = synth.getChannels(); 
        // Check for null; maybe not all 16 channels exist.
        if (chan[4] != null) {
             chan[4].noteOn(60, 100); 
        }
        Thread.sleep(2000);
    }
}
