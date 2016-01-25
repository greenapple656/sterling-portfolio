package midier;

public class VMDEvent implements Comparable<VMDEvent>
{
	private int note;
	private long frame;
	boolean on;
	
	public VMDEvent(){}
	public VMDEvent(int note, long frame, boolean on)
	{
		this.setNote(note);
		this.setFrame(frame);
		this.setOn(on);
	}
	
	//getters and setters.	
	public long getFrame() {return frame;}
	public void setFrame(long frame) {this.frame = frame;}
	public int getNoteName() {return note;}
	public void setNote(int note) {this.note = note;}
	public boolean getOn() {return on;}
	public void setOn(boolean on) {this.on = on;}
	@Override
	public int compareTo(VMDEvent other) 
	{
		return Long.compare(this.getFrame(), other.getFrame());
	}
	
}
