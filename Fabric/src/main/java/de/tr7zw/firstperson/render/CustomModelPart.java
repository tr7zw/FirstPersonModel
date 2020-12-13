package de.tr7zw.firstperson.render;

import java.util.Random;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.client.util.math.Vector4f;
import net.minecraft.util.math.Direction;

public class CustomModelPart extends ModelPart {
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

	private final ObjectList<CustomModelPart.Cuboid> cuboids = new ObjectArrayList<Cuboid>();
	private final ObjectList<CustomModelPart> children = new ObjectArrayList<CustomModelPart>();

	public CustomModelPart(Model model) {
		super(model);
		setTextureSize(model.textureWidth, model.textureHeight);
	}

	public CustomModelPart(Model model, int textureOffsetU, int textureOffsetV) {
		this(model.textureWidth, model.textureHeight, textureOffsetU, textureOffsetV);
		model.accept(this);
	}

	public CustomModelPart(int textureWidth, int textureHeight, int textureOffsetU, int textureOffsetV) {
		super(textureWidth, textureHeight, textureOffsetU, textureOffsetV);
		setTextureSize(textureWidth, textureHeight);
		setTextureOffset(textureOffsetU, textureOffsetV);
	}

	public void customCopyPositionAndRotation(ModelPart modelPart) {
		super.copyPositionAndRotation(modelPart);
		this.pitch = modelPart.pitch;
		this.yaw = modelPart.yaw;
		this.roll = modelPart.roll;
		this.pivotX = modelPart.pivotX;
		this.pivotY = modelPart.pivotY;
		this.pivotZ = modelPart.pivotZ;
	}

	public void addChild(CustomModelPart part) {
		this.children.add(part);
	}

	@Override
	public CustomModelPart setTextureOffset(int textureOffsetU, int textureOffsetV) {
		this.textureOffsetU = textureOffsetU;
		this.textureOffsetV = textureOffsetV;
		return this;
	}

	public CustomModelPart addCustomCuboid(String name, float x, float y, float z, int sizeX, int sizeY, int sizeZ,
			float extra, int textureOffsetU, int textureOffsetV) {
		setTextureOffset(textureOffsetU, textureOffsetV);
		addCustomCuboid(this.textureOffsetU, this.textureOffsetV, x, y, z, sizeX, sizeY, sizeZ, extra, extra, extra,
				this.mirror, false);
		return this;
	}

	public CustomModelPart addCustomCuboid(float x, float y, float z, float sizeX, float sizeY, float sizeZ) {
		addCustomCuboid(this.textureOffsetU, this.textureOffsetV, x, y, z, sizeX, sizeY, sizeZ, 0.0F, 0.0F, 0.0F, this.mirror,
				false);
		return this;
	}

	public CustomModelPart addCustomCuboid(float x, float y, float z, float sizeX, float sizeY, float sizeZ, boolean mirror) {
		addCustomCuboid(this.textureOffsetU, this.textureOffsetV, x, y, z, sizeX, sizeY, sizeZ, 0.0F, 0.0F, 0.0F, mirror,
				false);
		return this;
	}

	public void addCustomCuboid(float x, float y, float z, float sizeX, float sizeY, float sizeZ, float extra) {
		addCustomCuboid(this.textureOffsetU, this.textureOffsetV, x, y, z, sizeX, sizeY, sizeZ, extra, extra, extra,
				this.mirror, false);
	}

	public void addCustomCuboid(float x, float y, float z, float sizeX, float sizeY, float sizeZ, float extraX, float extraY,
			float extraZ) {
		addCustomCuboid(this.textureOffsetU, this.textureOffsetV, x, y, z, sizeX, sizeY, sizeZ, extraX, extraY, extraZ,
				this.mirror, false);
	}

	public void addCustomCuboid(float x, float y, float z, float sizeX, float sizeY, float sizeZ, float extra,
			boolean mirror) {
		addCustomCuboid(this.textureOffsetU, this.textureOffsetV, x, y, z, sizeX, sizeY, sizeZ, extra, extra, extra, mirror,
				false);
	}

	private void addCustomCuboid(int u, int v, float x, float y, float z, float sizeX, float sizeY, float sizeZ, float extraX,
			float extraY, float extraZ, boolean mirror, boolean bl) {
		this.cuboids.add(new CustomModelPart.Cuboid(u, v, x, y, z, sizeX, sizeY, sizeZ, extraX, extraY, extraZ, mirror,
				this.textureWidth, this.textureHeight));
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

	public void customRender(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green,
			float blue, float alpha) {
		if (!this.visible) {
			return;
		}
		if ((this.cuboids.isEmpty()) && (this.children.isEmpty())) {
			return;
		}

		matrices.push();
		rotate(matrices);

		customRenderCuboids(matrices.peek(), vertices, light, overlay, red, green, blue, alpha);

		for (ObjectListIterator<CustomModelPart> localObjectListIterator = this.children.iterator(); localObjectListIterator
				.hasNext();) {
			CustomModelPart modelPart = (CustomModelPart) localObjectListIterator.next();
			modelPart.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		}

		matrices.pop();
	}

	public void rotate(MatrixStack matrix) {
		matrix.translate(this.pivotX / 16.0F, this.pivotY / 16.0F, this.pivotZ / 16.0F);
		if (this.roll != 0.0F) {
			matrix.multiply(Vector3f.POSITIVE_Z.getRadialQuaternion(this.roll));
		}
		if (this.yaw != 0.0F) {
			matrix.multiply(Vector3f.POSITIVE_Y.getRadialQuaternion(this.yaw));
		}
		if (this.pitch != 0.0F) {
			matrix.multiply(Vector3f.POSITIVE_X.getRadialQuaternion(this.pitch));
		}
	}

	private void customRenderCuboids(MatrixStack.Entry matrices, VertexConsumer vertexConsumer, int light, int overlay,
			float red, float green, float blue, float alpha) {
		net.minecraft.util.math.Matrix4f matrix4f = matrices.getModel();
		net.minecraft.util.math.Matrix3f matrix3f = matrices.getNormal();
		for (ObjectListIterator<Cuboid> localObjectListIterator = this.cuboids.iterator(); localObjectListIterator.hasNext();) {
			CustomModelPart.Cuboid cuboid = (CustomModelPart.Cuboid) localObjectListIterator.next();
			for (CustomModelPart.Quad quad : cuboid.getSides()) {
				Vector3f vector3f = quad.direction.copy();
				vector3f.transform(matrix3f);

				float f = vector3f.getX();
				float g = vector3f.getY();
				float h = vector3f.getZ();

				for (int i = 0; i < 4; i++) {
					CustomModelPart.Vertex vertex = quad.vertices[i];
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
		return new net.minecraft.client.model.ModelPart.Cuboid(0,0,0,0,0,0,0,0,0,0,0,false,0,0);
	}

	public CustomModelPart setTextureSize(int width, int height) {
		this.textureWidth = width;
		this.textureHeight = height;
		return this;
	}

	public class Cuboid {
		private final CustomModelPart.Quad[] sides;
		public final float minX;
		public final float minY;
		public final float minZ;
		public final float maxX;
		public final float maxY;
		public final float maxZ;

		public Cuboid(int u, int v, float x, float y, float z, float sizeX, float sizeY, float sizeZ, float extraX,
				float extraY, float extraZ, boolean mirror, float textureWidth, float textureHeight) {
			this.minX = x;
			this.minY = y;
			this.minZ = z;
			this.maxX = (x + sizeX);
			this.maxY = (y + sizeY);
			this.maxZ = (z + sizeZ);
			this.sides = new CustomModelPart.Quad[6];

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

			CustomModelPart.Vertex vertex = new CustomModelPart.Vertex(x, y, z, 0.0F, 0.0F);
			CustomModelPart.Vertex vertex2 = new CustomModelPart.Vertex(f, y, z, 0.0F, 8.0F);
			CustomModelPart.Vertex vertex3 = new CustomModelPart.Vertex(f, g, z, 8.0F, 8.0F);
			CustomModelPart.Vertex vertex4 = new CustomModelPart.Vertex(x, g, z, 8.0F, 0.0F);

			CustomModelPart.Vertex vertex5 = new CustomModelPart.Vertex(x, y, h, 0.0F, 0.0F);
			CustomModelPart.Vertex vertex6 = new CustomModelPart.Vertex(f, y, h, 0.0F, 8.0F);
			CustomModelPart.Vertex vertex7 = new CustomModelPart.Vertex(f, g, h, 8.0F, 8.0F);
			CustomModelPart.Vertex vertex8 = new CustomModelPart.Vertex(x, g, h, 8.0F, 0.0F);

			float j = u;
			float k = u + sizeZ;
			float l = u + sizeZ + sizeX;
			//float m = u + sizeZ + sizeX + sizeX;
			float n = u + sizeZ + sizeX + sizeZ;
			float o = u + sizeZ + sizeX + sizeZ + sizeX;

			float p = v;
			float q = v + sizeZ;
			float r = v + sizeZ + sizeY;
			float t = v + sizeZ + sizeY;
			float s = v + sizeZ + sizeY + sizeZ;


			this.sides[2] = new CustomModelPart.Quad(new CustomModelPart.Vertex[] { vertex6, vertex5, vertex, vertex2 },
					k, p, l, q, textureWidth, textureHeight, mirror, Direction.DOWN);
			this.sides[3] = new CustomModelPart.Quad(
					new CustomModelPart.Vertex[] { vertex3, vertex4, vertex8, vertex7 }, k, t, l, s, textureWidth, // l, q, m, p | k, q, l, q
					textureHeight, mirror, Direction.UP);

			this.sides[1] = new CustomModelPart.Quad(new CustomModelPart.Vertex[] { vertex, vertex5, vertex8, vertex4 },
					j, q, k, r, textureWidth, textureHeight, mirror, Direction.WEST);
			this.sides[4] = new CustomModelPart.Quad(new CustomModelPart.Vertex[] { vertex2, vertex, vertex4, vertex3 },
					k, q, l, r, textureWidth, textureHeight, mirror, Direction.NORTH);
			this.sides[0] = new CustomModelPart.Quad(
					new CustomModelPart.Vertex[] { vertex6, vertex2, vertex3, vertex7 }, l, q, n, r, textureWidth,
					textureHeight, mirror, Direction.EAST);
			this.sides[5] = new CustomModelPart.Quad(
					new CustomModelPart.Vertex[] { vertex5, vertex6, vertex7, vertex8 }, n, q, o, r, textureWidth,
					textureHeight, mirror, Direction.SOUTH);
		}

		public CustomModelPart.Quad[] getSides() {
			return sides;
		}

	}

	class Quad {
		public final CustomModelPart.Vertex[] vertices;
		public final Vector3f direction;

		public Quad(CustomModelPart.Vertex[] vertices, float u1, float v1, float u2, float v2, float squishU,
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
					CustomModelPart.Vertex vertex = vertices[j];
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
		public final Vector3f pos;
		public final float u;
		public final float v;

		public Vertex(float x, float y, float z, float u, float v) {
			this(new Vector3f(x, y, z), u, v);
		}

		public Vertex remap(float u, float v) {
			return new Vertex(this.pos, u, v);
		}

		public Vertex(Vector3f pos, float u, float v) {
			this.pos = pos;
			this.u = u;
			this.v = v;
		}
	}

}
