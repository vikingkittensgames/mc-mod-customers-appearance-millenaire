# Customers Millénaire Appearance

Customers Millénaire Appearance is an optional NeoForge add-on for
[Customers](https://www.curseforge.com/minecraft/mc-mods/customers). It adds a
`Millénaire - Local Culture` appearance that makes spawned Customers and
Suppliers resemble villagers belonging to the Millénaire village around their
spawner.

The add-on reads Millénaire's loaded culture and villager definitions and uses
Millénaire's own renderer, models, and textures. It does not copy or
redistribute Millénaire assets.

## Documentation

Check out the full Customers documentation here:

https://vikingkittensgames.github.io/mc-mod-customers-appearance-millenaire

## Building

First build the Customers NeoForge production JAR at
`../neoforge/build/libs/customers-neoforge-1.21.1-2.1.0.jar`, then run:

```text
gradlew spotlessCheck test build
```

`gradlew runClient` starts a development client containing Customers,
Architectury API, Millénaire, and this add-on.

## Compatibility and licensing

This integration is separate from Customers because Millénaire is
NeoForge-only here, licensed All Rights Reserved, and does not expose a stable
integration API. Millénaire remains a required user-installed dependency; this
add-on does not redistribute it.

The add-on's source is licensed under GPLv3. Refer to Millénaire's project for
its license and asset terms. Report the exact Minecraft, NeoForge, Customers,
Millénaire, and add-on versions with compatibility issues.
