package net.impactdev.gts.bungee.util;

import net.kyori.text.TextComponent;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

public class PlayerDetailFetcher {

    public TextComponent getPlayerNameFromUUID(UUID user) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(user);
        return TextComponent.of(player.getDisplayName());
    }

}
