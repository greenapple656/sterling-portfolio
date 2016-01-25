package midier;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;

import javax.sound.midi.*;

public class MidiAnalyzer 
{
	private final int SET_TEMPO=0x51;
	private final int SET_TIMESIG=0x58;
	
	private final Sequence sequence;
	private ArrayList<VMDEvent> events=new ArrayList<VMDEvent>();
	
	private double tempo=120;	   //TODO: deal with non-tempo-based MIDIs
	private int timesigdenom=4;
	private int ticksPerClick=24;
	
	public MidiAnalyzer(File file) throws InvalidMidiDataException, IOException
	{
		sequence = MidiSystem.getSequence(file);
		getTimeInfo(sequence.getTracks()[0]);
	}
	private void getTimeInfo(Track track)
	{
		boolean hasTimesigged=false;
		boolean hasTempoed=false;
		for (int i=0; i < track.size(); i++)
		{ 
            MidiEvent event = track.get(i);
            if(event.getMessage() instanceof MetaMessage)
            {
            	MetaMessage message = (MetaMessage) event.getMessage();
            	if(message.getType()==SET_TIMESIG&&!hasTimesigged)
            	{
            		timesigdenom=(int) Math.pow(2, message.getData()[1]);
            		ticksPerClick=message.getData()[2];
            		hasTimesigged=true;
            	}
            	if(message.getType()==SET_TEMPO&&!hasTempoed)
            	{
            		byte[] data = new byte[4];
            		if(message.getData().length<4)
            		{
            			int first=0;
            			for(int j=0; j<4; j++)
            			{
            				if(4-j>message.getData().length)
            				{
            					data[j]=0;
            					first=j+1;
            				}
            				else
            					data[j]=message.getData()[j-first];
            			}
            		}
            		tempo=60000000/(ByteBuffer.wrap(data).getInt() * timesigdenom/4);
            		hasTempoed=true;
            	}
            }
        }
	}
	private int bytesToInt(byte[] bytes) //TODO: there should be a smarter way to convert.
	{
		int returnme=0;
		for(int i=0; i<bytes.length; i++)
		{
			returnme+=Math.pow(16, i) * bytes[bytes.length-i-1];
		}
		return returnme;
	}
	public ArrayList<VMDEvent> getEvents()
	{
		for(Track track:sequence.getTracks())
		{
			for(int i=0; i<track.size(); i++)
			{
				MidiEvent event = track.get(i);
				long tick = event.getTick();
				MidiMessage message = event.getMessage();
				if(message instanceof ShortMessage && (((ShortMessage) message).getCommand()==ShortMessage.NOTE_ON||
														((ShortMessage) message).getCommand()==ShortMessage.NOTE_OFF))
				{
					if(((ShortMessage) message).getMessage()[2]==80) //note_on. for some reason, getcommand() doesn't work
					{
						events.add(new VMDEvent(((ShortMessage) message).getData1(), //note number
								Math.round(ticksToFrames(tick)),
								false));
						events.add(new VMDEvent(((ShortMessage) message).getData1(), //note number
								Math.round(ticksToFrames(tick))+2,
								true));
					}
					else
					{
						events.add(new VMDEvent(((ShortMessage) message).getData1(), //note number
								Math.round(ticksToFrames(tick))-2, //makes it look more natural
								true));
						events.add(new VMDEvent(((ShortMessage) message).getData1(), //note number
								Math.round(ticksToFrames(tick)),
								false));
					}
				}
			}
		}
		Collections.sort(events); //so that the events will be added in the order they happen, since VMDs use relative time
		return events;
	}
	
	public double ticksToFrames(long ticks)
	{
		double secondsPerTick=60/(ticksPerClick*20*tempo); //I'm a novice with MIDI timing...for some reason, *ing by 20 gives the accurate result
		double framesPerSecond=30; //TODO:make this changeable?
		return ticks*secondsPerTick*framesPerSecond; //ticks*(seconds/ticks)*(frames/second)=frames
	}
}
