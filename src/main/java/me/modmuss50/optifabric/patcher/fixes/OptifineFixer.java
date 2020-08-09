package me.modmuss50.optifabric.patcher.fixes;

import me.modmuss50.optifabric.util.RemappingUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OptifineFixer {

	public static final OptifineFixer INSTANCE = new OptifineFixer();

	private final Map<String, List<ClassFixer>> classFixes = new HashMap<>();
	private final Set<String> skippedClass = new HashSet<>();

	private OptifineFixer() {
		//net/minecraft/client/render/chunk/ChunkBuilder$BuiltChunk
		registerFix("class_846$class_851$class_4578", new ChunkRendererFix());

		//net/minecraft/client/render/block/BlockModelRenderer
		registerFix("class_778", new BlockModelRendererFix());

		//net/minecraft/client/render/block/BlockModelRenderer$AmbientOcclusionCalculator
		registerFix("class_778$class_780", new AmbientOcclusionCalculatorFix());

		//net/minecraft/client/Keyboard
		registerFix("class_309", new KeyboardFix());

		//net/minecraft/client/render/item/HeldItemRenderer
		registerFix("class_759", new HeldItemRendererFix());

		//net/minecraft/client/render/item/HeldItemRenderer$1
		skipClass("class_759$1"); //Skip the unnecessary changes to the enum switch class
		
		//net/minecraft/client/texture/SpriteAtlasTexture
		registerFix("class_1059", new SpriteAtlasTextureFix());

		//net/minecraft/server/world/ThreadedAnvilChunkStorage
		registerFix("class_3898", new ThreadedAnvilChunkStorageFix());

		//net/minecraft/client/particle/ParticleManager
		skipClass("class_702"); //Skip a seemingly pointless patch to register particles by register name rather than ID
	}

	private void registerFix(String className, ClassFixer classFixer) {
		classFixes.computeIfAbsent(RemappingUtils.getClassName(className), s -> new ArrayList<>()).add(classFixer);
	}

	private void skipClass(String className) {
		skippedClass.add(RemappingUtils.getClassName(className));
	}

	public boolean shouldSkip(String className) {
		return skippedClass.contains(className);
	}

	public List<ClassFixer> getFixers(String className) {
		return classFixes.getOrDefault(className, Collections.emptyList());
	}
}
