package com.github.invasivekoala.magitech.entities;

import com.github.invasivekoala.magitech.events.ClientEvents;
import com.github.invasivekoala.magitech.items.ItemRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;


public class BroomEntity extends Entity {
    private static final float DECELERATION = -0.02f;

    public BroomEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }


    public double getPassengersRidingOffset() {
        return 0.1D;
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {

    }


    @Override
    public Packet<?> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }


    @Override
    public boolean isPickable() {
        return true;
    }


    @Override
    public InteractionResult interact(Player pPlayer, InteractionHand pHand) {
        if (pPlayer.isSecondaryUseActive()) {
            return InteractionResult.PASS;
        } else
            if (!this.level.isClientSide) {
                return pPlayer.startRiding(this) ? InteractionResult.CONSUME : InteractionResult.PASS;
            } else {
                return InteractionResult.SUCCESS;

        }
    }

    @Override
    public void onPassengerTurned(Entity pEntityToUpdate) {
        clampRotation(pEntityToUpdate);
    }
    private void clampRotation(Entity passenger){
        passenger.setYBodyRot(this.getYRot() + 90);
        float clampedRot = Mth.clamp(passenger.getYRot(), 0 + getYRot(), 179 + getYRot());
        //passenger.yRotO = passenger.getYRot();
        passenger.setYRot(clampedRot);
        passenger.setYHeadRot(passenger.getYRot());
    }
    /*public void positionRider(Entity pPassenger) {
        if (this.hasPassenger(pPassenger)) {
            float f = 0.0F;
            float f1 = (float)((this.isRemoved() ? (double)0.01F : this.getPassengersRidingOffset()) + pPassenger.getMyRidingOffset());
            Vec3 vec3 = (new Vec3(f, 0.0D, 0.0D)).yRot(-this.getYRot() * ((float)Math.PI / 180F) - ((float)Math.PI / 2F));
            pPassenger.setPos(this.getX() + vec3.x, this.getY() + (double)f1, this.getZ() + vec3.z);
            pPassenger.setYRot(pPassenger.getYRot() + this.deltaRotation);
            //pPassenger.setYHeadRot(pPassenger.getYHeadRot() + this.deltaRotation);
            this.clampRotation(pPassenger);

        }
    }*/
    // ---------------------------------
    // Broom Riding
    // ---------------------------------



    @Override
    public void tick() {
        super.tick();
        this.tickLerp();
        if (isControlledByLocalInstance()){
            controlBroom();
        }else {
            this.setDeltaMovement(Vec3.ZERO);
        }
        this.move(MoverType.SELF, this.getDeltaMovement());
    }

    // Copied from boat

    private int lerpSteps;
    private double lerpX;
    private double lerpY;
    private double lerpZ;
    private double lerpYRot;
    private double lerpXRot;

    //
    // This code is here from the Boat class - it's so movement and rotation are smooth
    //
    private void tickLerp() {
        if (this.isControlledByLocalInstance()) {
            this.lerpSteps = 0;
            this.setPacketCoordinates(this.getX(), this.getY(), this.getZ());
        }

        if (this.lerpSteps > 0) {
            double d0 = this.getX() + (this.lerpX - this.getX()) / (double)this.lerpSteps;
            double d1 = this.getY() + (this.lerpY - this.getY()) / (double)this.lerpSteps;
            double d2 = this.getZ() + (this.lerpZ - this.getZ()) / (double)this.lerpSteps;
            double d3 = Mth.wrapDegrees(this.lerpYRot - (double)this.getYRot());
            this.setYRot(this.getYRot() + (float)d3 / (float)this.lerpSteps);
            this.setXRot(this.getXRot() + (float)(this.lerpXRot - (double)this.getXRot()) / (float)this.lerpSteps);
            --this.lerpSteps;
            this.setPos(d0, d1, d2);
            this.setRot(this.getYRot(), this.getXRot());
        }
    }
    public void lerpTo(double pX, double pY, double pZ, float pYaw, float pPitch, int pPosRotationIncrements, boolean pTeleport) {
        this.lerpX = pX;
        this.lerpY = pY;
        this.lerpZ = pZ;
        this.lerpYRot = pYaw;
        this.lerpXRot = pPitch;
        this.lerpSteps = 3;
    }



    float currentRidingSpeed = 0.0f;
    private float deltaYRotation;
    private float deltaXRotation;
    private void controlBroom(){
        if (this.isVehicle()){

            float acceleration;
            if (ClientEvents.getClient().options.keyLeft.isDown()) deltaYRotation++;
            if (ClientEvents.getClient().options.keyRight.isDown()) deltaYRotation--;
            if (ClientEvents.getClient().options.keyJump.isDown()) deltaXRotation--; // TODO custom keys
            if (ClientEvents.getClient().options.keySprint.isDown()) deltaXRotation++;

            if (ClientEvents.getClient().options.keyUp.isDown())
            {
                acceleration = 0.01f;
                yRotO = getYRot();

            } else {
                acceleration = DECELERATION;
            }
            deltaYRotation *= 0.8;
            deltaXRotation *= 0.6;
            this.setYRot(getYRot() + deltaYRotation);
            this.setXRot(Mth.clamp(getXRot() + deltaXRotation, -50, 50));

            Vec3 lookVec = getForward().multiply(-1.0, 1.0, -1.0);
            currentRidingSpeed = Mth.clamp(currentRidingSpeed + acceleration, 0f, 0.5f);
            this.setDeltaMovement(lookVec.scale(currentRidingSpeed));
        }

        else {
            currentRidingSpeed = Mth.clamp(currentRidingSpeed + DECELERATION, 0f, 0.5f);
        }
    }

    @Nullable
    @Override
    public Entity getControllingPassenger() {
        if (getPassengers().isEmpty()) return null;
        return getPassengers().get(0);
    }

    @Override
    public Vec3 getDismountLocationForPassenger(LivingEntity pPassenger) {
        Vec3 vec = super.getDismountLocationForPassenger(pPassenger);
        if (pPassenger instanceof Player p){
            p.addItem(new ItemStack(ItemRegistry.BROOMSTICK.get()));
            kill();
        }
        return vec;
    }


}
