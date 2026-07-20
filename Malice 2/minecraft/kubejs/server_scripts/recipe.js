// Visit the wiki for more info - https://kubejs.com/
console.info('[Malice 2] Loaded custom recipe alterations')

ServerEvents.recipes(event => {
    // Removes every recipe that produces the item with ID 'minecraft:stick'.

    event.remove({ output: 'cold_sweat:hearth' });
    event.remove({ output: 'luminous_butterflies:jar' });

})


