package jSIMD;

public interface Interleave {

	public abstract void InterleaveLow(PackedByte A, PackedByte B, PackedByte C);

	public abstract void InterleaveLow(PackedShort A, PackedShort B,
			PackedShort C);

	public abstract void InterleaveLow(PackedInt A, PackedInt B, PackedInt C);

	public abstract void InterleaveLow(PackedLong A, PackedLong B, PackedLong C);

	public abstract void InterleaveHigh(PackedByte A, PackedByte B, PackedByte C);

	public abstract void InterleaveHigh(PackedShort A, PackedShort B,
			PackedShort C);

	public abstract void InterleaveHigh(PackedInt A, PackedInt B, PackedInt C);

	public abstract void InterleaveHigh(PackedLong A, PackedLong B, PackedLong C);
//
//	public abstract void Interleave(PackedShort A, PackedShort B, PackedInt C);
//
//	public abstract void Interleave(PackedInt A, PackedInt B, PackedLong C);
//
//	public abstract void Interleave(PackedByte A, PackedByte B, PackedShort C);

}