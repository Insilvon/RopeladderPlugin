package me.aetheria.insilvon.ropeladder;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Directional;
import org.bukkit.material.Ladder;
import org.bukkit.plugin.java.JavaPlugin;

public final class Ropeladder extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Plugin startup logic
        System.out.println("[Ropeladder] We are now live!");
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        System.out.println("[Ropeladder] Shutting down!");
    }

    @EventHandler
    public void onLadderBreak(BlockBreakEvent e){
        Player player = e.getPlayer();
        Block b = e.getBlock();
        Location placed = b.getLocation();
        int pos = placed.getBlockY()-1;
        if (b.getType() == Material.LADDER && player.isSneaking()) {
            while (pos > 0) {
                Location temp = new Location(placed.getWorld(), placed.getBlockX(), pos, placed.getBlockZ());
                if (temp.getBlock().getType() != Material.LADDER) break;
                Block currentBlock = temp.getBlock();
                currentBlock.setType(Material.AIR);
//            player.getInventory().addItem(new ItemStack(Material.LADDER));
                pos--;
            }
        }
//        player.getInventory().addItem(new ItemStack(Material.LADDER));
    }
    @EventHandler
    public void onLadderPlace(BlockPlaceEvent e){
//        System.out.println("[Ropeladder] You placed a block!");
        Player player = e.getPlayer(); //Get player
        Block b = e.getBlock(); //Get Block they placed
        Material m = b.getType(); //Get the material they placed
        ItemStack itemHeld = player.getInventory().getItemInMainHand();
        ItemMeta held = player.getInventory().getItemInMainHand().getItemMeta();

        if (m==Material.LADDER&&held.getDisplayName()!=null&&held.getDisplayName().equals("Ropeladder") && held.getLore()!=null&&held.getLore().contains("*~*")){

            Location placed = b.getLocation();
            int pos = placed.getBlockY()-1;

            Ladder myLadder = (Ladder) b.getState().getData();
            BlockFace blockFace = myLadder.getAttachedFace();
            System.out.println(blockFace);


//            System.out.println("Entering While loop");
            while (player.getInventory().contains(itemHeld)&&pos>1) {
//                System.out.println("Doing things!");
                Location temp = new Location(placed.getWorld(), placed.getBlockX(), pos, placed.getBlockZ());
                if (temp.getBlock().getType() != Material.AIR) {
//                    System.out.println("It's not air! Stop! Stop!");
                    break;
                }
                else {
//                    System.out.println("Setting block!");

                    Block currentBlock = temp.getBlock();
                    currentBlock.setType(Material.LADDER);

                    BlockState state = currentBlock.getState();
                    Ladder currentLadder = (Ladder) currentBlock.getState().getData();
                    currentLadder.setFacingDirection(blockFace);
                    state.setData(currentLadder);
                    state.update();
                }
                pos--;
                if (itemHeld.getAmount()>1) itemHeld.setAmount(itemHeld.getAmount()-1);
                else {
                    player.getInventory().clear(player.getInventory().getHeldItemSlot());
                }
            }

        }
        else {
//            System.out.println("Not the right item...");
        }
    }
}
