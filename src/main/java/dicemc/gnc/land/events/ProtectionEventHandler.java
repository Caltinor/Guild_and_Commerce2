package dicemc.gnc.land.events;

import java.util.UUID;

import dicemc.gnc.land.WorldWSD;
import dicemc.gnc.setup.Registration;
import dicemc.gnclib.protection.LogicProtection;
import dicemc.gnclib.realestate.ChunkData;
import dicemc.gnclib.realestate.items.IWhitelister;
import dicemc.gnclib.util.ChunkPos3D;
import dicemc.gnclib.util.ComVars;
import dicemc.gnclib.util.ResultType;
import dicemc.gnclib.util.TranslatableResult;
import net.minecraft.block.BlockState;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.common.util.Constants.BlockFlags;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.LeftClickBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.EntityPlaceEvent;
import net.minecraftforge.event.world.BlockEvent.FarmlandTrampleEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ProtectionEventHandler extends LogicProtection{
	@SubscribeEvent
	public static void onExplosion (ExplosionEvent.Detonate event) {
		int bottom = 0; //TODO 1.17 updated with actual dimension floor level;
		for (int i = event.getAffectedBlocks().size()-1; i > 0; i--) {
			ChunkPos3D pos = new ChunkPos3D(event.getAffectedBlocks().get(i).getX(), ChunkPos3D.chunkYFromAltitude(event.getAffectedBlocks().get(i).getY(), bottom), event.getAffectedBlocks().get(i).getZ());
			ChunkData cap = WorldWSD.get((ServerWorld) event.getWorld()).getOrCreate(pos);
			if (!cap.owner.refID.equals(ComVars.NIL) && !cap.canExplode) event.getAffectedBlocks().remove(i);
		}
		for (int i = event.getAffectedEntities().size()-1; i > 0; i--) {
			//TODO 1.17 check this to see if the logic adapted as expected to include the world floor as appropriate.
			ChunkPos3D pos = new ChunkPos3D(event.getAffectedEntities().get(i).xChunk, event.getAffectedEntities().get(i).yChunk, event.getAffectedEntities().get(i).zChunk);
			ChunkData cap = WorldWSD.get((ServerWorld) event.getWorld()).getCap().get(pos);
			if (!cap.owner.refID.equals(ComVars.NIL) && !cap.canExplode) event.getAffectedEntities().remove(i);
		}		
	}
	
	@SubscribeEvent
	public static void onBlockBreak(BreakEvent event) {
		if (event.getPlayer().isCreative()) return;
		ChunkPos3D pos = chunkPosFromBlock(event.getPos(), (World) event.getWorld(), 0); //TODO 1.17 update dimension bottom
		ChunkData data = WorldWSD.get((ServerWorld) event.getWorld()).getOrCreate(pos);
		TranslatableResult<ResultType> result = onBlockBreakLogic(data, event.getPlayer().getMainHandItem().getItem().getRegistryName().toString(), event.getPlayer().getUUID());
		TranslationTextComponent response = new TranslationTextComponent(result.translationKey);
		event.getPlayer().displayClientMessage(response, true);
		if (result.result.equals(ResultType.FAILURE)) event.setCanceled(true);
	}
	
	@SubscribeEvent
	public static void onBlockLeftClick(LeftClickBlock event) {
		ChunkPos3D pos = chunkPosFromBlock(event.getPos(), (World) event.getWorld(), 0); //TODO 1.17 update dimension bottom
		ChunkData data = WorldWSD.get((ServerWorld) event.getWorld()).getOrCreate(pos);
		ItemStack stack = event.getPlayer().getMainHandItem();
		String itemRL = stack.getItem().getRegistryName().toString();
		WhitelisterType type = getTypeFromItem(event.getItemStack());
		TranslatableResult<ResultType> result = onBlockLeftClickLogic(data, event.getPlayer().getUUID(), itemRL, type, confirmWhitelister(stack.getItem()), stack, true, event.getPlayer().isCrouching());
		TranslationTextComponent response = new TranslationTextComponent(result.translationKey);
		event.getPlayer().displayClientMessage(response, true);
		if (result.result.equals(ResultType.FAILURE)) event.setCanceled(true);
	}
	
	@SuppressWarnings("resource")
	@SubscribeEvent
	public static void onBlockRightClick(RightClickBlock event) {
		if (!event.getWorld().isClientSide) {
			ChunkPos3D pos = chunkPosFromBlock(event.getPos(), (World) event.getWorld(), 0); //TODO 1.17 update dimension bottom
			ChunkData data = WorldWSD.get((ServerWorld) event.getWorld()).getOrCreate(pos);
			ItemStack stack = event.getPlayer().getMainHandItem();
			String itemRL = stack.getItem().getRegistryName().toString();
			WhitelisterType type = getTypeFromItem(event.getItemStack());
			TranslatableResult<ResultType> result = onBlockRightClickLogic(data, event.getPlayer().getUUID(), itemRL, type, confirmWhitelister(stack.getItem()), stack, true, event.getPlayer().isCrouching());
			TranslationTextComponent response = new TranslationTextComponent(result.translationKey);
			event.getPlayer().displayClientMessage(response, true);
			if (result.result.equals(ResultType.FAILURE)) {
				event.setCanceled(true);
				BlockState state = event.getWorld().getBlockState(event.getPos());
				event.getWorld().sendBlockUpdated(event.getPos(), state, state, BlockFlags.DEFAULT);
			}
		}
	}
	
	@SubscribeEvent
	public static void onBlockPlace(EntityPlaceEvent event) {
		if (event.getEntity() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) event.getEntity();
			if (player.isCreative()) return;
 			ChunkPos3D pos = chunkPosFromBlock(event.getPos(), (World) event.getWorld(), 0); //TODO 1.17 update dimension bottom
			ChunkData data = WorldWSD.get((ServerWorld) event.getWorld()).getOrCreate(pos);
			TranslatableResult<ResultType> result = onBlockPlaceLogic(data, player.getUUID(), event.getPlacedBlock().getBlock().getRegistryName().toString());
			TranslationTextComponent response = new TranslationTextComponent(result.translationKey);
			player.displayClientMessage(response, true);
			if (result.result.equals(ResultType.PACKET)) {/*TODO send packet to client for autoclaim action*/}
			if (result.result.equals(ResultType.FAILURE)) event.setCanceled(true);
		}
		else {
			/*TODO figure out how I want to handle non-entity placement
			 * This will likely impact how machines are able to place.
			 * I expect there will need to be some soft compat for mods
			 * that provide owner UUIDs via NBT.  This should also factor
			 * in entities such as enderman and minecolonies workers.
			 */
		}
	}
	
	@SuppressWarnings("resource")
	@SubscribeEvent
	public static void onEntityInteract(EntityInteract event) {
		if (!event.getWorld().isClientSide) {
			PlayerEntity player = event.getPlayer();
			if (player.isCreative()) return;
			ChunkPos3D pos = chunkPosFromBlock(event.getPos(), (World) event.getWorld(), 0); //TODO 1.17 update dimension bottom
			ChunkData data = WorldWSD.get((ServerWorld) event.getWorld()).getOrCreate(pos);
			ItemStack stack = event.getPlayer().getMainHandItem();
			String itemRL = stack.getItem().getRegistryName().toString();
			WhitelisterType type = getTypeFromItem(event.getItemStack());
			TranslatableResult<ResultType> result = onEntityInteractLogic(data, player.getUUID(), itemRL, type, confirmWhitelister(stack.getItem()), stack, false, player.isCrouching());
			TranslationTextComponent response = new TranslationTextComponent(result.translationKey);
			event.getPlayer().displayClientMessage(response, true);
			if (result.result.equals(ResultType.FAILURE)) {
				event.setCanceled(true);
				BlockState state = event.getWorld().getBlockState(event.getPos());
				event.getWorld().sendBlockUpdated(event.getPos(), state, state, BlockFlags.DEFAULT);
			}
		}
	}
	
	@SuppressWarnings("resource")
	@SubscribeEvent
	public static void  onEntityAttack(AttackEntityEvent event) {
		PlayerEntity player = event.getPlayer();
		if (player.isCreative()) return;
		ChunkPos3D pos = chunkPosFromBlock(event.getTarget().blockPosition(), (World) event.getEntity().getCommandSenderWorld(), 0); //TODO 1.17 update dimension bottom
		ChunkData data = WorldWSD.get((ServerWorld) event.getEntity().getCommandSenderWorld()).getOrCreate(pos);
		ItemStack stack = event.getPlayer().getMainHandItem();
		String itemRL = stack.getItem().getRegistryName().toString();
		WhitelisterType type = getTypeFromItem(event.getPlayer().getMainHandItem());
		TranslatableResult<ResultType> result = null;
		if (!event.getEntity().getCommandSenderWorld().isClientSide) {
			result = isWhitelisterAction(data, player.getUUID(), itemRL, type, confirmWhitelister(stack.getItem()), stack, false, player.isCrouching());
			TranslationTextComponent response = new TranslationTextComponent(result.translationKey);
			player.displayClientMessage(response, true);
			if (result.result.equals(ResultType.SUCCESS)) {
				event.setCanceled(true);
				return;
			}			
		}
		result = onEntityHarmLogic(data, player.getUUID(), event.getTarget().getEncodeId());
		TranslationTextComponent response = new TranslationTextComponent(result.translationKey);
		player.displayClientMessage(response, true);
		if (result.result.equals(ResultType.FAILURE)) event.setCanceled(true);
	}
	
	@SuppressWarnings("resource")
	@SubscribeEvent
	public static void onEntityDamage(LivingDamageEvent event ) {
		if (!(event.getEntityLiving() instanceof MobEntity) && !(event.getEntityLiving() instanceof PlayerEntity) && (event.getSource().getEntity() instanceof PlayerEntity))	{
			PlayerEntity player = (PlayerEntity) event.getSource().getEntity();
			if (player.isCreative()) return;
			ChunkPos3D pos = chunkPosFromBlock(event.getEntity().blockPosition(), (World) event.getEntity().getCommandSenderWorld(), 0); //TODO 1.17 update dimension bottom
			ChunkData data = WorldWSD.get((ServerWorld) event.getEntity().getCommandSenderWorld()).getOrCreate(pos);
			ItemStack stack = player.getMainHandItem();
			String itemRL = stack.getItem().getRegistryName().toString();
			WhitelisterType type = getTypeFromItem(player.getMainHandItem());
			TranslatableResult<ResultType> result = null;
			if (!event.getEntity().getCommandSenderWorld().isClientSide) {
				result = isWhitelisterAction(data, player.getUUID(), itemRL, type, confirmWhitelister(stack.getItem()), stack, false, player.isCrouching());
				TranslationTextComponent response = new TranslationTextComponent(result.translationKey);
				player.displayClientMessage(response, true);
				if (result.result.equals(ResultType.SUCCESS)) {
					event.setCanceled(true);
					return;
				}			
			}
			result = onEntityHarmLogic(data, player.getUUID(), event.getEntity().getEncodeId());
			TranslationTextComponent response = new TranslationTextComponent(result.translationKey);
			player.displayClientMessage(response, true);
			if (result.result.equals(ResultType.FAILURE)) event.setCanceled(true);
		}
	}
	
	@SubscribeEvent
	public static void onTrample(FarmlandTrampleEvent event) {
		ChunkPos3D pos = chunkPosFromBlock(event.getPos(), (World) event.getWorld(), 0); //TODO 1.17 update dimension bottom
		ChunkData data = WorldWSD.get((ServerWorld) event.getWorld()).getOrCreate(pos);
		String item = event.getState().getBlock().getRegistryName().toString();
		UUID player = event.getEntity().getUUID();
		TranslatableResult<ResultType> result = onTrampleLogic(data, player, item);
		TranslationTextComponent response = new TranslationTextComponent(result.translationKey);
		if (event.getEntity() instanceof PlayerEntity) ((PlayerEntity)event.getEntity()).displayClientMessage(response, true);
		if (result.result.equals(ResultType.FAILURE)) event.setCanceled(true);
	}
	
	@SubscribeEvent
	public static void onBucketUse(FillBucketEvent event) {
		PlayerEntity player = (PlayerEntity) event.getPlayer();
		if (event.getTarget() == null || player.isCreative()) return;
		BlockPos bpos = new BlockPos(event.getTarget().getLocation().x, event.getTarget().getLocation().y, event.getTarget().getLocation().z); 		
		ChunkPos3D pos = chunkPosFromBlock(bpos, (World) event.getWorld(), 0); //TODO 1.17 update dimension bottom
		ChunkData data = WorldWSD.get((ServerWorld) event.getWorld()).getOrCreate(pos);
		TranslatableResult<ResultType> result = onBucketUseLogic(data, player.getUUID(), event.getWorld().getBlockState(bpos).getBlock().getRegistryName().toString());
		TranslationTextComponent response = new TranslationTextComponent(result.translationKey);
		player.displayClientMessage(response, true);
		if (result.result.equals(ResultType.PACKET)) {/*TODO send packet to client for autoclaim action*/}
		if (result.result.equals(ResultType.FAILURE)) event.setCanceled(true);
	}
	
	@SubscribeEvent
	public static void onBonmealUse(BonemealEvent event) {
		PlayerEntity player = (PlayerEntity) event.getPlayer();
		if (player.isCreative()) return;	
		ChunkPos3D pos = chunkPosFromBlock(event.getPos(), (World) event.getWorld(), 0); //TODO 1.17 update dimension bottom
		ChunkData data = WorldWSD.get((ServerWorld) event.getWorld()).getOrCreate(pos);
		TranslatableResult<ResultType> result = onBonemealUseLogic(data, player.getUUID(), event.getBlock().getBlock().getRegistryName().toString());
		TranslationTextComponent response = new TranslationTextComponent(result.translationKey);
		player.displayClientMessage(response, true);
		if (result.result.equals(ResultType.PACKET)) {/*TODO send packet to client for autoclaim action*/}
		if (result.result.equals(ResultType.FAILURE)) event.setCanceled(true);
	}
	
	//================ HELPER METHODS =========================================================
	private static ChunkPos3D chunkPosFromBlock(BlockPos pos, World world, int dimBottom) {
		Chunk ck = world.getChunkAt(pos);
		int y = ChunkPos3D.chunkYFromAltitude(pos.getY(), dimBottom);
		return new ChunkPos3D(ck.getPos().x, y, ck.getPos().z);
	}
	
	private static WhitelisterType getTypeFromItem(ItemStack stack) {
		if (stack.getItem() instanceof IWhitelister) {
			if (stack.getItem().equals(Registration.WHITELISTER.get())) return WhitelisterType.DEFAULT;
			else if (stack.getItem().equals(Registration.WHITELIST_STICK_GREEN.get())) return WhitelisterType.GREEN_STICK;
			else if (stack.getItem().equals(Registration.WHITELIST_STICK_RED.get())) return WhitelisterType.RED_STICK;
		}			
		return WhitelisterType.NOT_WHITELISTER;		
	}
	
	private static IWhitelister confirmWhitelister(Item item) {
		if (item instanceof IWhitelister) return (IWhitelister) item;
		else return null;
	}
}
