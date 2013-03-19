package jSIMD;

public interface SSE3Functions {

	public void AddSub(PackedDouble A, PackedDouble B, PackedDouble C);

	public void AddSub(PackedFloat A, PackedFloat B, PackedFloat C);

	public void HAdd(PackedDouble A, PackedDouble B, PackedDouble C);

	public void HAdd(PackedFloat A, PackedFloat B, PackedFloat C);

	public void HSub(PackedDouble A, PackedDouble B, PackedDouble C);

	public void HSub(PackedFloat A, PackedFloat B, PackedFloat C);

}