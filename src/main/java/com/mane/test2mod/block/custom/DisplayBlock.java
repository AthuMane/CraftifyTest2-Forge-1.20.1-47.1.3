package com.mane.test2mod.block.custom;

import com.mane.test2mod.Test2Mod;
import com.mane.test2mod.block.entity.DisplayBlockEntity;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class DisplayBlock extends BaseEntityBlock
{

    public DisplayBlock(Properties pProperties)
    {
        super(pProperties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos,
                                 Player player, InteractionHand hand,
                                 BlockHitResult hit) {

        if(level.isClientSide) return InteractionResult.SUCCESS;

        BlockEntity entity = level.getBlockEntity(pos);

        if(entity instanceof DisplayBlockEntity display) {

            ItemStack held = player.getItemInHand(hand);

            if(display.getItem().isEmpty() && !held.isEmpty()) {

                ItemStack copy = held.copyWithCount(1);
                display.setItem(copy);

                held.shrink(1);

                grantAdvancement(player);


                return InteractionResult.CONSUME;
            }

            if(!display.getItem().isEmpty())
            {

                player.addItem(display.getItem().copy());

                display.setItem(ItemStack.EMPTY);

                return InteractionResult.CONSUME;
            }
        }

        return InteractionResult.PASS;
    }

    private void grantAdvancement(Player player) {

        if(player instanceof ServerPlayer serverPlayer) {

            Advancement advancement = serverPlayer.server.getAdvancements()
                    .getAdvancement(new ResourceLocation(Test2Mod.MODID, "used_display_block"));

            if (advancement != null) {

                AdvancementProgress progress = serverPlayer.getAdvancements().getOrStartProgress(advancement);

                if (!progress.isDone()) {
                    for (String criterion : progress.getRemainingCriteria()) {
                        serverPlayer.getAdvancements().award(advancement, criterion);
                    }
                }
            }
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving)
    {
        if(!state.is(newState.getBlock()))
        {
            BlockEntity blockEntity = level.getBlockEntity(pos);

            if(blockEntity instanceof DisplayBlockEntity display)
            {
                ItemStack stack = display.getItem();

                if(!stack.isEmpty())
                {
                    popResource(level, pos, stack);
                }
            }

            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState pState)
    {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState)
    {
        return new DisplayBlockEntity(pPos, pState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType)
    {
        return super.getTicker(pLevel, pState, pBlockEntityType);
    }
}
