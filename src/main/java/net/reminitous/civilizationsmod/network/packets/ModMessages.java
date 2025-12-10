package net.reminitous.civilizationsmod.Network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.reminitous.civilizationsmod.Network.Packets.SyncCivilizationDataPacket;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ModMessages {

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("civilizationsmod", "main"),
            () -> "1.0",
            s -> true,
            s -> true
    );

    private static int packetId = 0;

    /** Helper to register packets */
    public static <T> void registerMessage(Class<T> clazz,
                                           BiConsumer<T, FriendlyByteBuf> encoder,
                                           Function<FriendlyByteBuf, T> decoder,
                                           BiConsumer<T, Supplier<NetworkEvent.Context>> handler) {
        CHANNEL.registerMessage(packetId++, clazz, encoder, decoder, handler);
    }

    /** Call this in your mod init */
    public static void registerPackets() {
        registerMessage(SyncCivilizationDataPacket.class,
                (packet, buf) -> packet.toBytes(buf),
                buf -> new SyncCivilizationDataPacket(buf),
                (packet, ctx) -> packet.handle(ctx));
    }

    /** Helper to send to one player */
    public static <T> void sendToPlayer(T message, net.minecraft.server.level.ServerPlayer player) {
        CHANNEL.sendTo(message, player.connection.connection, net.minecraftforge.network.NetworkDirection.PLAY_TO_CLIENT);
    }
}
