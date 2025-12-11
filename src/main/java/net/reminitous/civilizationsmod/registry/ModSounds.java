package net.reminitous.civilizationsmod.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.reminitous.civilizationsmod.CivilizationsMod;

/**
 * Registers all custom sounds.
 */
public class ModSounds {

    public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, CivilizationsMod.MODID);

    // Monument placed sound
    public static final RegistryObject<SoundEvent> MONUMENT_PLACED =
            SOUNDS.register("monument_placed",
                    () -> SoundEvent.createVariableRangeEvent(
                            new ResourceLocation(CivilizationsMod.MODID, "monument_placed")));

    // War declared sound
    public static final RegistryObject<SoundEvent> WAR_DECLARED =
            SOUNDS.register("war_declared",
                    () -> SoundEvent.createVariableRangeEvent(
                            new ResourceLocation(CivilizationsMod.MODID, "war_declared")));

    // Victory sound
    public static final RegistryObject<SoundEvent> CIVILIZATION_VICTORY =
            SOUNDS.register("civilization_victory",
                    () -> SoundEvent.createVariableRangeEvent(
                            new ResourceLocation(CivilizationsMod.MODID, "civilization_victory")));
}
