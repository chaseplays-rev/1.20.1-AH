package net.chase.ah.Extensions;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.LevelResource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

public class OfflinePlayer {
    private final MinecraftServer server;
    private final UUID uuid;
    private CompoundTag nbt;

    public OfflinePlayer(MinecraftServer server, UUID uuid) throws IOException {
        this.server = server;
        this.uuid = uuid;
        this.nbt = load();
    }

    private CompoundTag load() throws IOException {
        Path path = dataFile();
        if (!Files.exists(path)) {
            throw new IOException("Player data not found for " + uuid);
        }
        File file = path.toFile();
        CompoundTag root = NbtIo.readCompressed(file);
        if (root == null) throw new IOException("Failed to read NBT for " + uuid);
        return root;
    }

    /** Re-read from disk (discarding in-memory changes). */
    public void reload() throws IOException {
        this.nbt = load();
    }

    public void save() throws IOException {
        Path path = dataFile();
        Files.createDirectories(path.getParent());
        NbtIo.writeCompressed(nbt, path.toFile());
    }

    private Path dataFile() {
        return server.getWorldPath(LevelResource.PLAYER_DATA_DIR).resolve(uuid + ".dat");
    }

    public boolean exists() {
        return Files.exists(dataFile());
    }

    public CompoundTag getData() { return nbt; }

    public void setData(CompoundTag newData) { this.nbt = newData; }

    public UUID getUuid() { return uuid; }
}