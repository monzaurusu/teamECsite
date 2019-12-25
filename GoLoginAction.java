package com.internousdev.spring.action;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

public class GoLoginAction extends ActionSupport implements SessionAware {

	private Map<String,Object> session;
	private String cartFlg = null;

	public String execute() {
		if(cartFlg != null) {
			session.put("cartFlg", cartFlg);
		}
		return SUCCESS;
	}

	public Map<String, Object> getSession(){
		return this.session;
	}
	public void setSession (Map<String, Object> session) {
		this.session = session;
	}

	public void setCartFlg(String cartFlg) {
		this.cartFlg = cartFlg;
	}

}
