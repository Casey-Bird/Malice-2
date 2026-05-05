advancement revoke @s only yggdrasil:helheim/on_change
advancement revoke @s only yggdrasil:helheim/effect
advancement revoke @s only yggdrasil:helheim/reva
execute if predicate yggdrasil:helheim/check_if_reva_in_inventory run return fail
function yggdrasil:pet/reva/disable