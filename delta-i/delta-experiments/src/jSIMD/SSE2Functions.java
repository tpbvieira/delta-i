package jSIMD;

public interface SSE2Functions {

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


	/* (non-Javadoc)
	 * @see jSIMD.MMXFunctions#Add(PackedByte, PackedByte)
	 */
	public void Add(PackedByte A, PackedByte B, PackedByte C);
	/* (non-Javadoc)
	 * @see jSIMD.MMXFunctions#Add(PackedInt, PackedInt)
	 */
	public void Add(PackedInt A, PackedInt B, PackedInt C);

	/* (non-Javadoc)
	 * @see jSIMD.MMXFunctions#Add(PackedShort, PackedShort)
	 */
	public void Add(PackedShort A, PackedShort B, PackedShort C);

	/* (non-Javadoc)
	 * @see jSIMD.MMXFunctions#AddS(PackedByte, PackedByte)
	 */
	public void AddS(PackedByte A, PackedByte B, PackedByte C);

	/* (non-Javadoc)
	 * @see jSIMD.MMXFunctions#AddS(PackedShort, PackedShort)
	 */
	public void AddS(PackedShort A, PackedShort B, PackedShort C);
	public void And(PackedInt A, PackedInt B, PackedInt C);
	

	public void And(PackedDouble A, PackedDouble B, PackedDouble C);

	/* (non-Javadoc)
	 * @see jSIMD.MMXFunctions#Equal(PackedByte, PackedByte)
	 */
	public void Equal(PackedByte A, PackedByte B, PackedByte C);

	/* (non-Javadoc)
	 * @see jSIMD.MMXFunctions#Equal(PackedInt, PackedInt)
	 */
	public void Equal(PackedInt A, PackedInt B, PackedInt C);

	/* (non-Javadoc)
	 * @see jSIMD.MMXFunctions#Equal(PackedShort, PackedShort)
	 */
	public void Equal(PackedShort A, PackedShort B, PackedShort C);

	/* (non-Javadoc)
	 * @see jSIMD.MMXFunctions#GreaterThan(PackedByte, PackedByte)
	 */
	public void GreaterThan(PackedByte A, PackedByte B, PackedByte C);

	/* (non-Javadoc)
	 * @see jSIMD.MMXFunctions#GreaterThan(PackedInt, PackedInt)
	 */
	public void GreaterThan(PackedInt A, PackedInt B, PackedInt C);

	/* (non-Javadoc)
	 * @see jSIMD.MMXFunctions#GreaterThan(PackedShort, PackedShort)
	 */
	public void GreaterThan(PackedShort A, PackedShort B, PackedShort C);

	public void LessThan(PackedDouble A, PackedDouble B, PackedDouble C);

	/* (non-Javadoc)
	 * @see jSIMD.SSEFunctions#Max(PackedFloat, PackedFloat)
	 */
	public void Max(PackedFloat A, PackedFloat B, PackedFloat C);

	/* (non-Javadoc)
	 * @see jSIMD.SSEFunctions#Max(PackedShort, PackedShort)
	 */
	public void Max(PackedShort A, PackedShort B, PackedShort C);

	/* (non-Javadoc)
	 * @see jSIMD.SSEFunctions#Min(PackedShort, PackedShort)
	 */
	public void Min(PackedShort A, PackedShort B, PackedShort C);

	public void Mult(PackedDouble A, PackedDouble B, PackedDouble C);

	public void Mult(PackedInt A, PackedInt B, PackedLong C);

	/* (non-Javadoc)
	 * @see jSIMD.MMXFunctions#MultH(PackedShort, PackedShort)
	 */
	public void MultH(PackedShort A, PackedShort B, PackedShort C);

	/* (non-Javadoc)
	 * @see jSIMD.MMXFunctions#MultL(PackedShort, PackedShort)
	 */
	public void MultL(PackedShort A, PackedShort B, PackedShort C);

	public void Or(PackedDouble A, PackedDouble B, PackedDouble C);

	/* (non-Javadoc)
	 * @see jSIMD.MMXFunctions#SLL(PackedInt, int)
	 */
	public void SLL(PackedInt A, int count, PackedInt C);

	public void Sqrt(PackedDouble A, PackedDouble C);

	/* (non-Javadoc)
	 * @see jSIMD.MMXFunctions#SRA(PackedInt, int)
	 */
	public void SRA(PackedInt A, int count, PackedInt C);

	/* (non-Javadoc)
	 * @see jSIMD.MMXFunctions#Sub(PackedByte, PackedByte)
	 */
	public void Sub(PackedByte A, PackedByte B, PackedByte C);

	public void Sub(PackedDouble A, PackedDouble B, PackedDouble C);

	/* (non-Javadoc)
	 * @see jSIMD.MMXFunctions#Sub(PackedInt, PackedInt)
	 */
	public void Sub(PackedInt A, PackedInt B, PackedInt C);

	/* (non-Javadoc)
	 * @see jSIMD.MMXFunctions#Sub(PackedShort, PackedShort)
	 */
	public void Sub(PackedShort A, PackedShort B, PackedShort C);

	/* (non-Javadoc)
	 * @see jSIMD.MMXFunctions#SubS(PackedByte, PackedByte)
	 */
	public void SubS(PackedByte A, PackedByte B, PackedByte C);

	/* (non-Javadoc)
	 * @see jSIMD.MMXFunctions#SubS(PackedChar, PackedChar)
	 */
	public void SubS(PackedChar A, PackedChar B, PackedChar C);

	/* (non-Javadoc)
	 * @see jSIMD.MMXFunctions#SubS(PackedShort, PackedShort)
	 */
	public void SubS(PackedShort A, PackedShort B, PackedShort C);

	public void XOr(PackedDouble A, PackedDouble B, PackedDouble C);
	public void And(PackedInt A, int arg, PackedInt C);
	public void Or(PackedInt A, int arg, PackedInt C);
	public void Or(PackedInt A, PackedInt B, PackedInt C);
	
}
