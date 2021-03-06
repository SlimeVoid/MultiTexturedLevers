package eurymachus.mtl.client.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.src.ModLoader;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.MinecraftForgeClient;
import slimevoid.lib.IPacketHandling;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import eurymachus.mtl.client.network.ClientPacketHandler;
import eurymachus.mtl.client.render.RenderMTLever;
import eurymachus.mtl.core.MTLCore;
import eurymachus.mtl.core.MTLInit;
import eurymachus.mtl.core.MTLItemLevers;
import eurymachus.mtl.proxy.CommonProxy;

public class ClientProxy extends CommonProxy {

	@Override
	public String getMinecraftDir() {
		return Minecraft.getMinecraftDir().toString();
	}

	@Override
	public void registerRenderInformation() {
		MinecraftForgeClient.preloadTexture(MTLInit.MTL.getBlockSheet());
		MTLCore.mtLeverBlockRenderID = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(
				MTLCore.mtLeverBlockRenderID,
				new RenderMTLever());
	}

	@Override
	public void registerTileEntitySpecialRenderer(Class<? extends TileEntity> clazz) {
	}

	@Override
	public void displayTileEntityGui(EntityPlayer entityplayer, TileEntity tileentity) {
	}

	@SideOnly(Side.CLIENT)
	private static Minecraft mc = ModLoader.getMinecraftInstance();

	@Override
	public int getMouseOver() {
		if (mc.objectMouseOver != null) {
			int xPosition = mc.objectMouseOver.blockX;
			int yPosition = mc.objectMouseOver.blockY;
			int zPosition = mc.objectMouseOver.blockZ;
			return MTLInit.getDamageValue(
					mc.theWorld,
					xPosition,
					yPosition,
					zPosition);
		}
		return 0;
	}

	@Override
	public int getBelowPlayer(EntityPlayer player) {
		int playerX = (int) player.posX;
		int playerY = (int) player.posY;
		int playerZ = (int) player.posZ;
		return MTLInit.getDamageValue(
				mc.theWorld,
				playerX,
				playerY - 1,
				playerZ);
	}

	@Override
	public int getAtPlayer(EntityPlayer player) {
		int playerX = (int) player.posX;
		int playerY = (int) player.posY;
		int playerZ = (int) player.posZ;
		return MTLInit.getDamageValue(mc.theWorld, playerX, playerY, playerZ);
	}

	@Override
	public int getBlockTextureFromSideAndMetadata(int side, int meta) {
		if (side == 1000) {
			return MTLItemLevers.getTexture(meta);
		}
		return getBlockTextureFromMetadata(meta);
	}

	@Override
	public int getBlockTextureFromMetadata(int meta) {
		int texture = -1;
		EntityPlayer player = mc.thePlayer;
		if (player.onGround) {
			texture = getMouseOver();
		}
		if (texture == -1 && player.isAirBorne) {
			texture = getMouseOver();
		}
		if (texture == -1 && player.isAirBorne) {
			texture = getBelowPlayer(player);
		}
		if (texture == -1 && player.isAirBorne) {
			texture = getAtPlayer(player);
		}
		texture = MTLItemLevers.getTexture(texture);
		if (texture == -1)
			texture = 22;
		return texture;
	}

	@Override
	public IPacketHandling getPacketHandler() {
		return new ClientPacketHandler();
	}
}
