package ca.tweetzy.crafty.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.EntityType;

@AllArgsConstructor
@Getter
public enum ValidMob {

	// passive
	ALLAY(EntityType.ALLAY, MobBehaviour.PASSIVE, "https://textures.minecraft.net/texture/e50294a1747310f104124c6373cc639b712baa57b7d926297b645188b7bb9ab9"),
	FROG(EntityType.FROG, MobBehaviour.PASSIVE, "https://textures.minecraft.net/texture/87d62e7d4c313f2138250dbf50fa2f5a7792d9a523b18ebcac1cd73b4b656d74"),
	TADPOLE(EntityType.TADPOLE, MobBehaviour.PASSIVE, "https://textures.minecraft.net/texture/987035f5352334c2cba6ac4c65c2b9059739d6d0e839c1dd98d75d2e77957847"),


	OCELOT(EntityType.OCELOT, MobBehaviour.PASSIVE, "https://textures.minecraft.net/texture/8c433c1347313b23b67eec92f8807aed2566ec29fd416bdf7a59c22596628355"),
	MUSHROOM_COW(EntityType.MUSHROOM_COW, MobBehaviour.PASSIVE, "https://textures.minecraft.net/texture/d0bc61b9757a7b83e03cd2507a2157913c2cf016e7c096a4d6cf1fe1b8db"),
	PIG(EntityType.PIG, MobBehaviour.PASSIVE, "https://textures.minecraft.net/texture/1a371a06ea7858f89d27cc1055c317b23f105c9125bc516d3891aa4c835c299"),
	SHEEP(EntityType.SHEEP, MobBehaviour.PASSIVE, "https://textures.minecraft.net/texture/292df216ecd27624ac771bacfbfe006e1ed84a79e9270be0f88e9c8791d1ece4"),
	COW(EntityType.COW, MobBehaviour.PASSIVE, "https://textures.minecraft.net/texture/82fa683a1c584a9723603548c8c03eaf47e8365bafc16af15b14814268c7c778"),
	CHICKEN(EntityType.CHICKEN, MobBehaviour.PASSIVE, "https://textures.minecraft.net/texture/65dc8c491f17e82e3ee075f09fbdea97edf6d3e7db1e4bb8b2001a80d79a5b1f"),
	SQUID(EntityType.SQUID, MobBehaviour.PASSIVE, "https://textures.minecraft.net/texture/464bdc6f600656511bef596c1a16aab1d3f5dbaae8bee19d5c04de0db21ce92c"),
	PARROT(EntityType.PARROT, MobBehaviour.PASSIVE, "https://textures.minecraft.net/texture/a700ea674a6fc540f60f2a08cfe90fbba8408b95d8b4c52f798ec78e19fbdde3"),
	AXOLOTL(EntityType.AXOLOTL, MobBehaviour.PASSIVE, "https://textures.minecraft.net/texture/debf2b113f4a81370a1cf9d2504e8756b66deef79e9433187da774b96c9f35ba"),
	GLOW_SQUID(EntityType.GLOW_SQUID, MobBehaviour.PASSIVE, "https://textures.minecraft.net/texture/1501331fe83df67f234ddd6c089f3231d3b60228d37235ca9eb1a91ed6873a82"),
	VILLAGER(EntityType.VILLAGER, MobBehaviour.PASSIVE, "https://textures.minecraft.net/texture/f605a50dd59e5227701a51970ab0479ef57fdad9099073538fa713b26cce9fc3"),
	WANDERING_TRADER(EntityType.WANDERING_TRADER, MobBehaviour.PASSIVE, "https://textures.minecraft.net/texture/499d585a9abf59fae277bb684d24070cef21e35609a3e18a9bd5dcf73a46ab93"),
	TURTLE(EntityType.TURTLE, MobBehaviour.PASSIVE, "https://textures.minecraft.net/texture/0a4050e7aacc4539202658fdc339dd182d7e322f9fbcc4d5f99b5718a"),
	SALMON(EntityType.SALMON, MobBehaviour.PASSIVE, "https://textures.minecraft.net/texture/b770d917d1ccc12f3a1cf73fe7de8c6548f4a842086923c7bb4446bcc7aaebfa"),
	TROPICAL_FISH(EntityType.TROPICAL_FISH, MobBehaviour.PASSIVE, "https://textures.minecraft.net/texture/75ba393b926ee69a14d4d853b25d285a868b16c56c919d2b824413e6566d4e55"),
	COD(EntityType.COD, MobBehaviour.PASSIVE, "https://textures.minecraft.net/texture/7892d7dd6aadf35f86da27fb63da4edda211df96d2829f691462a4fb1cab0"),
	SKELETON_HORSE(EntityType.SKELETON_HORSE, MobBehaviour.PASSIVE, "https://textures.minecraft.net/texture/47effce35132c86ff72bcae77dfbb1d22587e94df3cbc2570ed17cf8973a"),
	ZOMBIE_HORSE(EntityType.ZOMBIE_HORSE, MobBehaviour.PASSIVE, "https://textures.minecraft.net/texture/d22950f2d3efddb18de86f8f55ac518dce73f12a6e0f8636d551d8eb480ceec"),
	DONKEY(EntityType.DONKEY, MobBehaviour.PASSIVE, "https://textures.minecraft.net/texture/399bb50d1a214c394917e25bb3f2e20698bf98ca703e4cc08b42462df309d6e6"),
	MULE(EntityType.MULE, MobBehaviour.PASSIVE, "https://textures.minecraft.net/texture/a0486a742e7dda0bae61ce2f55fa13527f1c3b334c57c034bb4cf132fb5f5f"),
	BAT(EntityType.BAT, MobBehaviour.PASSIVE, "https://textures.minecraft.net/texture/6681a72da7263ca9aef066542ecca7a180c40e328c0463fcb114cb3b83057552"),
	CAT(EntityType.CAT, MobBehaviour.PASSIVE, "https://textures.minecraft.net/texture/e566de34d04d14b39fcb15daaa7b547fb238e7d3187b424d426958adc514033"),
	SNOWMAN(EntityType.SNOWMAN, MobBehaviour.PASSIVE, "https://textures.minecraft.net/texture/9aed9fe4ed0893e325f4fbd32b093c1cc562cba27ff73359d356f1c288e441f9"),
	HORSE(EntityType.HORSE, MobBehaviour.PASSIVE, "https://textures.minecraft.net/texture/679d0cf0615ff81b1d5d0b791af85494ab6b5af971de18a46a8f911b3b59736e"),
	RABBIT(EntityType.RABBIT, MobBehaviour.PASSIVE, "https://textures.minecraft.net/texture/99f083ddd0af6c44553dc5ee8d113e07240407e2e8bf841ad44c881b690b668c"),
	FOX(EntityType.FOX, MobBehaviour.PASSIVE, "https://textures.minecraft.net/texture/e2f62858a09b09608397a33ad2de0341d39ddbd70fd480e5e83739e0cf79b212"),
	PUFFERFISH(EntityType.PUFFERFISH, MobBehaviour.PASSIVE, "https://textures.minecraft.net/texture/292350c9f0993ed54db2c7113936325683ffc20104a9b622aa457d37e708d931"),
	SNIFFER(EntityType.SNIFFER, MobBehaviour.PASSIVE, "https://textures.minecraft.net/texture/87ad920a66e38cc3426a5bff084667e8772116915e298098567c139f222e2c42"),

	// neutral
	LLAMA(EntityType.LLAMA, MobBehaviour.NEUTRAL, "https://textures.minecraft.net/texture/6d2ffce9a174fe1c084e2d82052182d94f95ed436b75ff7ea7a4e94d94c72d8a"),
	WOLF(EntityType.WOLF, MobBehaviour.NEUTRAL, "https://textures.minecraft.net/texture/28d408842e76a5a454dc1c7e9ac5c1a8ac3f4ad34d6973b5275491dff8c5c251"),
	IRON_GOLEM(EntityType.IRON_GOLEM, MobBehaviour.NEUTRAL, "https://textures.minecraft.net/texture/b69d0d4711153a089c5567a749b27879c769d3bdcea6fda9d6f66e93dd8c4512"),
	ZOMBIFIED_PIGLIN(EntityType.ZOMBIFIED_PIGLIN, MobBehaviour.NEUTRAL, "https://textures.minecraft.net/texture/93c3b28bf7afa4c60431b549f0d1518d74a771c06ede12634c554f1601f2ddd1"),
	TRADER_LLAMA(EntityType.TRADER_LLAMA, MobBehaviour.NEUTRAL, "https://textures.minecraft.net/texture/5a4eed85697c78f462c4eb5653b05b76576c1178f704f3c5676f505d8f3983b4"),
	DOLPHIN(EntityType.DOLPHIN, MobBehaviour.NEUTRAL, "https://textures.minecraft.net/texture/279413c788c7f450234bdab0cf0d0291c57f730e380c6d4c7746fde15928381"),
	PANDA(EntityType.PANDA, MobBehaviour.NEUTRAL, "https://textures.minecraft.net/texture/2266672e4ba54c5c0be59e0461a6e32ea0a7cf115d711867d2922ee9ca523690"),
	POLAR_BEAR(EntityType.POLAR_BEAR, MobBehaviour.NEUTRAL, "https://textures.minecraft.net/texture/c4fe926922fbb406f343b34a10bb98992cee4410137d3f88099427b22de3ab90"),
	BEE(EntityType.BEE, MobBehaviour.NEUTRAL, "https://textures.minecraft.net/texture/90bc9dbb4404b800f8cf0256220ff74b0b71dba8b66600b6734f4d63361618f5"),
	PIGLIN(EntityType.PIGLIN, MobBehaviour.NEUTRAL, "https://textures.minecraft.net/texture/83e2c2d66967d4715bbf6f14d2205a637d3d01b3fd106311c60737802f2bd757"),
	GOAT(EntityType.GOAT, MobBehaviour.NEUTRAL, "https://textures.minecraft.net/texture/c7967cec6ee58153c90783a0f3e0541db22e6e64d5ab921b1dbbc318de3f81ee"),
	ENDERMAN(EntityType.ENDERMAN, MobBehaviour.NEUTRAL, "https://textures.minecraft.net/texture/aacb357709d8cdf1cd9c9dbe313e7bab3276ae84234982e93e13839ab7cc5d16"),
	CAVE_SPIDER(EntityType.CAVE_SPIDER, MobBehaviour.NEUTRAL, "https://textures.minecraft.net/texture/41645dfd77d09923107b3496e94eeb5c30329f97efc96ed76e226e98224"),
	SPIDER(EntityType.SPIDER, MobBehaviour.NEUTRAL, "https://textures.minecraft.net/texture/5f7e82446fab1e41577ba70ab40e290ef841c245233011f39459ac6f852c8331"),
	CAMEL(EntityType.CAMEL, MobBehaviour.NEUTRAL, "https://textures.minecraft.net/texture/3642c9f71131b5df4a8c21c8c6f10684f22abafb8cd68a1d55ac4bf263a53a31"),

	// hostile
	WARDEN(EntityType.WARDEN, MobBehaviour.HOSTILE, "https://textures.minecraft.net/texture/53c4970510fb0f99be3f0c3d5a5919c6eeef04e433120e20107c66aba675a9b7"),

	ELDER_GUARDIAN(EntityType.ELDER_GUARDIAN, MobBehaviour.HOSTILE, "https://textures.minecraft.net/texture/4340a268f25fd5cc276ca147a8446b2630a55867a2349f7ca107c26eb58991"),
	WITHER_SKELETON(EntityType.WITHER_SKELETON, MobBehaviour.HOSTILE, "https://textures.minecraft.net/texture/2d26f2dfdf5dffc16fc80811a843524daf12c4931ec850307775c6d35a5f46c1"),
	STRAY(EntityType.STRAY, MobBehaviour.HOSTILE, "https://textures.minecraft.net/texture/6572747a639d2240feeae5c81c6874e6ee7547b599e74546490dc75fa2089186"),
	HUSK(EntityType.HUSK, MobBehaviour.HOSTILE, "https://textures.minecraft.net/texture/c096164f81950a5cc0e33e87999f98cde792517f4d7f99a647a9aedab23ae58"),
	ZOMBIE_VILLAGER(EntityType.ZOMBIE_VILLAGER, MobBehaviour.HOSTILE, "https://textures.minecraft.net/texture/1e3fc332103254b77675b7a4784d3c99c5e15765be43445c907ad97d7fcafe18"),
	EVOKER(EntityType.EVOKER, MobBehaviour.HOSTILE, "https://textures.minecraft.net/texture/e79f133a85fe00d3cf252a04d6f2eb2521fe299c08e0d8b7edbf962740a23909"),
	VEX(EntityType.VEX, MobBehaviour.HOSTILE, "https://textures.minecraft.net/texture/5e7330c7d5cd8a0a55ab9e95321535ac7ae30fe837c37ea9e53bea7ba2de86b"),
	VINDICATOR(EntityType.VINDICATOR, MobBehaviour.HOSTILE, "https://textures.minecraft.net/texture/9e1cab382458e843ac4356e3e00e1d35c36f449fa1a84488ab2c6557b392d"),
	ILLUSIONER(EntityType.ILLUSIONER, MobBehaviour.HOSTILE, "https://textures.minecraft.net/texture/4639d325f4494258a473a93a3b47f34a0c51b3fceaf59fee87205a5e7ff31f68"),
	CREEPER(EntityType.CREEPER, MobBehaviour.HOSTILE, "https://textures.minecraft.net/texture/ddcd7774446bddf79415a809ca60e445497f89551ceb21522cd81d1651166781"),
	SKELETON(EntityType.SKELETON, MobBehaviour.HOSTILE, "https://textures.minecraft.net/texture/c4070b639225b2d4f2a9f794f5acc48cb696eec49d72ebd54817f8bb52d59f34"),
	GIANT(EntityType.GIANT, MobBehaviour.HOSTILE, "https://textures.minecraft.net/texture/64528b3229660f3dfab42414f59ee8fd01e80081dd3df30869536ba9b414e089"),
	ZOMBIE(EntityType.ZOMBIE, MobBehaviour.HOSTILE, "https://textures.minecraft.net/texture/64528b3229660f3dfab42414f59ee8fd01e80081dd3df30869536ba9b414e089"),
	SLIME(EntityType.SLIME, MobBehaviour.HOSTILE, "https://textures.minecraft.net/texture/bb13133a8fb4ef00b71ef9bab639a66fbc7d5cffcc190c1df74bf2161dfd3ec7"),
	GHAST(EntityType.GHAST, MobBehaviour.HOSTILE, "https://textures.minecraft.net/texture/de8a38e9afbd3da10d19b577c55c7bfd6b4f2e407e44d4017b23be9167abff02"),
	SILVERFISH(EntityType.SILVERFISH, MobBehaviour.HOSTILE, "https://textures.minecraft.net/texture/da91dab8391af5fda54acd2c0b18fbd819b865e1a8f1d623813fa761e924540"),
	BLAZE(EntityType.BLAZE, MobBehaviour.HOSTILE, "https://textures.minecraft.net/texture/b20657e24b56e1b2f8fc219da1de788c0c24f36388b1a409d0cd2d8dba44aa3b"),
	MAGMA_CUBE(EntityType.MAGMA_CUBE, MobBehaviour.HOSTILE, "https://textures.minecraft.net/texture/a1c97a06efde04d00287bf20416404ab2103e10f08623087e1b0c1264a1c0f0c"),
	ENDER_DRAGON(EntityType.ENDER_DRAGON, MobBehaviour.HOSTILE, "https://textures.minecraft.net/texture/ffcdae586b52403b92b1857ee4331bac636af08bab92ba5750a54a83331a6353"),
	WITHER(EntityType.WITHER, MobBehaviour.HOSTILE, "https://textures.minecraft.net/texture/5da97c06c07a035bd8746b48d32dc04e84160bd161f26be1272b6271251aaa7"),
	WITCH(EntityType.WITCH, MobBehaviour.HOSTILE, "https://textures.minecraft.net/texture/fce6604157fc4ab5591e4bcf507a749918ee9c41e357d47376e0ee7342074c90"),
	ENDERMITE(EntityType.ENDERMITE, MobBehaviour.HOSTILE, "https://textures.minecraft.net/texture/5bc7b9d36fb92b6bf292be73d32c6c5b0ecc25b44323a541fae1f1e67e393a3e"),
	GUARDIAN(EntityType.GUARDIAN, MobBehaviour.HOSTILE, "https://textures.minecraft.net/texture/495290e090c238832bd7860fc033948c4d031353533ac8f67098823b7f667f1c"),
	SHULKER(EntityType.SHULKER, MobBehaviour.HOSTILE, "https://textures.minecraft.net/texture/94c0b73f063561490fd018a9dae07ca7d2e0537508556ae8b3340a5c9fb53008"),
	PHANTOM(EntityType.PHANTOM, MobBehaviour.HOSTILE, "https://textures.minecraft.net/texture/7e95153ec23284b283f00d19d29756f244313a061b70ac03b97d236ee57bd982"),
	DROWNED(EntityType.DROWNED, MobBehaviour.HOSTILE, "https://textures.minecraft.net/texture/24800e5f5e3752d4f69b525cc00dcca8687ae8ca0fb62c45719d9fce451ea45a"),
	PILLAGER(EntityType.PILLAGER, MobBehaviour.HOSTILE, "https://textures.minecraft.net/texture/4aee6bb37cbfc92b0d86db5ada4790c64ff4468d68b84942fde04405e8ef5333"),
	RAVAGER(EntityType.RAVAGER, MobBehaviour.HOSTILE, "https://textures.minecraft.net/texture/cd20bf52ec390a0799299184fc678bf84cf732bb1bd78fd1c4b441858f0235a8"),
	HOGLIN(EntityType.HOGLIN, MobBehaviour.HOSTILE, "https://textures.minecraft.net/texture/4409dc402a9fc3c7b892c44e5cd34a4a01d44419d05df8316f2e2d862ae0ba9c"),
	STRIDER(EntityType.STRIDER, MobBehaviour.HOSTILE, "https://textures.minecraft.net/texture/65ccbb547820b667cc9d3bc9fff1e3d65da2375655a3427b30e1d009eeb272ce"),
	ZOGLIN(EntityType.ZOGLIN, MobBehaviour.HOSTILE, "https://textures.minecraft.net/texture/db8bf7cadefa10f957a2fb938b4ba1225d9c89772ae7dd8d40c3176aa0433f1d"),
	PIGLIN_BRUTE(EntityType.PIGLIN_BRUTE, MobBehaviour.HOSTILE, "https://textures.minecraft.net/texture/3e300e9027349c4907497438bac29e3a4c87a848c50b34c21242727b57f4e1cf"),

	;

	final EntityType entityType;
	final MobBehaviour behaviour;
	final String headTexture;

}
