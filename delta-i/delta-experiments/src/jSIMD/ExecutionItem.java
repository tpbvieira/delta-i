package jSIMD;

public class ExecutionItem {
	public int Opcode;
	public int SourceA;
	public int SourceB;
	public int Destination;
	public int arg;
	
	public ExecutionItem()
	{
		
	}
	public ExecutionItem(int op, int sa, int sb, int de)
	{
		Opcode = op;
		SourceA = sa;
		SourceB = sb;
		Destination = de;
		arg = 0;
	}
	public ExecutionItem(int op, int sa, int sb, int de, int a)
	{
		Opcode = op;
		SourceA = sa;
		SourceB = sb;
		Destination = de;
		arg = a;
	}
}
