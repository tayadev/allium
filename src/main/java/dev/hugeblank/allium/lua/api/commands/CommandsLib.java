package dev.hugeblank.allium.lua.api.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.tree.CommandNode;
import dev.hugeblank.allium.Allium;
import dev.hugeblank.allium.loader.Script;
import dev.hugeblank.allium.lua.api.WrappedLuaLibrary;
import dev.hugeblank.allium.lua.type.annotation.LuaIndex;
import dev.hugeblank.allium.lua.type.annotation.LuaWrapped;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

import java.util.Collections;

// Functionally similar to ComputerCraft's commands API
// See: https://github.com/cc-tweaked/CC-Tweaked/blob/mc-1.16.x/src/main/java/dan200/computercraft/shared/computer/apis/CommandAPI.java
@LuaWrapped(name = "commands")
public class CommandsLib implements WrappedLuaLibrary {
    private final Script script;

    public CommandsLib(Script script) {
        this.script = script;
    }

    private boolean isServerNull() {
        if (Allium.SERVER == null) {
            script.getLogger().error("Cannot execute command: server is not loaded!");
            return true;
        }
        return false;
    }

    @LuaWrapped
    public Boolean exec(String... args) {
        if (isServerNull()) return null;

        String command = String.join(" ", args);

        CommandManager manager = Allium.SERVER.getCommandManager();
        ServerCommandSource source = Allium.SERVER.getCommandSource();
        CommandDispatcher<ServerCommandSource> dispatcher = manager.getDispatcher();
        ParseResults<ServerCommandSource> parseResults = dispatcher.parse(command, source);
        if(parseResults.getExceptions().size() != 0) {
            return false;
        }
        manager.execute(parseResults, command);
        return true;
    }

    @LuaIndex
    public BoundCommand index(String command) {
        if (isServerNull()) return null;

        CommandManager manager = Allium.SERVER.getCommandManager();
        ServerCommandSource source = Allium.SERVER.getCommandSource();
        CommandDispatcher<ServerCommandSource> dispatcher = manager.getDispatcher();
        CommandNode<?> node = dispatcher.findNode(Collections.singleton(command));
        ParseResults<ServerCommandSource> parseResults = dispatcher.parse(command, source);

        // TODO: idk what this is supposed to do, so idk how to fix it
        if (node == null) return null;
        else return (args) -> manager.execute(source, (command + " " + String.join(" ", args).trim())) != 0;
    }

    @FunctionalInterface
    public interface BoundCommand {
        boolean exec(String... args);
    }
}
