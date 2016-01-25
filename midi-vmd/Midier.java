package midier;

import javax.sound.midi.InvalidMidiDataException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MIDIer 
{
	public static void main(String[] args)
	{
		File midi=new File("default");
		File vmd=new File("default");
		MidiAnalyzer midianalyzer=null;
		VMDWriter vmdwriter=null;
		
		Get_MIDI:
			while(true) //loops infinitely while catching exceptions
			{
				try 
				{
					JFileChooser opener = new JFileChooser();
					int option = opener.showOpenDialog(new Frame("Select MIDI file to read:"));
					if (option == JFileChooser.APPROVE_OPTION) 
						midi = opener.getSelectedFile();
					if (option == JFileChooser.CANCEL_OPTION)
						System.exit(0);
					midianalyzer = new MidiAnalyzer(midi); //TODO: have user pick track
					break Get_MIDI;
				}
				catch (InvalidMidiDataException e) 
				{JOptionPane.showMessageDialog(new Frame(), "Invalid MIDI file.");} 
				catch (IOException e) 
				{JOptionPane.showMessageDialog(new Frame(), "Error reading file.");}
			}
		System.out.println("MIDIAnalyzer created.");
		
		Set_VMD:
			while(true)
			{
				try 
				{
					JFileChooser saver = new JFileChooser();
					int option = saver.showSaveDialog(new Frame("Save to:"));
					if (option == JFileChooser.APPROVE_OPTION) 
						vmd = saver.getSelectedFile();
					if (option == JFileChooser.CANCEL_OPTION)
						System.exit(0);
					vmdwriter=new VMDWriter(vmd);
					break Set_VMD;
				}
				catch (IOException e) 
				{JOptionPane.showMessageDialog(new Frame(), "Error reading file.");}
			}
		System.out.println("VMDWriter created.");
		
		ArrayList<VMDEvent> events=midianalyzer.getEvents();
		System.out.println("VMDEvents obtained. Events: " + events.size());
		for(VMDEvent event:events)
		{
			try 
			{
				vmdwriter.writeEvent(event);
				System.out.println("Event written: " + vmdwriter.boneNames.get(event.getNoteName()+3) + (event.getOn()?"on":"off") + ", " + event.getFrame());
			} 
			catch (IOException e)
			{
				JOptionPane.showMessageDialog(new Frame(), "Error writing to file.");
				System.exit(1); //TODO:error log?
			}
		}
		System.out.println("Done.");
	}
	public static String a(byte[] arr)
	{
		String returnme="[";
		for(byte b:arr)
		{
			returnme+=b+", ";
		}
		return returnme+"]";
	}
}
