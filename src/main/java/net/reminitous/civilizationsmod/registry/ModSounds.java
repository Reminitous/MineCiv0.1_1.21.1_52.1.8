package net.reminitous.civilizationsmod.sounds;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.reminitous.civilizationsmod.CivilizationsMod;

/**
 * Registers all custom sounds for Civilizations Mod.
 */
public class ModSounds {

    // Deferred register for sounds
    public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, CivilizationsMod.MOD_ID);

    // Example: Monument placed sound
    public static final RegistryObject<SoundEvent> MONUMENT_PLACED =
            registerSound("monument_placed");

    // Example: War declared sound
    public static final RegistryObject<SoundEvent> WAR_DECLARED =
            registerSound("war_declared");

    // Example: Civilization victory sound
    public static final RegistryObject<SoundEvent> CIVILIZATION_VICTORY =
            registerSound("civilization_victory");

    // ---------------- Helper method ----------------
    private static RegistryObject<SoundEvent> registerSound(String name) {
        ResourceLocation location = new ResourceLocation(CivilizationsMod.MOD_ID, name);
        return SOUNDS.register(name, () -> new SoundEvent(location));
    }
}
