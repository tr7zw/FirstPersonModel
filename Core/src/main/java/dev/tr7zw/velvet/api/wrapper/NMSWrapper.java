package dev.tr7zw.velvet.api.wrapper;

public interface NMSWrapper {

	/**
	 * Get backing Object
	 * 
	 * @return
	 */
	public Object getHandler();
	
	/**
	 * Get backing Object
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public default <T> T getHandler(Class<T> targetClass) {
		return (T) getHandler();
	}
	
}
