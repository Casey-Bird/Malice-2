// Visit the wiki for more info - https://kubejs.com/

LootJS.modifiers(event => {
    const itemsToRemove = [
        // End Remastered Eyes
        'endrem:rogue_eye', 'endrem:evil_eye', 'endrem:cryptic_eye', 'endrem:wither_eye',
        'endrem:witch_eye', 'endrem:exotic_eye', 'endrem:undead_eye', 'endrem:black_eye',
        'endrem:cold_eye', 'endrem:corrupted_eye', 'endrem:cursed_eye', 'endrem:guardian_eye',
        'endrem:lost_eye', 'endrem:magical_eye', 'endrem:nether_eye', 'endrem:old_eye'
    ];

    // Use addTableModifier with LootType parameters
    const lootTypes = [
        LootType.CHEST,    // Chests
        LootType.ENTITY,   // Mob drops
        LootType.BLOCK,    // Block drops
        LootType.FISHING,  // Fishing
        LootType.GIFT      // Cat gifts, piglin bartering, etc.
    ];

    // Loop through each type and remove items
    lootTypes.forEach(type => {
        event.addTableModifier(type)  // Using addTableModifier with LootType
        .removeLoot(itemsToRemove);
    });

});