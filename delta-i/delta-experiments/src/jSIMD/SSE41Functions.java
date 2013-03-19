package jSIMD;

public interface SSE41Functions {

	public void Blend(PackedByte A, PackedByte B, PackedByte Mask, PackedByte C);

	public void Blend(PackedDouble A, PackedDouble B, PackedDouble Mask, PackedDouble C);

	public void Blend(PackedFloat A, PackedFloat B, PackedFloat Mask, PackedFloat C);

	public void Blend(PackedInt A, PackedInt B, PackedInt Mask, PackedInt C);

	public void Ceil(PackedDouble A, PackedDouble C);

	public void Ceil(PackedFloat A, PackedFloat C);

	public void Equal(PackedLong A, PackedLong B, PackedLong C);

	public void Floor(PackedDouble A, PackedDouble C);

	public void Floor(PackedFloat A, PackedFloat C);

	public void GreaterThan(PackedLong A, PackedLong B, PackedLong C);

	public void MAcc(PackedDouble A, PackedDouble B, PackedDouble C);

	public void MAcc(PackedFloat A, PackedFloat B, PackedFloat C);

	public void Max(PackedByte A, PackedByte B, PackedByte C);

	public void Max(PackedInt A, PackedInt B, PackedInt C);

	public void Min(PackedByte A, PackedByte B, PackedByte C);

	public void Min(PackedInt A, PackedInt B, PackedInt C);

	/*
	 * (non-Javadoc)
	 * @see jSIMD.SSE2Functions#Mult(PackedInt, PackedInt)
	 */
	public void Mult(PackedInt A, PackedInt B, PackedLong C);

	public void MultL(PackedInt A, PackedInt B, PackedInt C);

	public void Round(PackedDouble A, int control, PackedDouble C);

	public void Round(PackedFloat A, int control, PackedFloat C);

}