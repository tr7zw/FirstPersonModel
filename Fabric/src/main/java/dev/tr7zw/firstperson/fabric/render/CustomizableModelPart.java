package dev.tr7zw.firstperson.fabric.render;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.math.Vector4f;

public class CustomizableModelPart extends ModelPart {
	private float textureWidth = 64.0F;
	private float textureHeight = 32.0F;

	private int textureOffsetU;

	private int textureOffsetV;

	public float pivotX;
	public float pivotY;
	public float pivotZ;
	public float pitch;
	public float yaw;
	public float roll;
	public boolean mirror;
	public boolean visible = true;

	private final ObjectList<CustomizableModelPart.Cuboid> cuboids = new ObjectArrayList<dev.tr7zw.firstperson.fabric.render.CustomizableModelPart.Cuboid>();
	private final ObjectList<CustomizableModelPart> children = new ObjectArrayList<CustomizableModelPart>();

	public CustomizableModelPart(Model model) {
		super(null, null);
	}

	public CustomizableModelPart(int textureWidth, int textureHeight, int textureOffsetU, int textureOffsetV) {
	    super(null, null);
		setTextureSize(textureWidth, textureHeight);
		setTextureOffset(textureOffsetU, textureOffsetV);
	}

	public void customCopyPositionAndRotation(ModelPart modelPart) {
		super.copyTransform(modelPart);
		this.pitch = modelPart.pitch;
		this.yaw = modelPart.yaw;
		this.roll = modelPart.roll;
		this.pivotX = modelPart.pivotX;
		this.pivotY = modelPart.pivotY;
		this.pivotZ = modelPart.pivotZ;
	}

	public void addChild(CustomizableModelPart part) {
		this.children.add(part);
	}

	public CustomizableModelPart setTextureOffset(int textureOffsetU, int textureOffsetV) {
		this.textureOffsetU = textureOffsetU;
		this.textureOffsetV = textureOffsetV;
		return this;
	}

	public CustomizableModelPart addCustomCuboid(String name, float x, float y, float z, int sizeX, int sizeY, int sizeZ,
			float extra, int textureOffsetU, int textureOffsetV) {
		setTextureOffset(textureOffsetU, textureOffsetV);
		addCustomCuboid(this.textureOffsetU, this.textureOffsetV, x, y, z, sizeX, sizeY, sizeZ, extra, extra, extra,
				this.mirror, false, new Direction[0]);
		return this;
	}

	public CustomizableModelPart addCustomCuboid(float x, float y, float z, float sizeX, float sizeY, float sizeZ, Direction[] hidden) {
		addCustomCuboid(this.textureOffsetU, this.textureOffsetV, x, y, z, sizeX, sizeY, sizeZ, 0.0F, 0.0F, 0.0F,
				this.mirror, false, hidden);
		return this;
	}

	public CustomizableModelPart addCustomCuboid(float x, float y, float z, float sizeX, float sizeY, float sizeZ,
			boolean mirror) {
		addCustomCuboid(this.textureOffsetU, this.textureOffsetV, x, y, z, sizeX, sizeY, sizeZ, 0.0F, 0.0F, 0.0F,
				mirror, false, new Direction[0]);
		return this;
	}

	public void addCustomCuboid(float x, float y, float z, float sizeX, float sizeY, float sizeZ, float extra) {
		addCustomCuboid(this.textureOffsetU, this.textureOffsetV, x, y, z, sizeX, sizeY, sizeZ, extra, extra, extra,
				this.mirror, false, new Direction[0]);
	}

	public void addCustomCuboid(float x, float y, float z, float sizeX, float sizeY, float sizeZ, float extraX,
			float extraY, float extraZ) {
		addCustomCuboid(this.textureOffsetU, this.textureOffsetV, x, y, z, sizeX, sizeY, sizeZ, extraX, extraY, extraZ,
				this.mirror, false, new Direction[0]);
	}

	public void addCustomCuboid(float x, float y, float z, float sizeX, float sizeY, float sizeZ, float extra,
			boolean mirror) {
		addCustomCuboid(this.textureOffsetU, this.textureOffsetV, x, y, z, sizeX, sizeY, sizeZ, extra, extra, extra,
				mirror, false, new Direction[0]);
	}

	private void addCustomCuboid(int u, int v, float x, float y, float z, float sizeX, float sizeY, float sizeZ,
			float extraX, float extraY, float extraZ, boolean mirror, boolean blm, Direction[] hidden) {
		this.cuboids.add(new CustomizableModelPart.Cuboid(u, v, x, y, z, sizeX, sizeY, sizeZ, extraX, extraY, extraZ, mirror,
				this.textureWidth, this.textureHeight, hidden));
	}

	@Override
	public void setPivot(float x, float y, float z) {
		this.pivotX = x;
		this.pivotY = y;
		this.pivotZ = z;
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay) {
		customRender(matrices, vertices, light, overlay);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green,
			float blue, float alpha) {
		customRender(matrices, vertices, light, overlay, red, green, blue, alpha);
	}

	public void customRender(MatrixStack matrices, VertexConsumer vertices, int light, int overlay) {
		customRender(matrices, vertices, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
	}

	public void customRender(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red,
			float green, float blue, float alpha) {
		if (!this.visible) {
			return;
		}
		if ((this.cuboids.isEmpty()) && (this.children.isEmpty())) {
			return;
		}

		matrices.push();
		rotate(matrices);

		customRenderCuboids(matrices.peek(), vertices, light, overlay, red, green, blue, alpha);

		for (ObjectListIterator<CustomizableModelPart> localObjectListIterator = this.children
				.iterator(); localObjectListIterator.hasNext();) {
			CustomizableModelPart modelPart = (CustomizableModelPart) localObjectListIterator.next();
			modelPart.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		}

		matrices.pop();
	}

	public void rotate(MatrixStack matrix) {
		matrix.translate(this.pivotX / 16.0F, this.pivotY / 16.0F, this.pivotZ / 16.0F);
		if (this.roll != 0.0F) {
			matrix.multiply(Vec3f.POSITIVE_Z.getRadialQuaternion(this.roll));
		}
		if (this.yaw != 0.0F) {
			matrix.multiply(Vec3f.POSITIVE_Y.getRadialQuaternion(this.yaw));
		}
		if (this.pitch != 0.0F) {
			matrix.multiply(Vec3f.POSITIVE_X.getRadialQuaternion(this.pitch));
		}
	}

	private void customRenderCuboids(MatrixStack.Entry matrices, VertexConsumer vertexConsumer, int light, int overlay,
			float red, float green, float blue, float alpha) {
		net.minecraft.util.math.Matrix4f matrix4f = matrices.getModel();
		net.minecraft.util.math.Matrix3f matrix3f = matrices.getNormal();
		for (ObjectListIterator<dev.tr7zw.firstperson.fabric.render.CustomizableModelPart.Cuboid> localObjectListIterator = this.cuboids.iterator(); localObjectListIterator
				.hasNext();) {
			CustomizableModelPart.Cuboid cuboid = (CustomizableModelPart.Cuboid) localObjectListIterator.next();
			for (CustomizableModelPart.Quad quad : cuboid.getSides()) {
				Vec3f vector3f = quad.direction.copy();
				vector3f.transform(matrix3f);

				float f = vector3f.getX();
				float g = vector3f.getY();
				float h = vector3f.getZ();

				for (int i = 0; i < 4; i++) {
					CustomizableModelPart.Vertex vertex = quad.vertices[i];
					float j = vertex.pos.getX() / 16.0F;
					float k = vertex.pos.getY() / 16.0F;
					float l = vertex.pos.getZ() / 16.0F;
					Vector4f vector4f = new Vector4f(j, k, l, 1.0F);
					vector4f.transform(matrix4f);

					vertexConsumer.vertex(vector4f.getX(), vector4f.getY(), vector4f.getZ(), red, green, blue, alpha,
							vertex.u, vertex.v, overlay, light, f, g, h);
				}
			}
		}
	}

	@Override
	public net.minecraft.client.model.ModelPart.Cuboid getRandomCuboid(Random random) {
		return new net.minecraft.client.model.ModelPart.Cuboid(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, false, 0, 0);
	}

	public CustomizableModelPart setTextureSize(int width, int height) {
		this.textureWidth = width;
		this.textureHeight = height;
		return this;
	}

	public class Cuboid {
		private final CustomizableModelPart.Quad[] sides;
		public final float minX;
		public final float minY;
		public final float minZ;
		public final float maxX;
		public final float maxY;
		public final float maxZ;

		public Cuboid(int u, int v, float x, float y, float z, float sizeX, float sizeY, float sizeZ, float extraX,
				float extraY, float extraZ, boolean mirror, float textureWidth, float textureHeight,
				Direction[] hiddenFaces) {
			this.minX = x;
			this.minY = y;
			this.minZ = z;
			this.maxX = (x + sizeX);
			this.maxY = (y + sizeY);
			this.maxZ = (z + sizeZ);
			this.sides = new CustomizableModelPart.Quad[6 - hiddenFaces.length];
			List<Direction> hidden = Arrays.asList(hiddenFaces);

			float f = x + sizeX;
			float g = y + sizeY;
			float h = z + sizeZ;

			x -= extraX;
			y -= extraY;
			z -= extraZ;
			f += extraX;
			g += extraY;
			h += extraZ;

			if (mirror) {
				float i = f;
				f = x;
				x = i;
			}

			CustomizableModelPart.Vertex vertex = new CustomizableModelPart.Vertex(x, y, z, 0.0F, 0.0F);
			CustomizableModelPart.Vertex vertex2 = new CustomizableModelPart.Vertex(f, y, z, 0.0F, 8.0F);
			CustomizableModelPart.Vertex vertex3 = new CustomizableModelPart.Vertex(f, g, z, 8.0F, 8.0F);
			CustomizableModelPart.Vertex vertex4 = new CustomizableModelPart.Vertex(x, g, z, 8.0F, 0.0F);

			CustomizableModelPart.Vertex vertex5 = new CustomizableModelPart.Vertex(x, y, h, 0.0F, 0.0F);
			CustomizableModelPart.Vertex vertex6 = new CustomizableModelPart.Vertex(f, y, h, 0.0F, 8.0F);
			CustomizableModelPart.Vertex vertex7 = new CustomizableModelPart.Vertex(f, g, h, 8.0F, 8.0F);
			CustomizableModelPart.Vertex vertex8 = new CustomizableModelPart.Vertex(x, g, h, 8.0F, 0.0F);

			float j = u;
			float k = u + sizeZ;
			float l = u + sizeZ + sizeX;
			// float m = u + sizeZ + sizeX + sizeX;
			float n = u + sizeZ + sizeX + sizeZ;
			float o = u + sizeZ + sizeX + sizeZ + sizeX;

			float p = v;
			float q = v + sizeZ;
			float r = v + sizeZ + sizeY;
			float t = v + sizeZ + sizeY;
			float s = v + sizeZ + sizeY + sizeZ;

			int id = 0;

			if (!hidden.contains(Direction.DOWN))
				this.sides[id++] = new CustomizableModelPart.Quad(
						new CustomizableModelPart.Vertex[] { vertex6, vertex5, vertex, vertex2 }, l, q, n, r, textureWidth,
						textureHeight, mirror, Direction.DOWN);
			if (!hidden.contains(Direction.UP))
				this.sides[id++] = new CustomizableModelPart.Quad(
						new CustomizableModelPart.Vertex[] { vertex3, vertex4, vertex8, vertex7 }, l, q, n, r, textureWidth,
						textureHeight, mirror, Direction.UP);
			if (!hidden.contains(Direction.WEST))
				this.sides[id++] = new CustomizableModelPart.Quad(
						new CustomizableModelPart.Vertex[] { vertex, vertex5, vertex8, vertex4 }, l, q, n, r, textureWidth,
						textureHeight, mirror, Direction.WEST);
			if (!hidden.contains(Direction.NORTH))
				this.sides[id++] = new CustomizableModelPart.Quad(
						new CustomizableModelPart.Vertex[] { vertex2, vertex, vertex4, vertex3 }, l, q, n, r, textureWidth,
						textureHeight, mirror, Direction.NORTH);
			if (!hidden.contains(Direction.EAST))
				this.sides[id++] = new CustomizableModelPart.Quad(
						new CustomizableModelPart.Vertex[] { vertex6, vertex2, vertex3, vertex7 }, l, q, n, r, textureWidth,
						textureHeight, mirror, Direction.EAST);
			if (!hidden.contains(Direction.SOUTH))
				this.sides[id++] = new CustomizableModelPart.Quad(
						new CustomizableModelPart.Vertex[] { vertex5, vertex6, vertex7, vertex8 }, l, q, n, r, textureWidth,
						textureHeight, mirror, Direction.SOUTH);
		}

		public CustomizableModelPart.Quad[] getSides() {
			return sides;
		}

	}

	class Quad {
		public final CustomizableModelPart.Vertex[] vertices;
		public final Vec3f direction;

		public Quad(CustomizableModelPart.Vertex[] vertices, float u1, float v1, float u2, float v2, float squishU,
				float squishV, boolean flip, Direction direction) {
			this.vertices = vertices;

			float f = 0.0F / squishU;
			float g = 0.0F / squishV;
			vertices[0] = vertices[0].remap(u2 / squishU - f, v1 / squishV + g);
			vertices[1] = vertices[1].remap(u1 / squishU + f, v1 / squishV + g);
			vertices[2] = vertices[2].remap(u1 / squishU + f, v2 / squishV - g);
			vertices[3] = vertices[3].remap(u2 / squishU - f, v2 / squishV - g);

			if (flip) {
				int i = vertices.length;
				for (int j = 0; j < i / 2; j++) {
					CustomizableModelPart.Vertex vertex = vertices[j];
					vertices[j] = vertices[(i - 1 - j)];
					vertices[(i - 1 - j)] = vertex;
				}
			}

			this.direction = direction.getUnitVector();
			if (flip) {
				this.direction.multiplyComponentwise(-1.0F, 1.0F, 1.0F);
			}
		}
	}

	class Vertex {
		public final Vec3f pos;
		public final float u;
		public final float v;

		public Vertex(float x, float y, float z, float u, float v) {
			this(new Vec3f(x, y, z), u, v);
		}

		public dev.tr7zw.firstperson.fabric.render.CustomizableModelPart.Vertex remap(float u, float v) {
			return new dev.tr7zw.firstperson.fabric.render.CustomizableModelPart.Vertex(this.pos, u, v);
		}

		public Vertex(Vec3f pos, float u, float v) {
			this.pos = pos;
			this.u = u;
			this.v = v;
		}
	}

}
