package sneckomod.cards;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import sneckomod.vfx.AnnouncementEffect;

import java.util.ArrayList;

public class Transmogrify extends AbstractSneckoCard {

    public final static String ID = makeID("Transmogrify");

    //stupid intellij stuff SKILL, SELF, RARE

    public Transmogrify() {
        super(ID, 1, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        exhaust = true;
        tags.add(CardTags.HEALING);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        ArrayList<AbstractRelic> eligibleRelicsList = new ArrayList<>(AbstractDungeon.player.relics);
        if (!eligibleRelicsList.isEmpty()) {
            AbstractRelic q = eligibleRelicsList.get(AbstractDungeon.cardRandomRng.random(eligibleRelicsList.size() - 1));
            q.flash();
            AbstractDungeon.player.loseRelic(q.relicId);
            if (q.tier == AbstractRelic.RelicTier.STARTER) {
                ArrayList<AbstractRelic> potentialStarters = new ArrayList<>(RelicLibrary.starterList);
                AbstractRelic s = potentialStarters.get(AbstractDungeon.cardRandomRng.random(potentialStarters.size() - 1));
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2F, Settings.HEIGHT / 2F, s.makeCopy());
                AbstractDungeon.effectsQueue.add(new AnnouncementEffect(Color.PURPLE.cpy(), q.name + " traded for " + s.name + "!!!", 5.5F));
            } else if (q.tier == AbstractRelic.RelicTier.SPECIAL) {
                ArrayList<AbstractRelic> potentialStarters = new ArrayList<>(RelicLibrary.specialList);
                AbstractRelic s = potentialStarters.get(AbstractDungeon.cardRandomRng.random(potentialStarters.size() - 1));
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2F, Settings.HEIGHT / 2F, s.makeCopy());
                AbstractDungeon.effectsQueue.add(new AnnouncementEffect(Color.PURPLE.cpy(), q.name + " traded for " + s.name + "!!!", 5.5F));
            } else {
                AbstractRelic s = AbstractDungeon.returnRandomRelic(q.tier);
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2F, Settings.HEIGHT / 2F, s);
                AbstractDungeon.effectsQueue.add(new AnnouncementEffect(Color.PURPLE.cpy(), q.name + " traded for " + s.name + "!!!", 5.5F));
            }
        }
    }

    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(0);
        }
    }
}