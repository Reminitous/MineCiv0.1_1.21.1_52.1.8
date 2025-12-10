package net.reminitous.civilizationsmod.npc;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.reminitous.civilizationsmod.CivilizationsMod;

/**
 * Registers all custom NPC entities for Civilizations Mod.
 */
public class ModEntities {

    // Deferred register for entities
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITIES, CivilizationsMod.MOD_ID);

    // ---------------- NPC Entities ----------------

    public static final RegistryObject<EntityType<FarmerNPC>> FARMER =
            ENTITIES.register("farmer",
                    () -> EntityType.Builder.of(FarmerNPC::new, MobCategory.CREATURE)
                            .sized(0.6f, 1.95f) // Width and height like player
                            .build("farmer"));

    public static final RegistryObject<EntityType<ShepherdNPC>> SHEPHERD =
            ENTITIES.register("shepherd",
                    () -> EntityType.Builder.of(ShepherdNPC::new, MobCategory.CREATURE)
                            .sized(0.6f, 1.95f)
                            .build("shepherd"));

    public static final RegistryObject<EntityType<LumberjackNPC>> LUMBERJACK =
            ENTITIES.register("lumberjack",
                    () -> EntityType.Builder.of(LumberjackNPC::new, MobCategory.CREATURE)
                            .sized(0.6f, 1.95f)
                            .build("lumberjack"));

    public static final RegistryObject<EntityType<KnightNPC>> KNIGHT =
            ENTITIES.register("knight",
                    () -> EntityType.Builder.of(KnightNPC::new, MobCategory.CREATURE)
                            .sized(0.6f, 1.95f)
                            .build("knight"));

    public static final RegistryObject<EntityType<WizardNPC>> WIZARD =
            ENTITIES.register("wizard",
                    () -> EntityType.Builder.of(WizardNPC::new, MobCategory.CREATURE)
                            .sized(0.6f, 1.95f)
                            .build("wizard"));

    public static final RegistryObject<EntityType<EngineerNPC>> ENGINEER =
            ENTITIES.register("engineer",
                    () -> EntityType.Builder.of(EngineerNPC::new, MobCategory.CREATURE)
                            .sized(0.6f, 1.95f)
                            .build("engineer"));

    public static final RegistryObject<EntityType<MerchantNPC>> MERCHANT =
            ENTITIES.register("merchant",
                    () -> EntityType.Builder.of(MerchantNPC::new, MobCategory.CREATURE)
                            .sized(0.6f, 1.95f)
                            .build("merchant"));

    // ---------------- End NPC Entities ----------------

}
