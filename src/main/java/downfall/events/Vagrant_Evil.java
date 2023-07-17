package downfall.events;


import basemod.helpers.CardModifierManager;
import collector.CollectorChar;
import collector.CollectorCollection;
import collector.cardmods.CollectedCardMod;
import collector.cards.collectibles.VagrantCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import downfall.cards.curses.PrideStandard;

public class Vagrant_Evil extends AbstractImageEvent {
    public static final String ID = "downfall:Vagrant";
    public static final String NAME;
    public static final String[] DESCRIPTIONS;
    public static final String[] OPTIONS;
    public static final String[] DESCRIPTIONSALT;
    public static final String[] OPTIONSALT;
    private static final EventStrings eventStrings;

    static {
        eventStrings = CardCrawlGame.languagePack.getEventString("Addict");
        NAME = eventStrings.NAME;
        DESCRIPTIONS = eventStrings.DESCRIPTIONS;
        OPTIONS = eventStrings.OPTIONS;
        DESCRIPTIONSALT = CardCrawlGame.languagePack.getEventString(ID).DESCRIPTIONS;
        OPTIONSALT = CardCrawlGame.languagePack.getEventString(ID).OPTIONS;
    }

    private int screenNum = 0;
    private int takeCost = 0;

    public Vagrant_Evil() {
        super(NAME, DESCRIPTIONS[0], "images/events/addict.jpg");
        takeCost = Math.round(AbstractDungeon.player.getAscensionMaxHPLoss() * 1.5F);
        if (AbstractDungeon.ascensionLevel >= 15) {
            takeCost *= 1.5F;
        }
        this.imageEventText.setDialogOption(OPTIONSALT[0] + takeCost + OPTIONSALT[1]);
        this.imageEventText.setDialogOption(OPTIONSALT[2], new PrideStandard());// 38
        this.imageEventText.setDialogOption(OPTIONS[5]);
        if (AbstractDungeon.player.chosenClass.equals(CollectorChar.Enums.THE_COLLECTOR)) {
            AbstractCard card = new VagrantCard();
            CardModifierManager.addModifier(card, new CollectedCardMod());
            imageEventText.setDialogOption(CollectorChar.COLLECTORTAKE, card);
        }
    }

    protected void buttonEffect(int buttonPressed) {
        switch (this.screenNum) {
            case 0:
                switch (buttonPressed) {
                    case 0:
                        AbstractDungeon.player.damage(new DamageInfo(null, takeCost, DamageInfo.DamageType.HP_LOSS));// 114
                        int gold = 85;
                        AbstractDungeon.player.gainGold(gold);
                        logMetricGainGoldAndDamage(ID, "Punch", gold, takeCost);
                        this.imageEventText.updateBodyText(DESCRIPTIONSALT[0]);
                        this.screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[5]);
                        this.imageEventText.clearRemainingOptions();
                        return;
                    case 1:
                        this.imageEventText.updateBodyText(DESCRIPTIONSALT[1]);// 60
                        AbstractCard card = new PrideStandard();// 62
                        AbstractRelic relic = AbstractDungeon.returnRandomScreenlessRelic(AbstractDungeon.returnRandomRelicTier());// 63 64
                        AbstractEvent.logMetricObtainCardAndRelic(ID, "Stole Relic", card, relic);// 65
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(card, (float) (Settings.WIDTH / 2), (float) (Settings.HEIGHT / 2)));// 66
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, relic);// 68
                        this.imageEventText.updateDialogOption(0, OPTIONS[5]);// 70
                        this.imageEventText.clearRemainingOptions();// 71
                        this.screenNum = 1;
                        return;// 73
                    case 2:
                        this.imageEventText.updateBodyText(DESCRIPTIONSALT[2]);
                        this.imageEventText.clearAllDialogs();
                        this.imageEventText.setDialogOption(OPTIONS[3]);
                        AbstractCard card2 = new VagrantCard();
                        CardModifierManager.addModifier(card2, new CollectedCardMod());
                        CollectorCollection.collection.addToTop(card2);

                        logMetric(ID, "Take", null, null, null, null, null, null, null,
                                0, 0, 0, 0, 0, 0);
                        this.screenNum = 1;
                    default:
                        logMetricIgnored(ID);
                        this.imageEventText.updateDialogOption(0, OPTIONS[5]);// 75
                        this.imageEventText.clearRemainingOptions();// 76
                        this.screenNum = 1;
                        this.openMap();// 77
                }
            default:
                this.openMap();
        }

    }
}
