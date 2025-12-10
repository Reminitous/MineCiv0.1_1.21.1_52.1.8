package net.reminitous.civilizationsmod.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.reminitous.civilizationsmod.data.CivilizationData;

import java.util.function.Supplier;

public class SyncCivilizationDataPacket {
    private final CivilizationData data;

    public SyncCivilizationDataPacket(CivilizationData data) {
        this.data = data;
    }

    // Decode
    public static net.reminitous.civilizationsmod.network.packets.SyncCivilizationDataPacket decode(FriendlyByteBuf buf) {
        return new net.reminitous.civilizationsmod.network.packets.SyncCivilizationDataPacket(CivilizationData.load(buf.readNbt()));
    }

    // Encode
    public void encode(FriendlyByteBuf buf) {
        buf.writeNbt(data.save(new net.minecraft.nbt.CompoundTag()));
    }

    // Handle packet
    public static void handle(net.reminitous.civilizationsmod.network.packets.SyncCivilizationDataPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // Client-side: update client data
            // Example: ClientCivilizationData.set(packet.data);
        });
        ctx.get().setPacketHandled(true);
    }
}
