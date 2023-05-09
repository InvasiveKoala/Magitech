package com.github.invasivekoala.magitech.entities;

import net.minecraft.core.NonNullList;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;


// Only reason this extends LivingEntity is because geckolib requires it lol
// I have no idea how to do animations solely in-code.
public class MagicPlatformEntity extends LivingEntity implements IAnimatable {

    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private static final EntityDataAccessor<Integer> DATA_DISAPPEAR_TIME = SynchedEntityData.defineId(MagicPlatformEntity.class, EntityDataSerializers.INT);
    protected Player owner;
    private boolean justSpawned;

    private final NonNullList<ItemStack> nullArmorItems = NonNullList.withSize(4, ItemStack.EMPTY);

    public MagicPlatformEntity(EntityType<MagicPlatformEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }


    public static MagicPlatformEntity spawnPlatform(Level pLevel, double pX, double pY, double pZ){
        MagicPlatformEntity platform = new MagicPlatformEntity(EntityRegistry.MAGIC_PLATFORM.get(), pLevel);
        platform.setPos(pX, pY, pZ);
        platform.xo = pX;
        platform.yo = pY;
        platform.zo = pZ;

        return platform;
    }

    public static AttributeSupplier setAttributes() {
        return createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 1.0d).build();
    }


    @Override
    public boolean canBeCollidedWith() {
        return true;
    }


    @Override
    public void tick() {
        this.setRemainingTime(getRemainingTime() - 1);
        if (getRemainingTime() <= 0) {
            this.discard();
        }
    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.LEFT;
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (isInvulnerableTo(pSource)) return false;

        discard();
        return true;
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return nullArmorItems;
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot pSlot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot pSlot, ItemStack pStack) {

    }

    @Override
    public boolean isInvulnerableTo(DamageSource pSource) {
        return pSource.isFire() || pSource.isFall() || pSource.isMagic();
    }

    public int getRemainingTime(){
        return entityData.get(DATA_DISAPPEAR_TIME);
    }
    public void setRemainingTime(int i){
        entityData.set(DATA_DISAPPEAR_TIME, i);
    }


    // Anims
    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 0, this::predicate));
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event){
        if (tickCount < 20){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.magic_platform.spawn", ILoopType.EDefaultLoopTypes.HOLD_ON_LAST_FRAME));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
    // Literally nothing
    // Just here to have a hitbox and disappear.
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(DATA_DISAPPEAR_TIME, 100);
    }



}
