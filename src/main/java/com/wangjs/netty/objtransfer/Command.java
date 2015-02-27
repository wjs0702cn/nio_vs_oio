package com.wangjs.netty.objtransfer;

import java.io.Serializable;

public class Command implements Serializable {

	private static final long serialVersionUID = 8955747162036990579L;
	private String actionName;

	public Command() {
	}

	public Command(String actionName) {
		super();
		this.actionName = actionName;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

}
