package com.bb1.tub;

/**
 * A string that can be/is formatted
 */
public abstract class Text {
	
	protected String rawString;
	
	protected Text() {
		
	}
	
	protected Text(String rawString) {
		this.rawString = rawString;
	}
	
	public abstract LitteralText toLitteralText();
	
	@Override
	public abstract String toString();
	
	public abstract Text clone();
	
	@Override
	public abstract boolean equals(Object obj);
	
	/**
	 * Just a list of all format codes in minecraft
	 */
	public static enum FormatCode {
		FORMAT_CODE("§", false),
		// Format
		BOLD("l", false),
		STRIKE("m", false), // Strikethrough
		UNDERLINE("n", false),
		ITALIC("o", false),
		RESET("r", true), // Is classified as a colour
		RAND("k", false), // Obfuscated
		// Colour
		BLACK("0", true),
		DARK_BLUE("1", true),
		DARK_GREEN("2", true),
		DARK_AQUA("3", true),
		DARK_RED("4", true),
		DARK_PURPLE("5", true),
		GOLD("6", true),
		GRAY("7", true),
		DARK_GRAY("8", true),
		BLUE("9", true),
		GREEN("a", true),
		AQUA("b", true),
		RED("c", true),
		LIGHT_PURPLE("d", true),
		YELLOW("e", true),
		WHITE("f", true),
		;
		
		private final String code;
		private final boolean isColor;
		
		FormatCode(String code) {
			this(code, false);
		}
		
		FormatCode(String code, boolean isColor) {
			this.code = code;
			this.isColor = isColor;
		}
		
		@Override
		public String toString() {
			return (this.code.startsWith(FORMAT_CODE.getCode()) ? this.code : FORMAT_CODE+this.code);
		}
		
		public String getCode() {
			return this.code;
		}

		public boolean isColor() {
			return this.isColor;
		}
	}
	
}
