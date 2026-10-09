# Customers Millénaire Appearance

Customers Millénaire Appearance is an optional NeoForge add-on for
[Customers](https://www.curseforge.com/minecraft/mc-mods/customers). It adds a
`Millénaire - Local Culture` appearance that makes spawned Customers and
Suppliers resemble villagers belonging to the Millénaire village around their
spawner.

The add-on reads Millénaire's loaded culture and villager definitions and uses
Millénaire's own renderer, models, and textures. It does not copy or
redistribute Millénaire assets.

## Requirements

- Minecraft 1.21.1
- NeoForge 21.1.230 or newer in the 21.1 line
- Customers 2.1.0 or newer
- Architectury API 13.0.8 or newer in the 13.0 line
- Millénaire 9.0.2

This release is intentionally pinned to Millénaire 9.0.2. The current mod does
not publish a supported integration API, so even patch updates may require a
compatible add-on update.

## Player setup

1. Install Customers, Architectury API, Millénaire, and this add-on.
2. Place a Customer Spawner or Supplier Spawner inside or near a Millénaire
   village.
3. Open the spawner's appearance settings.
4. Enable `Millénaire - Local Culture`.

The containing village is preferred. If the spawner is outside every village,
the nearest village is used only when its center is within the configured
distance. If no village has an eligible villager definition, Customers chooses
another enabled applicable appearance or Default.

The culture and villager definition are stored on each spawned entity. They do
not change when the entity walks away, a different village is built nearby, or
the world is reloaded.

## Appearance choices

Customers use adult, non-hostile definitions from the local culture. By
default, the selector excludes children, combat roles, leaders, and visitors.

Suppliers first look for definitions tagged by Millénaire as local merchants,
foreign merchants, or sellers. If the local culture has no eligible merchant,
they use the same ordinary adult pool as Customers.

The Customer's existing variation seed deterministically selects a loaded
definition, base texture, natural or free clothing layers, and scale. Custom
culture packs are supported because selections come from Millénaire's runtime
definitions rather than a hard-coded list of built-in cultures.

## Configuration

The common config is available through NeoForge's mod configuration screen and
`config/customers_millenaire_appearance-common.toml`.

| Setting | Default | Effect |
| --- | ---: | --- |
| Maximum nearby-village distance | 128 | Maximum spherical distance from the spawner to a fallback village center |
| Include visitors | false | Allows definitions tagged as visitors |
| Include leaders | false | Allows definitions tagged as chiefs |
| Include hostile and combat villagers | false | Allows hostile, raider, and defensive definitions |
| Include children | false | Allows child definitions |

Millénaire 9.0.2 does not retain source folders such as `quests`, `special`, or
`lonevillagers` on its public loaded `VillagerType` values. The add-on therefore
filters only public flags and tags. A custom definition without descriptive
tags may remain eligible.

## Technical behavior

The logical server resolves the local village and chooses a villager definition.
Customers persists the culture and definition IDs as generic appearance
properties and synchronizes them to clients.

On the client, this add-on creates an unspawned Millénaire villager render
proxy and delegates to Millénaire's renderer. Proxies are weakly cached by the
real Customer or Supplier. They are never added to the level, ticked, or saved.
This retains upstream models, scale, layered clothing, equipment rendering,
movement, damage animation, visibility, glowing, name tags, and sitting
behavior.

Implementation and compatibility decisions are recorded in
[appearance-millenaire.md](appearance-millenaire.md).

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
