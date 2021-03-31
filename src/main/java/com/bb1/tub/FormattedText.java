package com.bb1.tub;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.bb1.tub.api.entites.Entity;
import com.bb1.tub.api.items.ItemStack;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public final class FormattedText extends Text {
	
	private List<FormattedTextComponent> components;
	
	public static FormattedText parseString(String str) {
        List<FormattedTextComponent> comps = new ArrayList<FormattedTextComponent>();
		FormattedTextComponent component = new FormattedTextComponent("");
		List<FormatCode> formatCodes = new ArrayList<FormatCode>();
		for (int currentChar = 0; currentChar < str.length(); currentChar++) {
			char c = str.charAt(currentChar);
			if (FormatCode.FORMAT_CODE.getCode().equalsIgnoreCase(""+c)) {// Format
			    currentChar++; // Tick up by one to avoid format issue
				for (FormatCode f : FormatCode.values()) {
				    if (f.toString().equalsIgnoreCase(""+c+str.charAt(currentChar))) {
				        // Check for new component
				        if (f.equals(FormatCode.RESET)) {
				           for (FormatCode f2 : formatCodes) {
				               component.setFormat(f2, true);
				           }
				           comps.add(component);
				           // Clear all data
				           formatCodes.clear();
				           component = new FormattedTextComponent("");
				           component.setColor(f);
				        } else if (f.isColor()) {
				            for (FormatCode f2 : formatCodes) {
				               component.setFormat(f2, true);
				            }
				            comps.add(component);
				            component = new FormattedTextComponent("");
				            component.setColor(f);
				        } else {
				            if (!formatCodes.contains(f)) {
				                for (FormatCode f2 : formatCodes) {
				                    component.setFormat(f2, true);
				                }
				                comps.add(component);
				                component = new FormattedTextComponent("");
				                formatCodes.add(f);
				            }
				        }
				        break;
				    }
				}
			} else { // Normal text
				component.setText(component.getText()+c);
			}
		}
		for (FormatCode f2 : formatCodes) {
		    component.setFormat(f2, true);
		}
		comps.add(component);
		return new FormattedText(comps);
    }
	
	public FormattedText(FormattedTextComponent component, FormattedTextComponent... components) {
		List<FormattedTextComponent> list = new ArrayList<FormattedTextComponent>();
		list.add(component);
		if (components!=null && components.length>0) {
			list.addAll(Arrays.asList(components));
		}
		this.components = list;
	}
	
	public FormattedText(List<FormattedTextComponent> components) {
		this.components = components;
	}
	
	public FormattedText(Text text) {
		if (text instanceof FormattedText) {
			this.components = ((FormattedText)text).getComponents();
		} else {
			this.components = parseString(text.toLitteralText().toString()).getComponents();
		}
	}
	
	public List<FormattedTextComponent> getComponents() {
	    return this.components;
	}
	
	public JsonElement toJson() {
		return toJson(true);
	}
	
	public JsonElement toJson(boolean includeEvents) {
		JsonArray jsonArray = new JsonArray();
		for (FormattedTextComponent formattedTextComponent : components) {
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("text", formattedTextComponent.getText());
			jsonObject.addProperty("bold", formattedTextComponent.isBold());
			jsonObject.addProperty("italic", formattedTextComponent.isItalic());
			jsonObject.addProperty("underlined",formattedTextComponent.isUnderlined());
			jsonObject.addProperty("strikethrough", formattedTextComponent.isStrikethrough());
			jsonObject.addProperty("obfuscated", formattedTextComponent.isObfuscated());
			jsonObject.addProperty("color", (formattedTextComponent.getColor()==null ? FormatCode.RESET : formattedTextComponent.getColor()).name().toLowerCase());
			if (includeEvents) {
				if (formattedTextComponent.getHover()!=null) {
					jsonObject.add("hoverEvent", formattedTextComponent.getHover());
				}
				if (formattedTextComponent.getClick()!=null) {
					jsonObject.add("clickEvent", formattedTextComponent.getClick());
				}
			}
			jsonArray.add(jsonObject);
		}
		return (jsonArray.size()==0) ? new JsonPrimitive("") : (jsonArray.size()>1) ? jsonArray : jsonArray.get(0);
	}
	
	@Override
	public boolean equals(Object obj) {
		return toLitteralText().equals(obj);
	}
	
	@Override
	public String toString() { // Not perfect but eh will be worked on :) TODO make better
		String str = new String();
		for (FormattedTextComponent c : components) {
			if (c.isBold()) str = str + FormatCode.BOLD;
			if (c.isItalic()) str = str + FormatCode.ITALIC;
			if (c.isUnderlined()) str = str + FormatCode.UNDERLINE;
			if (c.isStrikethrough()) str = str + FormatCode.STRIKE;
			if (c.isObfuscated()) str = str + FormatCode.RAND;
			str = str + (c.getColor()!=null ? c.getColor() : "") + c.getText();
		}
		return str;
	}
	
	@Override
	public FormattedText clone() {
		List<FormattedTextComponent> l = new ArrayList<FormattedTextComponent>();
		this.components.forEach((c) -> l.add(c.clone()));
		return new FormattedText(l);
	}
	
	@Override
	public LitteralText toLitteralText() {
		return new LitteralText(toString());
	}
	
	public static final class FormattedTextComponent {
		
		private String text;
		private FormatCode color;
		private boolean bold = false;
		private boolean italic = false;
		private boolean underlined = false;
		private boolean strikethrough = false;
		private boolean obfuscated = false;
		
		// Hover
		private JsonElement hoverContents;
		private String hoverAction;
		
		// Click
		private String clickContents;
		private String clickAction;
		
		protected FormattedTextComponent(String text, FormatCode color, boolean bold, boolean italic, boolean underlined, boolean strikethrough, boolean obfuscated, JsonObject hover, JsonObject click) {
			this.text = text;
			this.color = color;
			this.bold = bold;
			this.italic = italic;
			this.underlined = underlined;
			this.strikethrough = strikethrough;
			this.obfuscated = obfuscated;
			if (hover!=null) {
				this.hoverAction = hover.get("action").getAsString();
				if (Tub.getVersion()>16D) { // 1.16+
					this.hoverContents = hover.get("contents").getAsJsonObject();
				} else {
					this.hoverContents = hover.get("value").getAsJsonObject();
				}
			}
			if (click!=null) {
				this.clickAction = hover.get("action").getAsString();
				this.clickContents = hover.get("value").getAsString();
			}
		}
		
		public FormattedTextComponent(String text) {
			String string = new String();
			List<FormatCode> formatCodes = new ArrayList<FormatCode>();
			for (int currentChar = 0; currentChar < text.length(); currentChar++) {
				char c = text.charAt(currentChar);
				if (FormatCode.FORMAT_CODE.getCode().equals(new String()+c)) {
					String str = (FormatCode.FORMAT_CODE.getCode()+text.charAt(currentChar+1));
					for (FormatCode formatCode : FormatCode.values()) {
						if (formatCode.toString().equalsIgnoreCase(str)) {
							formatCodes.add(formatCode);
							currentChar++;
							break;
						}
					}
				} else {
					string = string + c;
				}
			}
			this.setText(string);
			formatCodes.forEach((f) -> setFormat(f, true));
		}
		
		public void setFormat(FormatCode formatCode, boolean value) {
			switch (formatCode) {
				case BOLD:
					this.bold = value;
					break;
				case ITALIC:
					this.italic = value;
					break;
				case UNDERLINE:
					this.underlined = value;
					break;
				case STRIKE:
					this.strikethrough = value;
					break;
				case RAND:
					this.obfuscated = value;
					break;
				default:
					if (formatCode.isColor()) {
						setColor(formatCode);
						return;
					}
					break;
			}
		}
		
		public void setColor(FormatCode formatCode) {
			if (!formatCode.isColor()) return;
			this.color = formatCode;
		}
		
		public FormatCode getColor() {
			return this.color;
		}

		public boolean isBold() {
			return bold;
		}

		public boolean isItalic() {
			return italic;
		}

		public boolean isUnderlined() {
			return underlined;
		}

		public boolean isStrikethrough() {
			return strikethrough;
		}

		public boolean isObfuscated() {
			return obfuscated;
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}
		
		public void removeHover() {
			this.hoverAction = null;
			this.hoverContents = null;
		}
		
		public void setHoverText(String string) {
			this.hoverAction = "show_text";
			this.hoverContents = FormattedText.parseString(string).toJson(false);
		}
		
		public void setHoverText(Text text) {
			this.hoverAction = "show_text";
			this.hoverContents = ((text instanceof FormattedText) ? ((FormattedText)text) : FormattedText.parseString(text.toLitteralText().toString())).toJson(false);
		}
		
		public void setHoverText(FormattedText formattedText) {
			this.hoverAction = "show_text";
			this.hoverContents = formattedText.toJson(false);
		}
		
		public void setHoverItem(ItemStack itemStack) {
			this.hoverAction = "show_item";
			this.hoverContents = item(itemStack);
		}
		
		public void setHoverEntity(Entity entity) {
			this.hoverAction = "show_entity";
			this.hoverContents = entity(entity);
		}
		
		private static JsonObject item(ItemStack itemStack) {
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("id", itemStack.getIdentifier().toString());
			jsonObject.addProperty("count", itemStack.getAmount());
			jsonObject.add("tag", itemStack.getNBT().getJsonObject());
			return jsonObject;
		}
		
		private static JsonObject entity(Entity entity) {
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("id", entity.getUUID().toString());
			jsonObject.addProperty("type", entity.getIdentifier().toString());
			jsonObject.addProperty("name", entity.getName().toLitteralText().toString());
			return jsonObject;
		}
		
		public JsonObject getHover() {
			if (hoverAction==null) return null;
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("action", hoverAction);
			if (Tub.getVersion()>16D) { // 1.16+
				jsonObject.add("contents", hoverContents);
			} else {
				jsonObject.add("value", hoverContents);
			}
			return jsonObject;
		}
		
		public void removeClick() {
			this.clickAction = null;
			this.clickContents = null;
		}
		
		public void setClickURL(String url) {
			this.clickAction = "open_url";
			this.clickContents = url;
		}
		
		public void setClickFile(String filePath) {
			this.clickAction = "open_file";
			this.clickContents = filePath;
		}
		
		public void setClickCommand(String command) {
			this.clickAction = "run_command";
			this.clickContents = command;
		}
		
		public void setClickSuggestCommand(String command) {
			this.clickAction = "suggest_command";
			this.clickContents = command;
		}
		
		public void setClickBookPage(int page) {
			this.clickAction = "change_page";
			this.clickContents = Integer.toString(page);
		}
		
		public void setClickClipboard(String text) {
			this.clickAction = "copy_to_clipboard";
			this.clickContents = text;
		}
		
		public JsonObject getClick() {
			if (clickAction==null) return null;
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("action", clickAction);
			jsonObject.addProperty("value", clickContents);
			return jsonObject;
		}
		
		@Override
		public FormattedTextComponent clone() {
			return new FormattedTextComponent(this.text, this.color, this.bold, this.italic, this.underlined, this.strikethrough, this.obfuscated, getHover(), getClick());
		}
		
	}
	
}
