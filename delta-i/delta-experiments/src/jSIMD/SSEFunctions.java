package jSIMD;

public interface SSEFunctions {

	public void Add(PackedFloat A, PackedFloat B, PackedFloat C);

	public void And(PackedFloat A, PackedFloat B, PackedFloat C);

	public char Avg(PackedChar A);

	public void Divide(PackedFloat A, PackedFloat B, PackedFloat C);

	public void Equal(PackedFloat A, PackedFloat B, PackedFloat C);

	public void GreaterThan(PackedFloat A, PackedFloat B, PackedFloat C);

	public void GreaterThanOrEqual(PackedFloat A, PackedFloat B, PackedFloat C);

	public void LessThan(PackedFloat A, PackedFloat B, PackedFloat C);

	public void LessThanOrEqual(PackedFloat A, PackedFloat B, PackedFloat C);

	public void Max(PackedFloat A, PackedFloat B, PackedFloat C);

	public void Max(PackedShort A, PackedShort B, PackedShort C);

	public void Min(PackedFloat A, PackedFloat B, PackedFloat C);

	public void Min(PackedShort A, PackedShort B, PackedShort C);

	public void Mult(PackedFloat A, PackedFloat B, PackedFloat C);

	public void Nand(PackedFloat A, PackedFloat B, PackedFloat C);

	public void NotEqual(PackedFloat A, PackedFloat B, PackedFloat C);

	public void Or(PackedFloat A, PackedFloat B, PackedFloat C);

	public void Ordered(PackedFloat A, PackedFloat B, PackedFloat C);

	public void Recip(PackedFloat A, PackedFloat C);

	public void RecipSqrt(PackedFloat A, PackedFloat C);

	public void Shuf(PackedFloat A, PackedFloat B, int mask, PackedFloat C);

	public void Sqrt(PackedFloat A, PackedFloat C);

	public void Sub(PackedFloat A, PackedFloat B, PackedFloat C);

	public void Unordered(PackedFloat A, PackedFloat B, PackedFloat C);

	public void XOr(PackedFloat A, PackedFloat B, PackedFloat C);

}