# Millénaire Appearance Plan

## Status and implementation

The Millénaire appearance is implemented as this separate optional NeoForge
add-on for Minecraft 1.21.1. Server-side village and definition selection,
generic Customers property persistence, delegated client rendering,
configuration, unit tests, clean builds, and development-client startup have
been completed.

This plan was researched against Millénaire 9.0.2 for Minecraft 1.21.1. The
current compiled mod contains usable public classes and methods, but Millénaire
does not publish a supported integration API. Its linked public repository
contains culture content from the older implementation rather than the source
for the current NeoForge mod.

The integration must therefore be version-pinned and treated as potentially
breaking between any two Millénaire releases.

## Millénaire appearance groups

Millénaire villages have an explicit culture. Culture is a better match than
biome because biome rules are used to choose where villages generate, while
each generated village retains its actual culture ID.

Millénaire 9.0.2 includes these built-in cultures:

| Culture ID | Appearance theme |
| --- | --- |
| `millenaire:norman` | Norman |
| `millenaire:japanese` | Japanese |
| `millenaire:seljuk` | Seljuk |
| `millenaire:mayan` | Mayan |
| `millenaire:indian` | Indian |
| `millenaire:byzantines` | Byzantine |
| `millenaire:inuits` | Inuit |

Culture packs can add or replace cultures and villager definitions, so the
implementation must enumerate Millénaire's loaded definitions instead of
hard-coding only these seven cultures.

Within each culture, Millénaire groups villager definitions into categories
such as:

* Normal villagers
* Leaders and their spouses
* Local and foreign merchants
* Lone villagers
* Special villagers
* Quest villagers
* Marvel villagers

Villager definitions independently provide gender, model type, scale, base
textures, clothing layers, roles, and behavioral tags. The current model types
are male, symmetrical female, and asymmetrical female.

## Player-facing behavior

Register one spawner option named `Millénaire - Local Culture`.

When a Customer or Supplier spawns with this option enabled:

1. Resolve the Millénaire village containing the spawner position.
2. If no village contains it, find the nearest village within a bounded,
   configurable distance. Start with 128 blocks and validate that distance in
   playtesting.
3. Save the selected village's culture in the entity's
   `millenaire.culture` additional property.
4. Use the entity's existing variation seed to select its villager definition,
   base texture, clothing layers, model, and scale deterministically. Save the
   selected definition ID in `millenaire.villager_type` so save/reload and
   client rendering use the same definition.
5. If there is no suitable village, make the appearance inapplicable. Customers
   will then select another enabled appearance or fall back to Default.

The saved culture must not change when the entity walks away from the village
or when a different village is later built nearby.

### Customer selection

Customers should resemble ordinary adult residents of the selected culture.
The default eligible pool should:

* Include adult, non-hostile culture villagers.
* Exclude children, raiders, hostile villagers, defenders, and combat-only
  villagers.
* Exclude chiefs, major leaders, quest characters, and unique characters by
  default.
* Include both genders in the proportions represented by the eligible loaded
  definitions.
* Prefer normal culture villagers over lone villagers and visitors.

Do not associate impatient, casual, or normal Customer behavior with social
class or gender. Those are gameplay modes rather than Millénaire roles.

### Supplier selection

Suppliers should preferentially use adult definitions tagged as:

* `localmerchant`
* `foreignmerchant`
* `seller`

Hostile, raider, child, leader, and unique quest definitions should still be
excluded. If a culture has no eligible merchant, fall back to its ordinary
adult pool rather than the Default appearance.

## Integration approach

Use Millénaire's runtime data rather than maintaining copied lists:

* `VillageSavedData.get(ServerLevel)` for the world's village data.
* `VillageManager.findNearestVillage(BlockPos, double)` for a bounded lookup.
* `Village.getCultureId()` for the selected culture.
* `ModCultures.getAllCultures()` and `ModCultures.getAllVillagerTypes()` for
  loaded definitions.
* `VillagerType` accessors and tag helpers for eligibility, model, textures,
  clothing, scale, gender, and role data.

The renderer follows the existing MCA proxy pattern:

1. Construct a lightweight `MillVillager` render proxy.
2. Seed and initialize its appearance deterministically from the Customer or
   Supplier variation seed and resolved culture.
3. Synchronize position, rotation, movement, equipment, pose, damage animation,
   visibility, name, and sitting state from the real entity each frame.
4. Delegate to Millénaire's renderer so its three models, scale, base texture,
   and clothing layers are preserved.
5. Cache proxies weakly by source entity.

Verify sitting with all three Millénaire models. The proxy must report the
Customers seat entity when the Customer is sitting, without saving or spawning
the proxy into the world.

Use vanilla villager sounds initially. Millénaire does not expose a documented
public sound policy for arbitrary non-Millénaire entities.

## Customers appearance API work

Customers exposes the context needed by this add-on through
`CustomersVillager`:

* `getCustomersLevel()` provides the logical level.
* `getCustomersSpawnerPosition()` provides the originating spawner position.
* `getAdditionalProperties()` provides immutable, synchronized,
  appearance-specific state.

The registered appearance keeps the single stable ID
`customers_millenaire_appearance:local_culture`. When Customers selects it,
`CustomersVillagerAppearance.getAdditionalProperties(CustomersVillager)`
resolves the local culture and villager definition on the logical server.
Customers persists and synchronizes the returned key-value pairs generically.

The renderer and any future sound customization must read
`millenaire.culture` and `millenaire.villager_type` from the entity's
additional properties. Millénaire-specific data does not belong in Customers
entities or in dynamically generated appearance IDs.

## Add-on structure

Create a separately published NeoForge mod with required dependencies on:

* Customers
* Architectury API
* Millénaire

Use mod ID `customers_millenaire_appearance`, Maven group
`com.vikingkittens.mc.customersaddon`, and Java package
`com.vikingkittens.mc.customersaddon.appearance.millenaire`.

Keep server-side culture selection separate from client-only renderer and model
classes. No Millénaire class may be loaded when the add-on is absent, and the
main Customers JAR must not acquire a Millénaire dependency.

Do not copy or redistribute Millénaire textures, models, culture JSON, or other
assets. Resolve them from the installed Millénaire mod at runtime.

## Compatibility risks

* Millénaire 9.0.2 declares `license="All Rights Reserved"`.
* The current implementation source is not present in the linked public
  repository.
* There is no versioned or documented compatibility API.
* `ModCultures`, `VillageSavedData`, rendering classes, constructors, data
  formats, and model details may change without compatibility guarantees.
* Custom culture packs may contain unexpected model names, missing textures, or
  definitions that do not use the built-in category conventions.
* Millénaire's loaded `VillagerType` API does not retain its source category
  (`normalvillagers`, `quests`, `leadersandwives`, and similar folders).
  Eligibility must therefore rely on public flags and tags; untagged unique
  definitions may require explicit configuration exclusions.
* Server village data is not a substitute for client data. The server must
  resolve the culture and villager definition and return them as synchronized
  additional properties.

## Configuration

Register a NeoForge common config and expose it through the standard mod config
screen. The implemented common config contains:

* Maximum nearby-village distance, default `128`.
* Include visitors, default `false`.
* Include leaders, default `false`.
* Include hostile, raider, defender, combat-only, and child definitions,
  default `false`.

Unsafe or unusual categories remain opt-in. Millénaire does not preserve the
source folders needed for separate lone, special, quest, or leader-spouse
switches in its loaded public definitions. Filtering therefore uses the public
visitor and chief tags plus child, hostile, raider, and defensive flags.

Before release, ask the Millénaire maintainers whether the village, culture,
villager-definition, and rendering entry points used by the add-on can be
treated as supported API.

Pin each add-on release to a tested Millénaire version range. Do not declare an
open-ended compatible range until upstream provides API stability guarantees.

## Test plan

### Automated tests

* Culture resolution prefers a containing village over a merely nearby one.
* The nearest village is accepted only within the configured radius.
* No nearby village makes the appearance inapplicable.
* Eligibility excludes children, hostile villagers, raiders, leaders, and
  unique roles as specified.
* Supplier selection prefers merchant and seller tags.
* A variation seed always selects the same definition and texture indices.
* Culture and villager-type additional properties survive Customers save/load.
* Only the registered local-culture appearance appears in spawner choices.

Tests should exercise the production selection code. Do not duplicate its logic
in test-only fakes.

### Manual tests

* Spawn Customers and Suppliers near villages of all seven built-in cultures.
* Test custom culture content.
* Test no village, overlapping nearby villages, village removal, and crossing
  dimension boundaries.
* Test base textures, clothing layers, all three models, equipment, name tags,
  overhead wanted items, walking, damage, death, and sitting.
* Test dedicated server and production NeoForge client builds.
* Test save/reload after the originating village is no longer nearby.

## Built-in versus separate add-on

### Built into Customers

Pros:

* One installation for players.
* The integration can directly use internal Customers appearance hooks.
* Similar user experience to the MCA appearance.

Cons:

* Adds a NeoForge-only compatibility concern to a multi-loader mod.
* Couples Customers releases to an undocumented, rapidly changing dependency.
* Mixes Millénaire's All Rights Reserved distribution with the main
  Apache-licensed project.
* A Millénaire change could break or delay unrelated Customers releases.

### Separate add-on

Pros:

* Can pin and release independently for each Millénaire version.
* Keeps Millénaire classes and licensing concerns outside the main JAR.
* Demonstrates that the public Customers appearance extension is sufficient.
* Failures affect only packs that install the integration.

Cons:

* Players install and update another mod.
* Requires a small general Customers API enhancement first.
* Adds another artifact to test and publish.

Outcome: this project implements the recommended separate Millénaire
appearance add-on. The required Customers contextual-property extension is
implemented in the main project.

## Research sources

* [Millénaire on CurseForge](https://www.curseforge.com/minecraft/mc-mods/millenaire)
* [Millénaire 9.0.2](https://www.curseforge.com/minecraft/mc-mods/millenaire/files/8913592)
* [Millénaire public content repository](https://gitlab.com/Millenaire/Public/)
