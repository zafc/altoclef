package adris.altoclef.tasks.chest;

import adris.altoclef.AltoClef;
import adris.altoclef.Debug;
import adris.altoclef.tasks.slot.ClickSlotTask;
import adris.altoclef.tasks.slot.EnsureFreeInventorySlotTask;
import adris.altoclef.tasksystem.Task;
import adris.altoclef.trackers.storage.ContainerCache;
import adris.altoclef.trackers.storage.ContainerSubTracker;
import adris.altoclef.trackers.storage.ItemStorageTracker;
import adris.altoclef.util.ItemTarget;
import adris.altoclef.util.csharpisbetter.TimerGame;
import adris.altoclef.util.slots.Slot;
import net.minecraft.item.Item;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.math.BlockPos;

import java.util.Arrays;
import java.util.List;

public class PickupFromChestTask extends AbstractDoInChestTask {

    private final ItemTarget[] _targets;
    private final TimerGame _actionTimer = new TimerGame(0);

    private final BlockPos _targetChest;

    public PickupFromChestTask(BlockPos targetChest, ItemTarget... targets) {
        super(targetChest);
        _targets = targets;
        _targetChest = targetChest;
    }

    @Override
    protected Task doToOpenChestTask(AltoClef mod, GenericContainerScreenHandler handler) {
        _actionTimer.setInterval(mod.getModSettings().getContainerItemMoveDelay());
        if (_actionTimer.elapsed()) {
            _actionTimer.reset();

            List<ContainerCache> data = mod.getContainerSubTracker().getCachedContainers();
            if (data == null) {
                Debug.logWarning("Failed to find valid chest at " + _targetChest + ", hopefully this is handled up the chain!!!");
                return null;
            }
            for (ItemTarget target : _targets) {
                if (!mod.getItemStorage().targetsMet(target)) {
                    for (Item mightMove : target.getMatches()) {
                        // Pick up all items that might fit our criteria.
                        if (data.hasItem(mightMove)) {
                            if (!mod.getItemStorage().hasEmptyInventorySlot()) {
                                return new EnsureFreeInventorySlotTask();
                            }
                            //int maxMove = target.getTargetCount() - mod.getInventoryTracker().getItemCount(target);
                            Slot itemSlot = data.getItemSlotsWithItem(mightMove).get(0);
                            return new ClickSlotTask(itemSlot, SlotActionType.QUICK_MOVE);
                            //mod.getInventoryTracker().grabItem(itemSlot);
                            //return new QuickGrabSlotTask(new ItemTarget(mightMove, maxMove));
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    protected boolean isSubEqual(AbstractDoInChestTask other) {
        if (other instanceof PickupFromChestTask task) {
            return Arrays.equals(task._targets, _targets);
        }
        return false;
    }

    @Override
    protected String toDebugString() {
        return "Picking up from chest: " + Arrays.toString(_targets);
    }
}
