// Visit the wiki for more info - https://kubejs.com/
console.info('[Malice 2] Loaded custom recipe alterations')

ServerEvents.recipes(event => {
    // Removes every recipe that produces the item with ID 'minecraft:stick'.

    event.remove({ output: 'cold_sweat:hearth' });
    event.remove({ output: 'luminous_butterflies:jar' });


    event.shaped(
        Item.of('malady:nether_gateway', 1),
        [
            'A B',
            ' C ',
            'D E'
        ],
        {
            A: 'malady:infernal_soul_vial',
            B: 'malady:frosted_soul_vial',
            C: 'irons_spellbooks:energized_core',
            D: 'malady:kraken_soul_vial',
            E: 'malady:brimstone_soul_vial'
        }
    );


})


