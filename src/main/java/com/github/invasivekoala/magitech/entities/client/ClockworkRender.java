package com.github.invasivekoala.magitech.entities.client;

import com.github.invasivekoala.magitech.entities.clockwork.ClockworkEntity;
import com.github.invasivekoala.magitech.entities.client.layer.ClockworkHeldItemLayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class ClockworkRender extends GeoEntityRenderer<ClockworkEntity> {
    public ClockworkRender(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ClockworkModel());
        this.addLayer(new ClockworkHeldItemLayer(this));
        this.shadowRadius = 0.5f;
    }


}
