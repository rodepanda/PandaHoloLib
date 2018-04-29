# PandaHoloLib

Latest Build [![pipeline status](https://repo.rodepanda.net/spigot/PandaHoloLib/badges/master/pipeline.svg)](https://repo.rodepanda.net/spigot/PandaHoloLib/commits/master)

PandaHoloLib is a library which makes it easy to create interactive touchscreen menus for players.

#### Features

- Create interactive touch screens for your server.

- Lots of flexibility, use your own methods for touchscreen interactivity.

- Built-in support for [PlaceHolderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/) and [MVdWPlaceholderAPI](https://www.spigotmc.org/resources/mvdwplaceholderapi.11182/).

- Easy to use.

#### Dependencies

- Bukkit or Spigot 1.9 or higher.

- [ProtocolLib](https://www.spigotmc.org/resources/protocollib.1997/)

#### Maven repository
```xml
<repositories>
  <repository>
    <id>Rodepanda-repo</id>
    <url>https://nexus.rodepanda.net/repository/maven-public/</url>
  </repository>
</repositories>
```
```xml
<dependencies>
  <dependency>
    <groupId>net.rodepanda</groupId>
    <artifactId>PandaHoloLib</artifactId>
    <version>1.0</version>
  </dependency>
</dependencies>
```

#### Getting started

Creating a screen is very easy.
1. First you create a Page for all your buttons and text.

```java
Page page = new Page();
```

2. Next you add a component. In this example we'll put a ButtonComponent in the middle that says "CLICK!" when a player clicks on it.

```java
page.addComponent(new GuiButtonItemComponent(0.5, 0.5, true, "Click me", 0.3, Material.REDSTONE, p -> p.sendMessage("CLICK!") 0, true));
```

3. Finally we'll create the actual screen  by giving in the player the page the bottomleft corner in vector format, the top right corner in vector format and the update time in ticks.

```java
Screen s = new Screen(player, page, bottomleft, bottomright, 2);
```

4. Now the player can finally see his screen. Easy right?

To change the contents of a screen just create a new Page and put it in the new screen.

```java
Holo.getPlayerScreen(p).setSlide(newPage);
```

You don't need to delete a screen after use, you can just unload it's current slide.

```java
screen.unloadSlide();
```

Whenever a player logs out the screen will disable itself automatically.

#### Example code

For a full working example project you can take a look at [HoloExample](https://repo.rodepanda.net/spigot/HoloExample)