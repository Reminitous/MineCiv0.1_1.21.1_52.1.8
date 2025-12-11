package net.reminitous.civilizationsmod.registry;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.reminitous.civilizationsmod.CivilizationsMod;
import net.reminitous.civilizationsmod.npc.*;

/**
 * Registers all custom NPC entities.
 */
public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, CivilizationsMod.MODID);

    // Agricultural NPCs
    public static final RegistryObject<EntityType<FarmerNPC>> FARMER =
            ENTITIES.register("farmer",
                    () -> EntityType.Builder.of(FarmerNPC::new, MobCategory.CREATURE)
                            .sized(0.6f, 1.95f)
                            .clientTrackingRange(8)
                            .build("farmer"));

    public static final RegistryObject<EntityType<ShepherdNPC>> SHEPHERD =
            ENTITIES.register("shepherd",
                    () -> EntityType.Builder.of(ShepherdNPC::new, MobCategory.CREATURE)
                            .sized(0.6f, 1.95f)
                            .clientTrackingRange(8)
                            .build("shepherd"));

    // Resource Gathering NPCs
    public static final RegistryObject<EntityType<LumberjackNPC>> LUMBERJACK =
            ENTITIES.register("lumberjack",
                    () -> EntityType.Builder.of(LumberjackNPC::new, MobCategory.CREATURE)
                            .sized(0.6f, 1.95f)
                            .clientTrackingRange(8)
                            .build("lumberjack"));

    // Combat NPCs
    public static final RegistryObject<EntityType<KnightNPC>> KNIGHT =
            ENTITIES.register("knight",
                    () -> EntityType.Builder.of(KnightNPC::new, MobCategory.CREATURE)
                            .sized(0.6f, 1.95f)
                            .clientTrackingRange(10)
                            .build("knight"));

    public static final RegistryObject<EntityType<WarriorNPC>> WARRIOR =
            ENTITIES.register("warrior",
                    () -> EntityType.Builder.of(WarriorNPC::new, MobCategory.CREATURE)
                            .sized(0.6f, 1.95f)
                            .clientTrackingRange(10)
                            .build("warrior"));

    // Magic NPCs
    public static final RegistryObject<EntityType<WizardNPC>> WIZARD =
            ENTITIES.register("wizard",
                    () -> EntityType.Builder.of(WizardNPC::new, MobCategory.CREATURE)
                            .sized(0.6f, 1.95f)
                            .clientTrackingRange(8)
                            .build("wizard"));

    // Technology NPCs
    public static final RegistryObject<EntityType<EngineerNPC>> ENGINEER =
            ENTITIES.register("engineer",
                    () -> EntityType.Builder.of(EngineerNPC::new, MobCategory.CREATURE)
                            .sized(0.6f, 1.95f)
                            .clientTrackingRange(8)
                            .build("engineer"));

    // Merchant NPCs
    public static final RegistryObject<EntityType<MerchantNPC>> MERCHANT =
            ENTITIES.register("merchant",
                    () -> EntityType.Builder.of(MerchantNPC::new, MobCategory.CREATURE)
                            .sized(0.6f, 1.95f)
                            .clientTrackingRange(8)
                            .build("merchant"));
}
