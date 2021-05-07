package theShadeThatFades.characters;

import basemod.abstracts.CustomPlayer;
import basemod.animations.SpriterAnimation;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import theShadeThatFades.TheShadeMod;
import theShadeThatFades.cards.*;
import theShadeThatFades.cards.rare.ShadeEidolonForm;
import theShadeThatFades.cards.rare.ShadeEidolonFormInfinite;
import theShadeThatFades.cards.rare.ShadeUnveil;
import theShadeThatFades.cards.rare.ShadeUnveilInfinite;
import theShadeThatFades.cards.uncommon.*;
import theShadeThatFades.relics.*;

import java.util.ArrayList;

import static theShadeThatFades.TheShadeMod.*;
import static theShadeThatFades.characters.TheShade.Enums.COLOR_GRAY;

//Wiki-page https://github.com/daviscook477/BaseMod/wiki/Custom-Characters
//and https://github.com/daviscook477/BaseMod/wiki/Migrating-to-5.0
//All text (starting description and loadout, anything labeled TEXT[]) can be found in TheShadeMod-character-Strings.json in the resources

public class TheShade extends CustomPlayer {
    public static final Logger logger = LogManager.getLogger(TheShadeMod.class.getName());

    // =============== CHARACTER ENUMERATORS =================
    // These are enums for your Characters color (both general color and for the card library) as well as
    // an enum for the name of the player class - IRONCLAD, THE_SILENT, DEFECT, YOUR_CLASS ...
    // These are all necessary for creating a character. If you want to find out where and how exactly they are used
    // in the basegame (for fun and education) Ctrl+click on the PlayerClass, CardColor and/or LibraryType below and go down the
    // Ctrl+click rabbit hole

    public static class Enums {
        @SpireEnum
        public static AbstractPlayer.PlayerClass THE_DEFAULT;
        @SpireEnum(name = "DEFAULT_GRAY_COLOR") // These two HAVE to have the same absolutely identical name.
        public static AbstractCard.CardColor COLOR_GRAY;
        @SpireEnum(name = "DEFAULT_GRAY_COLOR") @SuppressWarnings("unused")
        public static CardLibrary.LibraryType LIBRARY_COLOR;
    }

    // =============== CHARACTER ENUMERATORS  =================


    // =============== BASE STATS =================

    public static final int ENERGY_PER_TURN = 3;
    public static final int STARTING_HP = 60;
    public static final int MAX_HP = 60;
    public static final int STARTING_GOLD = 99;
    public static final int CARD_DRAW = 5;
    public static final int ORB_SLOTS = 0;

    // =============== /BASE STATS/ =================


    // =============== STRINGS =================

    private static final String ID = makeID("DefaultCharacter");
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(ID);
    private static final String[] NAMES = characterStrings.NAMES;
    private static final String[] TEXT = characterStrings.TEXT;

    // =============== /STRINGS/ =================


    // =============== TEXTURES OF BIG ENERGY ORB ===============

    public static final String[] orbTextures = {
            "theShadeThatFadesResources/images/char/defaultCharacter/orb/layer1.png",
            "theShadeThatFadesResources/images/char/defaultCharacter/orb/layer2.png",
            "theShadeThatFadesResources/images/char/defaultCharacter/orb/layer3.png",
            "theShadeThatFadesResources/images/char/defaultCharacter/orb/layer4.png",
            "theShadeThatFadesResources/images/char/defaultCharacter/orb/layer5.png",
            "theShadeThatFadesResources/images/char/defaultCharacter/orb/layer6.png",
            "theShadeThatFadesResources/images/char/defaultCharacter/orb/layer1d.png",
            "theShadeThatFadesResources/images/char/defaultCharacter/orb/layer2d.png",
            "theShadeThatFadesResources/images/char/defaultCharacter/orb/layer3d.png",
            "theShadeThatFadesResources/images/char/defaultCharacter/orb/layer4d.png",
            "theShadeThatFadesResources/images/char/defaultCharacter/orb/layer5d.png",};

    // =============== /TEXTURES OF BIG ENERGY ORB/ ===============

    // =============== CHARACTER CLASS START =================

    public TheShade(String name, PlayerClass setClass) {
        super(name, setClass, orbTextures,
                "theShadeThatFadesResources/images/char/defaultCharacter/orb/vfx.png", null,
                new SpriterAnimation(
                        "theShadeThatFadesResources/images/char/defaultCharacter/Spriter/theDefaultAnimation.scml"));


        // =============== TEXTURES, ENERGY, LOADOUT =================  

        initializeClass(null, // required call to load textures and setup energy/loadout.
                // I left these in TheShadeMod.java (Ctrl+click them to see where they are, Ctrl+hover to see what they read.)
                THE_DEFAULT_SHOULDER_2, // campfire pose
                THE_DEFAULT_SHOULDER_1, // another campfire pose
                THE_DEFAULT_CORPSE, // dead corpse
                getLoadout(), 20.0F, -10.0F, 220.0F, 290.0F, new EnergyManager(ENERGY_PER_TURN)); // energy manager

        // =============== /TEXTURES, ENERGY, LOADOUT/ =================


        // =============== ANIMATIONS =================  

        loadAnimation(
                THE_DEFAULT_SKELETON_ATLAS,
                THE_DEFAULT_SKELETON_JSON,
                1.0f);
        AnimationState.TrackEntry e = state.setAnimation(0, "animation", true);
        e.setTime(e.getEndTime() * MathUtils.random());

        // =============== /ANIMATIONS/ =================


        // =============== TEXT BUBBLE LOCATION =================

        dialogX = (drawX + 0.0F * Settings.scale); // set location for text bubbles
        dialogY = (drawY + 220.0F * Settings.scale); // you can just copy these values

        // =============== /TEXT BUBBLE LOCATION/ =================

    }

    // =============== /CHARACTER CLASS END/ =================

    // Starting description and loadout
    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(NAMES[0], TEXT[0],
                STARTING_HP, MAX_HP, ORB_SLOTS, STARTING_GOLD, CARD_DRAW, this, getStartingRelics(),
                getStartingDeck(), false);
    }

    // Starting Deck
    @Override
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> retVal = new ArrayList<>();

        logger.info("Begin loading starter Deck Strings");
        // Actual starting deck vvv
        retVal.add(ShadeStrike.ID);
        retVal.add(ShadeStrike.ID);
        retVal.add(ShadeStrike.ID);
        retVal.add(ShadeStrike.ID);
        retVal.add(ShadeStrike.ID);
        retVal.add(ShadeShadows.ID);
        retVal.add(ShadeShadows.ID);
        retVal.add(ShadeShadows.ID);
//        retVal.add(ShadeShadows.ID);
        retVal.add(ShadePurge.ID);
        //Debug stuff vvv
//        retVal.add(ShadeTorture.ID);
        retVal.add(ShadeContaminate.ID);
//        retVal.add(ShadeShadowStrike.ID);
//        retVal.add(ShadeFatigue.ID);
//        retVal.add(ShadeSpite.ID);
//        retVal.add(ShadeSpite.ID);
//        retVal.add(ShadeSpite.ID);
//        retVal.add(ShadeSpite.ID);
//        retVal.add(ShadeSpite.ID);
//        retVal.add(ShadePursuit.ID);
//        retVal.add(ShadeCorruptBlade.ID);
//        retVal.add(ShadeFinalStrike.ID);
//        retVal.add(ShadeArcaneLightning.ID);
//        retVal.add(ShadeStrangle.ID);
//        retVal.add(ShadeArcaneFire.ID);
        retVal.add(ShadeMarkOfTheWitness.ID);
//        retVal.add(ShadeDescend.ID);
//        retVal.add(ShadeFade.ID);
//        retVal.add(ShadeShroud.ID);
//        retVal.add(ShadeExert.ID);
//        retVal.add(ShadeForbiddenThoughts.ID);
//        retVal.add(ShadeForbiddenThoughts.ID);
//        retVal.add(ShadeDusk.ID);
//        retVal.add(ShadeArcaneBarrier.ID);
//        retVal.add(ShadeEncroachingShadows.ID);
//        retVal.add(ShadeVesselOfSuffering.ID);
//        retVal.add(ShadeTorment.ID);
//        retVal.add(ShadeSchadenfreude.ID);
//        retVal.add(ShadePossession.ID);
//        retVal.add(ShadeDelve.ID);
//        retVal.add(ShadeDarkTendrils.ID);
//        retVal.add(ShadeDefile.ID);
//        retVal.add(ShadeBlackLightning.ID);
//        retVal.add(ShadeBlackLightning.ID);
//        retVal.add(ShadeScourge.ID);
//        retVal.add(ShadeFalteringBlade.ID);
//        retVal.add(ShadeRebuff.ID);
//        retVal.add(ShadeWitchFire.ID);
//        retVal.add(ShadeWrithingFlame.ID);
//        retVal.add(ShadeTarnish.ID);
//        retVal.add(ShadeAnoint.ID);
//        retVal.add(ShadeHaunt.ID);
//        retVal.add(ShadeForbiddenKnowledge.ID);
//        retVal.add(ShadeForbiddenKnowledge.ID);
//        retVal.add(ShadeRemission.ID);
//        retVal.add(ShadeShadeDance.ID);
//        retVal.add(ShadeSpreadingCorruption.ID);
//        retVal.add(ShadeProlongedSuffering.ID);
//        retVal.add(ShadeOverdrive.ID);
//        retVal.add(ShadeDistortion.ID);
//        retVal.add(ShadeDistortionInfinite.ID);
//        retVal.add(ShadeChaosStorm.ID);
//        retVal.add(ShadeChaosStorm.ID);
//        retVal.add(ShadeChaosStorm.ID);
//        retVal.add(ShadeDefenseAgainstTheDarkArts.ID);
//        retVal.add(ShadeDeception.ID);
//        retVal.add(ShadeBlackHeart.ID);
//        retVal.add(ShadeShatter.ID);
//        retVal.add(ShadeFeint.ID);
//        retVal.add(ShadeMidnightHorizon.ID);
//        retVal.add(ShadeExecute.ID);
//        retVal.add(ShadeUltimateStrike.ID);
//        retVal.add(ShadeVividNightmare.ID);
//        retVal.add(ShadeOblivion.ID);
//        retVal.add(ShadeMutualAnnihilation.ID);
//        retVal.add(ShadeMutualAnnihilation.ID);
//        retVal.add(ShadeMutualAnnihilation.ID);
//        retVal.add(ShadeMutualAnnihilation.ID);
//        retVal.add(ShadeArcaneTempest.ID);
//        retVal.add(ShadePureStrike.ID);
//        retVal.add(ShadeFexnil.ID);
//        retVal.add(ShadeArcaneInferno.ID);
//        retVal.add(ShadeDeathSentence.ID);
//        retVal.add(ShadeSubversion.ID);
//        retVal.add(ShadeEthericShift.ID);
//        retVal.add(ShadeShadowBarrier.ID);
//        retVal.add(ShadeShadowBarrier.ID);
//        retVal.add(ShadeShadowBarrier.ID);
//        retVal.add(ShadeShadowBarrier.ID);
//        retVal.add(ShadeImbue.ID);
//        retVal.add(ShadeElixirOfNoctis.ID);
//        retVal.add(ShadeApexCorruption.ID);
//        retVal.add(ShadeShadowBlend.ID);
//        retVal.add(ShadeAmalgamForm.ID);
//        retVal.add(ShadeBlightBolt.ID);
//        retVal.add(ShadeUnveil.ID);
//        retVal.add(ShadeUnveilInfinite.ID);
//        retVal.add(ShadeConflagration.ID);
//        retVal.add(ShadeVoodooDoll.ID);
//        retVal.add(ShadeInfusion.ID);
//        retVal.add(ShadeGrudge.ID);
//        retVal.add(ShadeEnkindle.ID);
//        retVal.add(ShadeEidolonForm.ID);
//        retVal.add(ShadeEidolonFormInfinite.ID);
        retVal.add(ShadeDestructivore.ID);
        return retVal;
    }

    // Starting Relics	
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<>();

        retVal.add(TwistedSorcery.ID);
        UnlockTracker.markRelicAsSeen(TwistedSorcery.ID);

        return retVal;
    }

    // character Select screen effect
    @Override
    public void doCharSelectScreenSelectEffect() {
        CardCrawlGame.sound.playA("ATTACK_DAGGER_1", 1.25f); // Sound Effect
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.LOW, ScreenShake.ShakeDur.SHORT,
                false); // Screen Effect
    }

    // character Select on-button-press sound effect
    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        return "ATTACK_DAGGER_1";
    }

    // Should return how much HP your maximum HP reduces by when starting a run at
    // Ascension 14 or higher. (ironclad loses 5, defect and silent lose 4 hp respectively)
    @Override
    public int getAscensionMaxHPLoss() {
        return 4;
    }

    // Should return the card color enum to be associated with your character.
    @Override
    public AbstractCard.CardColor getCardColor() {
        return COLOR_GRAY;
    }

    // Should return a color object to be used to color the trail of moving cards
    @Override
    public Color getCardTrailColor() {
        return TheShadeMod.DEFAULT_GRAY;
    }

    // Should return a BitmapFont object that you can use to customize how your
    // energy is displayed from within the energy orb.
    @Override
    public BitmapFont getEnergyNumFont() {
        return FontHelper.energyNumFontRed;
    }

    // Should return class name as it appears in run history screen.
    @Override
    public String getLocalizedCharacterName() {
        return NAMES[0];
    }

    //Which card should be obtainable from the Match and Keep event?
    @Override
    public AbstractCard getStartCardForEvent() {
        return new ShadeVoodooDoll();
    }

    // The class name as it appears next to your player name in-game
    @Override
    public String getTitle(AbstractPlayer.PlayerClass playerClass) {
        return NAMES[1];
    }

    // Should return a new instance of your character, sending name as its name parameter.
    @Override
    public AbstractPlayer newInstance() {
        return new TheShade(name, chosenClass);
    }

    // Should return a Color object to be used to color the miniature card images in run history.
    @Override
    public Color getCardRenderColor() {
        return TheShadeMod.DEFAULT_GRAY;
    }

    // Should return a Color object to be used as screen tint effect when your
    // character attacks the heart.
    @Override
    public Color getSlashAttackColor() {
        return TheShadeMod.DEFAULT_GRAY;
    }

    // Should return an AttackEffect array of any size greater than 0. These effects
    // will be played in sequence as your character's finishing combo on the heart.
    // Attack effects are the same as used in DamageAction and the like.
    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[]{
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL,
                AbstractGameAction.AttackEffect.FIRE,
                AbstractGameAction.AttackEffect.LIGHTNING};
    }

    // Should return a string containing what text is shown when your character is
    // about to attack the heart. For example, the defect is "NL You charge your
    // core to its maximum..."
    @Override
    public String getSpireHeartText() {
        return TEXT[1];
    }

    // The vampire events refer to the base game characters as "brother", "sister",
    // and "broken one" respectively.This method should return a String containing
    // the full text that will be displayed as the first screen of the vampires event.
    @Override
    public String getVampireText() {
        return TEXT[2];
    }

}
