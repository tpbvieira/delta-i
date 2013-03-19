package jSIMD;

public class Operations{

	public enum OperationList {
		ADD(0), 
		ADDS(1), 
		ADDSUB(2), 
		AND(3),
		AVG(4),
		BLEND(5),
		CEIL(6),
		EQUAL(7),
		FLOOR(8),
		GREATERTHAN(9),
		GREATERTHANOREQUAL(10),
		HORIZADD(11),
		LESSTHATOREQUAL(12),
		MACC(13),
		MAX(14),
		MIN(15),
		MULT(16),
		MULTH(17),
		MULTL(18),
		NAND(19),
		NOTEQUAL(20),
		OR(21),
		ORDERED(22),
		ROUND(23),
		SHUF(24),
		SHUFL(25),
		SHUFH(26),
		SLL(27),
		SQRT(28),
		SRA(29),
		SRL(30),
		SUB(31),
		SUBS(32),
		SUMABSDIFF(33),
		UNORDERED(34),
		XOR(35),
		DIV(36),
		RECIP(37),
		RECIPSQRT(38),
		LESSTHAN(39),
		HORIZSUB(40),
		POPCOUNT(41),
		INTERLEAVELOW(42),
		INTERLEAVEHIGH(43);

		private int code;

		private OperationList(int c) {
			code = c;
		}

		public int getCode() {
			return code;
		}

	}

}