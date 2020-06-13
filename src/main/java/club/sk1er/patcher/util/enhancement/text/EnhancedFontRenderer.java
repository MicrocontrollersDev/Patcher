package club.sk1er.patcher.util.enhancement.text;

import club.sk1er.mods.core.util.Multithreading;
import club.sk1er.patcher.Patcher;
import club.sk1er.patcher.util.enhancement.Enhancement;
import club.sk1er.patcher.util.hash.StringHash;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.CacheWriter;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;
import net.minecraft.client.renderer.GLAllocation;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.SharedDrawable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class EnhancedFontRenderer implements Enhancement {

    public static SharedDrawable drawable;
    private final List<StringHash> obfuscated = new ArrayList<>();
    private static final List<EnhancedFontRenderer> instances = new ArrayList<>();
    private final Map<String, Integer> stringWidthCache = new HashMap<>();
    private final Queue<Integer> glRemoval = new ConcurrentLinkedQueue<>();
    private final Cache<StringHash, CachedString> stringCache = Caffeine.newBuilder()
        .writer(new RemovalListener())
        .executor(Multithreading.POOL)
        .maximumSize(5000).build();

    public EnhancedFontRenderer() {
        instances.add(this);
    }

    @Override
    public String getName() {
        return "Enhanced Font Renderer";
    }

    @Override
    public void tick() {
        stringCache.invalidateAll(obfuscated);
        obfuscated.clear();

        if (drawable == null) {
            try {
                drawable = new SharedDrawable(Display.getDrawable());
            } catch (LWJGLException e) {
                Patcher.instance.getLogger().error("Failed to create shared drawable.", e);
            }
        }
    }

    public int getGlList() {
        Integer poll = glRemoval.poll();
        return poll == null ? GLAllocation.generateDisplayLists(1) : poll;
    }

    public CachedString get(StringHash key) {
        return stringCache.getIfPresent(key);
    }

    public void cache(StringHash key, CachedString value) {
        stringCache.put(key, value);
    }

    public Map<String, Integer> getStringWidthCache() {
        return stringWidthCache;
    }

    public void invalidateAll() {
        this.stringCache.invalidateAll();
    }

    public static List<EnhancedFontRenderer> getInstances() {
        return instances;
    }

    public List<StringHash> getObfuscated() {
        return obfuscated;
    }

    private class RemovalListener implements CacheWriter<StringHash, CachedString> {

        @Override
        public void write(@Nonnull StringHash key, @Nonnull CachedString value) {

        }

        @Override
        public void delete(@Nonnull StringHash key, CachedString value, @Nonnull RemovalCause cause) {
            if (value == null) {
                return;
            }

            glRemoval.add(value.getListId());
        }
    }
}