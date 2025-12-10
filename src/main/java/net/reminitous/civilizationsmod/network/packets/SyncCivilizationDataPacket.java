package net.reminitous.civilizationsmod.Network.Packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.reminitous.civilizationsmod.Civilization.Civilization;
import net.reminitous.civilizationsmod.Client.ClientCivilizationManager;

import java.util.function.Supplier;

public class SyncCivilizationDataPacket {

    private final Civilization civilization;

    public SyncCivilizationDataPacket(Civilization civilization) {
        this.civilization = civilization;
    }

    public SyncCivilizationDataPacket(FriendlyByteBuf buf) {
        this.civilization = Civilization.fromNBT(buf.readNbt());
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeNbt(civilization.toNBT());
    }

    public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> ClientCivilizationManager.updateCivilization(civilization));
        context.setPacketHandled(true);
        return true;
    }
}
