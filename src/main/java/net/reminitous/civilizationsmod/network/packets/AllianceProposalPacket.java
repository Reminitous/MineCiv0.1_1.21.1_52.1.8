package net.reminitous.civilizationsmod.network.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class AllianceProposalPacket {

    private final String allianceDetails;

    public AllianceProposalPacket(String allianceDetails) {
        this.allianceDetails = allianceDetails;
    }

    public static void encode(AllianceProposalPacket pkt, FriendlyByteBuf buf) {
        buf.writeUtf(pkt.allianceDetails);
    }

    public static AllianceProposalPacket decode(FriendlyByteBuf buf) {
        return new AllianceProposalPacket(buf.readUtf());
    }

    public static void handle(AllianceProposalPacket pkt, CustomPayloadEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            System.out.println("Alliance proposal received: " + pkt.getAllianceDetails());
        });
        ctx.setPacketHandled(true);
    }

    public String getAllianceDetails() {
        return allianceDetails;
    }
}
