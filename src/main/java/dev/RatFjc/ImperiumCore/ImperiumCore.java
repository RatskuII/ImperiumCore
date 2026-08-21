package dev.RatFjc.ImperiumCore;

import dev.RatFjc.ImperiumCore.extras.cmds.ToggleExperimentals;
import dev.RatFjc.ImperiumCore.extras.listener.OnByDefault;
import dev.RatFjc.ImperiumCore.extras.multithreading.AsyncModule;
import dev.RatFjc.ImperiumCore.init.*;
import dev.RatFjc.ImperiumCore.utility.BukkitUtil;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The global plugin instance that all modules will initialize from.
 */
public final class ImperiumCore extends JavaPlugin {

    private static ImperiumCore instance;

    // Modules
    private PetaPit petaPit;
    private TrainAddons trainAddons;
    private JoinLeave joinLeave;
    private NightVision nightVision;
    private Afk afk;
    private OffhandSlotBlocker offhandSlotBlocker;
    private BetterGod betterGod;
    private PetConverter petConverter;
    private UltraBans ultraBans;
    private HexSigns hexSigns;
    private InvisFrame invisFrame;
    private ShulkerBoxPreview shulkerBoxPreview;
    private PlayerWarps playerWarps;
    private PinataQuestCounter pinataQuestCounter;
    private MailboxModule mailboxModule;

    @Override
    public void onEnable() {
        instance = this;
        init();

        load(petaPit);
        load(trainAddons);
        load(joinLeave);
        load(nightVision);
        load(afk);
        load(offhandSlotBlocker);
        load(betterGod);
        load(petConverter);
        load(ultraBans);
        load(hexSigns);
        load(invisFrame);
        load(shulkerBoxPreview);
        load(playerWarps);
        load(pinataQuestCounter);
        load(mailboxModule);

        BukkitUtil.registerCommand(new ToggleExperimentals(), "allow-experiments");
        BukkitUtil.registerEvent(new OnByDefault());
    }

    @Override
    public void onDisable() {
        shutdown();
        instance = null;
    }

    public static ImperiumCore getInstance() {
        if (instance == null) {
            instance = new ImperiumCore();
            return instance;
        }

        return instance;
    }

    public void init() {
        petaPit = new PetaPit();
        trainAddons = new TrainAddons();
        joinLeave = new JoinLeave();
        nightVision = new NightVision();
        afk = new Afk();
        offhandSlotBlocker = new OffhandSlotBlocker();
        betterGod = new BetterGod();
        petConverter = new PetConverter();
        ultraBans = new UltraBans();
        hexSigns = new HexSigns();
        invisFrame = new InvisFrame();
        shulkerBoxPreview = new ShulkerBoxPreview();
        playerWarps = new PlayerWarps();
        pinataQuestCounter = new PinataQuestCounter();
        mailboxModule = new MailboxModule();
    }

    public void load(Module module) {
        if (!module.enabled()) return;
        module.load(instance);
        module.postStartup();

        if (module instanceof AsyncModule<?> asyncModule) {
            asyncModule.displayWarning();
        }
        if (module instanceof DependentModule dependentModule) {
            dependentModule.verify(module);
        }
    }

    private void shutdown() {
        TrainAddons.unregister();

        petaPit = null;
        trainAddons = null;
        joinLeave = null;
        nightVision = null;
        afk = null;
        offhandSlotBlocker = null;
        betterGod = null;
        petConverter = null;
        ultraBans = null;
        hexSigns = null;
        invisFrame = null;
        shulkerBoxPreview = null;
        playerWarps = null;
        pinataQuestCounter = null;
        mailboxModule = null;

    }
}
