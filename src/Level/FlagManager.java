package Level;

import java.util.HashMap;
import java.util.Map.Entry;

public class FlagManager {
    protected HashMap<String, Boolean> flags = new HashMap<>();

    // Add a flag with a default value of false
    public void addFlag(String flagName) {
        flags.put(flagName, false);
    }

    // Add a flag with a specified starting value
    public void addFlag(String flagName, boolean startingValue) {
        flags.put(flagName, startingValue);
    }

    // Set a flag to true
    public void setFlag(String flagName) {
        if (flags.containsKey(flagName)) {
            flags.put(flagName, true);
        }
    }

    // Unset a flag (set it to false)
    public void unsetFlag(String flagName) {
        if (flags.containsKey(flagName)) {
            flags.put(flagName, false);
        }
    }

    // Clear a flag completely from the manager
    public void clearFlag(String flagName) {
        if (flags.containsKey(flagName)) {
            flags.remove(flagName);
        }
    }

    // Reset all flags to false
    public void reset() {
        for (Entry<String, Boolean> entry : flags.entrySet()) {
            entry.setValue(false);
        }
    }

    // Check if a flag is set to true
    public boolean isFlagSet(String flagName) {
        return flags.getOrDefault(flagName, false);
    }
}
