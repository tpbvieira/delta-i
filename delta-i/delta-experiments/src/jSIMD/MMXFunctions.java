package jSIMD;

public interface MMXFunctions {

	public void Add(PackedByte A, PackedByte B, PackedByte C);

	public void Add(PackedInt A, PackedInt B, PackedInt C);

	public void Add(PackedShort A, PackedShort B, PackedShort C);

	public void AddS(PackedByte A, PackedByte B, PackedByte C);

	public void AddS(PackedShort A, PackedShort B, PackedShort C);

	public void And(PackedLong A, PackedLong B, PackedLong C);

	public void Equal(PackedByte A, PackedByte B, PackedByte C);

	public void Equal(PackedInt A, PackedInt B, PackedInt C);

	public void Equal(PackedShort A, PackedShort B, PackedShort C);

	public void GreaterThan(PackedByte A, PackedByte B, PackedByte C);

	public void GreaterThan(PackedInt A, PackedInt B, PackedInt C);

	public void GreaterThan(PackedShort A, PackedShort B, PackedShort C);

	public void MAcc(PackedShort A, PackedShort B, PackedInt C);

	public void MultH(PackedShort A, PackedShort B, PackedShort C);

	public void MultL(PackedShort A, PackedShort B, PackedShort C);

	public void Nand(PackedLong A, PackedLong B, PackedLong C);

	public void Or(PackedLong A, PackedLong B, PackedLong C);

	public void SLL(PackedInt A, int count, PackedInt C);

	public void SLL(PackedLong A, int count, PackedLong C);

	public void SLL(PackedShort A, int count, PackedShort C);

	public void SRA(PackedInt A, int count, PackedInt C);

	public void SRA(PackedLong A, int count, PackedLong C);

	public void SRL(PackedInt A, int count, PackedInt C);

	public void SRL(PackedLong A, int count, PackedLong C);

	public void SRL(PackedShort A, int count, PackedShort C);

	public void Sub(PackedByte A, PackedByte B, PackedByte C);

	public void Sub(PackedInt A, PackedInt B, PackedInt C);

	public void Sub(PackedShort A, PackedShort B, PackedShort C);

	public void SubS(PackedByte A, PackedByte B, PackedByte C);

	public void SubS(PackedChar A, PackedChar B, PackedChar C);

	public void SubS(PackedShort A, PackedShort B, PackedShort C);

	public void XOr(PackedLong A, PackedLong B, PackedLong C);

}