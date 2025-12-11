package net.reminitous.civilizationsmod.network.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class SyncCivilizationPacket {

    private final String civData;

    public SyncCivilizationPacket(String civData) {
        this.civData = civData;
    }

    public static void encode(SyncCivilizationPacket pkt, FriendlyByteBuf buf) {
        buf.writeUtf(pkt.civData);
    }

    public static SyncCivilizationPacket decode(FriendlyByteBuf buf) {
        return new SyncCivilizationPacket(buf.readUtf());
    }

    public static void handle(SyncCivilizationPacket pkt, CustomPayloadEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            // Client-side: update civilization data
            System.out.println("Received civ data: " + pkt.getCivData());
        });
        ctx.setPacketHandled(true);
    }

    public String getCivData() {
        return civData;
    }
}
