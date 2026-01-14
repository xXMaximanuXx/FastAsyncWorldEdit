package com.fastasyncworldedit.bukkit.util.image;

import com.fastasyncworldedit.core.util.image.Drawable;
import com.fastasyncworldedit.core.util.image.ImageViewer;
<<<<<<< HEAD
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Rotation;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
=======
>>>>>>> main
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;

import java.io.IOException;

/**
 * @deprecated for removal with no replacement. Out of scope for FAWE.
 */
@Deprecated(forRemoval = true, since = "2.13.1")
public class BukkitImageViewer implements ImageViewer {

    public BukkitImageViewer(Player player) {
        throw new UnsupportedOperationException("No longer supported.");
    }

    public void selectFrame(ItemFrame start) {
        throw new UnsupportedOperationException("No longer supported.");
    }

    public ItemFrame[][] getItemFrames() {
        throw new UnsupportedOperationException("No longer supported.");
    }

    @Override
    public void view(Drawable drawable) {
<<<<<<< HEAD
        view(null, drawable);
    }

    private void view(@Nullable BufferedImage image, @Nullable Drawable drawable) {
        if (image == null && drawable == null) {
            throw new IllegalArgumentException("An image or drawable must be provided. Both cannot be null");
        }
        boolean initializing = last == null;

        if (this.frames != null) {
            if (image == null && drawable != null) {
                image = drawable.draw();
            }
            last = image;
            int width = frames.length;
            int height = frames[0].length;
            BufferedImage scaled = ImageUtil.getScaledInstance(
                    image,
                    128 * width,
                    128 * height,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR,
                    false
            );
            MapWrapper mapWrapper = mapManager.wrapMultiImage(scaled, width, height);
            MultiMapController controller = (MultiMapController) mapWrapper.getController();
            controller.addViewer(player);
            controller.sendContent(player);
            controller.showInFrames(player, frames, true);
        } else {
            int slot = getMapSlot(player);
            TaskManager.taskManager().syncWith(() -> {
                if (slot == -1) {
                    if (initializing) {
                        player.getInventory().setItemInMainHand(new ItemStack(Material.MAP));
                    } else {
                        return null;
                    }
                } else if (player.getInventory().getHeldItemSlot() != slot) {
                    player.getInventory().setHeldItemSlot(slot);
                }
                return null;
            }, BukkitAdapter.adapt(player));
            if (image == null && drawable != null) {
                image = drawable.draw();
            }
            last = image;
            BufferedImage scaled = ImageUtil.getScaledInstance(
                    image,
                    128,
                    128,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR,
                    false
            );
            MapWrapper mapWrapper = mapManager.wrapImage(scaled);
            MapController controller = mapWrapper.getController();
            controller.addViewer(player);
            controller.sendContent(player);
            controller.showInHand(player, true);
        }
    }

    private int getMapSlot(Player player) {
        PlayerInventory inventory = player.getInventory();
        for (int i = 0; i < 9; i++) {
            ItemStack item = inventory.getItem(i);
            if (item != null && item.getType() == Material.MAP) {
                return i;
            }
        }
        return -1;
=======
        throw new UnsupportedOperationException("No longer supported.");
>>>>>>> main
    }

    public void refresh() {
        throw new UnsupportedOperationException("No longer supported.");
    }

    @Override
    public void close() throws IOException {
        throw new UnsupportedOperationException("No longer supported.");
    }

}
