package net.reminitous.civilizationsmod.network.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class WarProposalPacket {

    private final String warDetails;

    public WarProposalPacket(String warDetails) {
        this.warDetails = warDetails;
    }

    public static void encode(WarProposalPacket pkt, FriendlyByteBuf buf) {
        buf.writeUtf(pkt.warDetails);
    }

    public static WarProposalPacket decode(FriendlyByteBuf buf) {
        return new WarProposalPacket(buf.readUtf());
    }

    public static void handle(WarProposalPacket pkt, CustomPayloadEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            // TODO: Handle war proposal
        });
        ctx.setPacketHandled(true);
    }

    public String getWarDetails() {
        return warDetails;
    }
}
