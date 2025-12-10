package net.reminitous.civilizationsmod.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.UUID;

public class MonumentBlockEntity extends BlockEntity {
    private String civilizationId;
    private String civilizationName;
    private UUID creator;

    public MonumentBlockEntity(BlockPos pos, BlockState state) {
        super(net.reminitous.civilizationsmod.setup.ModBlockEntities.MONUMENT.get(), pos, state);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("civId")) this.civilizationId = tag.getString("civId");
        if (tag.contains("civName")) this.civilizationName = tag.getString("civName");
        if (tag.hasUUID("creator")) this.creator = tag.getUUID("creator");
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (civilizationId != null) tag.putString("civId", civilizationId);
        if (civilizationName != null) tag.putString("civName", civilizationName);
        if (creator != null) tag.putUUID("creator", creator);
    }

    public void setCivInfo(String id, String name, UUID creator) {
        this.civilizationId = id;
        this.civilizationName = name;
        this.creator = creator;
        setChanged();
    }

    public String getCivId() { return civilizationId; }
}
