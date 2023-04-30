package com.ivegotwood;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("ivegotwood")
public interface IveGotWoodConfig extends Config
{
	@ConfigItem(
			keyName = "enableWoodcuttingMessage",
			name = "Enable Woodcutting Message",
			description = "Displays 'Ive got wood!' overhead when a log is chopped",
			position = 4
	)
	default boolean enableWoodcuttingMessage()
	{
		return true;
	}

	@ConfigItem(
			keyName = "woodcuttingMessage",
			name = "Woodcutting Message",
			description = "Set your own custom message for the Woodcutting skill",
			position = 0
	)
	default String woodcuttingMessage()
	{
		return "I've got wood!";
	}



	@ConfigItem(
			keyName = "enableFishingMessage",
			name = "Enable Fishing Message",
			description = "Displays 'Wettin a line ;)' overhead when a fish is caught",
			position = 5
	)
	default boolean enableFishingMessage()
	{
		return true;
	}

	@ConfigItem(
			keyName = "fishingMessage",
			name = "Fishing Message",
			description = "Set your own custom message for the Fishing skill",
			position = 1
	)
	default String fishingMessage()
	{
		return "Wettin a line!";
	}

	@ConfigItem(
			keyName = "enableMiningMessage",
			name = "Enable Mining Message",
			description = "Displays 'This rocks!' overhead when an ore is mined",
			position = 6
	)
	default boolean enableMiningMessage()
	{
		return true;
	}

	@ConfigItem(
			keyName = "miningMessage",
			name = "Mining Message",
			description = "Set your own custom message for the Mining skill",
			position = 2
	)
	default String miningMessage()
	{
		return "This rocks!";
	}

	@ConfigItem(
			keyName = "enableConstructionMessage",
			name = "Enable Construction Message",
			description = "Displays 'Hammer time!' overhead when constructing an object",
			position = 7
	)
	default boolean enableConstructionMessage()
	{
		return true;
	}

	@ConfigItem(
			keyName = "constructionMessage",
			name = "Construction Message",
			description = "Set your own custom message for the Construction skill",
			position = 3
	)
	default String constructionMessage()
	{
		return "Hammer time!";
	}
}