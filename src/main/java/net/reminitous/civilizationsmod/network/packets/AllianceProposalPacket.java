package net.reminitous.civilizationsmod.network.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.event.NetworkEvent;   // <-- Correct import
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
        NetworkEvent.Context context = ctx.get();

        context.enqueueWork(() -> {
            // TODO: Add your server/client logic here
            System.out.println("Alliance proposal received: " + pkt.allianceDetails);
        });

        context.setPacketHandled(true);
    }

    public String getAllianceDetails() {
        return allianceDetails;
    }
}
