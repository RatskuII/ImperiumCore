package dev.RatFjc.ImperiumCore.init;

import dev.RatFjc.ImperiumCore.ImperiumCore;
import dev.RatFjc.ImperiumCore.Module;
import dev.RatFjc.ImperiumCore.modules.mailbox.MailMain;

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

            if (instance.getCommand("adminmail") != null) {
                instance.getCommand("adminmail").setExecutor(mailMain.getCommands());
            }

            if (instance.getCommand("mail") != null) {
                instance.getCommand("mail").setExecutor(mailMain.getCommands());
            }

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