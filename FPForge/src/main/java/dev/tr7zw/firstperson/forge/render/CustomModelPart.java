package dev.tr7zw.firstperson.forge.render;

import java.util.Random;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.math.vector.Vector4f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class CustomModelPart extends ModelRenderer{
	   private float textureWidth = 64.0F;
	   private float textureHeight = 32.0F;
	   private int textureOffsetX;
	   private int textureOffsetY;
	   public float rotationPointX;
	   public float rotationPointY;
	   public float rotationPointZ;
	   public float rotateAngleX;
	   public float rotateAngleY;
	   public float rotateAngleZ;
	   public boolean mirror;
	   public boolean showModel = true;
	   private final ObjectList<ModelBox> cubeList = new ObjectArrayList<>();
	   private final ObjectList<ModelRenderer> childModels = new ObjectArrayList<>();

	   public CustomModelPart(Model model) {
		   super(model);
	      model.accept(this);
	      this.setTextureSize(model.textureWidth, model.textureHeight);
	   }

	   public CustomModelPart(Model model, int texOffX, int texOffY) {
	      this(model.textureWidth, model.textureHeight, texOffX, texOffY);
	      model.accept(this);
	   }

	   public CustomModelPart(int textureWidthIn, int textureHeightIn, int textureOffsetXIn, int textureOffsetYIn) {
		   super(textureWidthIn, textureHeightIn, textureOffsetXIn, textureOffsetYIn);
	      this.setTextureSize(textureWidthIn, textureHeightIn);
	      this.setTextureOffset(textureOffsetXIn, textureOffsetYIn);
	   }

	   @Override
	   public void copyModelAngles(ModelRenderer modelRendererIn) {
		   super.copyModelAngles(modelRendererIn);
	      this.rotateAngleX = modelRendererIn.rotateAngleX;
	      this.rotateAngleY = modelRendererIn.rotateAngleY;
	      this.rotateAngleZ = modelRendererIn.rotateAngleZ;
	      this.rotationPointX = modelRendererIn.rotationPointX;
	      this.rotationPointY = modelRendererIn.rotationPointY;
	      this.rotationPointZ = modelRendererIn.rotationPointZ;
	   }

	   /**
	    * Sets the current box's rotation points and rotation angles to another box.
	    */
	   public void addChild(CustomModelPart renderer) {
	      this.childModels.add(renderer);
	   }

	   @Override
	   public ModelRenderer setTextureOffset(int x, int y) {
		   super.setTextureOffset(x, y);
	      this.textureOffsetX = x;
	      this.textureOffsetY = y;
	      return this;
	   }

	   public ModelRenderer addCustomBox(String partName, float x, float y, float z, int width, int height, int depth, float delta, int texX, int texY) {
	      this.setTextureOffset(texX, texY);
	      this.addCustomBox(this.textureOffsetX, this.textureOffsetY, x, y, z, (float)width, (float)height, (float)depth, delta, delta, delta, this.mirror, false);
	      return this;
	   }

	   public ModelRenderer addCustomBox(float x, float y, float z, float width, float height, float depth) {
	      this.addCustomBox(this.textureOffsetX, this.textureOffsetY, x, y, z, width, height, depth, 0.0F, 0.0F, 0.0F, this.mirror, false);
	      return this;
	   }

	   public ModelRenderer addCustomBox(float x, float y, float z, float width, float height, float depth, boolean mirrorIn) {
	      this.addCustomBox(this.textureOffsetX, this.textureOffsetY, x, y, z, width, height, depth, 0.0F, 0.0F, 0.0F, mirrorIn, false);
	      return this;
	   }

	   public void addCustomBox(float x, float y, float z, float width, float height, float depth, float delta) {
	      this.addCustomBox(this.textureOffsetX, this.textureOffsetY, x, y, z, width, height, depth, delta, delta, delta, this.mirror, false);
	   }

	   public void addCustomBox(float x, float y, float z, float width, float height, float depth, float deltaX, float deltaY, float deltaZ) {
	      this.addCustomBox(this.textureOffsetX, this.textureOffsetY, x, y, z, width, height, depth, deltaX, deltaY, deltaZ, this.mirror, false);
	   }

	   public void addCustomBox(float x, float y, float z, float width, float height, float depth, float delta, boolean mirrorIn) {
	      this.addCustomBox(this.textureOffsetX, this.textureOffsetY, x, y, z, width, height, depth, delta, delta, delta, mirrorIn, false);
	   }

	   private void addCustomBox(int texOffX, int texOffY, float x, float y, float z, float width, float height, float depth, float deltaX, float deltaY, float deltaZ, boolean mirorIn, boolean p_228305_13_) {
	      this.cubeList.add(new ModelBox(texOffX, texOffY, x, y, z, width, height, depth, deltaX, deltaY, deltaZ, mirorIn, this.textureWidth, this.textureHeight));
	   }

	   @Override
	   public void setRotationPoint(float rotationPointXIn, float rotationPointYIn, float rotationPointZIn) {
	      this.rotationPointX = rotationPointXIn;
	      this.rotationPointY = rotationPointYIn;
	      this.rotationPointZ = rotationPointZIn;
	   }

	   @Override
	   public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn) {
	      this.customRender(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
	   }
	   
	   public void customRender(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn) {
		      this.customRender(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
		   }

	   @Override
	   public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		   this.customRender(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
	   }
	   
	   public void customRender(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
	      if (this.showModel) {
	         if (!this.cubeList.isEmpty() || !this.childModels.isEmpty()) {
	            matrixStackIn.push();
	            this.translateRotate(matrixStackIn);
	            this.customRenderCuboids(matrixStackIn.getLast(), bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);

	            for(ModelRenderer modelrenderer : this.childModels) {
	               modelrenderer.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
	            }

	            matrixStackIn.pop();
	         }
	      }
	   }

	   public void translateRotate(MatrixStack matrixStackIn) {
	      matrixStackIn.translate((double)(this.rotationPointX / 16.0F), (double)(this.rotationPointY / 16.0F), (double)(this.rotationPointZ / 16.0F));
	      if (this.rotateAngleZ != 0.0F) {
	         matrixStackIn.rotate(Vector3f.ZP.rotation(this.rotateAngleZ));
	      }

	      if (this.rotateAngleY != 0.0F) {
	         matrixStackIn.rotate(Vector3f.YP.rotation(this.rotateAngleY));
	      }

	      if (this.rotateAngleX != 0.0F) {
	         matrixStackIn.rotate(Vector3f.XP.rotation(this.rotateAngleX));
	      }

	   }
	   
	   private void customRenderCuboids(MatrixStack.Entry matrixEntryIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
	      Matrix4f matrix4f = matrixEntryIn.getMatrix();
	      Matrix3f matrix3f = matrixEntryIn.getNormal();

	      for(ModelBox modelrenderer$modelbox : this.cubeList) {
	         for(TexturedQuad modelrenderer$texturedquad : modelrenderer$modelbox.quads) {
	            Vector3f vector3f = modelrenderer$texturedquad.normal.copy();
	            vector3f.transform(matrix3f);
	            float f = vector3f.getX();
	            float f1 = vector3f.getY();
	            float f2 = vector3f.getZ();

	            for(int i = 0; i < 4; ++i) {
	               PositionTextureVertex modelrenderer$positiontexturevertex = modelrenderer$texturedquad.vertexPositions[i];
	               float f3 = modelrenderer$positiontexturevertex.position.getX() / 16.0F;
	               float f4 = modelrenderer$positiontexturevertex.position.getY() / 16.0F;
	               float f5 = modelrenderer$positiontexturevertex.position.getZ() / 16.0F;
	               Vector4f vector4f = new Vector4f(f3, f4, f5, 1.0F);
	               vector4f.transform(matrix4f);
	               bufferIn.addVertex(vector4f.getX(), vector4f.getY(), vector4f.getZ(), red, green, blue, alpha, modelrenderer$positiontexturevertex.textureU, modelrenderer$positiontexturevertex.textureV, packedOverlayIn, packedLightIn, f, f1, f2);
	            }
	         }
	      }

	   }

	   /**
	    * Returns the model renderer with the new texture parameters.
	    */
	   public ModelRenderer setTextureSize(int textureWidthIn, int textureHeightIn) {
	      this.textureWidth = (float)textureWidthIn;
	      this.textureHeight = (float)textureHeightIn;
	      return this;
	   }

	   public ModelRenderer.ModelBox getRandomCube(Random randomIn) {
	      return new ModelRenderer.ModelBox(0,0,0,0,0,0,0,0,0,0,0,false,0,0);
	   }

	   @OnlyIn(Dist.CLIENT)
	   public static class ModelBox {
	      private final TexturedQuad[] quads;
	      public final float posX1;
	      public final float posY1;
	      public final float posZ1;
	      public final float posX2;
	      public final float posY2;
	      public final float posZ2;

	      public ModelBox(int texOffX, int texOffY, float x, float y, float z, float width, float height, float depth, float deltaX, float deltaY, float deltaZ, boolean mirorIn, float texWidth, float texHeight) {
	         this.posX1 = x;
	         this.posY1 = y;
	         this.posZ1 = z;
	         this.posX2 = x + width;
	         this.posY2 = y + height;
	         this.posZ2 = z + depth;
	         this.quads = new TexturedQuad[6];
	         float f = x + width;
	         float f1 = y + height;
	         float f2 = z + depth;
	         x = x - deltaX;
	         y = y - deltaY;
	         z = z - deltaZ;
	         f = f + deltaX;
	         f1 = f1 + deltaY;
	         f2 = f2 + deltaZ;
	         if (mirorIn) {
	            float f3 = f;
	            f = x;
	            x = f3;
	         }

	        PositionTextureVertex modelrenderer$positiontexturevertex7 = new PositionTextureVertex(x, y, z, 0.0F, 0.0F);
	         PositionTextureVertex modelrenderer$positiontexturevertex = new PositionTextureVertex(f, y, z, 0.0F, 8.0F);
	         PositionTextureVertex modelrenderer$positiontexturevertex1 = new PositionTextureVertex(f, f1, z, 8.0F, 8.0F);
	         PositionTextureVertex modelrenderer$positiontexturevertex2 = new PositionTextureVertex(x, f1, z, 8.0F, 0.0F);
	         PositionTextureVertex modelrenderer$positiontexturevertex3 = new PositionTextureVertex(x, y, f2, 0.0F, 0.0F);
	         PositionTextureVertex modelrenderer$positiontexturevertex4 = new PositionTextureVertex(f, y, f2, 0.0F, 8.0F);
	         PositionTextureVertex modelrenderer$positiontexturevertex5 = new PositionTextureVertex(f, f1, f2, 8.0F, 8.0F);
	         PositionTextureVertex modelrenderer$positiontexturevertex6 = new PositionTextureVertex(x, f1, f2, 8.0F, 0.0F);
	         float f4 = (float)texOffX;
	         float f5 = (float)texOffX + depth;
	         float f6 = (float)texOffX + depth + width;
	         float f7 = (float)texOffX + depth + width + width;
	         float f8 = (float)texOffX + depth + width + depth;
	         float f9 = (float)texOffX + depth + width + depth + width;
	         float f10 = (float)texOffY;
	         float f11 = (float)texOffY + depth;
	         float f12 = (float)texOffY + depth + height;
	         float f13 = (float)texOffY + depth + height + depth;
	         this.quads[2] = new TexturedQuad(new PositionTextureVertex[]{modelrenderer$positiontexturevertex4, modelrenderer$positiontexturevertex3, modelrenderer$positiontexturevertex7, modelrenderer$positiontexturevertex}, f5, f10, f6, f11, texWidth, texHeight, mirorIn, Direction.DOWN);
	         //f6, f11, f7, f10
	         this.quads[3] = new TexturedQuad(new PositionTextureVertex[]{modelrenderer$positiontexturevertex1, modelrenderer$positiontexturevertex2, modelrenderer$positiontexturevertex6, modelrenderer$positiontexturevertex5}, f5, f12, f6, f13, texWidth, texHeight, mirorIn, Direction.UP);
	         this.quads[1] = new TexturedQuad(new PositionTextureVertex[]{modelrenderer$positiontexturevertex7, modelrenderer$positiontexturevertex3, modelrenderer$positiontexturevertex6, modelrenderer$positiontexturevertex2}, f4, f11, f5, f12, texWidth, texHeight, mirorIn, Direction.WEST);
	         this.quads[4] = new TexturedQuad(new PositionTextureVertex[]{modelrenderer$positiontexturevertex, modelrenderer$positiontexturevertex7, modelrenderer$positiontexturevertex2, modelrenderer$positiontexturevertex1}, f5, f11, f6, f12, texWidth, texHeight, mirorIn, Direction.NORTH);
	         this.quads[0] = new TexturedQuad(new PositionTextureVertex[]{modelrenderer$positiontexturevertex4, modelrenderer$positiontexturevertex, modelrenderer$positiontexturevertex1, modelrenderer$positiontexturevertex5}, f6, f11, f8, f12, texWidth, texHeight, mirorIn, Direction.EAST);
	         this.quads[5] = new TexturedQuad(new PositionTextureVertex[]{modelrenderer$positiontexturevertex3, modelrenderer$positiontexturevertex4, modelrenderer$positiontexturevertex5, modelrenderer$positiontexturevertex6}, f8, f11, f9, f12, texWidth, texHeight, mirorIn, Direction.SOUTH);
	      }
	   }

	   @OnlyIn(Dist.CLIENT)
	   static class PositionTextureVertex {
	      public final Vector3f position;
	      public final float textureU;
	      public final float textureV;

	      public PositionTextureVertex(float x, float y, float z, float texU, float texV) {
	         this(new Vector3f(x, y, z), texU, texV);
	      }

	      public PositionTextureVertex setTextureUV(float texU, float texV) {
	         return new PositionTextureVertex(this.position, texU, texV);
	      }

	      public PositionTextureVertex(Vector3f posIn, float texU, float texV) {
	         this.position = posIn;
	         this.textureU = texU;
	         this.textureV = texV;
	      }
	   }

	   @OnlyIn(Dist.CLIENT)
	   static class TexturedQuad {
	      public final PositionTextureVertex[] vertexPositions;
	      public final Vector3f normal;

	      public TexturedQuad(PositionTextureVertex[] positionsIn, float u1, float v1, float u2, float v2, float texWidth, float texHeight, boolean mirrorIn, Direction directionIn) {
	         this.vertexPositions = positionsIn;
	         float f = 0.0F / texWidth;
	         float f1 = 0.0F / texHeight;
	         positionsIn[0] = positionsIn[0].setTextureUV(u2 / texWidth - f, v1 / texHeight + f1);
	         positionsIn[1] = positionsIn[1].setTextureUV(u1 / texWidth + f, v1 / texHeight + f1);
	         positionsIn[2] = positionsIn[2].setTextureUV(u1 / texWidth + f, v2 / texHeight - f1);
	         positionsIn[3] = positionsIn[3].setTextureUV(u2 / texWidth - f, v2 / texHeight - f1);
	         if (mirrorIn) {
	            int i = positionsIn.length;

	            for(int j = 0; j < i / 2; ++j) {
	               PositionTextureVertex modelrenderer$positiontexturevertex = positionsIn[j];
	               positionsIn[j] = positionsIn[i - 1 - j];
	               positionsIn[i - 1 - j] = modelrenderer$positiontexturevertex;
	            }
	         }

	         this.normal = directionIn.toVector3f();
	         if (mirrorIn) {
	            this.normal.mul(-1.0F, 1.0F, 1.0F);
	         }

	      }
	   }
}
