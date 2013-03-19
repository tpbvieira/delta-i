package jSIMD;


public class SSE3Processor extends SSE2Processor implements SSE3Functions 
{

	@Override
	public void AddSub(PackedDouble A, PackedDouble B, PackedDouble C) {
		if((A.length!=B.length) && (A.length!=C.length))
		{
			throw(new IllegalArgumentException("Arrays must be the same length"));
		}
		int opcode = this.createOpCode(Operations.OperationList.ADDSUB.getCode(), A.type, B.type, C.type);
		ExecutionItem ei = new ExecutionItem(opcode,A.index,B.index,C.index);
		this.el.Add(ei);
	}

	@Override
	public void AddSub(PackedFloat A, PackedFloat B, PackedFloat C) {
		if((A.length!=B.length) && (A.length!=C.length))
		{
			throw(new IllegalArgumentException("Arrays must be the same length"));
		}
		int opcode = this.createOpCode(Operations.OperationList.ADDSUB.getCode(), A.type, B.type, C.type);
		ExecutionItem ei = new ExecutionItem(opcode,A.index,B.index,C.index);
		this.el.Add(ei);
	}

	@Override
	public void HAdd(PackedDouble A, PackedDouble B, PackedDouble C) {
		if((A.length!=B.length) && (A.length!=C.length))
		{
			throw(new IllegalArgumentException("Arrays must be the same length"));
		}
		int opcode = this.createOpCode(Operations.OperationList.HORIZADD.getCode(), A.type, B.type, C.type);
		ExecutionItem ei = new ExecutionItem(opcode,A.index,B.index,C.index);
		this.el.Add(ei);
	}

	@Override
	public void HAdd(PackedFloat A, PackedFloat B, PackedFloat C) {
		if((A.length!=B.length) && (A.length!=C.length))
		{
			throw(new IllegalArgumentException("Arrays must be the same length"));
		}
		int opcode = this.createOpCode(Operations.OperationList.HORIZADD.getCode(), A.type, B.type, C.type);
		ExecutionItem ei = new ExecutionItem(opcode,A.index,B.index,C.index);
		this.el.Add(ei);
	}

	@Override
	public void HSub(PackedDouble A, PackedDouble B, PackedDouble C) {
		if((A.length!=B.length) && (A.length!=C.length))
		{
			throw(new IllegalArgumentException("Arrays must be the same length"));
		}
		int opcode = this.createOpCode(Operations.OperationList.HORIZSUB.getCode(), A.type, B.type, C.type);
		ExecutionItem ei = new ExecutionItem(opcode,A.index,B.index,C.index);
		this.el.Add(ei);
	}

	@Override
	public void HSub(PackedFloat A, PackedFloat B, PackedFloat C) {
		if((A.length!=B.length) && (A.length!=C.length))
		{
			throw(new IllegalArgumentException("Arrays must be the same length"));
		}
		int opcode = this.createOpCode(Operations.OperationList.HORIZSUB.getCode(), A.type, B.type, C.type);
		ExecutionItem ei = new ExecutionItem(opcode,A.index,B.index,C.index);
		this.el.Add(ei);
	}
}
