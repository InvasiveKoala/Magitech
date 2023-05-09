package com.github.invasivekoala.magitech.entities.clockwork;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class ClockworkEntity extends TamableAnimal implements IAnimatable {
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    private static final EntityDataAccessor<ItemStack> HAND_ITEM = SynchedEntityData.defineId(ClockworkEntity.class, EntityDataSerializers.ITEM_STACK);
    protected final ItemStackHandler inventory;

    public ClockworkEntity(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        inventory = new ItemStackHandler(getMaxInventorySize());
    }



    // -----------------------------
    // Tick Behavior
    // -----------------------------

    @Override
    public void tick() {
        super.tick();
        String prefix = (level.isClientSide)? "Client Says:" : "Server Says:";
        System.out.println(prefix + getHeldItem().toString());
    }


    // -----------------------------
    // Attributes and Goals
    // -----------------------------

    public static AttributeSupplier setAttributes() {
        return createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0d)
                .add(Attributes.ATTACK_DAMAGE, 3.0d)
                .add(Attributes.ATTACK_SPEED, 2.0d)
                .add(Attributes.MOVEMENT_SPEED, 0.3f).build();
    }

    protected void registerGoals() {

    }

    @Override
    public boolean canBeLeashed(Player pPlayer) {
        return false;
    }


    @Override
    public boolean canBreed() {
        return false;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        return null;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.put("Inventory", inventory.serializeNBT());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        inventory.deserializeNBT(pCompound.getCompound("Inventory"));
        // Update held item to be synced as well.
        // Otherwise, when you relog, the item won't appear in the clockwork's hand
        entityData.set(HAND_ITEM, inventory.getStackInSlot(0));

    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(HAND_ITEM, ItemStack.EMPTY);
    }

    // --------------------------------
    // Inventory
    // --------------------------------



    public ItemStackHandler getInventory(){
        return inventory;
    }
    // Maximum possible inventory size (including upgrades, etc.)
    protected int getMaxInventorySize(){
        return 2;
    }
    public void setHeldItem(ItemStack stack){
        setStackInSlot(0, stack);
        entityData.set(HAND_ITEM, stack);
    }
    public ItemStack getHeldItem(){
        return entityData.get(HAND_ITEM);
    }
    public void setStackInSlot(int slot, ItemStack stack){
        getInventory().setStackInSlot(slot, stack);
    }
    public ItemStack getStackInSlot(int slot){
        return getInventory().getStackInSlot(slot);
    }

    @Override
    public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        ItemStack stack = pPlayer.getItemInHand(pHand);

        ItemStack thisStack = getHeldItem();
        InteractionResult testResult = stack.interactLivingEntity(pPlayer, this, pHand);

        if (!testResult.consumesAction() && !stack.isEmpty()) {
            pPlayer.setItemInHand(pHand, thisStack);
            setHeldItem(stack);
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return testResult;
    }

    @Override
    protected void onOffspringSpawnedFromEgg(Player pPlayer, Mob pChild) {
    }

    // --------------------------------
    // Animations
    // --------------------------------

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 0, this::predicate));
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event){
        if (event.isMoving()){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.clockwork.walk", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.clockwork.idle", ILoopType.EDefaultLoopTypes.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    // --------------------------------
    // Sounds
    // --------------------------------


    @Override
    protected void playStepSound(BlockPos pPos, BlockState pState) {
        super.playStepSound(pPos, pState);
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return super.getAmbientSound();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return super.getHurtSound(pDamageSource);
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return super.getDeathSound();
    }

    @Override
    protected float getSoundVolume() {
        return super.getSoundVolume();
    }
}
