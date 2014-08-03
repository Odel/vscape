package com.rs2.cache.obj;

import com.rs2.cache.object.GameObjectData;

/**
 * An object definition listener, which is notified when object definitions have
 * been parsed.
 * 
 * @author Graham Edgecomb
 * 
 */
public interface ObjectDefinitionListener {

	/**
	 * Called when an object definition is parsed.
	 * 
	 * @param def
	 *            The definition that was parsed.
	 */
	public void objectDefinitionParsed(GameObjectData def);

}
