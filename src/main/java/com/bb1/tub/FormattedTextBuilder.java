package com.bb1.tub;

import java.util.ArrayList;
import java.util.List;

import com.bb1.tub.FormattedText.FormattedTextComponent;
import com.bb1.tub.Text.FormatCode;

public final class FormattedTextBuilder {
	
	private List<FormattedTextComponent> components = new ArrayList<FormattedTextComponent>();
	
	public FormattedTextBuilder() {
		
	}
	
	public FormattedTextBuilder(Text text) {
		add(text);
	}
	
	public FormattedTextBuilder(String text) {
		components.addAll(FormattedText.parseString(text).getComponents());
	}
	
	public FormattedTextBuilder add(FormattedTextComponent formattedTextComponent) {
		components.add(formattedTextComponent);
		return this;
	}
	
	public FormattedTextBuilder add(Text text) {
		if (text instanceof FormattedText) {
			components.addAll(((FormattedText)text).getComponents());
		} else {
			components.addAll(FormattedText.parseString(text.toLitteralText().toString()).getComponents());
		}
		return this;
	}
	
	public FormattedTextBuilder add(String string, FormatCode... formatCodes) {
		FormattedTextComponent formattedTextComponent = new FormattedTextComponent(string);
		for(FormatCode formatCode : formatCodes) {
			formattedTextComponent.setFormat(formatCode, true);
		}
		return add(formattedTextComponent);
	}
	
	public FormattedText build() {
		return new FormattedText(components);
	}
	
}
