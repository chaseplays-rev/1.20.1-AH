package net.chase.ah.Events;

import net.chase.ah.Data.ClientItemIconTooltipComponent;
import net.chase.ah.Data.ItemIconTooltipComponent;
import net.chase.ah.Main;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Main.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientModEvents {

    @SubscribeEvent
    public static void onRegisterClientTooltip(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(
                ItemIconTooltipComponent.class,
                ClientItemIconTooltipComponent::new
        );
    }
}