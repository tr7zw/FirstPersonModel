package de.tr7zw.firstperson.features;

public abstract class AbstractCosmetic {

	public abstract boolean shouldRender(Object player, Object matrixStack);
	public abstract ModelCreator getModel(FeatureRenderer featureRenderer);
	public abstract Object getRenderLayer(FeatureRenderer featureRenderer, Object player);
	public abstract AttatchedTo getAttachedTo();
	
	
	public abstract static class ModelCreator{
		public abstract ModelCreator addCuboid(float x, float y, float z, float sizeX, float sizeY, float sizeZ, float extra, boolean mirror);
		public ModelCreator addCuboid(float x, float y, float z, float sizeX, float sizeY, float sizeZ, float extra) {
			return addCuboid(x, y, z, sizeX, sizeY, sizeZ, extra, false);
		}
		public abstract ModelCreator setTextureOffset(int u, int v);
		public abstract Object getModel();
	}
	
	public static enum AttatchedTo{
		HEAD
	}
	
}
