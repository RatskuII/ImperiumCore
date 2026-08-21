package dev.RatFjc.ImperiumCore.init;

import dev.RatFjc.ImperiumCore.ImperiumCore;
import dev.RatFjc.ImperiumCore.Module;
import dev.RatFjc.ImperiumCore.modules.mailbox.MailMain;
import dev.RatFjc.ImperiumCore.utility.BukkitUtil;

public class MailboxModule extends Module {

    private MailMain mailMain;

    @Override
    public String name() {
        return "Mailbox";
    }

    @Override
    public boolean enabled() {
        return true;
    }

    @Override
    protected void load(ImperiumCore instance) {
        try {
            mailMain = new MailMain(instance);

            BukkitUtil.registerCommand(mailMain.getCommands(), "adminmail");
            BukkitUtil.registerCommand(mailMain.getCommands(), "mail");

            instance.getLogger().info("Mailbox module loaded.");

            mailMain.startReminder();

        } catch (Exception e) {
            e.printStackTrace();
            instance.getLogger().severe("Failed to load Mailbox module.");
        }
    }

    public MailMain getMailMain() {
        return mailMain;
    }
}