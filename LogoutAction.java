package com.internousdev.spring.action;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

public class LogoutAction extends ActionSupport implements SessionAware {

	private Map<String,Object> session;

	public String execute() {
		String userId = String.valueOf(session.get("userId"));
        //Object型をString型に変換して、boolean型変換準備　falseの場合そもそもsessionに入っていないからnull
		String tempSavedUserIdFlg = String.valueOf(session.get("savedUserIdFlg"));
        //三項条件　null,false,trueの三つの結果の中で、nullならばfalse、　それ以外ならばtrue
		boolean savedUserIdFlg = "null".equals(tempSavedUserIdFlg)? false : Boolean.valueOf(tempSavedUserIdFlg);
		session.clear();

		if(savedUserIdFlg) {
			session.put("savedUserIdFlg", savedUserIdFlg);
			session.put("userId", userId);

	    }


		return SUCCESS;
	}



	public Map<String,Object> getSession(){
		return this.session;
	}

	public void setSession(Map<String,Object> session) {
		this.session = session;
	}

}
