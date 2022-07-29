package me.partlysunny.game.util.utilities;

import java.util.ArrayList;
import java.util.List;

public class LateActionPerformer {

    private static final List<Runnable> toRemove = new ArrayList<>();
    private static final List<Runnable> tempRemove = new ArrayList<>();

    public static void addRun(Runnable e) {
        toRemove.add(e);
    }

    public static void process() {
        tempRemove.clear();
        tempRemove.addAll(toRemove);
        for (Runnable e : tempRemove) {
            e.run();
        }
        toRemove.clear();
    }

}
