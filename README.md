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
| `/is create <optional islandId>`                      | Create an island plot in a spiral from spawn. Specify id to create plots beside friends |
| `/is delete`                      | Delete your island.                         |
| `/is home <optional islandId>`                        | Teleport user to owner or member island.        |
| `/is sethome`                    | Change the island home to current player position. |
| `/is setbiome <biome>`                        | Sets the biome of the island.        |
| `/is visitors <enabled/disabled>`         | Set whether island is open to visitors.  |
| `/is visit <Player>`         | Teleport to the specified player island.  |
| `/is invite add <Player>`    | Invite player to be island member. |
| `/is invite accept <Player>` | Accept the player invitation.             |
| `/is invite decline <Player>`   | Decline the player invitation.            |
