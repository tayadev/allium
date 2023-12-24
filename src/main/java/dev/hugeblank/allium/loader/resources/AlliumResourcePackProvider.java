package dev.hugeblank.allium.loader.resources;

import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackProvider;
import net.minecraft.resource.ResourcePackSource;
import net.minecraft.resource.ResourcePackProfile.PackFactory;

import java.util.function.Consumer;

public class AlliumResourcePackProvider implements ResourcePackProvider {
    @Override
    public void register(Consumer<ResourcePackProfile> profileAdder) {
        AlliumResourcePack pack = AlliumResourcePack.create("Allium Generated");
        profileAdder.accept(ResourcePackProfile.of(
                "allium_generated",
                true,
                () -> pack,
                factory, // TODO: used to be available from args
                ResourcePackProfile.InsertionPosition.TOP,
                ResourcePackSource.BUILTIN
        ));
    }
}