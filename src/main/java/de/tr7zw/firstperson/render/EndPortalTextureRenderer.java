package de.tr7zw.firstperson.render;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import com.google.common.collect.ImmutableList;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;

public class EndPortalTextureRenderer {

	private static final Random RANDOM = new Random(31100L);
	private static final List<RenderLayer> field_21732 = IntStream.range(0, 16).mapToObj((i) -> {
		return RenderLayer.getEndPortal(i + 1);
	}).collect(ImmutableList.toImmutableList());
	
	public static void render(ModelPart model, VertexConsumerProvider vertexConsumerProvider, MatrixStack matrixStack, int light, int overlay) {
		method_23084(model, 0.15F, matrixStack, light, overlay,
				vertexConsumerProvider.getBuffer((RenderLayer) field_21732.get(0)));

		for (int l = 1; l < 15; ++l) {
			method_23084(model, 2.0F / (float) (18 - l), matrixStack, light, overlay,
					vertexConsumerProvider.getBuffer((RenderLayer) field_21732.get(l)));
		}
	}
	
	private static void method_23084(ModelPart model, float z, MatrixStack matrixStack, int light, int overlay,
			VertexConsumer vertexConsumer) {
		float r = (RANDOM.nextFloat() * 0.2F + 0.8F) * z; //0.5F + 0.1F // 0.2F + 0.8F
		float g = (RANDOM.nextFloat() * 0.1F + 0.1F) * z; //0.5F + 0.4F // 0.1F + 0.1F
		float b = (RANDOM.nextFloat() * 0.1F + 0.1F) * z; //0.5F + 0.5F // 0.1F + 0.1F
		model.render(matrixStack, vertexConsumer, light, overlay, r, g, b, 1F);
	}
	
}
