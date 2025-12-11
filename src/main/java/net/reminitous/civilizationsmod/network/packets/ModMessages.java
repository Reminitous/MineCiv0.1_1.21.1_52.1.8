package net.reminitous.civilizationsmod.network.packets;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.reminitous.civilizationsmod.CivilizationsMod;

public class ModMessages {
    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            ResourceLocation.fromNamespaceAndPath(CivilizationsMod.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int packetId = 0;

    public static int nextID() {
        return packetId++;
    }

    public static void register() {
        CHANNEL.messageBuilder(SyncCivilizationPacket.class, nextID())
                .encoder(SyncCivilizationPacket::encode)
                .decoder(SyncCivilizationPacket::decode)
                .consumerMainThread(SyncCivilizationPacket::handle)
                .add();

        CivilizationsMod.LOGGER.info("Network packets registered");
    }
}