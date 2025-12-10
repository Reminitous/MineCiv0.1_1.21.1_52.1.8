package net.reminitous.civilizationsmod.network.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

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

    public static void handle(AllianceProposalPacket pkt, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get(); // ensure correct type
        context.enqueueWork(() -> {
            // TODO: Add server/client logic
            System.out.println("Alliance proposal received: " + pkt.getAllianceDetails());
        });
        context.setPacketHandled(true);
    }

    public String getAllianceDetails() {
        return allianceDetails;
    }
}
