package net.quantumknight.somethingmod;

import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(SomethingMod.MODID);

    // ITEM 1: The Spark Stick (Trident Animation + Charge Mechanic)
    public static final DeferredItem<Item> SPARK_STICK = ITEMS.register("spark_stick",
            () -> new Item(new Item.Properties().stacksTo(1)) {

                @Override
                public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
                    player.startUsingItem(hand);
                    return InteractionResultHolder.consume(player.getItemInHand(hand));
                }

                @Override
                public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
                    if (entity instanceof Player player && !level.isClientSide) {
                        int duration = this.getUseDuration(stack, entity) - timeLeft;

                        // Only strike if charged for at least 1 second (20 ticks)
                        if (duration >= 20) {
                            HitResult hitResult = player.pick(50.0D, 0.0F, false);
                            if (hitResult.getType() != HitResult.Type.MISS) {
                                Vec3 strikePos = hitResult.getLocation();

                                // Scale: More strikes based on charge time (Max 3)
                                int strikes = Math.min(3, duration / 20);

                                for (int i = 0; i < strikes; i++) {
                                    LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(level);
                                    if (lightning != null) {
                                        // Slight offset for multiple strikes
                                        lightning.moveTo(strikePos.x + (i * 0.5), strikePos.y, strikePos.z + (i * 0.5));
                                        level.addFreshEntity(lightning);
                                    }
                                }
                            }
                        }
                    }
                }

                @Override
                public UseAnim getUseAnimation(ItemStack stack) {
                    return UseAnim.SPEAR; // Switched to Trident throw animation
                }

                @Override
                public int getUseDuration(ItemStack stack, LivingEntity entity) {
                    return 72000;
                }
            });

    // ITEM 2: The Phase Blade (Breeze Dash)
    public static final DeferredItem<Item> PHASE_BLADE = ITEMS.register("phase_blade",
            () -> new Item(new Item.Properties().stacksTo(1)) {
                @Override
                public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
                    if (!level.isClientSide) {
                        // 1. Start Sound: Breeze Shooting/Wind Effect
                        // We use a slightly higher pitch (1.2F) to make it feel snappier
                        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                                SoundEvents.BREEZE_SHOOT, SoundSource.PLAYERS, 0.8F, 1.2F);

                        Vec3 lookAngle = player.getLookAngle();
                        double dashDistance = 5.0;

                        player.teleportTo(
                                player.getX() + lookAngle.x * dashDistance,
                                player.getY() + lookAngle.y * dashDistance,
                                player.getZ() + lookAngle.z * dashDistance
                        );

                        // 2. End Sound: Quiet Enderman Teleport
                        // Lower volume (0.4F) ensures it doesn't overpower the Breeze sound
                        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                                SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 0.4F, 1.2F);

                        player.getCooldowns().addCooldown(this, 40);
                    }
                    return InteractionResultHolder.success(player.getItemInHand(hand));
                }
            });
}