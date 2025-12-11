package net.reminitous.civilizationsmod.network;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import com.example.mod.data.Civilization;
import com.example.mod.network.ModMessages; // Your channel registration class

public class CivilizationNetwork {

    public static void syncCivToAllMembers(Civilization civ) {
        for (String memberName : civ.getMembers()) {
            ServerPlayer player = findPlayerByName(memberName);
            if (player != null) {
                ModMessages.INSTANCE.sendTo(
                        new CivilizationSyncPacket(civ),
                        player.connection.connection,
                        NetworkDirection.PLAY_TO_CLIENT
                );
            }
        }
    }

    private static ServerPlayer findPlayerByName(String name) {
        for (ServerPlayer player : player.getServer().getPlayerList().getPlayers()) {
            if (player.getName().getString().equals(name)) return player;
        }
        return null;
    }
}
