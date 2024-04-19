package ca.tweetzy.crafty.impl;

import ca.tweetzy.crafty.api.drop.Condition;
import ca.tweetzy.flight.comp.enums.CompMaterial;
import ca.tweetzy.flight.utils.SerializeUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.AllArgsConstructor;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public final class DropCondition implements Condition {

	private int minimumPlayers;
	private int maximumPlayers;
	private String mobName;
	private boolean mobNameRequired;
	private ItemStack item;
	private boolean itemRequired;
	private String permission;
	private boolean permissionRequired;
	private List<Enchantment> enchantments;
	private List<PotionEffectType> potionEffects;

	public static DropCondition template() {
		return new DropCondition(1, 1000, "Mob", false, CompMaterial.DIAMOND_SWORD.parseItem(), false, "crafty.drops", false, new ArrayList<>(List.of(Enchantment.DURABILITY)), new ArrayList<>());
	}

	@Override
	public int getMinimumPlayers() {
		return this.minimumPlayers;
	}

	@Override
	public void setMinimumPlayers(int minimumPlayers) {
		this.minimumPlayers = minimumPlayers;
	}

	@Override
	public int getMaximumPlayers() {
		return this.maximumPlayers;
	}

	@Override
	public void setMaximumPlayers(int maximumPlayers) {
		this.maximumPlayers = maximumPlayers;
	}

	@Override
	public String getMobName() {
		return this.mobName;
	}

	@Override
	public void setMobName(String mobName) {
		this.mobName = mobName;
	}

	public boolean isMobNameRequired() {
		return this.mobNameRequired;
	}

	@Override
	public void setMobNameRequired(boolean enabled) {
		this.mobNameRequired = enabled;
	}

	@Override
	public ItemStack getItem() {
		return this.item;
	}

	@Override
	public void setItem(ItemStack item) {
		this.item = item;
	}

	@Override
	public boolean isItemRequired() {
		return this.itemRequired;
	}

	@Override
	public void setItemRequired(boolean required) {
		this.itemRequired = required;
	}

	@Override
	public String getPermission() {
		return this.permission;
	}

	@Override
	public void setPermission(String permission) {
		this.permission = permission;
	}

	@Override
	public boolean isPermissionRequired() {
		return this.permissionRequired;
	}

	@Override
	public void setPermissionRequired(boolean required) {
		this.permissionRequired = required;
	}

	@Override
	public List<Enchantment> getRequiredEnchants() {
		return this.enchantments;
	}

	@Override
	public List<PotionEffectType> getRequiredPotionEffects() {
		return this.potionEffects;
	}

	@Override
	public String getJSONString() {
		final JsonObject jsonObject = new JsonObject();

		jsonObject.addProperty("minimumPlayers", this.minimumPlayers);
		jsonObject.addProperty("maximumPlayers", this.maximumPlayers);

		jsonObject.addProperty("mobName", this.mobName);
		jsonObject.addProperty("mobNameRequired", this.mobNameRequired);

		jsonObject.addProperty("item", SerializeUtil.encodeItem(this.item));
		jsonObject.addProperty("itemRequired", this.itemRequired);

		jsonObject.addProperty("permission", this.permission);
		jsonObject.addProperty("permissionRequired", this.permissionRequired);

		// enchants
		JsonArray enchants = new JsonArray();
		this.enchantments.forEach(enchantment -> enchants.add(enchantment.getKey().getKey()));

		// potion effects
		JsonArray effects = new JsonArray();
		this.potionEffects.forEach(effectType -> effects.add(effectType.getKey().getKey()));

		jsonObject.add("enchants", enchants);
		jsonObject.add("potions", effects);

		return jsonObject.toString();
	}

	public static DropCondition decodeCondition(String json) {
		final JsonObject object = JsonParser.parseString(json).getAsJsonObject();
		final JsonArray enchantArr = object.get("enchants").getAsJsonArray();
		final JsonArray potionArr = object.get("potions").getAsJsonArray();

		List<Enchantment> enchantmentList = new ArrayList<>();
		List<PotionEffectType> potionEffectList = new ArrayList<>();

		enchantArr.forEach(element -> {
			final String rawEnchantString = element.getAsString();
			final Enchantment enchantment = Registry.ENCHANTMENT.get(NamespacedKey.minecraft(rawEnchantString));
			enchantmentList.add(enchantment);
		});

		potionArr.forEach(element -> {
			final String rawPotionEffect = element.getAsString();
			final PotionEffectType potionEffectType = Registry.EFFECT.get(NamespacedKey.minecraft(rawPotionEffect));
			potionEffectList.add(potionEffectType);
		});

		return new DropCondition(
				object.get("minimumPlayers").getAsInt(),
				object.get("maximumPlayers").getAsInt(),
				object.get("mobName").getAsString(),
				object.get("mobNameRequired").getAsBoolean(),
				SerializeUtil.decodeItem(object.get("item").getAsString()),
				object.get("itemRequired").getAsBoolean(),
				object.get("permission").getAsString(),
				object.get("permissionRequired").getAsBoolean(),
				enchantmentList,
				potionEffectList
		);
	}
}
