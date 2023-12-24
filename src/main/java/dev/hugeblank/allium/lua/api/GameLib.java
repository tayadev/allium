package dev.hugeblank.allium.lua.api;

import dev.hugeblank.allium.Allium;
import dev.hugeblank.allium.lua.type.annotation.CoerceToNative;
import dev.hugeblank.allium.lua.type.annotation.LuaWrapped;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import org.squiddev.cobalt.LuaError;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@LuaWrapped(name = "game")
public class GameLib implements WrappedLuaLibrary {
    @LuaWrapped
    public ServerPlayerEntity getPlayer(String username) throws LuaError {
        ServerPlayerEntity player = Allium.SERVER.getPlayerManager().getPlayer(username);
        if (player == null) throw new LuaError("Player '" + username + "' does not exist");
        return player;
    }

    @LuaWrapped
    public Block getBlock(String id) {
        return Objects.requireNonNull(Registries.BLOCK.get(new Identifier(id)));
    }

    @LuaWrapped
    public Item getItem(String id) {
        return Objects.requireNonNull(Registries.ITEM.get(new Identifier(id)));
    }

    @LuaWrapped
    public ServerWorld getWorld(String id) {
        // TODO: World registry doesn't seem to exist?
        return Objects.requireNonNull(Allium.SERVER.getWorld(RegistryKey.of(Registry.WORLD_KEY, new Identifier(id))));
    }

    @LuaWrapped
    public @CoerceToNative Map<String, Block> listBlocks() {
        return Registries.BLOCK.stream().collect(Collectors.toMap(x -> Registries.BLOCK.getId(x).toString(), x -> x));
    }

    @LuaWrapped
    public @CoerceToNative Map<String, Item> listItems() {
        return Registries.ITEM.stream().collect(Collectors.toMap(x -> Registries.ITEM.getId(x).toString(), x -> x));
    }

    @LuaWrapped
    public @CoerceToNative List<ServerPlayerEntity> listPlayers() {
        return Allium.SERVER.getPlayerManager().getPlayerList();
    }

    @LuaWrapped
    public @CoerceToNative Map<String, ServerWorld> listWorlds() {
        return StreamSupport.stream(Allium.SERVER.getWorlds().spliterator(), false)
                .collect(Collectors.toMap(x -> x.getRegistryKey().getValue().toString(), x -> x));
    }
}
