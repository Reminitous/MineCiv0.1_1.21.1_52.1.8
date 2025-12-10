package net.reminitous.civilizationsmod.network.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.event.NetworkEvent; // <--- Correct import
import java.util.function.Supplier;

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

    public static void handle(SyncCivilizationPacket pkt, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            // TODO: apply civData to client-side
            System.out.println("Received civ data: " + pkt.getCivData());
        });
        context.setPacketHandled(true);
    }

    public String getCivData() {
        return civData;
    }
}

