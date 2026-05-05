tag @s add yggdrasil.player.neva
tag @s remove yggdrasil.temp.check_reva
particle minecraft:trial_spawner_detection ~0.1 ~0.1 ~0.1 0.25 0 0.25 0.05 100
function yggdrasil:pet/reva/summon
playsound minecraft:block.trial_spawner.eject_item ambient @s ~ ~ ~ 1 1
function yggdrasil:pet/reva/give_effect