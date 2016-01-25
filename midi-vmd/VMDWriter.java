package midier;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

public class VMDWriter 
{
	public ArrayList<String> boneNames=new ArrayList<String>();
	private long lastframe=0l;
	private Path output;
	
	private final byte[] PMXHEADER; //TODO:can't deal with .PMDs
	private final byte[] KEYDOWN;
	private final byte[] KEYUP;
	
	public VMDWriter(File out) throws IOException
	{
		output=out.toPath();
		getBoneNames(new File("keybonelist.csv"));
		
		PMXHEADER=Files.readAllBytes(FileSystems.getDefault().getPath("header.vmd"));
		KEYDOWN=Files.readAllBytes(FileSystems.getDefault().getPath("keydown.vmd"));
		KEYUP=Files.readAllBytes(FileSystems.getDefault().getPath("keyup.vmd"));
		
		Files.write(output, PMXHEADER);
	}
	private void getBoneNames(File file)//TODO: actually get it from file, so works with more than one .pmd/x piano
	{
		String[] notenames = {"A","A#","B","C","C#","D",
							"D#","E","F","F#","G","G#"};
		for(int i=1; i<=88; i++)
		{
			boneNames.add(notenames[(i-1)%12]+(int)(i/12));
		}
	}

	public void writeEvent(VMDEvent event) throws IOException 
	{
		// name of bone; notes outside keyboard (88 keys) are brought back
		// within range TODO: make this more generalized?
		while (event.getNoteName() < 9) {event.setNote(event.getNoteName() + 12);}
		while (event.getNoteName() > 96) {event.setNote(event.getNoteName() - 12);}
		Files.write(output, boneNames.get(event.getNoteName() - 9).getBytes(),
				StandardOpenOption.APPEND);
		// any way to make longs longer? .vmd files leave 14 bytes (longs are 8) for frame no.
		Files.write(output, repeat(6, (byte) 0x00), StandardOpenOption.APPEND);
		// frame number
		long framerel = event.getFrame() - lastframe;
		if (lastframe == 0) {
			framerel = 0;
		} // if it's the first frame, the relative frame number is 0.
		lastframe = event.getFrame();
		Files.write(output,/* v long to byte[] */
				ByteBuffer.allocate(Long.SIZE / Byte.SIZE).putLong(framerel).array(),
					StandardOpenOption.APPEND);
		// rotations
		Files.write(output, (event.getOn()?KEYDOWN:KEYUP), StandardOpenOption.APPEND);

	}
	
	private static byte[] repeat(int times, byte repeatme) //I miss Haskell.
	{
		byte[] returnme=new byte[times];
		for(int i=0; i<times; i++)
			returnme[i]=repeatme;
		return returnme;
	}
}
