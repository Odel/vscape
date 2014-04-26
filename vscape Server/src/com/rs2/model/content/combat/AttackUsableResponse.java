package com.rs2.model.content.combat;

public class AttackUsableResponse {

	public enum Type {
		FAIL, WAIT, SUCCESS
	}

	private AttackScript script;
	private Type type;

	public AttackUsableResponse(AttackScript script, Type type) {
		this.script = script;
		this.type = type;
	}

	public AttackScript getScript() {
		return script;
	}

	public Type getType() {
		return type;
	}
}
