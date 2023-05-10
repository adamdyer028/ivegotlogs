package com.ivegotwood;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import java.util.HashMap;




@PluginDescriptor(
		name = "I've Got Wood",
		description = "Displays messages overhead while doing certain skilling activies.",
		tags = {"funny", "skilling", "text", "woodcutting", "fishing", "mining", "construction"}
)
@Slf4j
public class IveGotWoodPlugin extends Plugin
{
	private static final InventoryID INVENTORY = InventoryID.INVENTORY;

	private HashMap<Skill, Integer> oldXP = new HashMap<>();
	private HashMap<Skill, Integer> newXP = new HashMap<>();
	private boolean initialized;
	private boolean isXPUploaded;


	private enum Skills {
		WOODCUTTING(Skill.WOODCUTTING,"", true),
		FISHING(Skill.FISHING,"", true),
		MINING(Skill.MINING,"", true),
		CONSTRUCTION(Skill.CONSTRUCTION,"", true);

		private final Skill skill;
		private String message;
		private boolean enabled;

		Skills(Skill skill, String message, Boolean enabled) {
			this.skill = skill;
			this.message = message;
			this.enabled = enabled;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		public boolean isEnabled() {
			return enabled;
		}
		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}

	}

	private void updateSkill(Skills skill, boolean enabled, String message) {
		skill.setEnabled(enabled);
		skill.setMessage(message);
	}

	private void updateSkills() {
		updateSkill(Skills.WOODCUTTING, config.enableWoodcuttingMessage(), config.woodcuttingMessage());
		updateSkill(Skills.FISHING, config.enableFishingMessage(), config.fishingMessage());
		updateSkill(Skills.MINING, config.enableMiningMessage(), config.miningMessage());
		updateSkill(Skills.CONSTRUCTION, config.enableConstructionMessage(), config.constructionMessage());
	}

	@Inject
	private Client client;

	@Inject
	private IveGotWoodConfig config;

	@Override
	protected void startUp() throws Exception {
		log.info("Ive got wood started!");
		initialized = false;
		updateSkills();
	}


	@Override
	protected void shutDown() throws Exception
	{
		log.info("Ive got wood stopped!");
		oldXP.clear();
		newXP.clear();
	}

	@Subscribe
	public void onGameStateChanged (GameStateChanged event) {
		GameState state = event.getGameState();
		if (state == GameState.LOGIN_SCREEN || state == GameState.HOPPING) {
			oldXP.clear();
			newXP.clear();
			initialized = false;
		}
	}

	@Subscribe
	public void onGameTick(GameTick event) {
		for (Skills skill : Skills.values()) {
			oldXP.put(skill.skill, client.getSkillExperience(skill.skill));
		}
	}
	@Subscribe
	public void onItemContainerChanged(ItemContainerChanged event) {

		Player player = client.getLocalPlayer();

		if (!initialized) {
			initialized = true;
			return;
		}
		if (event.getContainerId() != INVENTORY.getId()) {
			return;
		}
		ItemContainer container = event.getItemContainer();
		if (container == null) {
			return;
		}
		for (Skills skill : Skills.values()) {
			newXP.put(skill.skill, client.getSkillExperience(skill.skill));
		}
		for (Skills skill : Skills.values()) {
			int thisOldXP = oldXP.get(skill.skill);
			int thisNewXP = newXP.get(skill.skill);
			if (thisNewXP > thisOldXP) {
				oldXP.replace(skill.skill, thisNewXP);
				if (skill.isEnabled()) {
					player.setOverheadText(skill.getMessage());
					player.setOverheadCycle(70);
					break;
				}
			}
		}
	}
	@Subscribe
	public void onConfigChanged(ConfigChanged event) {
		if (event.getGroup().equals("ivegotwood")) {
			updateSkills();
		}
	}

	@Provides
	IveGotWoodConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(IveGotWoodConfig.class);
	}
}