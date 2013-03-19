package jSIMD;

public class SSE2Processor extends SSEProcessor implements SSE2Functions {

	@Override
	public void Add(PackedByte A, PackedByte B, PackedByte C) {
		
		if((A.length!=B.length) && (A.length!=C.length))
		{
			throw(new IllegalArgumentException("Arrays must be the same length"));
		}
		int opcode = this.createOpCode(Operations.OperationList.ADD.getCode(), A.type, B.type, C.type);
		ExecutionItem ei = new ExecutionItem(opcode,A.index,B.index,C.index);
		this.el.Add(ei);
	}

	@Override
	public void Add(PackedInt A, PackedInt B, PackedInt C) {
		if((A.length!=B.length) && (A.length!=C.length))
		{
			throw(new IllegalArgumentException("Arrays must be the same length"));
		}
		int opcode = this.createOpCode(Operations.OperationList.ADD.getCode(), A.type, B.type, C.type);
		ExecutionItem ei = new ExecutionItem(opcode,A.index,B.index,C.index);
		this.el.Add(ei);
	}

	@Override
	public void Add(PackedShort A, PackedShort B, PackedShort C) {
		if((A.length!=B.length) && (A.length!=C.length))
		{
			throw(new IllegalArgumentException("Arrays must be the same length"));
		}
		int opcode = this.createOpCode(Operations.OperationList.ADD.getCode(), A.type, B.type, C.type);
		ExecutionItem ei = new ExecutionItem(opcode,A.index,B.index,C.index);
		this.el.Add(ei);
	}

	@Override
	public void AddS(PackedByte A, PackedByte B, PackedByte C) {
		if((A.length!=B.length) && (A.length!=C.length))
		{
			throw(new IllegalArgumentException("Arrays must be the same length"));
		}
		int opcode = this.createOpCode(Operations.OperationList.ADD.getCode(), A.type, B.type, C.type);
		ExecutionItem ei = new ExecutionItem(opcode,A.index,B.index,C.index);
		this.el.Add(ei);
	}

	@Override
	public void AddS(PackedShort A, PackedShort B, PackedShort C) {
		if((A.length!=B.length) && (A.length!=C.length))
		{
			throw(new IllegalArgumentException("Arrays must be the same length"));
		}
		int opcode = this.createOpCode(Operations.OperationList.ADDS.getCode(), A.type, B.type, C.type);
		ExecutionItem ei = new ExecutionItem(opcode,A.index,B.index,C.index);
		this.el.Add(ei);
	}

	@Override
	public void And(PackedDouble A, PackedDouble B, PackedDouble C) {
		if((A.length!=B.length) && (A.length!=C.length))
		{
			throw(new IllegalArgumentException("Arrays must be the same length"));
		}
		int opcode = this.createOpCode(Operations.OperationList.AND.getCode(), A.type, B.type, C.type);
		ExecutionItem ei = new ExecutionItem(opcode,A.index,B.index,C.index);
		this.el.Add(ei);
	}
	
	public void And(PackedInt A, PackedInt B, PackedInt C)
	{
		if((A.length!=B.length) && (A.length!=C.length))
		{
			throw(new IllegalArgumentException("Arrays must be the same length"));
		}
		int opcode = this.createOpCode(Operations.OperationList.AND.getCode(), A.type, B.type, C.type);
		ExecutionItem ei = new ExecutionItem(opcode,A.index,B.index,C.index);
		this.el.Add(ei);
	}
	public void And(PackedInt A, int arg, PackedInt C)
	{
		if(A.length!=C.length)
		{
			throw(new IllegalArgumentException("Arrays must be the same length"));
		}
		int opcode = this.createOpCode(Operations.OperationList.AND.getCode(), A.type, A.type|0x8, C.type);
		ExecutionItem ei = new ExecutionItem(opcode,A.index,A.index,C.index,arg);
		this.el.Add(ei);
	}

	@Override
	public void Equal(PackedByte A, PackedByte B, PackedByte C) {
		if((A.length!=B.length) && (A.length!=C.length))
		{
			throw(new IllegalArgumentException("Arrays must be the same length"));
		}
		int opcode = this.createOpCode(Operations.OperationList.EQUAL.getCode(), A.type, B.type, C.type);
		ExecutionItem ei = new ExecutionItem(opcode,A.index,B.index,C.index);
		this.el.Add(ei);
	}

	@Override
	public void Equal(PackedInt A, PackedInt B, PackedInt C) {
		if((A.length!=B.length) && (A.length!=C.length))
		{
			throw(new IllegalArgumentException("Arrays must be the same length"));
		}
		int opcode = this.createOpCode(Operations.OperationList.EQUAL.getCode(), A.type, B.type, C.type);
		ExecutionItem ei = new ExecutionItem(opcode,A.index,B.index,C.index);
		this.el.Add(ei);
	}

	@Override
	public void Equal(PackedShort A, PackedShort B, PackedShort C) {
		if((A.length!=B.length) && (A.length!=C.length))
		{
			throw(new IllegalArgumentException("Arrays must be the same length"));
		}
		int opcode = this.createOpCode(Operations.OperationList.EQUAL.getCode(), A.type, B.type, C.type);
		ExecutionItem ei = new ExecutionItem(opcode,A.index,B.index,C.index);
		this.el.Add(ei);
	}

	@Override
	public void GreaterThan(PackedByte A, PackedByte B, PackedByte C) {
		if((A.length!=B.length) && (A.length!=C.length))
		{
			throw(new IllegalArgumentException("Arrays must be the same length"));
		}
		int opcode = this.createOpCode(Operations.OperationList.GREATERTHAN.getCode(), A.type, B.type, C.type);
		ExecutionItem ei = new ExecutionItem(opcode,A.index,B.index,C.index);
		this.el.Add(ei);
	}

	@Override
	public void GreaterThan(PackedInt A, PackedInt B, PackedInt C) {
		if((A.length!=B.length) && (A.length!=C.length))
		{
			throw(new IllegalArgumentException("Arrays must be the same length"));
		}
		int opcode = this.createOpCode(Operations.OperationList.GREATERTHAN.getCode(), A.type, B.type, C.type);
		ExecutionItem ei = new ExecutionItem(opcode,A.index,B.index,C.index);
		this.el.Add(ei);
	}

	@Override
	public void GreaterThan(PackedShort A, PackedShort B, PackedShort C) {
		if((A.length!=B.length) && (A.length!=C.length))
		{
			throw(new IllegalArgumentException("Arrays must be the same length"));
		}
		int opcode = this.createOpCode(Operations.OperationList.GREATERTHAN.getCode(), A.type, B.type, C.type);
		ExecutionItem ei = new ExecutionItem(opcode,A.index,B.index,C.index);
		this.el.Add(ei);
	}

	@Override
	public void LessThan(PackedDouble A, PackedDouble B, PackedDouble C) {
		if((A.length!=B.length) && (A.length!=C.length))
		{
			throw(new IllegalArgumentException("Arrays must be the same length"));
		}
		int opcode = this.createOpCode(Operations.OperationList.LESSTHAN.getCode(), A.type, B.type, C.type);
		ExecutionItem ei = new ExecutionItem(opcode,A.index,B.index,C.index);
		this.el.Add(ei);
	}

	@Override
	public void Max(PackedFloat A, PackedFloat B, PackedFloat C) {
		if((A.length!=B.length) && (A.length!=C.length))
		{
			throw(new IllegalArgumentException("Arrays must be the same length"));
		}
		int opcode = this.createOpCode(Operations.OperationList.MAX.getCode(), A.type, B.type, C.type);
		ExecutionItem ei = new ExecutionItem(opcode,A.index,B.index,C.index);
		this.el.Add(ei);
	}

	@Override
	public void Max(PackedShort A, PackedShort B, PackedShort C) {
		if((A.length!=B.length) && (A.length!=C.length))
		{
			throw(new IllegalArgumentException("Arrays must be the same length"));
		}
		int opcode = this.createOpCode(Operations.OperationList.MAX.getCode(), A.type, B.type, C.type);
		ExecutionItem ei = new ExecutionItem(opcode,A.index,B.index,C.index);
		this.el.Add(ei);
	}

	@Override
	public void Min(PackedShort A, PackedShort B, PackedShort C) {
		if((A.length!=B.length) && (A.length!=C.length))
		{
			throw(new IllegalArgumentException("Arrays must be the same length"));
		}
		int opcode = this.createOpCode(Operations.OperationList.MIN.getCode(), A.type, B.type, C.type);
		ExecutionItem ei = new ExecutionItem(opcode,A.index,B.index,C.index);
		this.el.Add(ei);
	}

	@Override
	public void Mult(PackedDouble A, PackedDouble B, PackedDouble C) {
		if((A.length!=B.length) && (A.length!=C.length))
		{
			throw(new IllegalArgumentException("Arrays must be the same length"));
		}
		int opcode = this.createOpCode(Operations.OperationList.MIN.getCode(), A.type, B.type, C.type);
		ExecutionItem ei = new ExecutionItem(opcode,A.index,B.index,C.index);
		this.el.Add(ei);
	}

	@Override
	public void Mult(PackedInt A, PackedInt B, PackedLong C) {
		if((A.length!=B.length) && (A.length!=C.length))
		{
			throw(new IllegalArgumentException("Arrays must be the same length"));
		}
		int opcode = this.createOpCode(Operations.OperationList.MULT.getCode(), A.type, B.type, C.type);
		ExecutionItem ei = new ExecutionItem(opcode,A.index,B.index,C.index);
		this.el.Add(ei);
	}

	@Override
	public void MultH(PackedShort A, PackedShort B, PackedShort C) {
		if((A.length!=B.length) && (A.length!=C.length))
		{
			throw(new IllegalArgumentException("Arrays must be the same length"));
		}
		int opcode = this.createOpCode(Operations.OperationList.MULTH.getCode(), A.type, B.type, C.type);
		ExecutionItem ei = new ExecutionItem(opcode,A.index,B.index,C.index);
		this.el.Add(ei);
	}

	@Override
	public void MultL(PackedShort A, PackedShort B, PackedShort C) {
		if((A.length!=B.length) && (A.length!=C.length))
		{
			throw(new IllegalArgumentException("Arrays must be the same length"));
		}
		int opcode = this.createOpCode(Operations.OperationList.MULTL.getCode(), A.type, B.type, C.type);
		ExecutionItem ei = new ExecutionItem(opcode,A.index,B.index,C.index);
		this.el.Add(ei);
	}

	@Override
	public void Or(PackedDouble A, PackedDouble B, PackedDouble C) {
		if((A.length!=B.length) && (A.length!=C.length))
		{
			throw(new IllegalArgumentException("Arrays must be the same length"));
		}
		int opcode = this.createOpCode(Operations.OperationList.OR.getCode(), A.type, B.type, C.type);
		ExecutionItem ei = new ExecutionItem(opcode,A.index,B.index,C.index);
		this.el.Add(ei);
	}

	public void Or(PackedInt A, PackedInt B, PackedInt C) {
		if((A.length!=B.length) && (A.length!=C.length))
		{
			throw(new IllegalArgumentException("Arrays must be the same length"));
		}
		int opcode = this.createOpCode(Operations.OperationList.OR.getCode(), A.type, B.type, C.type);
		ExecutionItem ei = new ExecutionItem(opcode,A.index,B.index,C.index);
		this.el.Add(ei);
	}
	public void Or(PackedInt A, int arg, PackedInt C) {
		if(A.length!=C.length)
		{
			throw(new IllegalArgumentException("Arrays must be the same length"));
		}
		int opcode = this.createOpCode(Operations.OperationList.OR.getCode(), A.type, A.type|0x8, C.type);
		ExecutionItem ei = new ExecutionItem(opcode,A.index,A.index,C.index,arg);
		this.el.Add(ei);
	}

	@Override
	public void SLL(PackedInt A, int count, PackedInt C) {
		if(A.length!=C.length)
		{
			throw(new IllegalArgumentException("Arrays must be the same length"));
		}
		int opcode = this.createOpCode(Operations.OperationList.SLL.getCode(), A.type, A.type|0x8, C.type);
		ExecutionItem ei = new ExecutionItem(opcode,A.index,A.index,C.index, count);
		this.el.Add(ei);
	}

	@Override
	public void SRA(PackedInt A, int count, PackedInt C) {
		if(A.length!=C.length)
		{
			throw(new IllegalArgumentException("Arrays must be the same length"));
		}
		int opcode = this.createOpCode(Operations.OperationList.SRA.getCode(), A.type, A.type|0x8, C.type);
		ExecutionItem ei = new ExecutionItem(opcode,A.index,A.index,C.index, count);
		this.el.Add(ei);
	}
	
	public void SRL(PackedInt A, int count, PackedInt C)
	{
		if(A.length!=C.length)
		{
			throw(new IllegalArgumentException("Arrays must be the same length"));
		}
		int opcode = this.createOpCode(Operations.OperationList.SRL.getCode(), A.type, A.type|8, C.type);
		ExecutionItem ei = new ExecutionItem(opcode,A.index,A.index,C.index, count);
		this.el.Add(ei);
	}

	@Override
	public void Sqrt(PackedDouble A, PackedDouble C) {
		if(A.length!=C.length)
		{
			throw(new IllegalArgumentException("Arrays must be the same length"));
		}
		int opcode = this.createOpCode(Operations.OperationList.SQRT.getCode(), A.type, A.type, C.type);
		ExecutionItem ei = new ExecutionItem(opcode,A.index,A.index,C.index);
		this.el.Add(ei);
	}

	@Override
	public void Sub(PackedByte A, PackedByte B, PackedByte C) {
		if((A.length!=B.length) && (A.length!=C.length))
		{
			throw(new IllegalArgumentException("Arrays must be the same length"));
		}
		int opcode = this.createOpCode(Operations.OperationList.SUB.getCode(), A.type, B.type, C.type);
		ExecutionItem ei = new ExecutionItem(opcode,A.index,B.index,C.index);
		this.el.Add(ei);
	}

	@Override
	public void Sub(PackedDouble A, PackedDouble B, PackedDouble C) {
		if((A.length!=B.length) && (A.length!=C.length))
		{
			throw(new IllegalArgumentException("Arrays must be the same length"));
		}
		int opcode = this.createOpCode(Operations.OperationList.SUB.getCode(), A.type, B.type, C.type);
		ExecutionItem ei = new ExecutionItem(opcode,A.index,B.index,C.index);
		this.el.Add(ei);
	}

	@Override
	public void Sub(PackedInt A, PackedInt B, PackedInt C) {
		if((A.length!=B.length) && (A.length!=C.length))
		{
			throw(new IllegalArgumentException("Arrays must be the same length"));
		}
		int opcode = this.createOpCode(Operations.OperationList.SUB.getCode(), A.type, B.type, C.type);
		ExecutionItem ei = new ExecutionItem(opcode,A.index,B.index,C.index);
		this.el.Add(ei);
	}

	@Override
	public void Sub(PackedShort A, PackedShort B, PackedShort C) {
		if((A.length!=B.length) && (A.length!=C.length))
		{
			throw(new IllegalArgumentException("Arrays must be the same length"));
		}
		int opcode = this.createOpCode(Operations.OperationList.SUB.getCode(), A.type, B.type, C.type);
		ExecutionItem ei = new ExecutionItem(opcode,A.index,B.index,C.index);
		this.el.Add(ei);
	}

	@Override
	public void SubS(PackedByte A, PackedByte B, PackedByte C) {
		if((A.length!=B.length) && (A.length!=C.length))
		{
			throw(new IllegalArgumentException("Arrays must be the same length"));
		}
		int opcode = this.createOpCode(Operations.OperationList.SUBS.getCode(), A.type, B.type, C.type);
		ExecutionItem ei = new ExecutionItem(opcode,A.index,B.index,C.index);
		this.el.Add(ei);
	}

	@Override
	public void SubS(PackedChar A, PackedChar B, PackedChar C) {
		if((A.length!=B.length) && (A.length!=C.length))
		{
			throw(new IllegalArgumentException("Arrays must be the same length"));
		}
		int opcode = this.createOpCode(Operations.OperationList.SUBS.getCode(), A.type, B.type, C.type);
		ExecutionItem ei = new ExecutionItem(opcode,A.index,B.index,C.index);
		this.el.Add(ei);
	}

	@Override
	public void SubS(PackedShort A, PackedShort B, PackedShort C) {
		if((A.length!=B.length) && (A.length!=C.length))
		{
			throw(new IllegalArgumentException("Arrays must be the same length"));
		}
		int opcode = this.createOpCode(Operations.OperationList.SUBS.getCode(), A.type, B.type, C.type);
		ExecutionItem ei = new ExecutionItem(opcode,A.index,B.index,C.index);
		this.el.Add(ei);
	}

	@Override
	public void XOr(PackedDouble A, PackedDouble B, PackedDouble C) {
		if((A.length!=B.length) && (A.length!=C.length))
		{
			throw(new IllegalArgumentException("Arrays must be the same length"));
		}
		int opcode = this.createOpCode(Operations.OperationList.XOR.getCode(), A.type, B.type, C.type);
		ExecutionItem ei = new ExecutionItem(opcode,A.index,B.index,C.index);
		this.el.Add(ei);
	}

//	@Override
//	public void Interleave(PackedShort A, PackedShort B, PackedInt C) {
//		if((A.length!=B.length) && (A.length!=C.length))
//		{
//			throw(new IllegalArgumentException("Arrays must be the same length"));
//		}
//		int opcode = this.createOpCode(Operations.OperationList.INTERLEAVE.getCode(), A.type, B.type, C.type);
//		ExecutionItem ei = new ExecutionItem(opcode,A.index,B.index,C.index);
//		this.el.Add(ei);
//		
//	}
//
//	@Override
//	public void Interleave(PackedInt A, PackedInt B, PackedLong C) {
//		if((A.length!=B.length) && (A.length!=C.length))
//		{
//			throw(new IllegalArgumentException("Arrays must be the same length"));
//		}
//		int opcode = this.createOpCode(Operations.OperationList.INTERLEAVE.getCode(), A.type, B.type, C.type);
//		ExecutionItem ei = new ExecutionItem(opcode,A.index,B.index,C.index);
//		this.el.Add(ei);
//		
//	}
//
//	@Override
//	public void Interleave(PackedByte A, PackedByte B, PackedShort C) {
//		if((A.length!=B.length) && (A.length!=C.length))
//		{
//			throw(new IllegalArgumentException("Arrays must be the same length"));
//		}
//		int opcode = this.createOpCode(Operations.OperationList.INTERLEAVE.getCode(), A.type, B.type, C.type);
//		ExecutionItem ei = new ExecutionItem(opcode,A.index,B.index,C.index);
//		this.el.Add(ei);
//		
//	}

	@Override
	public void InterleaveHigh(PackedByte A, PackedByte B, PackedByte C) {
		if((A.length!=B.length) && (A.length!=C.length))
		{
			throw(new IllegalArgumentException("Arrays must be the same length"));
		}
		int opcode = this.createOpCode(Operations.OperationList.INTERLEAVEHIGH.getCode(), A.type, B.type, C.type);
		ExecutionItem ei = new ExecutionItem(opcode,A.index,B.index,C.index);
		this.el.Add(ei);
		
	}

	@Override
	public void InterleaveHigh(PackedShort A, PackedShort B, PackedShort C) {
		if((A.length!=B.length) && (A.length!=C.length))
		{
			throw(new IllegalArgumentException("Arrays must be the same length"));
		}
		int opcode = this.createOpCode(Operations.OperationList.INTERLEAVEHIGH.getCode(), A.type, B.type, C.type);
		ExecutionItem ei = new ExecutionItem(opcode,A.index,B.index,C.index);
		this.el.Add(ei);
		
	}

	@Override
	public void InterleaveHigh(PackedInt A, PackedInt B, PackedInt C) {
		if((A.length!=B.length) && (A.length!=C.length))
		{
			throw(new IllegalArgumentException("Arrays must be the same length"));
		}
		int opcode = this.createOpCode(Operations.OperationList.INTERLEAVEHIGH.getCode(), A.type, B.type, C.type);
		ExecutionItem ei = new ExecutionItem(opcode,A.index,B.index,C.index);
		this.el.Add(ei);
		
	}

	@Override
	public void InterleaveHigh(PackedLong A, PackedLong B, PackedLong C) {
		if((A.length!=B.length) && (A.length!=C.length))
		{
			throw(new IllegalArgumentException("Arrays must be the same length"));
		}
		int opcode = this.createOpCode(Operations.OperationList.INTERLEAVEHIGH.getCode(), A.type, B.type, C.type);
		ExecutionItem ei = new ExecutionItem(opcode,A.index,B.index,C.index);
		this.el.Add(ei);
		
	}

	@Override
	public void InterleaveLow(PackedByte A, PackedByte B, PackedByte C) {
		if((A.length!=B.length) && (A.length!=C.length))
		{
			throw(new IllegalArgumentException("Arrays must be the same length"));
		}
		int opcode = this.createOpCode(Operations.OperationList.INTERLEAVELOW.getCode(), A.type, B.type, C.type);
		ExecutionItem ei = new ExecutionItem(opcode,A.index,B.index,C.index);
		this.el.Add(ei);
		
	}

	@Override
	public void InterleaveLow(PackedShort A, PackedShort B, PackedShort C) {
		if((A.length!=B.length) && (A.length!=C.length))
		{
			throw(new IllegalArgumentException("Arrays must be the same length"));
		}
		int opcode = this.createOpCode(Operations.OperationList.INTERLEAVELOW.getCode(), A.type, B.type, C.type);
		ExecutionItem ei = new ExecutionItem(opcode,A.index,B.index,C.index);
		this.el.Add(ei);
		
	}

	@Override
	public void InterleaveLow(PackedInt A, PackedInt B, PackedInt C) {
		if((A.length!=B.length) && (A.length!=C.length))
		{
			throw(new IllegalArgumentException("Arrays must be the same length"));
		}
		int opcode = this.createOpCode(Operations.OperationList.INTERLEAVELOW.getCode(), A.type, B.type, C.type);
		ExecutionItem ei = new ExecutionItem(opcode,A.index,B.index,C.index);
		this.el.Add(ei);
		
	}

	@Override
	public void InterleaveLow(PackedLong A, PackedLong B, PackedLong C) {
		if((A.length!=B.length) && (A.length!=C.length))
		{
			throw(new IllegalArgumentException("Arrays must be the same length"));
		}
		int opcode = this.createOpCode(Operations.OperationList.INTERLEAVELOW.getCode(), A.type, B.type, C.type);
		ExecutionItem ei = new ExecutionItem(opcode,A.index,B.index,C.index);
		this.el.Add(ei);
	}
	
	public void Execute(){
		el.populateVectors();
		simdMapper.NativeLibrary.Execute(el);
		el = new ExecutionList();
	}
}