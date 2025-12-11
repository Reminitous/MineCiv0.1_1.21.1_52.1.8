package net.reminitous.civilizationsmod.network.packets;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.network.NetworkDirection;
import net.reminitous.civilizationsmod.network.packets.SyncCivilizationDataPacket;


import net.reminitous.civilizationsmod.CivilizationsMod;

public class ModMessages {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(CivilizationsMod.MODID, "main"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();

    private static int packetId = 0;

    public static int nextID() {
        return packetId++;
    }

    public static void register() {
        CHANNEL.registerMessage(
                nextID(),
                SyncCivilizationPacket.class,
                SyncCivilizationPacket::encode,
                SyncCivilizationPacket::decode,
                SyncCivilizationPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT) // server -> client
        );
    }
}
