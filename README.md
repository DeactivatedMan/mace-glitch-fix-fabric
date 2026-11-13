For the Paper version, see **[this repo](https://github.com/DeactivatedMan/mace-glitch-fix)**

## What the glitch is
With a small amount of redstone and good timing (or even just a mod), players can make their client not see a block. Serverside it's still there, but if the player were to walk onto it they would see themselves fall a fraction of a block and then teleport back up repeatedly. This somehow keeps your fall distance from resetting to 0 so you keep your mace damage bonus. To any other player it just looks like you're standing still, yet it can still one shot them and really any entity for that matter as long as they fell far enough.

## How the mod works
The moment that a player tries to damage anything with this exploit, it will be entirely cancelled with no damage dealt. Other than in **v1.0** which prevented use on the ground, this will not affect regular mace use at all, if there are any false positives then please make a [new issue on my GitHub](https://github.com/DeactivatedMan/mace-glitch-fix-fabric/issues).

## Showcase GIF
![Showcase of what happens in survival. The attack gets cancelled and the player dies to fall damage.](https://cdn.modrinth.com/data/4EbMD1LY/images/dd6c40d33a41c38b327ed4b7cd13d89becfe805a.gif)

## Config
`min-fall-distance` - Players falling less than this (default 5) will be ignored   
`cancel-fall-dmg` - If fall damage that the player would take in setting up should still be applied   
`do-ground-attack` - Act as if the glitched attack was a regular on-ground attack   
`log-cancels` - If the mod should log each detected attack to console