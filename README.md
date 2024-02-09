# Simple SkyBlock

A simple SkyBlock plugin for Spigot servers without any useless features, just the necessary.

![screenshot](https://zupimages.net/up/21/27/c38w.png)

## Version

This plugin was originally made for `Spigot 1.19` but you can totally edit it to use it with others Minecraft versions.

## Installation

1. Compile the project with `mvn compile && mvn package` (Assuming Maven is installed)
2. Place the plugin into your server plugin's directory.
3. Restart your server or reload it with the command `/reload`.
4. You can now configure the plugin in the `config.yml` file.

## Dependencies

1. Requires a MySql server setup with a schema for `/skyrama`
2. Requires WorldEdit >=7.2.13
3. Requires WorldGuard >=7.0.8

## Commands

| Command                           | Description                               |
| --------------------------------- | ----------------------------------------- |
| `/is` or `/island`                | Show the help menu with all the commands. |
| `/is create`                      | Create an island.                         |
| `/is home`                        | Teleport user to his island spawn.        |
| `/is setspawn`                    | Change the island spawn to user position. |
| `/is visit <Player Name>`         | Teleport to the specified player island.  |
| `/is invite add <Player Name>`    | Invite the player to play on your island. |
| `/is invite accept <Player Name>` | Accept the player invitation.             |
| `/is invite deny <Player Name>`   | Decline the player invitation.            |

## Permissions

| Permissions                | Description                                  |
| -------------------------- | -------------------------------------------- |
| `skyrama.command.invite`   | Invite players / accept or deny invitations. |
| `skyrama.command.create`   | Create an Island.                            |
| `skyrama.command.home`     | Teleport user to his island spawn.           |
| `skyrama.command.visit`    | Teleport to the specified player island.     |
| `skyrama.command.setspawn` | Change the island spawn to user position.    |
| `skyrama.event.break`      | Break Blocks on a other Island without Trust |
| `skyrama.event.place`      | Place Blocks on a other Island without Trust |
