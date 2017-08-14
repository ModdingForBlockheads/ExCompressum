package net.blay09.mods.excompressum.container;

import net.blay09.mods.excompressum.tile.TileAutoSieveBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerAutoSieve extends Container {

    private final TileAutoSieveBase tileEntity;

    private float lastProgress;
    private int lastEnergy;

    public ContainerAutoSieve(InventoryPlayer inventoryPlayer, TileAutoSieveBase tileEntity) {
        this.tileEntity = tileEntity;

        ItemStackHandler itemHandler = tileEntity.getItemHandler();

        addSlotToContainer(new SlotItemHandler(itemHandler, 0, 8, 22));
        addSlotToContainer(new SlotItemHandler(itemHandler, 21, 8, 49));

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                addSlotToContainer(new SlotOutput(itemHandler, 1 + (i * 5) + j, 57 + (j * 18), 8 + (i * 18)));
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
        }
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        if(lastProgress != tileEntity.getProgress() || lastEnergy != tileEntity.getEnergyStored()) {
            for (IContainerListener listener : listeners) {
                listener.sendWindowProperty(this, 0, (int) (100 * tileEntity.getProgress()));
                listener.sendWindowProperty(this, 1, tileEntity.getEnergyStored());
            }
        }
        lastProgress = tileEntity.getProgress();
        lastEnergy = tileEntity.getEnergyStored();
    }

    @Override
    public void updateProgressBar(int var, int val) {
        switch(var) {
            case 0: tileEntity.setProgress((float) val / 100f);
            case 1: tileEntity.setEnergyStored(val);
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return tileEntity.isUseableByPlayer(entityPlayer);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer entityPlayer, int slotNumber) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(slotNumber);
        if(slot != null && slot.getHasStack()) {
            ItemStack slotStack = slot.getStack();
            itemStack = slotStack.copy();
            if (slotNumber <= 21) {
                if (!mergeItemStack(slotStack, 21, 57, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (tileEntity.getItemHandler().isItemValid(0, slotStack)) {
                if (!mergeItemStack(slotStack, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (tileEntity.getItemHandler().isItemValid(21, slotStack)) {
                if (!mergeItemStack(slotStack, 1, 2, false)) {
                    return ItemStack.EMPTY;
                }
            }
            if (slotStack.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
            if (slotStack.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(entityPlayer, slotStack);
        }
        return itemStack;
    }
}
