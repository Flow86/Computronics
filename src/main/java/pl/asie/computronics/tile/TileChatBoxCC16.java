package pl.asie.computronics.tile;

import java.util.HashSet;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.ServerChatEvent;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;

public class TileChatBoxCC16 extends TileChatBoxBase implements IPeripheral {
	// ComputerCraft API

	@Override
	public String getType() {
		return "chat_box";
	}

	@Override
	public String[] getMethodNames() {
		return new String[]{"say", "getDistance", "setDistance"};
	}

	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext context,
			int method, Object[] arguments) throws Exception {
		if(method == 0) {
			if(arguments.length >= 1 && arguments[0] instanceof String) {
				this.sendChatMessage((String)arguments[0]);
			}
		} else if(method == 1) {
			return new Object[]{distance};
		} else if(method == 2) {
			if(arguments.length >= 1 && arguments[0] instanceof Integer)
				this.setDistance(((Integer)arguments[0]).intValue());
		}
		return null;
	}
	
	private final HashSet<IComputerAccess> ccComputers = new HashSet<IComputerAccess>();

	@Override
	public void attach(IComputerAccess computer) {
		ccComputers.add(computer);
	}

	@Override
	public void detach(IComputerAccess computer) {
		ccComputers.remove(computer);
	}

	@Override
	public void receiveChatMessageCC(ServerChatEvent event) {
		// Send CC event
		for(IComputerAccess computer: ccComputers) {
			computer.queueEvent("chat_message", new Object[]{event.username, event.message.replace("\u00a7", "&")});
		}
	}

	@Override
	public boolean equals(IPeripheral other) {
		if(!(other instanceof TileChatBoxCC16)) return false;
		TileEntity te = (TileEntity)other;
		return (te.worldObj.equals(this.worldObj) && te.xCoord == this.xCoord && te.yCoord == this.yCoord && te.zCoord == this.zCoord);
	}
}