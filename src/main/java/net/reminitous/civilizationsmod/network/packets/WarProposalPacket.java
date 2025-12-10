package com.yourmod.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class WarProposalPacket {

    private final String warDetails;

    public WarProposalPacket(String warDetails) {
        this.warDetails = warDetails;
    }

    // Encode data to bytes
    public static void encode(WarProposalPacket pkt, FriendlyByteBuf buf) {
        buf.writeUtf(pkt.warDetails);
    }

    // Decode data from bytes
    public static WarProposalPacket decode(FriendlyByteBuf buf) {
        return new WarProposalPacket(buf.readUtf());
    }

    // Handle the packet
    public static void handle(WarProposalPacket pkt, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // TODO: Add your server/client logic here
        });
        ctx.get().setPacketHandled(true);
    }

    public String getWarDetails() {
        return warDetails;
    }
}
