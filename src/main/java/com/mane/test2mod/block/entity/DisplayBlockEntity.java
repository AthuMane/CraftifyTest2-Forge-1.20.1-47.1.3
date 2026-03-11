package com.mane.test2mod.block.entity;

import com.mane.test2mod.block.entity.ModBlockEntities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class DisplayBlockEntity extends BlockEntity {

    private ItemStack item = ItemStack.EMPTY;

    public DisplayBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.DISPLAY_BLOCK_BE.get(), pos, state);
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack stack) {

        if(stack.isEmpty())
        {
            this.item = ItemStack.EMPTY;
        }
        else
        {
            this.item = stack.copyWithCount(1);
        }

        setChanged();

        if(level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    // SAVE DATA
    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        tag.put("item", item.save(new CompoundTag()));
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        item = ItemStack.of(tag.getCompound("item"));
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        load(tag);
    }
}