package com.bb1.tub.api.loader;

public class AddonLoadException extends RuntimeException {
	
	private static final long serialVersionUID = 5571685488691272993L;
	
	public AddonLoadException(String reason) {
		super(reason);
	}
	
	public AddonLoadException(Exception exception) {
		super(exception);
	}
	
	public AddonLoadException(String reason, Exception exception) {
		super(reason, exception);
	}
	
}
