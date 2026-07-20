
const weapons = [


    { id: 'block_factorys_bosses:dagger', damage: 3.5, speed: 1.40, slot: "mainhand" },



    { id: 'minecraft:wooden_axe', damage: 4, speed: 0.2, slot: "mainhand" },
    { id: 'minecraft:stone_axe', damage: 4.5, speed: 0.4, slot: "mainhand" },
    { id: 'minecraft:iron_axe', damage: 5, speed: 0.4, slot: "mainhand" },
    { id: 'minecraft:golden_axe', damage: 6, speed: 0.5, slot: "mainhand" },
    { id: 'minecraft:diamond_axe', damage: 6.5, speed: 0.5, slot: "mainhand" },
    { id: 'minecraft:netherite_axe', damage: 7.5, speed: 0.6, slot: "mainhand" },

    { id: 'aether:skyroot_axe', damage: 5.4, speed: 0.4, slot: "mainhand" },
    { id: 'aether:holystone_axe', damage: 5.6, speed: 0.4, slot: "mainhand" },
    { id: 'aether:zanite_axe', damage: 5, speed: 0.4, slot: "mainhand" },
    { id: 'deep_aether:skyjade_axe', damage: 5.3, speed: 0.4, slot: "mainhand" },
    { id: 'aether:gravitite_axe', damage: 4.8, speed: 0.4, slot: "mainhand" },
    { id: 'deep_aether:stratus_axe', damage: 4.5, speed: 0.4, slot: "mainhand" },
    { id: 'aether:valkyrie_axe', damage: 5.2, speed: 0.6, slot: "mainhand" },
    { id: 'twilightforest:ironwood_axe', damage: 6.1, speed: 0.4, slot: "mainhand" },
    { id: 'twilightforest:steeleaf_axe', damage: 6.2, speed: 0.4, slot: "mainhand" },
    { id: 'twilightforest:knightmetal_axe', damage: 6.7, speed: 0.4, slot: "mainhand" },
    { id: 'twilightforest:gold_minotaur_axe', damage: 7, speed: 0.4, slot: "mainhand" },
    { id: 'twilightforest:diamond_minotaur_axe', damage: 7.5, speed: 0.4, slot: "mainhand" },
    { id: 'deeperdarker:resonarium_axe', damage: 8, speed: 0.4, slot: "mainhand" },
    { id: 'deeperdarker:warden_axe', damage: 9.5, speed: 0.4, slot: "mainhand" },
    { id: 'malum:soul_stained_steel_axe', damage: 7.2, speed: 0.4, slot: "mainhand" },
    { id: 'malum:spellweaving_axe', damage: 4.5, speed: 0.4, slot: "mainhand" },


    { id: 'block_factorys_bosses:large_sword', damage: 8, speed: 0.6, slot: "mainhand" },
    { id: 'block_factorys_bosses:warrior_sword', damage: 7, speed: 0.6, slot: "mainhand" },
    { id: 'block_factorys_bosses:knight_sword', damage: 7.5, speed: 0.6, slot: "mainhand" },


    //{ id: "minecraft:wooden_sword", damage: 1, speed: 0.6, slot: "mainhand" },
    //{ id: "minecraft:stone_sword", damage: 2, speed: 0.6, slot: "mainhand" },
    //{ id: "minecraft:iron_sword", damage: 3.5, speed: 0.6, slot: "mainhand" },
    { id: 'aether:skyroot_sword', damage: 4.3, speed: 0.6, slot: "mainhand" },
    { id: 'aether:holystone_sword', damage: 4.2, speed: 0.6, slot: "mainhand" },
    { id: 'aether:zanite_sword', damage: 5, speed: 0.6, slot: "mainhand" },
    { id: 'aether:candy_cane_sword', damage: 5.4, speed: 0.6, slot: "mainhand" },
    { id: 'aether:holy_sword', damage: 4.8, speed: 0.6, slot: "mainhand" },
    { id: 'aether:lightning_sword', damage: 4, speed: 0.6, slot: "mainhand" },
    { id: 'aether:flaming_sword', damage: 6, speed: 0.6, slot: "mainhand" },
    { id: 'deep_aether:storm_sword', damage: 6, speed: 0.6, slot: "mainhand" },
    { id: 'twilightforest:ironwood_sword', damage: 3.4, speed: 0.6, slot: "mainhand" },
    { id: 'deep_aether:skyjade_sword', damage: 3.8, speed: 0.6, slot: "mainhand" },
    { id: 'aether:gravitite_sword', damage: 3.7, speed: 0.6, slot: "mainhand" },
    { id: 'deep_aether:stratus_sword', damage: 4.1, speed: 0.6, slot: "mainhand" },
    { id: 'twilightforest:steeleaf_sword', damage: 3.5, speed: 0.6, slot: "mainhand" },
    { id: 'twilightforest:knightmetal_sword', damage: 3.9, speed: 0.6, slot: "mainhand" },
    { id: 'twilightforest:fiery_sword', damage: 4, speed: 0.6, slot: "mainhand" },
    { id: 'twilightforest:ice_sword', damage: 3.8, speed: 0.6, slot: "mainhand" },
    { id: 'twilightforest:giant_sword', damage: 7, speed: 0.6, slot: "mainhand" },
    { id: 'twilightforest:glass_sword', damage: 4.3, speed: 0.6, slot: "mainhand" },
    { id: 'deeperdarker:resonarium_sword', damage: 4.8, speed: 0.6, slot: "mainhand" },
    { id: 'deeperdarker:warden_sword', damage: 8, speed: 0.6, slot: "mainhand" },
    { id: "minecraft:golden_sword", damage: 5, speed: 0.8, slot: "mainhand" },
    { id: "minecraft:diamond_sword", damage: 6, speed: 0.6, slot: "mainhand" },
    { id: "minecraft:netherite_sword", damage: 7, speed: 0.5, slot: "mainhand" }




];

ItemEvents.modification((event) => {
    weapons.forEach(weapon => {
        event.modify(weapon.id, (modifiedItem) => {
            modifiedItem.setAttributeModifiersWithTooltip([
                {
                    attribute: "minecraft:generic.attack_damage",
                    modifier: {
                        amount: weapon.damage,
                        id: "kubejs:attack_damage_override",
                        operation: "add_value",
                    },
                    slot: weapon.slot,
                },
                {
                    attribute: "minecraft:generic.attack_speed",
                    modifier: {
                        amount: weapon.speed,
                        id: "kubejs:attack_speed_override",
                        operation: "add_value",
                    },
                    slot: weapon.slot,
                }
            ]);
        });
    });
});
