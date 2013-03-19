package jSIMD;

public class SSE41Processor extends SSE3Processor implements
		SSE41Functions {

	@Override
	public void Blend(PackedByte A, PackedByte B, PackedByte Mask, PackedByte C) {
		// TODO Auto-generated method stub
	}

	@Override
	public void Blend(PackedDouble A, PackedDouble B, PackedDouble Mask, PackedDouble C) {
		// TODO Auto-generated method stub
	}

	@Override
	public void Blend(PackedFloat A, PackedFloat B, PackedFloat Mask, PackedFloat C) {
		// TODO Auto-generated method stub
	}

	@Override
	public void Blend(PackedInt A, PackedInt B, PackedInt Mask, PackedInt C) {
		// TODO Auto-generated method stub
	}

	@Override
	public void Ceil(PackedDouble A, PackedDouble C) {
		// TODO Auto-generated method stub
	}

	@Override
	public void Ceil(PackedFloat A, PackedFloat C) {
		// TODO Auto-generated method stub
	}

	@Override
	public void Equal(PackedLong A, PackedLong B, PackedLong C) {
		
		// TODO Auto-generated method stub
	}

	@Override
	public void Floor(PackedDouble A, PackedDouble C) {
		// TODO Auto-generated method stub
	}

	@Override
	public void Floor(PackedFloat A, PackedFloat C) {
		// TODO Auto-generated method stub
	}

	@Override
	public void GreaterThan(PackedLong A, PackedLong B, PackedLong C) {
		// TODO Auto-generated method stub
	}

	@Override
	public void MAcc(PackedDouble A, PackedDouble B, PackedDouble C) {
		// TODO Auto-generated method stub
	}

	@Override
	public void MAcc(PackedFloat A, PackedFloat B, PackedFloat C) {
		// TODO Auto-generated method stub
	}

	@Override
	public void Max(PackedByte A, PackedByte B, PackedByte C) {
		// TODO Auto-generated method stub
	}

	@Override
	public void Max(PackedInt A, PackedInt B, PackedInt C) {
		// TODO Auto-generated method stub
	}

	@Override
	public void Min(PackedByte A, PackedByte B, PackedByte C) {
		// TODO Auto-generated method stub
	}

	@Override
	public void Min(PackedInt A, PackedInt B, PackedInt C) {
		// TODO Auto-generated method stub
	}

	@Override
	public void Mult(PackedInt A, PackedInt B, PackedLong C) {
		// TODO Auto-generated method stub
	}

	@Override
	public void MultL(PackedInt A, PackedInt B, PackedInt C) {
		if((A.length!=B.length) && (A.length!=C.length))
		{
			throw(new IllegalArgumentException("Arrays must be the same length"));
		}
		int opcode = this.createOpCode(Operations.OperationList.MULTL.getCode(), A.type, B.type, C.type);
		ExecutionItem ei = new ExecutionItem(opcode,A.index,B.index,C.index);
		this.el.Add(ei);
	}

	@Override
	public void Round(PackedDouble A, int control, PackedDouble C) {
		// TODO Auto-generated method stub
	}

	@Override
	public void Round(PackedFloat A, int control, PackedFloat C) {
		// TODO Auto-generated method stub
	}

}
