package com.bb1.tub;

public final class LitteralText extends Text {
	
	public LitteralText() {
		this("");
	}
	
	public LitteralText(String string) {
		super(string);
	}
	
	@Override
	public LitteralText toLitteralText() {
		return this;
	}

	@Override
	public String toString() {
		return this.rawString;
	}

	@Override
	public LitteralText clone() {
		return new LitteralText(this.rawString);
	}
	
	@Override
	public boolean equals(Object obj) {
		return (obj instanceof Text) ? ((Text)obj).toLitteralText().toString().equals(this.toString()) : ((obj instanceof String) ? ((String)obj).equals(this.toString()) : false);
	}

}
