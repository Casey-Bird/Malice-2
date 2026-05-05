advancement revoke @a[tag=yggdrasil.dokkalfar_ruins.teleportation] only yggdrasil:dokkalfar_ruins/teleporation
execute as @a[tag=yggdrasil.dokkalfar_ruins.teleportation] run effect give @s slow_falling 30 0 true
execute as @a[tag=yggdrasil.dokkalfar_ruins.teleportation] at @s in minecraft:overworld run tp @s ~ 300 ~
tag @a[tag=yggdrasil.dokkalfar_ruins.teleportation] remove yggdrasil.dokkalfar_ruins.teleportation