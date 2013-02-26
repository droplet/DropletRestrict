/*
 * This file is part of DropletRestrict.
 *
 * Copyright (c) 2013 Spout LLC <http://www.spout.org/>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.spout.droplet.restrict;

import java.util.logging.Level;

import org.spout.api.entity.Player;
import org.spout.api.event.EventHandler;
import org.spout.api.event.Listener;
import org.spout.api.event.player.PlayerInteractEvent;
import org.spout.api.material.Material;
import org.spout.api.plugin.CommonPlugin;

public class DropletRestrict extends CommonPlugin implements Listener {
	@Override
	public void onEnable() {
		getEngine().getEventManager().registerEvents(this, this);
		getLogger().log(Level.INFO, "DropletRestrict enabled.");
	}

	@Override
	public void onDisable() {
		getLogger().log(Level.INFO, "DropletRestrict disabled.");
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		// Save local references so they are easier to type out
		Player player = event.getPlayer();
		Material material = event.getHeldItem().getMaterial();

		// If the player has been given explicit permission to use all items, let them
		if (player.hasPermission("itemrestriction.allowall")) {
			return;
		}

		// If the player has been given explicit permission NOT to use all items, stop them
		if (player.hasPermission("itemrestriction.blockall")) {
			player.sendMessage("You aren't allowed to use any items.");
			event.setCancelled(true);
			return;
		}

		// If the player is allowed to use this specific item, let them
		if (player.hasPermission("itemrestriction.allow." + material.getName()) || player.hasPermission("itemrestriction.allowdisplayname." + material.getDisplayName())) {
			return;
		}

		// If the player is NOT allowed to use this specific item, stop them
		if (player.hasPermission("itemrestriction.block." + material.getName()) || player.hasPermission("itemrestriction.blockdisplayname." + material.getDisplayName())) {
			player.sendMessage("You aren't allowed to use any " + material.getDisplayName() + ".");
			event.setCancelled(true);
		}
	}
}
