package me.nickimpact.gts.pixelmon.entries;

import com.nickimpact.impactor.api.configuration.ConfigBase;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic.HiddenPower;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.EVStore;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.IVStore;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.storage.NbtKeys;
import me.nickimpact.gts.configuration.MsgConfigKeys;
import me.nickimpact.gts.internal.TextParsingUtils;
import me.nickimpact.gts.pixelmon.ReforgedBridge;
import me.nickimpact.gts.pixelmon.config.PokemonConfigKeys;
import me.nickimpact.gts.pixelmon.config.PokemonMsgConfigKeys;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.text.DecimalFormat;
import java.util.function.Function;

public enum EnumPokemonFields {

	NAME(pokemon -> {
		if(pokemon.isEgg()) {
			return "Pokemon Egg";
		}

		return pokemon.getSpecies().getLocalizedName();
	}),
	ABILITY(pokemon -> pokemon.getAbility().getLocalizedName()),
	NATURE(pokemon -> pokemon.getNature().getLocalizedName()),
	NATURE_INCREASED(pokemon -> "+" + toRep(pokemon.getNature().increasedStat)),
	NATURE_DECREASED(pokemon -> "-" + toRep(pokemon.getNature().decreasedStat)),
	GENDER(pokemon -> {
		switch(pokemon.getGender()) {
			case Male:
				return Text.of(TextColors.AQUA, Gender.Male.getLocalizedName());
			case Female:
				return Text.of(TextColors.LIGHT_PURPLE, Gender.Female.getLocalizedName());
			default:
				return Text.of(TextColors.WHITE, Gender.None.getLocalizedName());
		}
	}),
	SHINY(pokemon -> {
		if(!pokemon.isShiny())
			return Text.EMPTY;

		return Text.of(TextColors.GRAY, "(", TextColors.GOLD, TextParsingUtils.fetchAndParseMsg(null, ReforgedBridge.getInstance().getMsgConfig(), PokemonMsgConfigKeys.SHINY_TRANSLATION, null, null), TextColors.GRAY, ")");
	}),

	GROWTH(pokemon -> pokemon.getGrowth().getLocalizedName()),
	LEVEL(pokemon -> {
		if(pokemon.isEgg()) {
			return 1;
		}

		return pokemon.getLevel();
	}),
	FORM(Pokemon::getForm),
	FORM_NAME(pokemon -> {
		String form = pokemon.getFormEnum().getFormSuffix();
		if(form.startsWith("-")) {
			return form.substring(1);
		} else {
			return form;
		}
	}),
	CLONES(pokemon -> {
		if(pokemon.getSpecies().equals(EnumSpecies.Mew)) {
			NBTTagCompound nbt = new NBTTagCompound();
			pokemon.writeToNBT(nbt);
			return nbt.getShort(NbtKeys.STATS_NUM_CLONED);
		}
		return 0;
	}),
	CLONES_REMAINING(pokemon -> {
		if(pokemon.getSpecies().equals(EnumSpecies.Mew)) {
			NBTTagCompound nbt = new NBTTagCompound();
			pokemon.writeToNBT(nbt);
			return 3 - nbt.getShort(NbtKeys.STATS_NUM_CLONED);
		}
		return 0;
	}),
	ENCHANTED(pokemon -> {
		switch (pokemon.getSpecies()) {
			case Mesprit:
			case Azelf:
			case Uxie:
				NBTTagCompound nbt = new NBTTagCompound();
				pokemon.writeToNBT(nbt);
				return nbt.getShort(NbtKeys.STATS_NUM_ENCHANTED);
			default:
				return 0;
		}
	}),
	EV_PERCENT(pokemon -> new DecimalFormat("#0.##").format(totalEVs(pokemon.getStats().evs) / 510.0 * 100) + "%"),
	IV_PERCENT(pokemon -> new DecimalFormat("#0.##").format(totalIVs(pokemon.getStats().ivs) / 186.0 * 100) + "%"),
	EV_TOTAL(pokemon -> (int)totalEVs(pokemon.getStats().evs)),
	IV_TOTAL(pokemon -> (int)totalIVs(pokemon.getStats().ivs)),
	NICKNAME(pokemon -> TextSerializers.LEGACY_FORMATTING_CODE.deserialize(pokemon.getNickname() != null  && !pokemon.getNickname().isEmpty() ? pokemon.getNickname() : "N/A")),
	EV_HP(pokemon -> pokemon.getStats().evs.hp),
	EV_ATK(pokemon -> pokemon.getStats().evs.attack),
	EV_DEF(pokemon -> pokemon.getStats().evs.defence),
	EV_SPATK(pokemon -> pokemon.getStats().evs.specialAttack),
	EV_SPDEF(pokemon -> pokemon.getStats().evs.specialDefence),
	EV_SPEED(pokemon -> pokemon.getStats().evs.speed),
	IV_HP(pokemon -> pokemon.getStats().ivs.hp),
	IV_ATK(pokemon -> pokemon.getStats().ivs.attack),
	IV_DEF(pokemon -> pokemon.getStats().ivs.defence),
	IV_SPATK(pokemon -> pokemon.getStats().ivs.specialAttack),
	IV_SPDEF(pokemon -> pokemon.getStats().ivs.specialDefence),
	IV_SPEED(pokemon -> pokemon.getStats().ivs.speed),
	TEXTURE(pokemon -> {
		NBTTagCompound nbt = new NBTTagCompound();
		pokemon.writeToNBT(nbt);

		String texture = nbt.getString(NbtKeys.CUSTOM_TEXTURE);
		if(!texture.isEmpty()) {
			ConfigBase config = ReforgedBridge.getInstance().getConfig();
			if(config.get(PokemonConfigKeys.TEXTUREFLAG_CAPITALIZE)) {
				StringBuilder sb = new StringBuilder();
				String[] split = texture.split("\\s+");

				boolean first = true;
				for(String word : split) {
					if(!first) {
						sb.append(" ");
					}
					sb.append(word.substring(0, 1).toUpperCase()).append(word.substring(1).toLowerCase());
					first = false;
				}

				texture = sb.toString();
			}

			if(config.get(PokemonConfigKeys.TEXTUREFLAG_TRIM_TRAILING_NUMS)) {
				texture = texture.replaceAll("\\d*$", "");
			}

			return texture;
		}

		return pokemon.isShiny() ? TextParsingUtils.fetchAndParseMsg(null, ReforgedBridge.getInstance().getMsgConfig(), PokemonMsgConfigKeys.SHINY_TRANSLATION, null, null).toPlain() : "";
	}),
	SPECIAL_TEXTURE(pokemon -> {
		return pokemon.getSpecialTexture().name();
	}),
	HIDDEN_POWER(pokemon -> HiddenPower.getHiddenPowerType(pokemon.getStats().ivs).getLocalizedName()),
	MOVES_1(pokemon -> pokemon.getMoveset().attacks[0].baseAttack.getLocalizedName()),
	MOVES_2(pokemon -> pokemon.getMoveset().attacks[1].baseAttack.getLocalizedName()),
	MOVES_3(pokemon -> pokemon.getMoveset().attacks[2].baseAttack.getLocalizedName()),
	MOVES_4(pokemon -> pokemon.getMoveset().attacks[3].baseAttack.getLocalizedName()),
	SHINY_STATE(pokemon -> TextParsingUtils.fetchAndParseMsg(null, pokemon.isShiny() ? MsgConfigKeys.TRANSLATIONS_YES : MsgConfigKeys.TRANSLATIONS_NO, null, null)),
	POKERUS_STATE(pokemon -> TextParsingUtils.fetchAndParseMsg(null, pokemon.isShiny() ? MsgConfigKeys.TRANSLATIONS_YES : MsgConfigKeys.TRANSLATIONS_NO, null, null)),
	POKERUS(pokemon ->  pokemon.getPokerus() != null ? TextParsingUtils.fetchAndParseMsg(null, PokemonMsgConfigKeys.POKERUS_TRANSLATION, null, null) : null),
	UNBREEDABLE(pokemon -> {
		PokemonSpec unbreedable = new PokemonSpec("unbreedable");
		if(unbreedable.matches(pokemon)){
			return TextParsingUtils.fetchAndParseMsg(null, ReforgedBridge.getInstance().getMsgConfig(), PokemonMsgConfigKeys.BREEDABLE_TRANSLATION, null, null);
		}else{
			return TextParsingUtils.fetchAndParseMsg(null, ReforgedBridge.getInstance().getMsgConfig(), PokemonMsgConfigKeys.UNBREEDABLE_TRANSLATION, null, null);
		}
	}),
	POKE_BALL_NAME(pokemon ->{
		return pokemon.getCaughtBall().name();
	});


	public final Function<Pokemon, Object> function;

	private EnumPokemonFields(Function<Pokemon, Object> function) {
		this.function = function;
	}

	private static double totalEVs(EVStore evs) {
		return evs.hp + evs.attack + evs.defence + evs.specialAttack + evs.specialDefence + evs.speed;
	}

	private static double totalIVs(IVStore ivs) {
		return ivs.hp + ivs.attack + ivs.defence + ivs.specialAttack + ivs.specialDefence + ivs.speed;
	}

	private static String toRep(StatsType stat) {
		switch(stat) {
			case HP:
				return "HP";
			case Attack:
				return "Atk";
			case Defence:
				return "Def";
			case SpecialAttack:
				return "SpAtk";
			case SpecialDefence:
				return "SpDef";
			case Speed:
				return "Speed";
			default:
				return "???";
		}
	}
}
