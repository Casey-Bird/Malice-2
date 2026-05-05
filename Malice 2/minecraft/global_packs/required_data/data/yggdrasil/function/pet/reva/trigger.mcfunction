schedule function yggdrasil:pet/reva/debounce 1s replace
tag @s add yggdrasil.temp.check_reva

execute if entity @s[tag=yggdrasil.player.neva,tag=yggdrasil.temp.check_reva] run function yggdrasil:pet/reva/disable
execute if entity @s[tag=!yggdrasil.player.neva,tag=yggdrasil.temp.check_reva] run function yggdrasil:pet/reva/enable
