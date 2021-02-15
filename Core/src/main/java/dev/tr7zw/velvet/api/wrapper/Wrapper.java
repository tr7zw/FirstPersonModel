package dev.tr7zw.velvet.api.wrapper;

public interface Wrapper {

	public WrappedScreen wrapScreen(Object screen);
	
	public WrappedText wrapText(Object text);
	
	public WrappedText getTranslateableText(String text);
	
	public WrappedKeybind createKeyBind(String name, int key, String namespace);
	
	public WrappedWorld wrapWorld(Object world);
	
	public WrappedEntity wrapEntity(Object entity);
	
	public WrappedEntityTrackerUpdate wrapEntityTrackerUpdatePacket(Object packet);
	
}
