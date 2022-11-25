package thefellas.safepoint.impl.command.commands;

import thefellas.safepoint.Safepoint;
import thefellas.safepoint.impl.command.Command;
import thefellas.safepoint.core.initializers.NotificationManager;

import java.util.ArrayList;
import java.util.List;

public class FriendCommand extends Command {
    public FriendCommand() {
        super("friend", "Add/Remove/List Friends", "friend" + " " + "[add/remove/list]" + " " + "[playername]");
    }

    @Override
    public void runCommand(List<String> args) {
        if (args.size() >= 1) {
            if (args.size() >= 2) {
                try {
                    if (args.get(0).equalsIgnoreCase("add") && !Safepoint.friendInitializer.isFriend(args.get(1))) {
                        if (!Safepoint.friendInitializer.isFriend(args.get(1))) {
                            Safepoint.friendInitializer.addFriend((args.get(1)));
                            NotificationManager.sendMessage("Added", args.get(1));
                        } else {
                            NotificationManager.sendMessage("Error", args.get(1) + " Is Already A Friend");
                        }
                    }
                    if (args.get(0).equalsIgnoreCase("remove") && Safepoint.friendInitializer.isFriend(args.get(1))) {
                        if (Safepoint.friendInitializer.isFriend(args.get(1))) {
                            ArrayList friend = Safepoint.friendInitializer.getFriendList();
                            Safepoint.friendInitializer.isFriend(friend.toString());
                            NotificationManager.sendMessage("Removed", args.get(1));
                        } else {
                            NotificationManager.sendMessage("Error", args.get(1) + " Is Not A Friend");
                        }
                    }

                } catch (NullPointerException ignored) {
                }

            }
            if (args.get(0).equalsIgnoreCase("list")) {
                if (Safepoint.friendInitializer.getFriendList().isEmpty()) {
                    NotificationManager.sendMessage("Error", "No friends lol");
                } else {

                }
            }
        } else {
            NotificationManager.sendMessage("Error", getSyntax());
        }
    }
}
