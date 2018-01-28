/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.tryzub.adminka;

/**
 *
 * @author tszin
 */
public enum ScopeType {
	//Really not a scope but a marker
	ALL("all"),
	IMPLICIT("implicit"),
 
	REQUEST("request"),
	SESSION("session"),
	APPLICATION("application");
 
	String scope;
	ScopeType(String scope) {
		this.scope = scope;
	}
 
	public String toString() {
		return scope;
	}
 
 
	/**
	 * Get Enum from value
	 * @param name
	 * @return
	 */
	public static ScopeType fromValue(String name) {
		name=name!=null?name.toUpperCase():"";
		for(ScopeType v:values()){
			if(v.name().equals(name)){
				return v;
			}
		}
		//By default return all scoped objects
		return ScopeType.ALL;
	}
}
