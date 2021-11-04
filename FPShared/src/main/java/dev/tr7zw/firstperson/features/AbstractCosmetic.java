package dev.tr7zw.firstperson.features;

public abstract class AbstractCosmetic {

	public abstract void init(FeatureRenderer featureRenderer);
	public void initTextures() {
		
	}
	public abstract boolean shouldRender(Object player, Object matrixStack);
	public abstract ModelCreator getModel();
	public abstract Object getRenderLayer(FeatureRenderer featureRenderer, Object player);
	public abstract AttatchedTo getAttachedTo();
	public void updateModel(Object player) {
		
	}
	
	
	public abstract static class ModelCreator{
		public abstract ModelCreator addCuboid(float x, float y, float z, float sizeX, float sizeY, float sizeZ, float extra, boolean mirror);
		public ModelCreator addCuboid(float x, float y, float z, float sizeX, float sizeY, float sizeZ, float extra) {
			return addCuboid(x, y, z, sizeX, sizeY, sizeZ, extra, false);
		}
		public ModelCreator addCuboid(float x, float y, float z, float sizeX, float sizeY, float sizeZ) {
			return addCuboid(x, y, z, sizeX, sizeY, sizeZ, 0f, false);
		}
		public abstract ModelCreator setTextureOffset(int u, int v);
		public abstract ModelCreator setPivot(float x, float y, float z);
		public abstract void addChild(ModelCreator child);
		public abstract void setRotationAngle(float x, float y, float z);
		public abstract Object getModel();
	}
	
	public static enum AttatchedTo{
		HEAD, BODY
	}
	
}
