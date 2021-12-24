package adris.altoclef.commands;


import adris.altoclef.AltoClef;
import adris.altoclef.Debug;
import adris.altoclef.Playground;
import adris.altoclef.TaskCatalogue;
import adris.altoclef.commandsystem.*;
import adris.altoclef.tasks.SchematicBuildTask;
import adris.altoclef.tasks.chest.FillTargetChestTask;
import adris.altoclef.tasksystem.Task;
import adris.altoclef.trackers.ContainerTracker;
import adris.altoclef.ui.MessagePriority;
import adris.altoclef.util.ItemTarget;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

public class TrackChestCommand extends Command {

    //hehehe... chat length is 256 so more commands than that cannot be passed anyway.
    private static ArgBase[] genArgBase() throws CommandException {
        final ArgBase[] args = new Arg[256];
        for (int i = 0; i < args.length; i++) {
            args[i] = new Arg(String.class, "arg" + i, "", i);
        }

        return args;
    }

    public TrackChestCommand() throws CommandException {
        super("track", "Tracks the chest you are looking at. Usage: @trackchest", genArgBase());
    }

    @Override
    protected void call(AltoClef mod, ArgParser parser) throws CommandException {

	ContainerTracker containerTracker = mod.getContainerTracker();
	HitResult r = MinecraftClient.getInstance().crosshairTarget;
	if (r != null && r.getType() == net.minecraft.util.hit.HitResult.Type.BLOCK) {
	    BlockHitResult blockHitResult = (BlockHitResult)r;
            final BlockPos blockPos = blockHitResult.getBlockPos();
            if (!(MinecraftClient.getInstance().world.getBlockState(blockPos).getBlock() instanceof ChestBlock)) {
		mod.log("You need to look at a chest.", MessagePriority.OPTIONAL);
                finish();
		return;
            }
	    //containerTracker.onBlockInteract(blockPos, Blocks.CHEST);
	
	}
	mod.log("Added chest to tracker", MessagePriority.OPTIONAL);
	
    }
}
