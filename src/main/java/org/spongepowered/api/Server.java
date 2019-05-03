/*
 * This file is part of SpongeAPI, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.api;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.profile.GameProfileManager;
import org.spongepowered.api.resource.pack.PackRepository;
import org.spongepowered.api.resource.ReloadableResourceManager;
import org.spongepowered.api.resourcepack.ResourcePack;
import org.spongepowered.api.scoreboard.Scoreboard;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.world.chunk.ChunkTicketManager;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.WorldArchetype;
import org.spongepowered.api.world.storage.ChunkLayout;
import org.spongepowered.api.world.storage.WorldProperties;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Represents a typical Minecraft Server.
 */
public interface Server extends Engine {

    /**
     * Gets the {@link Player}s currently online.
     *
     * @return A {@link Collection} of online players
     */
    Collection<Player> getOnlinePlayers();

    /**
     * Gets the max players allowed on this server.
     *
     * @return Maximum number of connected players
     */
    int getMaxPlayers();

    /**
     * Gets a {@link Player} by their UUID.
     *
     * @param uniqueId The UUID to get the player from
     * @return The {@link Player} or empty if not found
     */
    Optional<Player> getPlayer(UUID uniqueId);

    /**
     * Gets a {@link Player} by their name.
     *
     * <p>This only works for online players.</p>
     *
     * <p><b>Note: Do not use names for persistent storage, the
     * Notch of today may not be the Notch of yesterday.</b></p>
     *
     * @param name The name to get the player from
     * @return The {@link Player} or empty if not found
     */
    Optional<Player> getPlayer(String name);

    /**
     * Gets all currently loaded {@link World}s.
     *
     * @return A collection of loaded worlds
     */
    Collection<World> getWorlds();

    /**
     * Gets the properties of all unloaded worlds.
     *
     * <p>It is left up to the implementation to determine it's offline worlds and no contract is enforced
     * that states that they must returns all unloaded worlds that actually exist.</p>
     *
     * @return A collection of world properties
     */
    Collection<WorldProperties> getUnloadedWorlds();

    /**
     * Gets the properties of all worlds.
     *
     * <p>It is left up to the implementation to determine it's offline worlds and no contract is enforced
     * that states that they must returns all unloaded worlds that actually exist.</p>
     *
     * @return A collection of world properties
     */
    Collection<WorldProperties> getAllWorldProperties();

    /**
     * Gets a loaded {@link World} by its unique id ({@link UUID}), if it
     * exists.
     *
     * @param uniqueId UUID to lookup
     * @return The world, if found
     */
    Optional<World> getWorld(UUID uniqueId);

    /**
     * Gets a loaded {@link World} by it's directory name, if it exists.
     *
     * @param directoryName Name to lookup
     * @return The world, if found
     */
    Optional<World> getWorld(String directoryName);

    /**
     * Gets the properties of default world.
     *
     * @return The world properties
     */
    Optional<WorldProperties> getDefaultWorld();

    /**
     * Gets the default {@link World} name that the server creates and loads.
     *
     * @return The name
     */
    String getDefaultWorldName();

    /**
     * Loads a {@link World} from the default storage container. If a world with
     * the given name is already loaded then it is returned instead.
     *
     * @param directoryName The name to lookup
     * @return The world, if found
     */
    Optional<World> loadWorld(String directoryName);

    /**
     * Loads a {@link World} from the default storage container. If a world with
     * the given UUID is already loaded then it is returned instead.
     *
     * @param uniqueId The UUID to lookup
     * @return The world, if found
     */
    Optional<World> loadWorld(UUID uniqueId);

    /**
     * Loads a {@link World} from the default storage container. If the world
     * associated with the given properties is already loaded then it is
     * returned instead.
     *
     * @param properties The properties of the world to load
     * @return The world, if found
     */
    Optional<World> loadWorld(WorldProperties properties);

    /**
     * Gets the {@link WorldProperties} of a world. If a world with the given
     * name is loaded then this is equivalent to calling
     * {@link World#getProperties()}. However, if no loaded world is found then
     * an attempt will be made to match to a known unloaded world.
     *
     * @param directoryName The name to lookup
     * @return The world properties, if found
     */
    Optional<WorldProperties> getWorldProperties(String directoryName);

    /**
     * Gets the {@link WorldProperties} of a world. If a world with the given
     * UUID is loaded then this is equivalent to calling
     * {@link World#getProperties()}. However, if no loaded world is found then
     * an attempt will be made to match to a known unloaded world.
     *
     * @param uniqueId The UUID to lookup
     * @return The world properties, if found
     */
    Optional<WorldProperties> getWorldProperties(UUID uniqueId);

    /**
     * Unloads a {@link World}.
     *
     * <p>The conditions for how and when a world may be unloaded are left up to the
     * implementation to define.</p>
     *
     * <p>Should the {@link WorldProperties} of the unloaded world return {@link WorldProperties#isEnabled()}
     * 'true' then this server will attempt to load the world during the next startup phase.</p>
     *
     * @param world The world to unload
     * @return Whether the operation was successful
     */
    boolean unloadWorld(World world);

    /**
     * Creates a new {@link WorldProperties} from the given
     * {@link WorldArchetype}. For the creation of the WorldArchetype please see
     * {@link org.spongepowered.api.world.WorldArchetype.Builder}.
     *
     * <p>If the {@link World} exists at the directory name given, the properties
     * representing that directory name are returned instead.</p>
     *
     * <p>Although the world is created it is not loaded at this time. Please
     * see one of the following methods for loading the world.</p>
     *
     * <ul> <li>{@link #loadWorld(String)}</li> <li>{@link #loadWorld(UUID)}
     * </li> <li>{@link #loadWorld(WorldProperties)}</li> </ul>
     *
     * @param directoryName The name of the directory for the world
     * @param archetype The archetype for creation
     * @return The new or existing world properties, if creation was successful
     * @throws IOException If there are any io issues creating the properties
     *      file
     */
    Optional<WorldProperties> createWorldProperties(String directoryName, WorldArchetype archetype) throws IOException;

    /**
     * Creates a world copy asynchronously using the new name given and returns
     * the new world properties if the copy was possible.
     *
     * <p>If the world is already loaded then the following will occur:</p>
     *
     * <ul>
     * <li>World is saved.</li>
     * <li>World saving is disabled.</li>
     * <li>World is copied. </li>
     * <li>World saving is enabled.</li>
     * </ul>
     *
     * @param directoryName The directory name
     * @param copyName The copies' name
     * @return An {@link Optional} containing the properties of the new world
     *         instance, if the copy was successful
     */
    CompletableFuture<Optional<WorldProperties>> copyWorld(String directoryName, String copyName);

    /**
     * Renames a {@link WorldProperties}.
     *
     * <p>If the properties represents an online world, an attempt will be made to unload it. Once unloaded and if
     * the attempt is successful, an attempt will be made to load it. It is left up to the implementation to determine
     * the conditions for a rename to be successful.</p>
     *
     * @param oldDirectoryName The old directory name
     * @param newDirectoryName The new directory name
     * @return An {@link Optional} containing the new {@link WorldProperties}
     *         if the rename was successful
     */
    Optional<WorldProperties> renameWorld(String oldDirectoryName, String newDirectoryName);

    /**
     * Deletes the provided world's files asynchronously from the disk.
     *
     * @param directoryName The directory name to delete
     * @return True if the deletion was successful.
     */
    CompletableFuture<Boolean> deleteWorld(String directoryName);

    /**
     * Persists the given {@link WorldProperties} to the world storage for it,
     * updating any modified values.
     *
     * @param properties The world properties to save
     * @return True if the save was successful
     */
    boolean saveWorldProperties(WorldProperties properties);

    /**
     * Gets the 'server' scoreboard. In Vanilla, this is the scoreboard of
     * dimension 0 (the overworld).
     *
     * <p>The sever scoreboard is used with the Vanilla /scoreboard command,
     * automatic score updating through criteria, and other things.</p>
     *
     * <p>The server scoreboard may not be available if dimension 0
     * is not yet loaded. In Vanilla, this will only occur when the
     * server is first starting, as dimension 0 is normally always loaded.</p>
     *
     * @return the server scoreboard, if available.
     */
    Optional<Scoreboard> getServerScoreboard();

    /**
     * Returns information about the chunk layout used by this server
     * implementation.
     *
     * @return The chunk layout used by the implementation
     */
    ChunkLayout getChunkLayout();

    /**
     * Gets the time, in ticks, since this server began running for the current
     * session.
     *
     * <p>This value is not persisted across server restarts, it is set to zero
     * each time the server starts.</p>
     *
     * @return The number of ticks since this server started running
     */
    int getRunningTimeTicks();

    /**
     * Gets the message channel that server-wide messages are sent through.
     *
     * @return The server-wide broadcast channel
     */
    MessageChannel getBroadcastChannel();

    /**
     * Sets the channel that server-wide messages should be sent through.
     *
     * @param channel The broadcast channel
     */
    void setBroadcastChannel(MessageChannel channel);

    /**
     * Gets the bound {@link InetSocketAddress} from where this server is
     * accepting connections.
     *
     * @return The address or Optional.empty() if not found
     */
    Optional<InetSocketAddress> getBoundAddress();

    /**
     * Tests if the server has a whitelist enabled.
     * @return True if enabled, false if not
     */
    boolean hasWhitelist();

    /**
     * Sets whether the server is utilizing a whitelist.
     * @param enabled True to enable the whitelist, false to disable
     */
    void setHasWhitelist(boolean enabled);

    /**
     * Tests if this server is set to online mode.
     *
     * <b>Online mode authenticates users against Minecraft's servers, false
     * performs no validity checks.</b>
     *
     * @return True if enabled, false if not
     */
    boolean getOnlineMode();

    /**
     * Gets the default message that is displayed in the server list of the
     * client.
     *
     * @return The server's default description (MOTD)
     */
    Text getMotd();

    /**
     * Shuts down the server, and kicks all players with the default kic
     * k message.
     *
     * <p>For the Sponge implementation on the client, this will trigger the
     * Integrated Server to shutdown a tick later.</p>
     */
    void shutdown();

    /**
     * Shuts down the server, and kicks all players with the given message.
     *
     * @param kickMessage The message to kick players with
     */
    void shutdown(Text kickMessage);

    /**
     * Gets the ChunkTicketManager used for requesting tickets to force load
     * chunks.
     *
     * @return This server's chunk load service
     */
    ChunkTicketManager getChunkTicketManager();

    /**
     * Gets the {@link GameProfileManager} for resolving game profiles.
     *
     * @return This server's game profile manager
     */
    GameProfileManager getGameProfileManager();

    /**
     * Gets the current ticks per second. A tick represents one cycle of the
     * game loop.
     *
     * <p>Note: The server aims to limit itself at 20 ticks per second. Lower
     * ticks per second may elude to the server taking more time to process
     * information per tick. Examples of overburdening the server per tick
     * include spawning 10,000 cows in a small area.</p>
     *
     * @return The current ticks per second
     */
    double getTicksPerSecond();

    /**
     * Gets the default resource pack. The default resource pack is sent to
     * players when they join the server.
     *
     * @return The default resource pack
     */
    Optional<ResourcePack> getDefaultResourcePack();

    /**
     * Gets the player idle timeout, in minutes.
     *
     * <p>A return value of {@code 0} disables the player idle timeout.</p>
     *
     * @return The player idle timeout
     */
    int getPlayerIdleTimeout();

    /**
     * Sets the player idle timeout, in minutes.
     *
     * <p>A value of {@code 0} disables the player idle timeout.</p>
     *
     * @param timeout The player idle timeout
     */
    void setPlayerIdleTimeout(int timeout);
}
